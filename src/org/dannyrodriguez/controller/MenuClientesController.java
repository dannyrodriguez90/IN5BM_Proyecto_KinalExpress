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
import org.dannyrodriguez.bean.Clientes;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuClientesController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Clientes> listaClientes;

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
    private Button btnAgregarCliente;
    @FXML
    private Button btnEliminarCliente;
    @FXML
    private Button btnReportesClientes;
    @FXML
    private Button btnEditarCliente;
    @FXML
    private TextField txtIdCliente;
    @FXML
    private TextField txtNit;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TableView tblClientes;
    @FXML
    private TableColumn colClienteC;
    @FXML
    private TableColumn colNitC;
    @FXML
    private TableColumn colNombreC;
    @FXML
    private TableColumn colApellidoC;
    @FXML
    private TableColumn colDireccionC;
    @FXML
    private TableColumn colTelefonoC;
    @FXML
    private TableColumn colCorreoC;

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
                btnAgregarCliente.setText("Guardar");
                btnEliminarCliente.setText("Cancelar");
                btnEditarCliente.setDisable(true);
                btnReportesClientes.setDisable(true);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarCliente.setText("Agregar");
                btnEliminarCliente.setText("Eliminar");
                btnEditarCliente.setDisable(false);
                btnReportesClientes.setDisable(false);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/agregar-usuario.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/basura.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;

        }

    }

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarCliente.setText("Agregar");
                btnEliminarCliente.setText("Eliminar");
                btnEditarCliente.setDisable(false);
                btnReportesClientes.setDisable(false);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del registro", "Eliminar Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarClientes(?)}");
                            procedimiento.setInt(1, ((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getClienteId());
                            procedimiento.execute();
                            listaClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Necesitas seleccionar un Cliente antes..");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    btnEditarCliente.setText("Actualizar");
                    btnReportesClientes.setText("Cancelar");
                    btnAgregarCliente.setDisable(true);
                    btnEliminarCliente.setDisable(true);
                    imgEditar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                    imgReporte.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                    activarControles();
                    txtIdCliente.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarCliente.setText("Editar");
                btnReportesClientes.setText("Reporte");
                btnAgregarCliente.setDisable(false);
                btnEliminarCliente.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarClientes(?, ?, ?, ?, ?, ?, ?)}");
            Clientes registro = (Clientes) tblClientes.getSelectionModel().getSelectedItem();
            registro.setNIT(txtNit.getText());
            registro.setNombreCliente(txtNombre.getText());
            registro.setApellidoCliente(txtApellido.getText());
            registro.setDireccionCliente(txtDireccion.getText());
            registro.setTelefonoCliente(txtTelefono.getText());
            Procedimiento.setInt(1, registro.getClienteId());
            Procedimiento.setString(2, registro.getNIT());
            Procedimiento.setString(3, registro.getNombreCliente());
            Procedimiento.setString(4, registro.getApellidoCliente());
            Procedimiento.setString(5, registro.getDireccionCliente());
            Procedimiento.setString(6, registro.getTelefonoCliente());
            Procedimiento.setString(7, registro.getCorreoCliente());
            Procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelar() {

    }
    
    
    
    

    public void cargarDatos() {
        tblClientes.setItems(getClientes());
        colClienteC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("clienteId"));
        colNitC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("NIT"));
        colNombreC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreCliente"));
        colApellidoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidoCliente"));
        colDireccionC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionCliente"));
        colTelefonoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colCorreoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correoCliente"));
    }

    public void seleccionarElemento() {
        txtIdCliente.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getClienteId()));
        txtNit.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNIT());
        txtNombre.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNombreCliente());
        txtApellido.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getApellidoCliente());
        txtDireccion.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente());
        txtTelefono.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente());
        txtCorreo.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getCorreoCliente());

    }

    public void desactivarControles() {
        txtIdCliente.setEditable(false);
        txtNit.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtCorreo.setEditable(false);
    }

    public void activarControles() {
        txtIdCliente.setEditable(true);
        txtNit.setEditable(true);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtCorreo.setEditable(true);
    }

    public void limpiarControles() {
        txtIdCliente.clear();
        txtNit.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtCorreo.clear();
    }

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes ()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Clientes(resultado.getInt("clienteId"),
                        resultado.getString("NIT"),
                        resultado.getString("nombreCliente"),
                        resultado.getString("apellidoCliente"),
                        resultado.getString("direccionCliente"),
                        resultado.getString("telefonoCliente"),
                        resultado.getString("correoCliente")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableList(lista);
    }

    public void guardar() {
        Clientes registro = new Clientes();
        registro.setClienteId(Integer.parseInt(txtIdCliente.getText()));
        registro.setNIT(txtNit.getText());
        registro.setNombreCliente(txtNombre.getText());
        registro.setApellidoCliente(txtApellido.getText());
        registro.setDireccionCliente(txtDireccion.getText());
        registro.setTelefonoCliente(txtTelefono.getText());
        registro.setCorreoCliente(txtCorreo.getText());
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getInstance().getConexion().prepareCall("{call sp_AgregarClientes(?,?,?,?,?,?,?)}");
            Procedimiento.setInt(1, registro.getClienteId());
            Procedimiento.setString(2, registro.getNIT());
            Procedimiento.setString(3, registro.getNombreCliente());
            Procedimiento.setString(4, registro.getApellidoCliente());
            Procedimiento.setString(5, registro.getDireccionCliente());
            Procedimiento.setString(6, registro.getTelefonoCliente());
            Procedimiento.setString(7, registro.getCorreoCliente());
            listaClientes.add(registro);
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
