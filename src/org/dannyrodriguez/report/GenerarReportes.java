
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
    public static void mostrarReoportes(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport ReporteClientes2 = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteimpreso = JasperFillManager.fillReport(ReporteClientes2, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteimpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        }catch (Exception e){
                e.printStackTrace();
        }
    }

}