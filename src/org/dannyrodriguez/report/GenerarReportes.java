
package org.dannyrodriguez.report;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.dannyrodriguez.db.Conexion;

public class GenerarReportes {
    public static void mostrarReportes(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport Reporte_Cliente = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteimpreso = JasperFillManager.fillReport(Reporte_Cliente, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteimpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        }catch (Exception e){
                e.printStackTrace();
        }
    }
    
     public static void mostrarReportesProveedores(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport Reporte_Proveedores = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteimpreso = JasperFillManager.fillReport(Reporte_Proveedores, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteimpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        }catch (Exception e){
                e.printStackTrace();
        }
    }
     
     public static void mostrarReportesProductos(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport Reporte_Productos = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteimpreso = JasperFillManager.fillReport(Reporte_Productos, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteimpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        }catch (Exception e){
                e.printStackTrace();
        }
    }
     public static void mostrarReportesFactura(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport Reporte_Factura = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteimpreso = JasperFillManager.fillReport(Reporte_Factura, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteimpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        }catch (Exception e){
                e.printStackTrace();
        }
    }
     


}