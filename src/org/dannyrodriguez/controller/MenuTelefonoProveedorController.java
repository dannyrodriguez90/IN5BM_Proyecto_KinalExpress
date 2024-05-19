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
import org.dannyrodriguez.bean.TelefonoProveedor;
import org.dannyrodriguez.bean.Proveedor;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuTelefonoProveedorController implements Initializable {

    private ObservableList<TelefonoProveedor> listaTelefonoProveedor;
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
    private TableView<TelefonoProveedor> tvTelefono;
    @FXML
    private TableColumn<TelefonoProveedor, Integer> colcodigoTelefonoProveedor;
    @FXML
    private TableColumn<TelefonoProveedor, String> colnumeroPrincipal;
    @FXML
    private TableColumn<TelefonoProveedor, String> colnumeroSecundario;
    @FXML
    private TableColumn<TelefonoProveedor, String> colobservaciones;
    @FXML
    private TableColumn<TelefonoProveedor, Integer> colCodigoProveedor;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtcodigoTelefonoProveedor;
    @FXML
    private TextField txtnumeroPrincipal;
    @FXML
    private TextField txtnumeroSecundario;
    @FXML
    private TextField txtobservaciones;
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
        tvTelefono.setItems(getTelefonosProveedor());
        colcodigoTelefonoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoTelefonoProveedor"));
        colnumeroPrincipal.setCellValueFactory(new PropertyValueFactory<>("numeroPrincipal"));
        colnumeroSecundario.setCellValueFactory(new PropertyValueFactory<>("numeroSecundario"));
        colobservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    public ObservableList<TelefonoProveedor> getTelefonosProveedor() {
        ArrayList<TelefonoProveedor> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_ListarTelefonoProveedor()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new TelefonoProveedor(
                                resultado.getInt("codigoTelefonoProveedor"),
                                resultado.getString("numeroPrincipal"),
                                resultado.getString("numeroSecundario"),
                                resultado.getString("observaciones"),
                                resultado.getInt("codigoProveedor")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTelefonoProveedor = FXCollections.observableList(lista);
    }

    public ObservableList<Proveedor> getProveedor() {
        ArrayList<Proveedor> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_ListarProveedor()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
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
                    System.err.println("Error al agregar el teléfono: " + e.getMessage());
                }
                break;
        }
    }

    public void guardar() {
        TelefonoProveedor reg = new TelefonoProveedor();

        // Obtener el proveedor seleccionado del ComboBox
        Proveedor proveedorSeleccionado = cmbCodigoProveedor.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado == null) {
            System.err.println("Error: Debe seleccionar un proveedor válido.");
            return;
        }

        // Configurar los atributos del teléfono con los valores de los campos de texto y los ComboBox
        reg.setCodigoTelefonoProveedor(Integer.parseInt(txtcodigoTelefonoProveedor.getText())); // Asignar el valor del campo de texto
        reg.setNumeroPrincipal(txtnumeroPrincipal.getText());
        reg.setNumeroSecundario(txtnumeroSecundario.getText());
        reg.setObservaciones(txtobservaciones.getText());
        reg.setCodigoProveedor(proveedorSeleccionado.getCodigoProveedor());

        try {
            // Preparar la llamada al procedimiento almacenado para agregar el teléfono
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTelefonoProveedor(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, reg.getCodigoTelefonoProveedor());
            procedimiento.setString(2, reg.getNumeroPrincipal());
            procedimiento.setString(3, reg.getNumeroSecundario());
            procedimiento.setString(4, reg.getObservaciones());
            procedimiento.setInt(5, reg.getCodigoProveedor());

            // Ejecutar el procedimiento almacenado
            procedimiento.execute();

            // Agregar el teléfono a la lista observable
            listaTelefonoProveedor.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el teléfono: " + e.getMessage());
        }
    }

    public void seleccionar() {
        if (tvTelefono.getSelectionModel().getSelectedItem() != null) {
            TelefonoProveedor telefonoSeleccionado = tvTelefono.getSelectionModel().getSelectedItem();
            txtcodigoTelefonoProveedor.setText(String.valueOf(telefonoSeleccionado.getCodigoTelefonoProveedor())); // Corrección aquí
            txtnumeroPrincipal.setText(telefonoSeleccionado.getNumeroPrincipal());
            txtnumeroSecundario.setText(telefonoSeleccionado.getNumeroSecundario());
            txtobservaciones.setText(telefonoSeleccionado.getObservaciones());
            cmbCodigoProveedor.getSelectionModel().select(buscarProveedor(telefonoSeleccionado.getCodigoProveedor()));
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tvTelefono.getSelectionModel().getSelectedItem() != null) {
                    TelefonoProveedor telefonoSeleccionado = tvTelefono.getSelectionModel().getSelectedItem();
                    txtcodigoTelefonoProveedor.setText(String.valueOf(telefonoSeleccionado.getCodigoTelefonoProveedor())); // Corrección aquí
                    txtnumeroPrincipal.setText(telefonoSeleccionado.getNumeroPrincipal());
                    txtnumeroSecundario.setText(telefonoSeleccionado.getNumeroSecundario());
                    txtobservaciones.setText(telefonoSeleccionado.getObservaciones());
                    cmbCodigoProveedor.getSelectionModel().select(buscarProveedor(telefonoSeleccionado.getCodigoProveedor()));
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
        TelefonoProveedor telefonoSeleccionado = tvTelefono.getSelectionModel().getSelectedItem();
        telefonoSeleccionado.setNumeroPrincipal(txtcodigoTelefonoProveedor.getText());
        telefonoSeleccionado.setNumeroPrincipal(txtnumeroPrincipal.getText());
        telefonoSeleccionado.setNumeroSecundario(txtnumeroSecundario.getText());
        telefonoSeleccionado.setObservaciones(txtobservaciones.getText());
        Proveedor proveedorSeleccionado = cmbCodigoProveedor.getSelectionModel().getSelectedItem();
        telefonoSeleccionado.setCodigoProveedor(proveedorSeleccionado.getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarTelefonoProveedor(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, telefonoSeleccionado.getCodigoTelefonoProveedor());
            procedimiento.setString(2, telefonoSeleccionado.getNumeroPrincipal());
            procedimiento.setString(3, telefonoSeleccionado.getNumeroSecundario());
            procedimiento.setString(4, telefonoSeleccionado.getObservaciones());
            procedimiento.setInt(5, telefonoSeleccionado.getCodigoProveedor());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el teléfono: " + e.getMessage());
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
            imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
            imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
            tipoDeOperaciones = operaciones.NULL;
        } else {
            if (tvTelefono.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Teléfono", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarTelefonoProveedor(?)}");
                        procedimiento.setInt(1, tvTelefono.getSelectionModel().getSelectedItem().getCodigoTelefonoProveedor());
                        procedimiento.execute();
                        listaTelefonoProveedor.remove(tvTelefono.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error al eliminar el teléfono: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Debe seleccionar un elemento.");
            }
        }
    }

    public void activarControles() {
        txtnumeroPrincipal.setEditable(true);
        txtnumeroSecundario.setEditable(true);
        txtobservaciones.setEditable(true);
        cmbCodigoProveedor.setDisable(false);
    }

    public void desactivarControles() {
        txtnumeroPrincipal.setEditable(false);
        txtnumeroSecundario.setEditable(false);
        txtobservaciones.setEditable(false);
        cmbCodigoProveedor.setDisable(true);
    }

    public void limpiarControles() {
        txtnumeroPrincipal.clear();
        txtnumeroSecundario.clear();
        txtobservaciones.clear();
        cmbCodigoProveedor.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    private Proveedor buscarProveedor(int codigoProveedor) {
        for (Proveedor proveedor : listaProveedor) {
            if (proveedor.getCodigoProveedor() == codigoProveedor) {
                return proveedor;
            }
        }
        return null;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }

    }

}
