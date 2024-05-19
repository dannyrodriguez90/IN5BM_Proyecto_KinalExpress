/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dannyrodriguez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.dannyrodriguez.system.Principal;

public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    @FXML
    MenuItem btnMenuClientes;
    @FXML
    MenuItem btnMenuProgramador;
    @FXML
    MenuItem btnMenuProveedor;
    @FXML
    MenuItem btnTipoDeProducto;
    @FXML
    MenuItem btnMenuCompras;
    @FXML
    MenuItem btnMenuCargoEmpleado;
    @FXML
    MenuItem btnMenuProductos;
    @FXML
    MenuItem btnMenuEmailProveedor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnMenuClientes) {
            escenarioPrincipal.menuClienteView();
        } else if (event.getSource() == btnMenuProgramador) {
            escenarioPrincipal.menuProgramadorView();
        } else if (event.getSource() == btnMenuProveedor) {
            escenarioPrincipal.menuProveedorView();
        } else if (event.getSource() == btnTipoDeProducto) {
            escenarioPrincipal.menuTipoDeProductoView();
        } else if (event.getSource() == btnMenuCompras) {
            escenarioPrincipal.menuComprasView();
        } else if (event.getSource() == btnMenuCargoEmpleado) {
            escenarioPrincipal.menuCargoEmpleadoView();
        }else if (event.getSource() == btnMenuProductos) {
            escenarioPrincipal.menuProductoView();
        }else if (event.getSource() == btnMenuEmailProveedor) {
            escenarioPrincipal.menuEmailProveedorView();
        }

    }
}
