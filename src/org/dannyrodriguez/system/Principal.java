
package org.dannyrodriguez.system;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.dannyrodriguez.controller.MenuCargoEmpleadoController;
import org.dannyrodriguez.controller.MenuClientesController;
import org.dannyrodriguez.controller.MenuComprasController;
import org.dannyrodriguez.controller.MenuEmailProveedorController;
import org.dannyrodriguez.controller.MenuEmpleadoController;
import org.dannyrodriguez.controller.MenuPrincipalController;
import org.dannyrodriguez.controller.MenuProductosController;
import org.dannyrodriguez.controller.MenuProgramadorController;
import org.dannyrodriguez.controller.MenuProveedorController;
import org.dannyrodriguez.controller.MenuTelefonoProveedorController;
import org.dannyrodriguez.controller.MenuTipoProductoController;
 
 
 
public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
 
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("KinalExpress");
         menuPrincipalView ();
//       Parent root = FXMLLoader.load(getClass().getResource("/org/dannyrodriguez/view/MenuPrincipalView.fxml"));
//       Scene escena = new Scene(root);
//       escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
    }
    public Initializable cambiarEscena(String fxmlname, int width, int heigth) throws Exception{
            Initializable resultado;
            FXMLLoader loader = new FXMLLoader();
            InputStream file  = Principal.class.getResourceAsStream("/org/dannyrodriguez/view/" + fxmlname);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(Principal.class.getResource("/org/dannyrodriguez/view/" + fxmlname));
            escena = new Scene ((AnchorPane)loader.load(file), width, heigth);
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.sizeToScene();
            resultado = (Initializable)loader.getController();

    return resultado;
    }
public void menuPrincipalView (){
        try{
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController)cambiarEscena
        ("MenuPrincipalView.fxml", 600,370);
            menuPrincipalView.setEscenarioPrincipal(this);  
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void menuClienteView (){
    try{
        MenuClientesController menuClienteView = (MenuClientesController)cambiarEscena
        ("MenuClienteView.fxml",685,490 );
        menuClienteView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }
    
        public void menuProgramadorView (){
    try{
        MenuProgramadorController menuProgramadorView = (MenuProgramadorController)cambiarEscena
        ("MenuProgramadorView.fxml",486,418 );
         menuProgramadorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }
        
        public void menuProveedorView (){
    try{
        MenuProveedorController menuProveedorView = (MenuProveedorController)cambiarEscena
        ("MenuProveedorView.fxml",760,500);
         menuProveedorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }
    
      public void menuTipoDeProductoView (){
    try{
        MenuTipoProductoController menuTipoDeProductoView = (MenuTipoProductoController)cambiarEscena
        ("MenuTipoProductoView.fxml",618,437 );
         menuTipoDeProductoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }
      
      public void menuComprasView (){
    try{
        MenuComprasController menuComprasView = (MenuComprasController)cambiarEscena
        ("MenuComprasView.fxml",700,470 );
        menuComprasView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }
      
      public void menuCargoEmpleadoView (){
    try{
        MenuCargoEmpleadoController menuCargoEmpleadoView = (MenuCargoEmpleadoController)cambiarEscena
        ("MenuCargoEmpleadoView.fxml",625,440 );
        menuCargoEmpleadoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }
      
      public void menuProductoView (){
    try{
        MenuProductosController menuProductoView = (MenuProductosController)cambiarEscena
        ("MenuProductosView.fxml",625,440 );
        menuProductoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }
      
     public void menuEmailProveedorView (){
    try{
        MenuEmailProveedorController menuEmailProveedorView = (MenuEmailProveedorController)cambiarEscena
        ("MenuEmailProveedorView.fxml",625,440 );
        menuEmailProveedorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }
    
    public void menuEmpleadoView (){
    try{
        MenuEmpleadoController menuEmpleadoView = (MenuEmpleadoController)cambiarEscena
        ("MenuEmpleadosView.fxml",625,440 );
        menuEmpleadoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }
    
    public void menuTelefonoProveedorView (){
    try{
        MenuTelefonoProveedorController menuTelefonoProveedorView = (MenuTelefonoProveedorController)cambiarEscena
        ("MenuTelefonoProveedorView.fxml",625,440 );
        menuTelefonoProveedorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage()); 
        }
    }

    
    
    public static void main(String[] args) {
        launch(args);   
    }
}