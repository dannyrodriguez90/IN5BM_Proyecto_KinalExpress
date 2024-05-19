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
import org.dannyrodriguez.bean.CargoEmpleado;
import org.dannyrodriguez.bean.Empleados;
import org.dannyrodriguez.db.Conexion;
import org.dannyrodriguez.system.Principal;

public class MenuEmpleadoController implements Initializable {
    private ObservableList<Empleados> listaEmpleados;
    private ObservableList<CargoEmpleado> listaCargo;
    private Principal escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }

    private operaciones tipoDeOperaciones = operaciones.NULL;

    @FXML
    private TableView<Empleados> tvEmpleados;
    @FXML
    private TableColumn<Empleados, Integer> colCodigoEmpleado;
    @FXML
    private TableColumn<Empleados, String> colNombreEmpleado;
    @FXML
    private TableColumn<Empleados, String> colApellidoEmpleado;
    @FXML
    private TableColumn<Empleados, Double> colSueldo;
    @FXML
    private TableColumn<Empleados, String> colDireccion;
    @FXML
    private TableColumn<Empleados, String> colTurno;
    @FXML
    private TableColumn<Empleados, Integer> colCargoEmpleado;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtCodigoEmpleado;
    @FXML
    private TextField txtNombreEmpleado;
    @FXML
    private TextField txtApellidoEmpleado;
    @FXML
    private TextField txtSueldo;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTurno;
    @FXML
    private ComboBox<CargoEmpleado> cmbCargoEmpleado;
    @FXML
    private Button btnRegresar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCargoEmpleado.setItems(getCargoEmpleado());
    }

    public void cargarDatos() {
        tvEmpleados.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombresEmpleado"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<>("apellidosEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));
        colCargoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoCargoEmpleado"));
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

    public ObservableList<CargoEmpleado> getCargoEmpleado() {
        ArrayList<CargoEmpleado> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_ListarCargoEmpleado()}");
                     ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new CargoEmpleado(
                                resultado.getInt("codigoCargoEmpleado"),
                                resultado.getString("nombreCargo"),
                                resultado.getString("descripcionCargo")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCargo = FXCollections.observableList(lista);
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
                    System.err.println("Error al agregar el empleado: " + e.getMessage());
                }
                break;
        }
    }

   public void guardar() {
    Empleados reg = new Empleados();
    Object cargoSeleccionadoObj = cmbCargoEmpleado.getSelectionModel().getSelectedItem();
    
    if (cargoSeleccionadoObj instanceof CargoEmpleado) {
        CargoEmpleado cargoSeleccionado = (CargoEmpleado) cargoSeleccionadoObj;
        reg.setCodigoCargoEmpleado(cargoSeleccionado.getCodigoCargoEmpleado());
    } else {
        System.err.println("Error: Debe seleccionar un cargo válido.");
        return;
    }
    
    reg.setCodigoEmpleado(Integer.parseInt(txtCodigoEmpleado.getText()));
    reg.setNombresEmpleado(txtNombreEmpleado.getText());
    reg.setApellidosEmpleado(txtApellidoEmpleado.getText());
    reg.setSueldo(Double.parseDouble(txtSueldo.getText()));
    reg.setDireccion(txtDireccion.getText());
    reg.setTurno(txtTurno.getText());
    
    try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoEmpleado());
        procedimiento.setString(2, reg.getNombresEmpleado());
        procedimiento.setString(3, reg.getApellidosEmpleado());
        procedimiento.setDouble(4, reg.getSueldo());
        procedimiento.setString(5, reg.getDireccion());
        procedimiento.setString(6, reg.getTurno());
        procedimiento.setInt(7, reg.getCodigoCargoEmpleado());
        procedimiento.execute();
        listaEmpleados.add(reg);
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error al guardar el empleado: " + e.getMessage());
    }
}


    public void seleccionar() {
        if (tvEmpleados.getSelectionModel().getSelectedItem() != null) {
            Empleados empleadoSeleccionado = tvEmpleados.getSelectionModel().getSelectedItem();
            txtCodigoEmpleado.setText(String.valueOf(empleadoSeleccionado.getCodigoEmpleado()));
            txtNombreEmpleado.setText(empleadoSeleccionado.getNombresEmpleado());
            txtApellidoEmpleado.setText(empleadoSeleccionado.getApellidosEmpleado());
            txtSueldo.setText(String.valueOf(empleadoSeleccionado.getSueldo()));
            txtDireccion.setText(empleadoSeleccionado.getDireccion());
            txtTurno.setText(empleadoSeleccionado.getTurno());
            cmbCargoEmpleado.getSelectionModel().select(buscarCargoEmpleado(empleadoSeleccionado.getCodigoCargoEmpleado()));
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tvEmpleados.getSelectionModel().getSelectedItem() != null) {
                    Empleados empleadoSeleccionado = tvEmpleados.getSelectionModel().getSelectedItem();
                    txtCodigoEmpleado.setText(String.valueOf(empleadoSeleccionado.getCodigoEmpleado()));
                    txtNombreEmpleado.setText(empleadoSeleccionado.getNombresEmpleado());
                    txtApellidoEmpleado.setText(empleadoSeleccionado.getApellidosEmpleado());
                    txtSueldo.setText(String.valueOf(empleadoSeleccionado.getSueldo()));
                    txtDireccion.setText(empleadoSeleccionado.getDireccion());
                    txtTurno.setText(empleadoSeleccionado.getTurno());
                    cmbCargoEmpleado.getSelectionModel().select(buscarCargoEmpleado(empleadoSeleccionado.getCodigoCargoEmpleado()));
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un empleado");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                cargarDatos();
                limpiarControles();
                desactivarControles();
                break;
        }
    }

    public void actualizar() {
    try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_actualizarEmpleado(?, ?, ?, ?, ?, ?, ?)}");
        Empleados empleadoSeleccionado = tvEmpleados.getSelectionModel().getSelectedItem();
        empleadoSeleccionado.setNombresEmpleado(txtNombreEmpleado.getText());
        empleadoSeleccionado.setApellidosEmpleado(txtApellidoEmpleado.getText());
        empleadoSeleccionado.setSueldo(Double.parseDouble(txtSueldo.getText()));
        empleadoSeleccionado.setDireccion(txtDireccion.getText());
        empleadoSeleccionado.setTurno(txtTurno.getText());
        CargoEmpleado cargoSeleccionado = cmbCargoEmpleado.getSelectionModel().getSelectedItem();
        procedimiento.setInt(1, empleadoSeleccionado.getCodigoEmpleado());
        procedimiento.setString(2, empleadoSeleccionado.getNombresEmpleado());
        procedimiento.setString(3, empleadoSeleccionado.getApellidosEmpleado());
        procedimiento.setDouble(4, empleadoSeleccionado.getSueldo());
        procedimiento.setString(5, empleadoSeleccionado.getDireccion());
        procedimiento.setString(6, empleadoSeleccionado.getTurno());
        procedimiento.setInt(7, cargoSeleccionado.getCodigoCargoEmpleado());
        procedimiento.execute();
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error al actualizar el empleado: " + e.getMessage());
    }
}

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case AGREGAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                break;
            default:
                if (tvEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarEmpleado(?)}");
                            procedimiento.setInt(1, tvEmpleados.getSelectionModel().getSelectedItem().getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleados.remove(tvEmpleados.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un empleado");
                }
                break;
        }
    }

    private void activarControles() {
        txtCodigoEmpleado.setEditable(true);
        txtNombreEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtSueldo.setEditable(true);
        txtDireccion.setEditable(true);
        txtTurno.setEditable(true);
        cmbCargoEmpleado.setDisable(false);
    }

    private void desactivarControles() {
        txtCodigoEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtSueldo.setEditable(false);
        txtDireccion.setEditable(false);
        txtTurno.setEditable(false);
        cmbCargoEmpleado.setDisable(true);
    }

    private void limpiarControles() {
        txtCodigoEmpleado.clear();
        txtNombreEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtSueldo.clear();
        txtDireccion.clear();
        txtTurno.clear();
        cmbCargoEmpleado.getSelectionModel().clearSelection();
    }

    

   public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public CargoEmpleado buscarCargoEmpleado(int codigoCargoEmpleado) {
        for (CargoEmpleado cargo : listaCargo) {
            if (cargo.getCodigoCargoEmpleado() == codigoCargoEmpleado) {
                return cargo;
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
