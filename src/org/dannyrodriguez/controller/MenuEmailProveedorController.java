package org.dannyrodriguez.controller;

import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.dannyrodriguez.bean.EmailProveedor;
import org.dannyrodriguez.bean.Proveedor;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuEmailProveedorController implements Initializable {
    private ObservableList<EmailProveedor> listaEmail;
    private ObservableList<Proveedor> listaProveedor;
    private Principal escenarioPrincipal;
    
    
    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operaciones tipoDeOperaciones = operaciones.NULL;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TableView<EmailProveedor> tvEmail;
    @FXML
    private TableColumn<EmailProveedor, Integer> colCoEmailPro;
    @FXML
    private TableColumn<EmailProveedor, String> colEmailProveedores;
    @FXML
    private TableColumn<EmailProveedor, String> colDescripcion;
    @FXML
    private TableColumn<EmailProveedor, Integer> colCodigoProveedor;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtCoEmailPro;
    @FXML
    private TextField txtEmailProveedores;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private ComboBox<Proveedor> cmbCodigoProveedor;
    @FXML
    private Button btnRegresar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoProveedor.setItems(getProveedor());
    }

    public void cargarDatos() {
        tvEmail.setItems(getEmailProveedor());
        colCoEmailPro.setCellValueFactory(new PropertyValueFactory<>("codigoEmailProveedor"));
        colEmailProveedores.setCellValueFactory(new PropertyValueFactory<>("emailProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    public void seleccionarEmailProveedor() {
    if (tvEmail.getSelectionModel().getSelectedItem() != null) {
        EmailProveedor emailProveedorSeleccionado = tvEmail.getSelectionModel().getSelectedItem();
        txtCoEmailPro.setText(String.valueOf(emailProveedorSeleccionado.getCodigoEmailProveedor()));
        txtEmailProveedores.setText(emailProveedorSeleccionado.getEmailProveedor());
        txtDescripcion.setText(emailProveedorSeleccionado.getDescripcion());
        cmbCodigoProveedor.getSelectionModel().select((emailProveedorSeleccionado.getCodigoProveedor()));
    }
}

    public void agregar() {
        switch (tipoDeOperaciones) {
            case NULL:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.AGREGAR;
                break;
            case AGREGAR:
                try {
                    guardar();
                    desactivarControles();
                    cargarDatos();
                    limpiarControles();
                    btnAgregar.setText("Agregar");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgAgregar.setImage(new Image("/org/dannyrodriguez/images/agregar-usuario.png"));
                    imgEliminar.setImage(new Image("/org/dannyrodriguez/images/contenedor-de-basura.png"));
                    tipoDeOperaciones = operaciones.NULL;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.err.println("Error al agregar el email del proveedor: " + e.getMessage());
                }
                break;
        }
    }

    public void guardar() {
    EmailProveedor reg = new EmailProveedor();

    // Obtener el proveedor seleccionado del ComboBox
    Proveedor proveedorSeleccionado = cmbCodigoProveedor.getSelectionModel().getSelectedItem();
    if (proveedorSeleccionado == null) {
        System.err.println("Error: Debe seleccionar un proveedor válido.");
        return;
    }
    
    // Configurar los atributos del email del proveedor con los valores de los campos de texto y el ComboBox
    reg.setCodigoEmailProveedor(Integer.parseInt(txtCoEmailPro.getText()));
    reg.setEmailProveedor(txtEmailProveedores.getText());
    reg.setDescripcion(txtDescripcion.getText());
    reg.setCodigoProveedor(proveedorSeleccionado.getCodigoProveedor());
    
    try {
        // Preparar la llamada al procedimiento almacenado para agregar el email del proveedor
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmailProveedor(?, ?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoEmailProveedor());
        procedimiento.setString(2, reg.getEmailProveedor());
        procedimiento.setString(3, reg.getDescripcion());
        procedimiento.setInt(4, reg.getCodigoProveedor());
        
        // Ejecutar el procedimiento almacenado
        procedimiento.execute();
        
        // Agregar el email del proveedor a la lista observable
        listaEmail.add(reg);
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error al guardar el email del proveedor: " + e.getMessage());
    }
}



    public ObservableList<EmailProveedor> getEmailProveedor() {
        ArrayList<EmailProveedor> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_ListarEmailProveedor()}"); ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new EmailProveedor(
                                resultado.getInt("codigoEmailProveedor"),
                                resultado.getString("emailProveedor"),
                                resultado.getString("descripcion"),
                                resultado.getInt("codigoProveedor")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmail = FXCollections.observableList(lista);
    }

    public ObservableList<Proveedor> getProveedor() {
        ArrayList<Proveedor> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_ListarProveedor()}"); ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Proveedor(
                                resultado.getInt("codigoProveedor"),
                                resultado.getString("NITProveedor"),
                                resultado.getString("nombreProveedor"),
                                resultado.getString("apellidoProveedor"),
                                resultado.getString("direccionProveedor"),
                                resultado.getString("razonSocial"),
                                resultado.getString("contactoPrincipal"),
                                resultado.getString("paginaWeb")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProveedor = FXCollections.observableList(lista);
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tvEmail.getSelectionModel().getSelectedItem() != null) {
                    EmailProveedor emailSeleccionado = tvEmail.getSelectionModel().getSelectedItem();
                    txtCoEmailPro.setText(String.valueOf(emailSeleccionado.getCodigoEmailProveedor()));
                    txtEmailProveedores.setText(emailSeleccionado.getEmailProveedor());
                    txtDescripcion.setText(emailSeleccionado.getDescripcion());
                    cmbCodigoProveedor.getSelectionModel().select((emailSeleccionado.getCodigoProveedor()));
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                    imgReporte.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    System.out.println("Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                cargarDatos();
                limpiarControles();
                tipoDeOperaciones = operaciones.NULL;
                break;
        }
    }

    public void actualizar() {
    EmailProveedor emailProveedorSeleccionado = tvEmail.getSelectionModel().getSelectedItem();
    if (emailProveedorSeleccionado != null) {
        emailProveedorSeleccionado.setCodigoEmailProveedor(Integer.parseInt(txtCoEmailPro.getText()));
        emailProveedorSeleccionado.setEmailProveedor(txtEmailProveedores.getText());
        emailProveedorSeleccionado.setDescripcion(txtDescripcion.getText());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ActualizarEmailProveedor(?, ?, ?, ?)}");
            procedimiento.setInt(1, emailProveedorSeleccionado.getCodigoEmailProveedor());
            procedimiento.setString(2, emailProveedorSeleccionado.getEmailProveedor());
            procedimiento.setString(3, emailProveedorSeleccionado.getDescripcion());
            procedimiento.setInt(4, emailProveedorSeleccionado.getCodigoProveedor());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } else {
        System.err.println("Error: Debe seleccionar un email del proveedor.");
    }
}




    
    public void eliminar() {
    if (tipoDeOperaciones == operaciones.AGREGAR) {
        desactivarControles();
        limpiarControles();
        btnAgregar.setText("Agregar");
        btnEliminar.setText("Eliminar");
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
        imgAgregar.setImage(new Image("/org/dannyrodriguez/images/agregar-usuario.png"));
        imgEliminar.setImage(new Image("/org/dannyrodriguez/images/contenedor-de-basura.png"));
        tipoDeOperaciones = operaciones.NULL;
    } else {
        if (tvEmail.getSelectionModel().getSelectedItem() != null) {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Email Proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    EmailProveedor seleccionado = tvEmail.getSelectionModel().getSelectedItem();
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarEmailProveedor(?)}");
                    procedimiento.setInt(1, seleccionado.getCodigoEmailProveedor());
                    procedimiento.execute();
                    listaEmail.remove(seleccionado);
                    limpiarControles();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error al eliminar el email del proveedor: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Debe seleccionar un elemento.");
        }
    }
}



// Métodos de activación, desactivación y limpieza de controles
    public void activarControles() {
        txtCoEmailPro.setEditable(true);
        txtEmailProveedores.setEditable(true);
        txtDescripcion.setEditable(true);
        cmbCodigoProveedor.setDisable(false);
    }

    public void desactivarControles() {
        txtCoEmailPro.setEditable(false);
        txtEmailProveedores.setEditable(false);
        txtDescripcion.setEditable(false);
        cmbCodigoProveedor.setDisable(true);
    }

    public void limpiarControles() {
        txtCoEmailPro.clear();
        txtEmailProveedores.clear();
        txtDescripcion.clear();
        cmbCodigoProveedor.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    
    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }

    }

}
