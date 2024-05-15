package org.dannyrodriguez.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.dannyrodriguez.bean.Compras;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuComprasController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Compras> listaCompras;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
        @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAgregarCompra;
    @FXML
    private Button btnEliminarCompra;
    @FXML
    private Button btnReportesCompras;
    @FXML
    private Button btnEditarCompra;
    @FXML
    private TextField txtNumeroDocumento;
    @FXML
    private TextField txtFechaDocumento;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtTotalDocumento;
    @FXML
    private TableView tblCompras;
    @FXML
    private TableColumn colNumeroDocumento;
    @FXML
    private TableColumn colFechaDocumento;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colTotalDocumento;

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
                btnAgregarCompra.setText("Guardar");
                btnEliminarCompra.setText("Cancelar");
                btnEditarCompra.setDisable(true);
                btnReportesCompras.setDisable(true);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.AGREGAR;
                break;
            case AGREGAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarCompra.setText("Agregar");
                btnEliminarCompra.setText("Eliminar");
                btnEditarCompra.setDisable(false);
                btnReportesCompras.setDisable(false);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/agregar-producto.png"));
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
                btnAgregarCompra.setText("Agregar");
                btnEliminarCompra.setText("Eliminar");
                btnEditarCompra.setDisable(false);
                btnReportesCompras.setDisable(false);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del registro",
                            "Eliminar Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarCompras(?)}");
                            procedimiento.setInt(1, ((Compras) tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompras.remove(tblCompras.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Necesitas seleccionar un Proveedor antes..");
                }
        }
    }


    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                    btnEditarCompra.setText("Actualizar");
                    btnReportesCompras.setText("Cancelar");
                    btnAgregarCompra.setDisable(true);
                    btnEliminarCompra.setDisable(true);
                    imgEditar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                    imgReporte.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                    activarControles();
                    txtNumeroDocumento.setEditable(false);
                    tipoDeOperaciones = operaciones.EDITAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un proveedor para editar");
                }
                break;
            case EDITAR:
                actualizar();
                btnEditarCompra.setText("Editar");
                btnReportesCompras.setText("Reporte");
                btnAgregarCompra.setDisable(false);
                btnEliminarCompra.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }


    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarCompras(?, ?, ?, ?)}");
            Compras registro = (Compras) tblCompras.getSelectionModel().getSelectedItem();
            registro.setFechaDocumento(Date.valueOf(txtFechaDocumento.getText()));
            registro.setDescripcion(txtDescripcion.getText());
            registro.setTotalDocumento(BigDecimal.valueOf(Double.parseDouble(txtTotalDocumento.getText())));
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setDate(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setBigDecimal(4, registro.getTotalDocumento());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cargarDatos() {
        tblCompras.setItems(getCompras());
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));
        colFechaDocumento.setCellValueFactory(new PropertyValueFactory<>("fechaDocumento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colTotalDocumento.setCellValueFactory(new PropertyValueFactory<>("totalDocumento"));
    }

    public void limpiarControles() {
        txtNumeroDocumento.clear();
        txtFechaDocumento.clear();
        txtDescripcion.clear();
        txtTotalDocumento.clear();
    }

    public void activarControles() {
        txtNumeroDocumento.setEditable(true);
        txtFechaDocumento.setEditable(true);
        txtDescripcion.setEditable(true);
        txtTotalDocumento.setEditable(true);
    }

    public void desactivarControles() {
        txtNumeroDocumento.setEditable(false);
        txtFechaDocumento.setEditable(false);
        txtDescripcion.setEditable(false);
        txtTotalDocumento.setEditable(false);
    }

    public ObservableList<Compras> getCompras() {
        ArrayList<Compras> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCompras()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Compras(resultado.getInt("numeroDocumento"),
                        resultado.getDate("fechaDocumento"),
                        resultado.getString("descripcion"),
                        resultado.getBigDecimal("totalDocumento")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCompras = FXCollections.observableArrayList(lista);
    }

    public void guardar() {
        Compras registro = new Compras();
        registro.setNumeroDocumento(Integer.parseInt(txtNumeroDocumento.getText()));
        registro.setFechaDocumento(Date.valueOf(txtFechaDocumento.getText()));
        registro.setDescripcion(txtDescripcion.getText());
        registro.setTotalDocumento(BigDecimal.valueOf(Double.parseDouble(txtTotalDocumento.getText())));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCompras(?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setDate(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setBigDecimal(4, registro.getTotalDocumento());
            procedimiento.execute();
            listaCompras.add(registro);
        } catch (SQLException e) {
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
