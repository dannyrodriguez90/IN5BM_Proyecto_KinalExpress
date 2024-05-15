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
import org.dannyrodriguez.bean.TipoDeProducto;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuTipoProductoController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<TipoDeProducto> listaTiposProducto;

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
    private Button btnAgregarTipo;
    @FXML
    private Button btnEliminarTipo;
    @FXML
    private Button btnReportesTipos;
    @FXML
    private Button btnEditarTipo;
    @FXML
    private TextField txtCodigoTipo;
    @FXML
    private TextField txtDescripcionTipo;
    @FXML
    private TableView tblTiposProducto;
    @FXML
    private TableColumn colCodigoTipoProducto;
    @FXML
    private TableColumn colDescripcion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaTiposProducto = FXCollections.observableArrayList();
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
                btnAgregarTipo.setText("Guardar");
                btnEliminarTipo.setText("Cancelar");
                btnEditarTipo.setDisable(true);
                btnReportesTipos.setDisable(true);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarTipo.setText("Agregar");
                btnEliminarTipo.setText("Eliminar");
                btnEditarTipo.setDisable(false);
                btnReportesTipos.setDisable(false);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/contenedor-de-basura.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;

        }

    }

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarTipo.setText("Agregar");
                btnEliminarTipo.setText("Eliminar");
                btnEditarTipo.setDisable(false);
                btnReportesTipos.setDisable(false);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblTiposProducto.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del registro",
                            "Eliminar Tipo Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarTipoDeProducto(?)}");
                            procedimiento.setInt(1, ((TipoDeProducto) tblTiposProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listaTiposProducto.remove(tblTiposProducto.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Necesitas seleccionar un Tipo de Producto antes..");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblTiposProducto.getSelectionModel().getSelectedItem() != null) {
                    btnEditarTipo.setText("Actualizar");
                    btnReportesTipos.setText("Cancelar");
                    btnAgregarTipo.setDisable(true);
                    btnEliminarTipo.setDisable(true);
                    imgEditar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                    imgReporte.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                    activarControles();
                    txtCodigoTipo.setEditable(false);
                    tipoDeOperaciones = operaciones.EDITAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Tipo de Producto para editar.");
                }
                break;
            case EDITAR:
                actualizar();
                btnEditarTipo.setText("Editar");
                btnReportesTipos.setText("Reporte");
                btnAgregarTipo.setDisable(false);
                btnEliminarTipo.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarTipoDeProducto(?,?)}");
            TipoDeProducto registro = (TipoDeProducto) tblTiposProducto.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionTipo.getText());
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelar() {
    }

    public void cargarDatos() {
        tblTiposProducto.setItems(getTiposProducto());
        colCodigoTipoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoTipoProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    public void seleccionarElemento() {
        if (tblTiposProducto.getSelectionModel().getSelectedItem() != null) {
            txtCodigoTipo.setText(String.valueOf(((TipoDeProducto) tblTiposProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
            txtDescripcionTipo.setText(((TipoDeProducto) tblTiposProducto.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }

    public void desactivarControles() {
        txtCodigoTipo.setEditable(false);
        txtDescripcionTipo.setEditable(false);
    }

    public void activarControles() {
        txtCodigoTipo.setEditable(true);
        txtDescripcionTipo.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoTipo.clear();
        txtDescripcionTipo.clear();
    }

    public ObservableList<TipoDeProducto> getTiposProducto() {
        ArrayList<TipoDeProducto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoDeProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoDeProducto(
                        resultado.getInt("codigoTipoProducto"),
                        resultado.getString("descripcion")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTiposProducto = FXCollections.observableList(lista);
    }

   public void guardar() {
        TipoDeProducto registro = new TipoDeProducto();
        registro.setCodigoTipoProducto(Integer.parseInt(txtCodigoTipo.getText()));
        registro.setDescripcion(txtDescripcionTipo.getText());
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getInstance().getConexion().prepareCall("{call sp_AgregarTipoDeProducto(?,?,)}");
            Procedimiento.setInt(1, registro.getCodigoTipoProducto());
            Procedimiento.setString(2, registro.getDescripcion());
            listaTiposProducto.add(registro);
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
