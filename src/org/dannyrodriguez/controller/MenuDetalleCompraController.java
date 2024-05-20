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
import org.dannyrodriguez.bean.Productos;
import org.dannyrodriguez.bean.DetalleCompra;
import org.dannyrodriguez.bean.Compras;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuDetalleCompraController implements Initializable {

    private ObservableList<DetalleCompra> listaDetalleCompra;
    private ObservableList<Compras> listaCompras;
    private ObservableList<Productos> listaProductos;
    private Principal escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }

    private operaciones tipoDeOperaciones = operaciones.NULL;
    //@FXML
    //private ImageView imgAgregar;
    //@FXML
    //private ImageView imgEliminar;
    //@FXML
    //private ImageView imgEditar;
    //@FXML
    //private ImageView imgReporte;
    @FXML
    private TableView<DetalleCompra> tvDetalleCompra;
    @FXML
    private TableColumn<DetalleCompra, Integer> colCodigoDetalleCompra;
    @FXML
    private TableColumn<DetalleCompra, Double> colCostoUnitario;
    @FXML
    private TableColumn<DetalleCompra, Integer> colCantidad;
    @FXML
    private TableColumn<DetalleCompra, String> colCodigoProducto;
    @FXML
    private TableColumn<DetalleCompra, Integer> colNumeroDocumento;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtCodigoDetalleCompra;
    @FXML
    private TextField txtCostoUnitario;
    @FXML
    private TextField txtCantidad;
    @FXML
    private ComboBox<Productos> cmdCodigoProducto;
    @FXML
    private ComboBox<Compras> cmdNumeroDocumento;
    @FXML
    private Button btnRegresar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmdCodigoProducto.setItems(getProductos());
        cmdNumeroDocumento.setItems(getCompras());
    }

    public void cargarDatos() {
        tvDetalleCompra.setItems(getDetalleCompra());
        colCodigoDetalleCompra.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleCompra"));
        colCostoUnitario.setCellValueFactory(new PropertyValueFactory<>("costoUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));
    }

    public void seleccionar() {
        if (tvDetalleCompra.getSelectionModel().getSelectedItem() != null) {
            DetalleCompra detalleSeleccionado = tvDetalleCompra.getSelectionModel().getSelectedItem();
            txtCodigoDetalleCompra.setText(String.valueOf(detalleSeleccionado.getCodigoDetalleCompra()));
            txtCostoUnitario.setText(String.valueOf(detalleSeleccionado.getCostoUnitario()));
            txtCantidad.setText(String.valueOf(detalleSeleccionado.getCantidad()));

            // Obtener el producto correspondiente al código del detalle seleccionado
            String codigoProducto = detalleSeleccionado.getCodigoProducto();
            for (Productos producto : listaProductos) {
                if (producto.getCodigoProducto().equals(codigoProducto)) {
                    cmdCodigoProducto.getSelectionModel().select(producto);
                    break;
                }
            }

            // Obtener la compra correspondiente al número de documento del detalle seleccionado
            int numeroDocumento = detalleSeleccionado.getNumeroDocumento();
            for (Compras compra : listaCompras) {
                if (compra.getNumeroDocumento() == numeroDocumento) {
                    cmdNumeroDocumento.getSelectionModel().select(compra);
                    break;
                }
            }
        }
    }

    public ObservableList<DetalleCompra> getDetalleCompra() {
        ArrayList<DetalleCompra> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_ListarDetallesCompra()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new DetalleCompra(
                                resultado.getInt("codigoDetalleCompra"),
                                resultado.getDouble("costoUnitario"),
                                resultado.getInt("cantidad"),
                                resultado.getString("codigoProducto"),
                                resultado.getInt("numeroDocumento")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDetalleCompra = FXCollections.observableList(lista);
    }

    public ObservableList<Compras> getCompras() {
        ArrayList<Compras> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_ListarCompras()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Compras(
                                resultado.getInt("numeroDocumento"),
                                resultado.getDate("fechaDocumento"),
                                resultado.getString("descripcion"),
                                resultado.getBigDecimal("totalDocumento")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCompras = FXCollections.observableList(lista);
    }

    public ObservableList<Productos> getProductos() {
        ArrayList<Productos> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_ListarProducto()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
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
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableList(lista);
    }

    public void agregar() {
        switch (tipoDeOperaciones) {
            case NULL:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                //imgAgregar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                //imgEliminar.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
                tipoDeOperaciones = operaciones.AGREGAR;
                break;
            case AGREGAR:
                guardar();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                //imgAgregar.setImage(new Image("/org/dannyrodriguez/images/agregar-producto.png"));
                //imgEliminar.setImage(new Image("/org/dannyrodriguez/images/carpeta.png"));
                tipoDeOperaciones = operaciones.NULL;
                break;
        }
    }

    public void guardar() {
        DetalleCompra nuevoDetalle = new DetalleCompra();
        nuevoDetalle.setCodigoDetalleCompra(Integer.parseInt(txtCodigoDetalleCompra.getText())); // Asignar el valor proporcionado por el usuario
        nuevoDetalle.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
        nuevoDetalle.setCantidad(Integer.parseInt(txtCantidad.getText()));
        nuevoDetalle.setCodigoProducto(cmdCodigoProducto.getValue().getCodigoProducto());
        nuevoDetalle.setNumeroDocumento(cmdNumeroDocumento.getValue().getNumeroDocumento());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarDetalleCompra(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, nuevoDetalle.getCodigoDetalleCompra()); // Agregar el código proporcionado por el usuario
            procedimiento.setDouble(2, nuevoDetalle.getCostoUnitario());
            procedimiento.setInt(3, nuevoDetalle.getCantidad());
            procedimiento.setString(4, nuevoDetalle.getCodigoProducto());
            procedimiento.setInt(5, nuevoDetalle.getNumeroDocumento());
            procedimiento.execute();
            listaDetalleCompra.add(nuevoDetalle);
            limpiarControles();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al agregar el detalle de compra: " + e.getMessage());
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tvDetalleCompra.getSelectionModel().getSelectedItem() != null) {
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    //imgEditar.setImage(new Image("/org/dannyrodriguez/images/guardar.png"));
                    //imgReporte.setImage(new Image("/org/dannyrodriguez/images/cancelar.png"));
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
        DetalleCompra detalleSeleccionado = tvDetalleCompra.getSelectionModel().getSelectedItem();
        if (detalleSeleccionado != null) {
            detalleSeleccionado.setCodigoDetalleCompra(Integer.parseInt(txtCodigoDetalleCompra.getText()));
            detalleSeleccionado.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
            detalleSeleccionado.setCantidad(Integer.parseInt(txtCantidad.getText()));
            detalleSeleccionado.setCodigoProducto(cmdCodigoProducto.getValue().getCodigoProducto());
            detalleSeleccionado.setNumeroDocumento(cmdNumeroDocumento.getValue().getNumeroDocumento());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ActualizarDetalleCompra(?, ?, ?, ?, ?)}");
                procedimiento.setInt(1, detalleSeleccionado.getCodigoDetalleCompra());
                procedimiento.setDouble(2, detalleSeleccionado.getCostoUnitario());
                procedimiento.setInt(3, detalleSeleccionado.getCantidad());
                procedimiento.setString(4, detalleSeleccionado.getCodigoProducto());
                procedimiento.setInt(5, detalleSeleccionado.getNumeroDocumento());
                procedimiento.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error al actualizar el detalle de compra: " + e.getMessage());
            }
        } else {
            System.out.println("Debe seleccionar un detalle de compra.");
        }
    }


    public void eliminar() {
        if (tvDetalleCompra.getSelectionModel().getSelectedItem() != null) {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Detalle de Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarDetalleCompra(?)}");
                    procedimiento.setInt(1, tvDetalleCompra.getSelectionModel().getSelectedItem().getCodigoDetalleCompra());
                    procedimiento.execute();
                    listaDetalleCompra.remove(tvDetalleCompra.getSelectionModel().getSelectedIndex());
                    limpiarControles();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error al eliminar el detalle de compra: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Debe seleccionar un elemento.");
        }
    }

    public void activarControles() {
        txtCostoUnitario.setEditable(true);
        txtCantidad.setEditable(true);
        cmdCodigoProducto.setDisable(false);
        cmdNumeroDocumento.setDisable(false);
    }

    public void desactivarControles() {
        txtCostoUnitario.setEditable(false);
        txtCantidad.setEditable(false);
        cmdCodigoProducto.setDisable(true);
        cmdNumeroDocumento.setDisable(true);
    }

    public void limpiarControles() {
        txtCostoUnitario.clear();
        txtCantidad.clear();
        cmdCodigoProducto.getSelectionModel().clearSelection();
        cmdNumeroDocumento.getSelectionModel().clearSelection();
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
