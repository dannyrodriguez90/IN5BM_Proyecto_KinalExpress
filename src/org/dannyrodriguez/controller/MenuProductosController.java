package org.dannyrodriguez.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.dannyrodriguez.bean.Proveedor;
import org.dannyrodriguez.bean.TipoDeProducto;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.report.GenerarReportes;
import org.dannyrodriguez.system.Principal;

public class MenuProductosController implements Initializable {

    private ObservableList<Productos> listaProducto;
    private ObservableList<Proveedor> listaProveedor;
    private ObservableList<TipoDeProducto> listaTipoDeProducto;
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
    private TableView<Productos> tvProductos;
    @FXML
    private TableColumn<Productos, String> colCodProd;
    @FXML
    private TableColumn<Productos, String> colDescProd;
    @FXML
    private TableColumn<Productos, Double> colPrecioU;
    @FXML
    private TableColumn<Productos, Double> colPrecioD;
    @FXML
    private TableColumn<Productos, Double> colPrecioM;
    @FXML
    private TableColumn<Productos, Integer> colExistencia;
    @FXML
    private TableColumn<Productos, Integer> colCodTipoProd;
    @FXML
    private TableColumn<Productos, Integer> colCodProv;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtCodigoProd;
    @FXML
    private TextField txtDescPro;
    @FXML
    private TextField txtPrecioU;
    @FXML
    private TextField txtPrecioD;
    @FXML
    private TextField txtPrecioM;
    @FXML
    private TextField txtExistencia;
    @FXML
    private ComboBox<TipoDeProducto> cmbCodigoTipoProducto;
    @FXML
    private ComboBox<Proveedor> cmbCodigoProveedor;
    @FXML
    private Button btnRegresar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoProveedor.setItems(getProveedor());
        cmbCodigoTipoProducto.setItems(getTipoDeProducto());
    }

    public void cargarDatos() {
        tvProductos.setItems(getProductos());
        colCodProd.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colDescProd.setCellValueFactory(new PropertyValueFactory<>("descripcionProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<>("precioDocena"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<>("precioMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<>("codigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
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
                                resultado.getInt("codigoTipoProducto"),
                                resultado.getInt("codigoProveedor")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableList(lista);
    }

    public ObservableList<TipoDeProducto> getTipoDeProducto() {
        ArrayList<TipoDeProducto> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_ListarTipoDeProducto()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new TipoDeProducto(
                                resultado.getInt("codigoTipoProducto"),
                                resultado.getString("descripcion")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTipoDeProducto = FXCollections.observableList(lista);
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
                    imgAgregar.setImage(new Image("/org/dannyrodriguez/images/agregar-producto.png"));
                    imgEliminar.setImage(new Image("/org/dannyrodriguez/images/carpeta.png"));
                    tipoDeOperaciones = operaciones.NULL;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.err.println("Error al agregar el producto: " + e.getMessage());
                }
                break;
        }
    }

    public void guardar() {
        Productos reg = new Productos();

        // Obtener el tipo de producto seleccionado del ComboBox
        TipoDeProducto tipoProductoSeleccionado = cmbCodigoTipoProducto.getSelectionModel().getSelectedItem();
        if (tipoProductoSeleccionado == null) {
            System.err.println("Error: Debe seleccionar un tipo de producto válido.");
            return;
        }

        // Obtener el proveedor seleccionado del ComboBox
        Proveedor proveedorSeleccionado = cmbCodigoProveedor.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado == null) {
            System.err.println("Error: Debe seleccionar un proveedor válido.");
            return;
        }

        // Configurar los atributos del producto con los valores de los campos de texto y los ComboBox
        reg.setCodigoProducto(txtCodigoProd.getText());
        reg.setDescripcionProducto(txtDescPro.getText());
        reg.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
        reg.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
        reg.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
        reg.setExistencia(Integer.parseInt(txtExistencia.getText()));
        reg.setCodigoTipoProducto(tipoProductoSeleccionado.getCodigoTipoProducto());
        reg.setCodigoProveedor(proveedorSeleccionado.getCodigoProveedor());

        try {
            // Preparar la llamada al procedimiento almacenado para agregar el producto
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, reg.getCodigoProducto());
            procedimiento.setString(2, reg.getDescripcionProducto());
            procedimiento.setDouble(3, reg.getPrecioUnitario());
            procedimiento.setDouble(4, reg.getPrecioDocena());
            procedimiento.setDouble(5, reg.getPrecioMayor());
            procedimiento.setInt(6, reg.getExistencia());
            procedimiento.setInt(7, reg.getCodigoTipoProducto());
            procedimiento.setInt(8, reg.getCodigoProveedor());

            // Ejecutar el procedimiento almacenado
            procedimiento.execute();

            // Agregar el producto a la lista observable
            listaProducto.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el producto: " + e.getMessage());
        }
    }

    public void seleccionar() {
        if (tvProductos.getSelectionModel().getSelectedItem() != null) {
            Productos productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
            txtCodigoProd.setText(productoSeleccionado.getCodigoProducto());
            txtDescPro.setText(productoSeleccionado.getDescripcionProducto());
            txtPrecioU.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
            txtPrecioD.setText(String.valueOf(productoSeleccionado.getPrecioDocena()));
            txtPrecioM.setText(String.valueOf(productoSeleccionado.getPrecioMayor()));
            txtExistencia.setText(String.valueOf(productoSeleccionado.getExistencia()));
            cmbCodigoTipoProducto.getSelectionModel().select((productoSeleccionado.getCodigoTipoProducto()));
            cmbCodigoProveedor.getSelectionModel().select((productoSeleccionado.getCodigoProveedor()));
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tvProductos.getSelectionModel().getSelectedItem() != null) {
                    Productos productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
                    txtCodigoProd.setText(productoSeleccionado.getCodigoProducto());
                    txtDescPro.setText(productoSeleccionado.getDescripcionProducto());
                    txtPrecioU.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
                    txtPrecioD.setText(String.valueOf(productoSeleccionado.getPrecioDocena()));
                    txtPrecioM.setText(String.valueOf(productoSeleccionado.getPrecioMayor()));
                    txtExistencia.setText(String.valueOf(productoSeleccionado.getExistencia()));
                    cmbCodigoTipoProducto.getSelectionModel().select((productoSeleccionado.getCodigoTipoProducto()));
                    cmbCodigoProveedor.getSelectionModel().select((productoSeleccionado.getCodigoProveedor()));
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
        Productos productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
        productoSeleccionado.setCodigoProducto(txtCodigoProd.getText());
        productoSeleccionado.setDescripcionProducto(txtDescPro.getText());
        productoSeleccionado.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText())); // Corregido
        productoSeleccionado.setPrecioDocena(Double.parseDouble(txtPrecioD.getText())); // Corregido
        productoSeleccionado.setPrecioMayor(Double.parseDouble(txtPrecioM.getText())); // Corregido
        productoSeleccionado.setExistencia(Integer.parseInt(txtExistencia.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, productoSeleccionado.getCodigoProducto());
            procedimiento.setString(2, productoSeleccionado.getDescripcionProducto());
            procedimiento.setDouble(3, productoSeleccionado.getPrecioUnitario());
            procedimiento.setDouble(4, productoSeleccionado.getPrecioDocena());
            procedimiento.setDouble(5, productoSeleccionado.getPrecioMayor());
            procedimiento.setInt(6, productoSeleccionado.getExistencia());
            procedimiento.setInt(7, productoSeleccionado.getCodigoTipoProducto());
            procedimiento.setInt(8, productoSeleccionado.getCodigoProveedor());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el producto: " + e.getMessage());
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
            if (tvProductos.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarProducto(?)}");
                        procedimiento.setString(1, tvProductos.getSelectionModel().getSelectedItem().getCodigoProducto());
                        procedimiento.execute();
                        listaProducto.remove(tvProductos.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error al eliminar el producto: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Debe seleccionar un elemento.");
            }
        }
    }

    // Métodos de activación, desactivación y limpieza de controles
    public void activarControles() {
        txtCodigoProd.setEditable(true);
        txtDescPro.setEditable(true);
        txtPrecioU.setEditable(true);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoTipoProducto.setDisable(false);
        cmbCodigoProveedor.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoProd.setEditable(false);
        txtDescPro.setEditable(false);
        txtPrecioU.setEditable(false);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtExistencia.setEditable(false);
        cmbCodigoTipoProducto.setDisable(true);
        cmbCodigoProveedor.setDisable(true);
    }

    public void limpiarControles() {
        txtDescPro.clear();
        txtDescPro.clear();
        txtPrecioU.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtExistencia.clear();
        cmbCodigoTipoProducto.getSelectionModel().clearSelection();
        cmbCodigoProveedor.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void reporte(){
        switch (tipoDeOperaciones) {
            case NULL:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEliminar.setText("Editar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = MenuProductosController.operaciones.NULL;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        GenerarReportes.mostrarReportesProductos("Reporte_Productos.jasper", "reporte de Productos", parametros);
    }

    

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }

    }
}
