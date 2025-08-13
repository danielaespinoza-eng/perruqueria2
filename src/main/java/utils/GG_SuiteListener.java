package main.java.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import test.java.GG_BaseTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GG_SuiteListener implements ITestListener, IAnnotationTransformer {
    public static ExtentTest logger;

    private void guardarScreenshot(ITestResult result, String carpeta) {
        try {
            // Evita error si driver es null
            if (GG_BaseTest.driver == null) {
                System.out.println("[Listener] No se pudo tomar screenshot: driver es null.");
                return;
            }

            // Manejar alertas abiertas antes de capturar pantalla
            try {
                WebDriver driver = GG_BaseTest.driver;
                Alert alert = driver.switchTo().alert();
                System.out.println("[Listener] Alert detectado antes de screenshot: " + alert.getText());
                alert.accept();  // o alert.dismiss() si prefieres
                Thread.sleep(500); // esperar que desaparezca el alert
            } catch (NoAlertPresentException ex) {
                // No hay alerta, continuar normalmente
            } catch (InterruptedException e) {
                System.out.println("[Listener] Error en sleep: " + e.getMessage());
            }

            // Obtener Fecha y Hora
            LocalTime hhora = LocalTime.now();
            DateTimeFormatter f_t = DateTimeFormatter.ofPattern("HHmmss");

            LocalDate ffecha = LocalDate.now();
            DateTimeFormatter f_d = DateTimeFormatter.ofPattern("yyyyMMdd");

            String xHora = hhora.format(f_t);
            String xFecha = ffecha.format(f_d);

            String xSufijo = xFecha + "_" + xHora;

            // Ruta archivo
            String fileName = CC_Parametros.gloDir + File.separator + "screenshots"
                    + File.separator + carpeta + File.separator
                    + result.getMethod().getMethodName() + "_" + xSufijo;

            // Tomar screenshot
            File f = ((TakesScreenshot) GG_BaseTest.driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(f, new File(fileName + ".png"));

            // Guardar nombre en archivo temporal
            String fileName2 = CC_Parametros.gloDir + File.separator + "screenshots"
                    + File.separator + carpeta + File.separator + "Archivo_Paso.txt";

            File xArchivo = new File(fileName2);
            if (xArchivo.exists()) {
                xArchivo.delete();
            }
            xArchivo.createNewFile();

            try (BufferedWriter archivoIndice = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(xArchivo, true), "Windows-1252"))) {
                archivoIndice.write(fileName + ".png");
            }

            System.out.println("[Listener] Screenshot guardado en: " + fileName + ".png");

        } catch (IOException e) {
            System.out.println("[Listener] Error guardando screenshot: " + e.getMessage());
        } catch (Exception ex) {
            System.out.println("[Listener] Error inesperado tomando screenshot: " + ex.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        guardarScreenshot(result, "passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        guardarScreenshot(result, "failed");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
    }
}
