package org.dannyrodriguez.controller;

import java.net.URL;
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
import org.dannyrodriguez.bean.DetalleFactura;
import org.dannyrodriguez.bean.Productos;
import org.dannyrodriguez.bean.Factura;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuDetalleFacturaController implements Initializable {

    private ObservableList<Factura> listaFacturas;
    private ObservableList<Productos> listaProductos;
    private ObservableList<DetalleFactura> listaDetalleFactura;
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
    private TableView<DetalleFactura> tvDetalleFactura;
    @FXML
    private TableColumn<DetalleFactura, Integer> colcodigoDetalleFactura;
    @FXML
    private TableColumn<DetalleFactura, Double> colprecioUnitario;
    @FXML
    private TableColumn<DetalleFactura, Integer> colcantidad;
    @FXML
    private TableColumn<DetalleFactura, Integer> colnumeroFactura;
    @FXML
    private TableColumn<DetalleFactura, String> colcodigoProducto;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtcodigoDetalleFactura;
    @FXML
    private TextField txtprecioUnitario;
    @FXML
    private TextField txtcantidad;
    @FXML
    private ComboBox<Factura> cmdnumeroFactura;
    @FXML
    private ComboBox<Productos> cmdcodigoProducto;
    @FXML
    private Button btnRegresar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmdnumeroFactura.setItems(getFactura());
        cmdcodigoProducto.setItems(getProductos());
    }

    public void cargarDatos() {
        tvDetalleFactura.setItems(getDetalleFactura());
        colcodigoDetalleFactura.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleFactura"));
        colprecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colcantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colnumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colcodigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
    }

    public ObservableList<DetalleFactura> getDetalleFactura() {
        ArrayList<DetalleFactura> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarDetallesFactura()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new DetalleFactura(
                        resultado.getInt("codigoDetalleFactura"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getInt("numeroFactura"),
                        resultado.getString("codigoProducto")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDetalleFactura = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Factura> getFactura() {
        ArrayList<Factura> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarFactura()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Factura(
                        resultado.getInt("numeroFactura"),
                        resultado.getString("estado"),
                        resultado.getDouble("totalFactura"),
                        resultado.getString("fechaFactura"),
                        resultado.getInt("clienteId"),
                        resultado.getInt("codigoEmpleado")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaFacturas = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Productos> getProductos() {
        ArrayList<Productos> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos(
                        resultado.getString("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getInt("existencia"),
                        resultado.getInt("codigoProveedor"),
                        resultado.getInt("codigoTipoProducto")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }

    public void seleccionar() {
        if (tvDetalleFactura.getSelectionModel().getSelectedItem() != null) {
            DetalleFactura detalleSeleccionado = tvDetalleFactura.getSelectionModel().getSelectedItem();
            txtcodigoDetalleFactura.setText(String.valueOf(detalleSeleccionado.getCodigoDetalleFactura()));
            txtprecioUnitario.setText(String.valueOf(detalleSeleccionado.getPrecioUnitario()));
            txtcantidad.setText(String.valueOf(detalleSeleccionado.getCantidad()));
            cmdnumeroFactura.getSelectionModel().select(buscarFactura(detalleSeleccionado.getNumeroFactura()));
            cmdcodigoProducto.getSelectionModel().select(buscarProducto(detalleSeleccionado.getCodigoProducto()));
        }
    }

    public Factura buscarFactura(int numeroFactura) {
        Factura resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarFactura(?)}");
            procedimiento.setInt(1, numeroFactura);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Factura(
                        registro.getInt("numeroFactura"),
                        registro.getString("estado"),
                        registro.getDouble("totalFactura"),
                        registro.getString("fechaFactura"),
                        registro.getInt("clienteId"),
                        registro.getInt("codigoEmpleado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Productos buscarProducto(String codigoProducto) {
        Productos resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarProducto(?)}");
            procedimiento.setString(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Productos(
                        registro.getString("codigoProducto"),
                        registro.getString("descripcionProducto"),
                        registro.getDouble("precioUnitario"),
                        registro.getDouble("precioDocena"),
                        registro.getDouble("precioMayor"),
                        registro.getInt("existencia"),
                        registro.getInt("codigoProveedor"),
                        registro.getInt("codigoTipoProducto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
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
                imgAgregar.setImage(new Image("/org/dannyrodriguez/images/agregar.png"));
                imgEliminar.setImage(new Image("/org/dannyrodriguez/images/basuraz.png"));
                tipoDeOperaciones = operaciones.NULL;
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.err.println("Error al agregar el producto: " + e.getMessage());
            }
            break;
    }
}


    public void guardar() {
    DetalleFactura detalle = new DetalleFactura();
    detalle.setCodigoDetalleFactura(Integer.parseInt(txtcodigoDetalleFactura.getText()));
    detalle.setPrecioUnitario(Double.parseDouble(txtprecioUnitario.getText()));
    detalle.setCantidad(Integer.parseInt(txtcantidad.getText()));
    detalle.setNumeroFactura(cmdnumeroFactura.getSelectionModel().getSelectedItem().getNumeroFactura());
    detalle.setCodigoProducto(cmdcodigoProducto.getSelectionModel().getSelectedItem().getCodigoProducto());

    try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarDetalleFactura(?, ?, ?, ?, ?)}");
        procedimiento.setInt(1, detalle.getCodigoDetalleFactura());
        procedimiento.setDouble(2, detalle.getPrecioUnitario());
        procedimiento.setInt(3, detalle.getCantidad());
        procedimiento.setInt(4, detalle.getNumeroFactura());
        procedimiento.setString(5, detalle.getCodigoProducto());

        procedimiento.execute();

        // Agregar el detalle a la lista observable
        listaDetalleFactura.add(detalle);
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error al agregar el detalle de factura: " + e.getMessage());
    }
}


    public void actualizar() {
        DetalleFactura detalleSeleccionado = tvDetalleFactura.getSelectionModel().getSelectedItem();
        detalleSeleccionado.setPrecioUnitario(Double.parseDouble(txtprecioUnitario.getText()));
        detalleSeleccionado.setCantidad(Integer.parseInt(txtcantidad.getText()));
        detalleSeleccionado.setNumeroFactura(cmdnumeroFactura.getSelectionModel().getSelectedItem().getNumeroFactura());
        detalleSeleccionado.setCodigoProducto(cmdcodigoProducto.getSelectionModel().getSelectedItem().getCodigoProducto());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ActualizarDetalleFactura(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, detalleSeleccionado.getCodigoDetalleFactura());
            procedimiento.setDouble(2, detalleSeleccionado.getPrecioUnitario());
            procedimiento.setInt(3, detalleSeleccionado.getCantidad());
            procedimiento.setInt(4, detalleSeleccionado.getNumeroFactura());
            procedimiento.setString(5, detalleSeleccionado.getCodigoProducto());

            procedimiento.execute();
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el detalle de factura: " + e.getMessage());
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tvDetalleFactura.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                    imgReporte.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                    activarControles();
                    tipoDeOperaciones = operaciones.EDITAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
            case EDITAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NULL;
                break;
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
            if (tvDetalleFactura.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Detalle de Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarDetalleFactura(?)}");
                        procedimiento.setInt(1, tvDetalleFactura.getSelectionModel().getSelectedItem().getCodigoDetalleFactura());
                        procedimiento.execute();
                        listaDetalleFactura.remove(tvDetalleFactura.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error al eliminar el detalle de factura: " + e.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
            }
        }
    }

    public void activarControles() {
        txtcodigoDetalleFactura.setEditable(true);
        txtprecioUnitario.setEditable(true);
        txtcantidad.setEditable(true);
        cmdnumeroFactura.setDisable(false);
        cmdcodigoProducto.setDisable(false);
    }

    public void desactivarControles() {
        txtcodigoDetalleFactura.setEditable(false);
        txtprecioUnitario.setEditable(false);
        txtcantidad.setEditable(false);
        cmdnumeroFactura.setDisable(true);
        cmdcodigoProducto.setDisable(true);
    }

    public void limpiarControles() {
        txtcodigoDetalleFactura.clear();
        txtprecioUnitario.clear();
        txtcantidad.clear();
        cmdnumeroFactura.getSelectionModel().clearSelection();
        cmdcodigoProducto.getSelectionModel().clearSelection();
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
