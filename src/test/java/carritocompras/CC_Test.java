package test.java.carritocompras;

import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.testng.annotations.DataProvider;
import com.opencsv.exceptions.CsvValidationException;

import main.java.pageEvents.CC_PasosFuncionales;
import main.java.utils.GG_OpenCSV;
import main.java.utils.GG_Utils;
import test.java.GG_BaseTest;
import main.java.utils.CC_Parametros;

//Clase que contiene los Pasos Funcionales automatizados
public class CC_Test extends GG_BaseTest {
	static int gloFilas = 0;
	public static String gloVerFlujo = "S";

	@Test(enabled = true, dataProvider = "Data")
	public void CC_QA_Automatizacion(String args[]) throws InterruptedException {

		GG_Utils.infoTestCase("Carrito de compras",
				"Validar la generacion de una compra al agregar un producto al carrito de compras");

		CC_PasosFuncionales.iniciarSesion(args[0], args[1], "1");
		CC_PasosFuncionales.seleccionarProducto(args[2], args[3], args[4], "2");
	}
	
	@DataProvider(name = "Data")
	public Object[][] dataBrokerAPAlternative() throws CsvValidationException, InterruptedException, IOException {

		int xColumnas = CC_Parametros.gloColumnas;
		
		OpenTxt();
		System.out.println("*** Archivo leido: " + CC_Parametros.gloDir + "\\data\\TotalCasosDePruebas.txt");
		System.out.println("*** Total Casos de Prueba a ejecutar: " + gloFilas + ".");
		
		if (gloVerFlujo.equals("S")) {
			System.out.println("*** La ejecución del Flujo será visible por pantalla.");
		} else {
			System.out.println("*** La ejecución del Flujo NO será visible por pantalla, se ejecutará en Background.");
		}
		
		Object[][] data = GG_OpenCSV.getCSVParameters(CC_Parametros.gloNombreCSV, gloFilas, xColumnas);
		return data;
	}
	
public static void OpenTxt() {
		
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		
		gloFilas = 0;
		File archivo2 = new File(CC_Parametros.gloDir + "\\data\\TotalCasosDePruebas.txt");

        if (archivo2.exists()) {

			try {
				// Apertura del fichero y creacion de BufferedReader para poder
	
				archivo = new File(CC_Parametros.gloDir + "\\data\\TotalCasosDePruebas.txt");
				fr = new FileReader(archivo);
				br = new BufferedReader(fr);
	
				// Lectura del fichero
				String linea;
				
				// Linea 1: Leer comentario
				linea = br.readLine();
				
				// Linea 2: Leer Cantidad de Casos de Pruebas a ejecutar
				linea = br.readLine();
				gloFilas = Integer.valueOf(linea);
				
				// Linea 3: Leer indicador S/N, S=Ver Flujo por pantalla, N=No Ver Flujo, ejecutar en background
				linea = br.readLine();
				gloVerFlujo = linea;
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// En el finally cerramos el fichero, para asegurarnos
				// que se cierra tanto si todo va bien como si salta
				// una excepcion.
				try {
					if (null != fr) {
						fr.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
        }else {
        	System.out.println("Archivo no existe: " + CC_Parametros.gloDir + "\\data\\TotalCasosDePruebas.txt");
    	}
	}
}
