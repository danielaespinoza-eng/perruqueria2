package main.java.pageEvents;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.java.pageObjects.CC_Localizadores;
import main.java.utils.GG_ElementFetch;
import main.java.utils.GG_Eventos;
import main.java.utils.GG_Utils;
import main.java.utils.GG_Validations;
import test.java.carritocompras.CC_Test;

//En esta clase se ejecutan los Pasos de la P�gina.
public class CC_PasosFuncionales extends CC_Test{

	public CC_PasosFuncionales(WebDriver driver) {
		CC_Test.driver = driver;
	}

	public static void iniciarSesion(String usuario, String contrasena, String xNumero) {
		
		String currentEvent = new Throwable().getStackTrace()[0].getMethodName();

		try {
			GG_Utils.outputInfo(xNumero + ") PASO FUNCIONAL iniciado: " + currentEvent);

			WebDriverWait wait = new WebDriverWait(driver, 50);
			GG_ElementFetch elementFetch = new GG_ElementFetch();
			
			Thread.sleep(1000);

			//Se escribe el Nombre del Usuario
			WebElement inputNombreUsuarioElement = elementFetch.getWebElement("XPATH", CC_Localizadores.inputUsuario);
			wait.until(ExpectedConditions.visibilityOf(inputNombreUsuarioElement));
			GG_Eventos.writeOnInput(inputNombreUsuarioElement, usuario);
			
			Thread.sleep(3000);

			//Se escribe la Contrasena
			WebElement inputContrasenaElement = elementFetch.getWebElement("XPATH", CC_Localizadores.inputContrasena);
			wait.until(ExpectedConditions.visibilityOf(inputContrasenaElement));
			GG_Eventos.writeOnInput(inputContrasenaElement, contrasena);
			
			Thread.sleep(5000);

			//Click en Boton rojo LOGIN
			WebElement buttonIniciarSesionElement = elementFetch.getWebElement("XPATH", CC_Localizadores.buttonIniciarSesion);
			wait.until(ExpectedConditions.elementToBeClickable(buttonIniciarSesionElement));
			GG_Eventos.clickButton(buttonIniciarSesionElement);
			
			Thread.sleep(2000);

			//Verifica si se llego a la segunda pantalla.
			WebElement labelPaginaProductosElement = elementFetch.getWebElement("XPATH", CC_Localizadores.labelPaginaProductos);
			wait.until(ExpectedConditions.visibilityOf(labelPaginaProductosElement));
			String textoPagina = labelPaginaProductosElement.getText();

			GG_Validations.trueBooleanCondition(textoPagina.equalsIgnoreCase("Products"),
					"Se ha ingresado correctamente a la pagina: " + textoPagina,
					"No se ha ingresado correctamente a la pagina: ", currentEvent);

		} catch (Exception e) {
			GG_Utils.eventFailed(currentEvent, e.getMessage());
		}
	}

	public static void seleccionarProducto(String producto, String producto2, String totalProductos, String xNumero) {
		int xCuenta = 0;
		String currentEvent = new Throwable().getStackTrace()[0].getMethodName();

		try {
			
			GG_Utils.outputInfo(xNumero + ") PASO FUNCIONAL iniciado: " + currentEvent);

			WebDriverWait wait = new WebDriverWait(driver, 50);
			GG_ElementFetch elementFetch = new GG_ElementFetch();

			List<WebElement> listProductosElement = elementFetch.getListWebElements("XPATH", CC_Localizadores.listProductos);
			wait.until(ExpectedConditions.visibilityOfAllElements(listProductosElement));
			
			Thread.sleep(2000);
			
			//Se recorre la Lista para buscar los Productos a seleccionar
			for (int i = 0; i < listProductosElement.size(); i++) {
				String nombreProducto = listProductosElement.get(i).getText();
				if ((nombreProducto.equalsIgnoreCase(producto)) || (nombreProducto.equalsIgnoreCase(producto2))) {
					//Se selecciona el producto
					xCuenta += 1;
					GG_Utils.outputInfo("-- Producto Nro. " + xCuenta + " a seleccionar: '" + nombreProducto + "'");
					
					//Hacer Click en el Producto seleccionado
					WebElement buttonAgregarElement = elementFetch.getWebElement("XPATH", CC_Localizadores.buttonAgregarCarro.replace("0", String.valueOf(i+1)));
					GG_Eventos.clickButton(buttonAgregarElement);
					
					Thread.sleep(2000);
				}
			}
			
			Thread.sleep(2000);
			
			//Se verifica que el carrito tenga el mismo numero que los Productos seleccionados
			WebElement spanCarritoElement = elementFetch.getWebElement("XPATH", CC_Localizadores.labelNumeroCarrito);
			wait.until(ExpectedConditions.visibilityOf(spanCarritoElement));
			String cantidadCarrito = spanCarritoElement.getText();
			GG_Validations.trueBooleanCondition(cantidadCarrito.contains(totalProductos),
					"Se ha agregado producto(s) al carrito correctamente",
					"No se ha agregado producto(s) al carrito correctamente, se esperaba '" + totalProductos + "'", currentEvent);
			
			Thread.sleep(2000);

		} catch (Exception e) {
			GG_Utils.eventFailed(currentEvent, "('" + producto + "' / '" + producto2 + "') " + e.getMessage());
		}
	}
}
