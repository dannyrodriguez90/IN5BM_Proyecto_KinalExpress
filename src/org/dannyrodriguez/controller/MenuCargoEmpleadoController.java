package org.dannyrodriguez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.dannyrodriguez.bean.CargoEmpleado;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuCargoEmpleadoController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<CargoEmpleado> listaCargoEmpleado;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    @FXML
    private Button btnRegresar;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private Button btnAgregarCargoEmpleado;
    @FXML
    private Button btnEliminarCargoEmpleado;
    @FXML
    private Button btnReportesCargoEmpleado;
    @FXML
    private Button btnEditarCargoEmpleado;
    @FXML
    private TextField txtCodigoCargoEmpleado;
    @FXML
    private TextField txtNombreCargo;
    @FXML
    private TextField txtDescripcionCargo;
    @FXML
    private TableView tblCargoEmpleado;
    @FXML
    private TableColumn colCodigoCargoEmpleado;
    @FXML
    private TableColumn colNombreCargo;
    @FXML
    private TableColumn colDescripcionCargo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregarCargoEmpleado.setText("Guardar");
                btnEliminarCargoEmpleado.setText("Cancelar");
                btnEditarCargoEmpleado.setDisable(true);
                btnReportesCargoEmpleado.setDisable(true);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarCargoEmpleado.setText("Agregar");
                btnEliminarCargoEmpleado.setText("Eliminar");
                btnEditarCargoEmpleado.setDisable(false);
                btnReportesCargoEmpleado.setDisable(false);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/carpeta.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;

        }

    }

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarCargoEmpleado.setText("Agregar");
                btnEliminarCargoEmpleado.setText("Eliminar");
                btnEditarCargoEmpleado.setDisable(false);
                btnReportesCargoEmpleado.setDisable(false);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblCargoEmpleado.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del registro",
                            "Eliminar Cargo de Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarCargoEmpleado(?)}");
                            procedimiento.setInt(1, ((CargoEmpleado) tblCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            listaCargoEmpleado.remove(tblCargoEmpleado.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Necesitas seleccionar un Cargo de Empleado antes..");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblCargoEmpleado.getSelectionModel().getSelectedItem() != null) {
                    btnEditarCargoEmpleado.setText("Actualizar");
                    btnReportesCargoEmpleado.setText("Cancelar");
                    btnAgregarCargoEmpleado.setDisable(true);
                    btnEliminarCargoEmpleado.setDisable(true);
                    imgEditar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                    imgReporte.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                    activarControles();
                    txtCodigoCargoEmpleado.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un Cargo de Empleado para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarCargoEmpleado.setText("Editar");
                btnReportesCargoEmpleado.setText("Reporte");
                btnAgregarCargoEmpleado.setDisable(false);
                btnEliminarCargoEmpleado.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarCargoEmpleado(?, ?, ?)}");
            CargoEmpleado registro = (CargoEmpleado) tblCargoEmpleado.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombreCargo.getText());
            registro.setDescripcionCargo(txtDescripcionCargo.getText());
            Procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            Procedimiento.setString(2, registro.getNombreCargo());
            Procedimiento.setString(3, registro.getDescripcionCargo());
            Procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelar() {

    }

    public void cargarDatos() {
        tblCargoEmpleado.setItems(getCargoEmpleado());
        colCodigoCargoEmpleado.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, Integer>("codigoCargoEmpleado"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("nombreCargo"));
        colDescripcionCargo.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("descripcionCargo"));
    }

    public void seleccionarElemento() {
        txtCodigoCargoEmpleado.setText(String.valueOf(((CargoEmpleado) tblCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txtNombreCargo.setText(((CargoEmpleado) tblCargoEmpleado.getSelectionModel().getSelectedItem()).getNombreCargo());
        txtDescripcionCargo.setText(((CargoEmpleado) tblCargoEmpleado.getSelectionModel().getSelectedItem()).getDescripcionCargo());
    }

    public void desactivarControles() {
        txtCodigoCargoEmpleado.setEditable(false);
        txtNombreCargo.setEditable(false);
        txtDescripcionCargo.setEditable(false);
    }

    public void activarControles() {
        txtCodigoCargoEmpleado.setEditable(true);
        txtNombreCargo.setEditable(true);
        txtDescripcionCargo.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoCargoEmpleado.clear();
        txtNombreCargo.clear();
        txtDescripcionCargo.clear();
    }

    public ObservableList<CargoEmpleado> getCargoEmpleado() {
        ArrayList<CargoEmpleado> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargoEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new CargoEmpleado(resultado.getInt("codigoCargoEmpleado"),
                        resultado.getString("nombreCargo"),
                        resultado.getString("descripcionCargo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCargoEmpleado = FXCollections.observableList(lista);
    }

    public void guardar() {
        CargoEmpleado registro = new CargoEmpleado();
        registro.setCodigoCargoEmpleado(Integer.parseInt(txtCodigoCargoEmpleado.getText()));
        registro.setNombreCargo(txtNombreCargo.getText());
        registro.setDescripcionCargo(txtDescripcionCargo.getText());
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCargoEmpleado(?, ?, ?)}");
            Procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            Procedimiento.setString(2, registro.getNombreCargo());
            Procedimiento.setString(3, registro.getDescripcionCargo());
            listaCargoEmpleado.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

}
