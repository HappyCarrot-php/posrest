package com.restaurante.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Utilitario centralizado para cargar y mostrar reportes de JasperReports.
 */
public final class JasperReportManager {

    private static final String REPORT_FOLDER = "/reportes/";

    private JasperReportManager() {
    }

    /**
     * Compila (si es necesario) y muestra un reporte Jasper en pantalla usando la
     * conexión configurada en {@link ConexionDB}.
     *
     * @param nombreReporte nombre base del archivo dentro de src/main/resources/reportes (sin extensión)
     * @param parametros    mapa de parámetros a inyectar en el reporte
     * @throws JRException   si falla la carga o generación del reporte
     * @throws SQLException  si no es posible obtener la conexión a la base de datos
     */
    public static void mostrarReporteEnPantalla(String nombreReporte, Map<String, Object> parametros)
        throws JRException, SQLException {

        JasperReport reporte = cargarReporte(nombreReporte);
        Connection conexion = ConexionDB.obtenerConexion();
        JasperPrint impresion = JasperFillManager.fillReport(reporte, parametros, conexion);

        JasperViewer viewer = new JasperViewer(impresion, false);
        viewer.setTitle("Reportes Jasper");
        viewer.setVisible(true);
    }

    /**
     * Intenta cargar un archivo .jasper precompilado y, si no existe, compila el .jrxml
     * correspondiente almacenado en el mismo directorio.
     *
     * @param nombreReporte nombre base del archivo (sin extensión)
     * @return reporte Jasper listo para ser llenado con datos
     * @throws JRException si los archivos no existen o hay errores al compilarlos
     */
    public static JasperReport cargarReporte(String nombreReporte) throws JRException {
        String basePath = REPORT_FOLDER + nombreReporte;

        try (InputStream jasperStream = JasperReportManager.class.getResourceAsStream(basePath + ".jasper")) {
            if (jasperStream != null) {
                return (JasperReport) JRLoader.loadObject(jasperStream);
            }
        } catch (IOException e) {
            throw new JRException("Error al leer el archivo .jasper: " + e.getMessage(), e);
        }

        try (InputStream jrxmlStream = JasperReportManager.class.getResourceAsStream(basePath + ".jrxml")) {
            if (jrxmlStream == null) {
                throw new JRException("No se encontró la plantilla Jasper: " + basePath + ".jrxml");
            }
            return JasperCompileManager.compileReport(jrxmlStream);
        } catch (IOException e) {
            throw new JRException("Error al leer la plantilla .jrxml: " + e.getMessage(), e);
        }
    }
}
