import java.util.Map;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.Connection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Principal {

	public static void main(String[] args) {
		String reportSource = "./plantilla/plantilla_Actividad13.jrxml";
		String reportHTML = "./informes/Informe.html";
		String reportPDF = "./informes/Informe.pdf";
		String reportXML = "./informes/Informe.xml";

		
		//Los parámetros se almacenan en un HashMap, para posteriormente crear el jprint (JasperPrint)
		//añadiendo los parámetros (título, autor y fecha)
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("titulo", "RESUMEN DATOS DE DEPARTAMENTOS.");
		params.put("autor", "ARM");
		params.put("fecha", (new java.util.Date()).toString());

		try {
			//Se compila la plantilla para obtener un objeto JasperReport            
            JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
            
            //Carga driver y realiza laconexión a la Base de Datos
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/ejemplo", "root", "");
			
			//Obtenemos los datos del informe "JasperFillManager.fillreport" y genera un fichero .jprint
			//con el objeto jasperReport, los parámetros del informe y la conexión a la BD
			JasperPrint MiInforme = JasperFillManager.fillReport(jasperReport, params, conn);

			// Visualizar en pantalla
			//Si no queremos que se abra el visor, unicamente generar el informe, JasperViewer.viewReport(MiInforme,false);
			JasperViewer.viewReport(MiInforme,false);
			
			// Generar el informe en HTML
			JasperExportManager.exportReportToHtmlFile(MiInforme, reportHTML);

			// Generar el informe en PDF
			JasperExportManager.exportReportToPdfFile(MiInforme, reportPDF);

			// Generar el informe en XML, false es para indicar que no hay imágenes
			// (isEmbeddingImages)
			JasperExportManager.exportReportToXmlFile(MiInforme, reportXML, false);

			System.out.println("ARCHIVOS CREADOS");
		} catch (CommunicationsException c) {
			System.out.println(" Error de comunicación con la BD. No está arrancada.");
		} catch (ClassNotFoundException e) {
			System.out.println(" Error driver. ");
		} catch (SQLException e) {
			System.out.println(" Error al ejecutar sentencia SQL ");
		} catch (JRException ex) {
			System.out.println(" Error Jasper.");
			ex.printStackTrace();
		}
	}

}
