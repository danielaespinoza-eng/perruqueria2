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
            if (GG_BaseTest.driver == null) {
                System.out.println("[Listener] No se pudo tomar screenshot: driver es null.");
                return;
            }

            // Manejo de alertas antes del pantallazo
            try {
                Alert alert = GG_BaseTest.driver.switchTo().alert();
                System.out.println("[Listener] Alert detectado antes de screenshot: " + alert.getText());
                alert.accept();
                Thread.sleep(500);
            } catch (NoAlertPresentException ex) {
                // No hay alerta, continuar
            }

            // Fecha y hora
            String xFecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String xHora = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
            String xSufijo = xFecha + "_" + xHora;

            // Carpeta en target/screenshots/carpeta
            String targetPath = System.getProperty("user.dir") 
                    + File.separator + "target" 
                    + File.separator + "screenshots" 
                    + File.separator + carpeta;

            File dir = new File(targetPath);
            if (!dir.exists()) dir.mkdirs();

            // Nombre del archivo
            String fileName = result.getMethod().getMethodName() + "_" + xSufijo + ".png";

            // Tomar screenshot
            File srcFile = ((TakesScreenshot) GG_BaseTest.driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(dir, fileName);
            FileUtils.copyFile(srcFile, destFile);

            // Ruta relativa para Jenkins (apunta a artifact/target/screenshots/...)
            String relativePath = "screenshots/" + carpeta + "/" + fileName;

            // Agregar enlace y miniatura al reporte TestNG
            org.testng.Reporter.log(
                "<a href='" + relativePath + "' target='_blank'>Ver Screenshot</a><br>" +
                "<img src='" + relativePath + "' height='200'><br>"
            );

            System.out.println("[Listener] Screenshot guardado en: " + destFile.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("[Listener] Error guardando screenshot: " + e.getMessage());
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
