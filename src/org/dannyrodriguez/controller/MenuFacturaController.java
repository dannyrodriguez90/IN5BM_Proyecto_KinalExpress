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
import javax.swing.JOptionPane;
import org.dannyrodriguez.bean.Clientes;
import org.dannyrodriguez.bean.Empleados;
import org.dannyrodriguez.bean.Factura;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuFacturaController implements Initializable {

    private ObservableList<Factura> listaFacturas;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Empleados> listaEmpleados;
    private Principal escenarioPrincipal;
 
    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }

    private operaciones tipoDeOperaciones = operaciones.NULL;

    @FXML
    private TableView<Factura> tvFactura;
    @FXML
    private TableColumn<Factura, Integer> colNumeroFactura;
    @FXML
    private TableColumn<Factura, String> colEstado;
    @FXML
    private TableColumn<Factura, Double> colTotalFactura;
    @FXML
    private TableColumn<Factura, String> colFechaFactura;
    @FXML
    private TableColumn<Factura, Integer> colClienteId;
    @FXML
    private TableColumn<Factura, Integer> colCodigoEmpleado;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtNumeroFactura;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtTotalFactura;
    @FXML
    private TextField txtFechaFactura;
    @FXML
    private ComboBox<Clientes> cmdClienteId;
    @FXML
    private ComboBox<Empleados> cmdCodigoEmpleado;
    @FXML
    private Button btnRegresar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmdClienteId.setItems(getClientes());
        cmdCodigoEmpleado.setItems(getEmpleados());
    }

    public void cargarDatos() {
        tvFactura.setItems(getFacturas());
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotalFactura.setCellValueFactory(new PropertyValueFactory<>("totalFactura"));
        colFechaFactura.setCellValueFactory(new PropertyValueFactory<>("fechaFactura"));
        colClienteId.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
    }

    public ObservableList<Factura> getFacturas() {
        ArrayList<Factura> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_ListarFactura()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Factura(
                                resultado.getInt("numeroFactura"),
                                resultado.getString("estado"),
                                resultado.getDouble("totalFactura"),
                                resultado.getString("fechaFactura"),
                                resultado.getInt("clienteId"),
                                resultado.getInt("codigoEmpleado")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaFacturas = FXCollections.observableList(lista);
    }

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_ListarClientes()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Clientes(
                                resultado.getInt("clienteId"),
                                resultado.getString("NIT"),
                                resultado.getString("nombreCliente"),
                                resultado.getString("apellidoCliente"),
                                resultado.getString("direccionCliente"),
                                resultado.getString("telefonoCliente"),
                                resultado.getString("correoCliente")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableList(lista);
    }

    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_ListarEmpleado()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Empleados(
                                resultado.getInt("codigoEmpleado"),
                                resultado.getString("nombresEmpleado"),
                                resultado.getString("apellidosEmpleado"),
                                resultado.getDouble("sueldo"),
                                resultado.getString("direccion"),
                                resultado.getString("turno"),
                                resultado.getInt("codigoCargoEmpleado")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableList(lista);
    }

    public void agregar() {
        switch (tipoDeOperaciones) {
            case NULL:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
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
                    tipoDeOperaciones = operaciones.NULL;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.err.println("Error al agregar la factura: " + e.getMessage());
                }
                break;
        }
    }

    public void guardar() {
        Factura reg = new Factura();

        // Obtener el cliente seleccionado del ComboBox
        Clientes clienteSeleccionado = cmdClienteId.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            System.err.println("Error: Debe seleccionar un cliente válido.");
            return;
        }

        // Obtener el empleado seleccionado del ComboBox
        Empleados empleadoSeleccionado = cmdCodigoEmpleado.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado == null) {
            System.err.println("Error: Debe seleccionar un empleado válido.");
            return;
        }

        // Configurar los atributos de la factura con los valores de los campos de texto y los ComboBox
        reg.setNumeroFactura(Integer.parseInt(txtNumeroFactura.getText()));
        reg.setEstado(txtEstado.getText());
        reg.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
        reg.setFechaFactura(txtFechaFactura.getText());
        reg.setClienteId(clienteSeleccionado.getClienteId());
        reg.setCodigoEmpleado(empleadoSeleccionado.getCodigoEmpleado());

        try {
            // Preparar la llamada al procedimiento almacenado para agregar la factura
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarFactura(?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, reg.getNumeroFactura());
            procedimiento.setString(2, reg.getEstado());
            procedimiento.setDouble(3, reg.getTotalFactura());
            procedimiento.setString(4, reg.getFechaFactura());
            procedimiento.setInt(5, reg.getClienteId());
            procedimiento.setInt(6, reg.getCodigoEmpleado());

            // Ejecutar el procedimiento almacenado
            procedimiento.execute();

            // Agregar la factura a la lista observable
            listaFacturas.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar la factura: " + e.getMessage());
        }
    }

    public void seleccionar() {
        if (tvFactura.getSelectionModel().getSelectedItem() != null) {
            Factura facturaSeleccionada = tvFactura.getSelectionModel().getSelectedItem();
            txtNumeroFactura.setText(String.valueOf(facturaSeleccionada.getNumeroFactura()));
            txtEstado.setText(facturaSeleccionada.getEstado());
            txtTotalFactura.setText(String.valueOf(facturaSeleccionada.getTotalFactura()));
            txtFechaFactura.setText(facturaSeleccionada.getFechaFactura());
            cmdClienteId.getSelectionModel().select((facturaSeleccionada.getClienteId()));
            cmdCodigoEmpleado.getSelectionModel().select((facturaSeleccionada.getCodigoEmpleado()));
        }
    }

    

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tvFactura.getSelectionModel().getSelectedItem() != null) {
                    seleccionar();
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    tipoDeOperaciones = operaciones.EDITAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
            case EDITAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        try {
            Factura reg = tvFactura.getSelectionModel().getSelectedItem();

            // Configurar los atributos de la factura con los valores de los campos de texto y los ComboBox
            reg.setEstado(txtEstado.getText());
            reg.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
            reg.setFechaFactura(txtFechaFactura.getText());

            // Obtener el cliente seleccionado del ComboBox
            Clientes clienteSeleccionado = cmdClienteId.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado == null) {
                System.err.println("Error: Debe seleccionar un cliente válido.");
                return;
            }
            reg.setClienteId(clienteSeleccionado.getClienteId());

            // Obtener el empleado seleccionado del ComboBox
            Empleados empleadoSeleccionado = cmdCodigoEmpleado.getSelectionModel().getSelectedItem();
            if (empleadoSeleccionado == null) {
                System.err.println("Error: Debe seleccionar un empleado válido.");
                return;
            }
            reg.setCodigoEmpleado(empleadoSeleccionado.getCodigoEmpleado());

            // Preparar la llamada al procedimiento almacenado para actualizar la factura
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarFactura(?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, reg.getNumeroFactura());
            procedimiento.setString(2, reg.getEstado());
            procedimiento.setDouble(3, reg.getTotalFactura());
            procedimiento.setString(4, reg.getFechaFactura());
            procedimiento.setInt(5, reg.getClienteId());
            procedimiento.setInt(6, reg.getCodigoEmpleado());

            // Ejecutar el procedimiento almacenado
            procedimiento.execute();

            cargarDatos();
            limpiarControles();
            desactivarControles();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar la factura: " + e.getMessage());
        }
    }

    public void eliminar() {
        if (tipoDeOperaciones == operaciones.EDITAR) {
            limpiarControles();
            desactivarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnAgregar.setDisable(false);
            btnEliminar.setDisable(false);
            tipoDeOperaciones = operaciones.NULL;
        } else {
            if (tvFactura.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        Factura reg = tvFactura.getSelectionModel().getSelectedItem();

                        // Preparar la llamada al procedimiento almacenado para eliminar la factura
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarFactura(?)}");
                        procedimiento.setInt(1, reg.getNumeroFactura());

                        // Ejecutar el procedimiento almacenado
                        procedimiento.execute();

                        listaFacturas.remove(reg);
                        limpiarControles();
                        desactivarControles();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error al eliminar la factura: " + e.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
            }
        }
    }

    

    public void desactivarControles() {
        txtNumeroFactura.setEditable(false);
        txtEstado.setEditable(false);
        txtTotalFactura.setEditable(false);
        txtFechaFactura.setEditable(false);
        cmdClienteId.setDisable(true);
        cmdCodigoEmpleado.setDisable(true);
    }

    public void activarControles() {
        txtNumeroFactura.setEditable(true);
        txtEstado.setEditable(true);
        txtTotalFactura.setEditable(true);
        txtFechaFactura.setEditable(true);
        cmdClienteId.setDisable(false);
        cmdCodigoEmpleado.setDisable(false);
    }

    public void limpiarControles() {
        txtNumeroFactura.clear();
        txtEstado.clear();
        txtTotalFactura.clear();
        txtFechaFactura.clear();
        cmdClienteId.getSelectionModel().clearSelection();
        cmdCodigoEmpleado.getSelectionModel().clearSelection();
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
