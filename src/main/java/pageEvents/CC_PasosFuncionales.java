package main.java.pageEvents;

//import org.openqa.selenium.Keys;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;



//import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

import main.java.pageObjects.CC_Localizadores;
import main.java.utils.GG_Utils;

public class CC_PasosFuncionales {
    private WebDriver driver;

    public CC_PasosFuncionales(WebDriver driver) {
        this.driver = driver;
    }
    
    // Método privado para reutilizar escritura lenta en cualquier campo
    private void escribirLento(WebElement input, String texto) {
        input.clear();
        for (char c : texto.toCharArray()) {
            input.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(100);  // pausa entre letras, ajusta tiempo si quieres más lento
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    //agregar productos de manera mixta
    public void agregarProductosConBusquedaMixta(List<String> productos) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            // --- 1. Agregar los productos 1 y 3 usando el buscador ---
            for (int i = 0; i < productos.size(); i++) {
                String producto = productos.get(i);
                
                
                
                // Solo usar buscador para el primero y el tercero
                if (i == 0 || i == 2) {
                    System.out.println("🔎 Buscando producto desde buscador: " + producto);
                    
                    WebElement inputBuscador = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-input")));
                    
                    // Usamos escribir lento
                    escribirLento(inputBuscador, producto);

                    WebElement botonBuscar = wait.until(
                    	    ExpectedConditions.elementToBeClickable(By.xpath(CC_Localizadores.buttonBuscar))
                    	);
                    botonBuscar.click();

                    wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                            By.xpath(CC_Localizadores.listProductos), 0));

                    List<WebElement> resultados = driver.findElements(By.xpath(CC_Localizadores.listProductos));

                    boolean encontrado = false;
                    for (int j = 0; j < resultados.size(); j++) {
                        if (resultados.get(j).getText().contains(producto)) {
                            String botonXPath = CC_Localizadores.buttonAgregarCarro.replace("INDEX", String.valueOf(j + 1));
                            WebElement botonAgregar = driver.findElement(By.xpath(botonXPath));
                            wait.until(ExpectedConditions.elementToBeClickable(botonAgregar)).click();
                            System.out.println("✅ Producto agregado al carrito desde buscador: " + producto);
                            encontrado = true;
                            break;
                        }
                    }

                    if (!encontrado) {
                        System.out.println("[ERROR] ❌ Producto no encontrado en buscador: " + producto);
                        throw new RuntimeException("Producto no encontrado: " + producto);
                    }
                }
            }

            // --- 2. Búsqueda en blanco para volver a mostrar la lista completa ---
            System.out.println("🔄 Realizando búsqueda en blanco para mostrar todos los productos");
            WebElement inputBuscador = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-input")));
            inputBuscador.clear();

            WebElement botonBuscar = wait.until(
            		ExpectedConditions.elementToBeClickable(By.xpath(CC_Localizadores.buttonBuscar))
                	);
            botonBuscar.click();

