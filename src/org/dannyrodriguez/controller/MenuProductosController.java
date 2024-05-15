/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dannyrodriguez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.dannyrodriguez.bean.Productos;
import org.dannyrodriguez.system.Principal;

/**
 *
 * @author informatica
 */
public class MenuProductosController implements Initializable {

    private Principal escenararioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Productos> listaProductos;
    @FXML
    private Button btnRegresar;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporteProducto;
    @FXML
    private Button btnAgregarProducto;
    @FXML
    private Button btnEliminarProducto;
    @FXML
    private Button btnReportesProducto;
    @FXML
    private Button btnEditarProducto;
    @FXML
    private TextField txtcodigoProducto;
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
    @FXML 
    private ComboBox cmbCodigoTipoProducto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void desactivarControles(){
    
    
    }
    
    

}