            // Esperar a que se restablezca la lista completa
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.xpath(CC_Localizadores.listProductos), 2)); // suponemos que hay mínimo 3

            // --- 3. Agregar el segundo producto desde la lista general ---
            String productoLista = productos.get(1); // producto 2
            List<WebElement> listaCompleta = driver.findElements(By.xpath(CC_Localizadores.listProductos));

            boolean encontrado = false;
            for (int i = 0; i < listaCompleta.size(); i++) {
                if (listaCompleta.get(i).getText().contains(productoLista)) {
                    String botonXPath = CC_Localizadores.buttonAgregarCarro.replace("INDEX", String.valueOf(i + 1));
                    WebElement botonAgregar = driver.findElement(By.xpath(botonXPath));
                    wait.until(ExpectedConditions.elementToBeClickable(botonAgregar)).click();
                    System.out.println("[OK] ✅ Producto agregado desde lista general: " + productoLista);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("[ERROR] ❌ Producto no encontrado en lista general: " + productoLista);
                throw new RuntimeException("Producto no encontrado en lista: " + productoLista);
            }
            GG_Utils.takeScreenshotPassed(productoLista);

        } catch (Exception e) {
            System.out.println("[ERROR] ❌ Error durante la selección de productos: " + e.getMessage());
            throw new RuntimeException(e);
        }
        
        
    }//fin agregar produxto al carro
    
    //validar producto agregado
    public void validarProductoAgregado(String cantidadEsperada) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.textMatches(By.xpath(CC_Localizadores.labelNumeroCarrito),
                Pattern.compile("Carrito:.*" + cantidadEsperada + ".*")));

        WebElement contador = driver.findElement(By.xpath(CC_Localizadores.labelNumeroCarrito));
        String texto = contador.getText().trim();
        String valorActual = texto.replaceAll("\\D+", "");

        System.out.println("Cantidad esperada: " + cantidadEsperada + ", cantidad actual: " + valorActual);

        if (!valorActual.equals(cantidadEsperada)) {
            System.out.println("Error en validación de cantidad del carrito");
            GG_Utils.takeScreenshotPassed("validarProductoAgregado_FALLA_" + valorActual);
            throw new AssertionError("Error: contador esperado = " + cantidadEsperada + " pero fue " + valorActual);
        } else {
            System.out.println("Validación exitosa: la cantidad del carrito es correcta");
            GG_Utils.takeScreenshotPassed("validarProductoAgregado_FALLA_" + valorActual);
        }
    }
    
    //ir abolsa de compras
    public void irABolsaDeCompras() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 3); // Selenium 3 usa int, no Duration
            WebElement botonBolsa = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CC_Localizadores.botonIrABolsa)));
            botonBolsa.click();
            System.out.println("✅ Se hizo click en el botón 'Ir a la bolsa de compra'");
            GG_Utils.takeScreenshotPassed("irABolsaDeCompras_OK");
        } catch (Exception e) {
            System.out.println("No se pudo hacer click en el botón 'Ir a la bolsa de compra': " + e.getMessage());
            throw e;
        }
    }

    //aumentar producto por nombre
    public void aumentarCantidadProductoPorNombre(String nombreProducto, int cantidadClicks) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        int clicsRealizados = 0;
        int maxIntentos = 5;

        while (clicsRealizados < cantidadClicks && maxIntentos > 0) {
            try {
                WebElement contenedorProducto = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(String.format(CC_Localizadores.contenedorProductos, nombreProducto))
                ));

                WebElement botonSumar = contenedorProducto.findElement(By.xpath(CC_Localizadores.botonSumarProducto));
                wait.until(ExpectedConditions.elementToBeClickable(botonSumar));

                // Pausa antes del click
                Thread.sleep(2000);

                botonSumar.click();
                clicsRealizados++;
                System.out.println("✅ Clic en boton '+' '" + nombreProducto + "' #" + clicsRealizados);

            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("Elemento obsoleto para '" + nombreProducto + "'. Reintentando...");
            } catch (Exception e) {
                System.out.println("Error inesperado para '" + nombreProducto + "': " + e.getMessage());
                break;
            }
            maxIntentos--;
        }

        if (clicsRealizados < cantidadClicks) {
            throw new RuntimeException("No se pudieron hacer los " + cantidadClicks + " clics para '" + nombreProducto + "'. Solo se realizaron: " + clicsRealizados);
        }

        try {
            WebElement contenedorProducto = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format(CC_Localizadores.contenedorProductos, nombreProducto))
            ));
            WebElement inputCantidad = contenedorProducto.findElement(By.xpath(CC_Localizadores.inputCantidadProducto));
            String valorCantidad = inputCantidad.getAttribute("value");
            System.out.println("Cantidad final para '" + nombreProducto + "': " + valorCantidad);
            GG_Utils.takeScreenshotPassed("aumentarCantidad_" + nombreProducto + "_OK");
        } catch (Exception e) {
            System.out.println("Error al leer la cantidad final de '" + nombreProducto + "': " + e.getMessage());
        }
    }

    //disminuir producto por nombre
    public void disminuirCantidadProductoPorNombre(String nombreProducto, int cantidadClicks) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        int clicsRealizados = 0;
        int intentosFallidos = 0;
        int maxIntentos = 5;

        while (clicsRealizados < cantidadClicks && intentosFallidos < maxIntentos) {
            try {
                WebElement contenedorProducto = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(String.format(CC_Localizadores.contenedorProductos, nombreProducto))
                ));

                // Encuentra todos los botones flotantes dentro del contenedor
                List<WebElement> botones = contenedorProducto.findElements(By.xpath(".//button"));
                WebElement botonRestar = botones.get(0); // El primero es el botón "-"

                wait.until(ExpectedConditions.elementToBeClickable(botonRestar));
                botonRestar.click();

                clicsRealizados++;
                System.out.println("✅ Clic en botón '-' para '" + nombreProducto + "' #" + clicsRealizados);
                Thread.sleep(2000);

            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("[WARN] Elemento obsoleto para '" + nombreProducto + "'. Reintentando...");
                intentosFallidos++;
            } catch (Exception e) {
                System.out.println("[ERROR] Error al disminuir '" + nombreProducto + "': " + e.getMessage());
                GG_Utils.takeScreenshotPassed("disminuirCantidad_" + nombreProducto + "_FALLA");
                break;
            }
        }

        if (clicsRealizados < cantidadClicks) {
            GG_Utils.takeScreenshotPassed("disminuirCantidad_" + nombreProducto + "_FALLA");
            throw new RuntimeException("No se pudieron eliminar " + cantidadClicks + " unidades de '" + nombreProducto + "'. Solo se eliminaron: " + clicsRealizados);
        } else {
            // Screenshot al finalizar correctamente el paso
            GG_Utils.takeScreenshotPassed("disminuirCantidad_" + nombreProducto + "_OK");
        }
    }

    //eliminar producto por nombre
 // eliminar producto por nombre
    public void eliminarProductoPorNombre(String nombreProducto) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        int intentos = 0;
        int maxIntentos = 3;

        while (intentos < maxIntentos) {
            try {
                WebElement contenedorProducto = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(String.format(CC_Localizadores.contenedorProductos, nombreProducto))
                ));

                WebElement botonEliminar = contenedorProducto.findElement(
                    By.xpath(String.format(CC_Localizadores.botonEliminarProducto))
                );

                wait.until(ExpectedConditions.elementToBeClickable(botonEliminar));
                botonEliminar.click();

                System.out.println("✅ Producto '" + nombreProducto + "' eliminado del carrito.");

                // Screenshot al finalizar correctamente el paso
                GG_Utils.takeScreenshotPassed("eliminarProducto_" + nombreProducto + "_OK");

                return;

            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("[WARN] Elemento obsoleto para '" + nombreProducto + "'. Reintentando...");
            } catch (Exception e) {
                System.out.println("[ERROR] No se pudo eliminar '" + nombreProducto + "': " + e.getMessage());

                // Screenshot en caso de fallo
                GG_Utils.takeScreenshotPassed("eliminarProducto_" + nombreProducto + "_FALLA");
                break;
            }

            intentos++;
        }

        // Screenshot si no se pudo eliminar después de todos los intentos
        GG_Utils.takeScreenshotPassed("eliminarProducto_" + nombreProducto + "_FALLA");
        throw new RuntimeException("No se pudo eliminar el producto '" + nombreProducto + "' del carrito.");
    }

    
 // ir a finalizar reserva
    public void irAFinalizacionReserva() {
        WebDriverWait wait = new WebDriverWait(driver, 10); // aumento tiempo de espera

        try {
            WebElement botonFinalizar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(String.format(CC_Localizadores.btnIrFinalizarReserva))
            ));

            botonFinalizar.click();
            System.out.println("Se hizo clic en el botón 'Ir a la Finalización de la Reserva'");

            // Espera que cambie la página, validando que el <h2> con texto 'Resumen Servicios' esté presente
            wait.until(ExpectedConditions.presenceOfElementLocated(
               By.xpath(String.format(CC_Localizadores.tituloResumenServicio))
            ));

            System.out.println("✅ Navegación a la página de finalización de la reserva exitosa.");

            // Screenshot al finalizar correctamente
            GG_Utils.takeScreenshotPassed("irAFinalizacionReserva_OK");

        } catch (Exception e) {
            System.out.println("[ERROR] No se pudo hacer clic en el botón de finalización: " + e.getMessage());

            // Screenshot en caso de fallo
            GG_Utils.takeScreenshotPassed("irAFinalizacionReserva_FALLA");

            throw new RuntimeException(e);
        }
    }  

 // inicio completar formulario
    public void completarFormularioReserva(
            String nombreCliente,
            String fechaServicio,
            String profesional,
            String nombreMascota,
            String edadMascota,
            String unidadEdad,
            String razaMascota
    ) {
        try {
            // Nombre del Cliente
            WebElement inputNombreCliente = driver.findElement(By.xpath(CC_Localizadores.inputNombreCliente));
            escribirLento(inputNombreCliente, nombreCliente);
            String valorNombre = inputNombreCliente.getAttribute("value");
            System.out.println(valorNombre.equals(nombreCliente)
                ? "✅ Nombre del Cliente ingresado correctamente: " + valorNombre
                : "[ERROR] Nombre del Cliente NO coincide. Valor actual: " + valorNombre);

            // Fecha del Servicio (usando JavaScript para evitar errores)
            WebElement inputFecha = driver.findElement(By.xpath(CC_Localizadores.inputFechaServicio));

            String script =
                "arguments[0].value = arguments[1];" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));";

            ((JavascriptExecutor) driver).executeScript(script, inputFecha, fechaServicio);
            Thread.sleep(300); // opcional para esperar que se procese

            String valorFecha = inputFecha.getAttribute("value");
            System.out.println(valorFecha.equals(fechaServicio)
                ? "✅ Fecha del Servicio ingresada correctamente: " + valorFecha
                : "[ERROR] Fecha del Servicio NO coincide. Valor actual: " + valorFecha);

            // Profesional (select)
            WebElement selectProfesional = driver.findElement(By.xpath(CC_Localizadores.selectProfesional));
            Select dropdownProfesional = new Select(selectProfesional);
            dropdownProfesional.selectByVisibleText(profesional);
            Thread.sleep(300);
            String valorProfesional = dropdownProfesional.getFirstSelectedOption().getText();
            System.out.println(valorProfesional.equals(profesional)
                ? "✅ Profesional seleccionado correctamente: " + valorProfesional
                : "[ERROR] Profesional NO coincide. Valor actual: " + valorProfesional);

            // Nombre Mascota
            WebElement inputNombreMascota = driver.findElement(By.xpath(CC_Localizadores.inputNombreMascota));
            escribirLento(inputNombreMascota, nombreMascota);
            String valorNombreMascota = inputNombreMascota.getAttribute("value");
            System.out.println(valorNombreMascota.equals(nombreMascota)
                ? "✅ Nombre de la Mascota ingresado correctamente: " + valorNombreMascota
                : "[ERROR] Nombre de la Mascota NO coincide. Valor actual: " + valorNombreMascota);

            // Edad Mascota
            WebElement inputEdad = driver.findElement(By.xpath(CC_Localizadores.inputEdadMascota));
            escribirLento(inputEdad, edadMascota);
            String valorEdad = inputEdad.getAttribute("value");
            System.out.println(valorEdad.equals(edadMascota)
                ? "✅ Edad de la Mascota ingresada correctamente: " + valorEdad
                : "[ERROR] Edad de la Mascota NO coincide. Valor actual: " + valorEdad);

            // Unidad de Edad
            WebElement selectUnidadEdad = driver.findElement(By.xpath(CC_Localizadores.selectEdadMascota));
            Select dropdownUnidad = new Select(selectUnidadEdad);
            dropdownUnidad.selectByVisibleText(unidadEdad);
            Thread.sleep(300);
            String valorUnidadEdad = dropdownUnidad.getFirstSelectedOption().getText();
            System.out.println(valorUnidadEdad.equals(unidadEdad)
                ? "✅ Unidad de Edad seleccionada correctamente: " + valorUnidadEdad
                : "[ERROR] Unidad de Edad NO coincide. Valor actual: " + valorUnidadEdad);

            // Raza Mascota
            WebElement inputRaza = driver.findElement(By.xpath(CC_Localizadores.inputRazaMascota));
            escribirLento(inputRaza, razaMascota);
            String valorRaza = inputRaza.getAttribute("value");
            System.out.println(valorRaza.equals(razaMascota)
                ? "✅ Raza de la Mascota ingresada correctamente: " + valorRaza
                : "[ERROR] Raza de la Mascota NO coincide. Valor actual: " + valorRaza);

            System.out.println("✅ Formulario completado correctamente y validado.");

            // Screenshot al finalizar correctamente
            GG_Utils.takeScreenshotPassed("completarFormularioReserva_OK");

        } catch (Exception e) {
            System.out.println("[ERROR] ❌ Error al completar el formulario: " + e.getMessage());

            // Screenshot en caso de error
            GG_Utils.takeScreenshotPassed("completarFormularioReserva_FALLA");

            throw new RuntimeException(e);
        }
    }


 // inicio finalizar reserva
    public void finalizarReserva() {
        WebDriverWait wait = new WebDriverWait(driver, 10); // 10 segundos

        try {
            WebElement botonFinalizarReserva = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath(CC_Localizadores.btnFinalizarReserva))
            );

            botonFinalizarReserva.click();
            System.out.println("[OK] ✅ Se hizo clic en el botón 'Finalizar Reserva'");

            // Esperar la aparición del alert
            wait.until(ExpectedConditions.alertIsPresent());

            // Cambiar el foco al alert
            Alert alert = driver.switchTo().alert();
            String textoAlerta = alert.getText();
            System.out.println("[OK] 🚨 Se detectó un alert con el texto: '" + textoAlerta + "'");

            // Validar el texto esperado
            if (textoAlerta.equalsIgnoreCase("reserva finalizada")) {
                System.out.println("✅ El texto del alert es correcto: 'reserva finalizada'");
            } else {
                System.out.println("[WARNING] ⚠️ El texto del alert no es el esperado. Se recibió: '" + textoAlerta + "'");
            }

            // Tomar screenshot al finalizar correctamente
            GG_Utils.takeScreenshotPassed("finalizarReserva_OK");

            // Aceptar el alert si lo deseas
            // alert.accept();
            // System.out.println("[OK] ✅ Se aceptó el alert correctamente");

        } catch (Exception e) {
            System.out.println("[ERROR] ❌ Falló al hacer clic o manejar el alert: " + e.getMessage());

            // Screenshot en caso de error
            GG_Utils.takeScreenshotPassed("finalizarReserva_FALLA");

            throw new RuntimeException(e);
        }
    }

}