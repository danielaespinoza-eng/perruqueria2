package test.java.carritocompras;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import java.util.Arrays;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.exceptions.CsvException;

import main.java.pageEvents.CC_PasosFuncionales;
import main.java.utils.CC_Parametros;
import test.java.GG_BaseTest;

public class CC_Test extends GG_BaseTest{
    static int gloFilas = 0;
    public static String gloVerFlujo = "S";

    @Test(dataProvider = "DataProductosMultiples")
    public void CC_QA_AgregarProductosAlCarrito(
        String producto1,
        String producto2,
        String producto3,
        String cantidadEsperada,
        String nombreCliente,
        String fechaServicio,
        String profesional,
        String nombreMascota,
        String edadMascota,
        String unidadEdad,
        String razaMascota
    ) {
        // Usamos el driver ya iniciado en GG_BaseTest
        CC_PasosFuncionales pasos = new CC_PasosFuncionales(GG_BaseTest.driver);

        List<String> productos = Arrays.asList(producto1, producto2, producto3);
        pasos.agregarProductosConBusquedaMixta(productos);

        pasos.validarProductoAgregado(cantidadEsperada);
        pasos.irABolsaDeCompras();

        pasos.aumentarCantidadProductoPorNombre("Baño y Corte", 2);
        pasos.aumentarCantidadProductoPorNombre("Baño y Peinado", 2);

        pasos.disminuirCantidadProductoPorNombre("Baño y Peinado", 2);
        pasos.disminuirCantidadProductoPorNombre("Baño y Corte", 1);

        pasos.eliminarProductoPorNombre("Corte de Uñas");

        pasos.irAFinalizacionReserva();

        pasos.completarFormularioReserva(
            nombreCliente,
            fechaServicio,
            profesional,
            nombreMascota,
            edadMascota,
            unidadEdad,
            razaMascota
        );

        pasos.finalizarReserva();
    }

    @DataProvider(name = "DataProductosMultiples")
    public Object[][] dataProviderMultiples() throws IOException, CsvValidationException, CsvException {
    	String rutaCSV = CC_Parametros.gloDir + File.separator + "data" + File.separator + CC_Parametros.gloNombreCSV;
        Object[][] datosFiltrados = filtrarCSVPorCantidadDeProductos(rutaCSV, 2);

        if (datosFiltrados.length > 0) {
            return new Object[][] { datosFiltrados[0] };
        } else {
            throw new IllegalArgumentException("⚠️ No se encontraron filas válidas en el CSV.");
        }
    }

    @DataProvider(name = "DataProductoUnico")
    public Object[][] dataProviderUnico() throws IOException, CsvValidationException, CsvException {
    	String rutaCSV = CC_Parametros.gloDir + File.separator + "data" + File.separator + CC_Parametros.gloNombreCSV;
        return filtrarCSVPorCantidadDeProductos(rutaCSV, 1);
    }

    private Object[][] filtrarCSVPorCantidadDeProductos(String path, int cantidadProductosRequerida) throws IOException, CsvValidationException, CsvException {
        try (CSVReader reader = new CSVReader(new java.io.InputStreamReader(new java.io.FileInputStream(path), java.nio.charset.StandardCharsets.UTF_8))) {

            List<String[]> rows = reader.readAll();
            if (rows.isEmpty()) {
                throw new IllegalArgumentException("⚠️ El archivo CSV está vacío: " + path);
            }

            rows.remove(0); // Eliminar encabezado
            List<Object[]> datosFiltrados = new ArrayList<>();

            for (String[] fila : rows) {
                if (fila.length < 11) {
                    System.out.println("⚠️ Fila con columnas insuficientes: " + String.join(",", fila));
                    continue;
                }

                int productosNoVacios = 0;
                for (int i = 0; i <= 2; i++) {
                    if (fila[i] != null && !fila[i].trim().isEmpty()) {
                        productosNoVacios++;
                    }
                }

                if (cantidadProductosRequerida == 1 && productosNoVacios == 1) {
                    datosFiltrados.add(new Object[]{
                        fila[0].trim(), // producto1
                        "", "",         // producto2 y 3 vacíos
                        fila[3].trim(), // cantidadEsperada
                        fila[4].trim(), fila[5].trim(), fila[6].trim(),
                        fila[7].trim(), fila[8].trim(), fila[9].trim(), fila[10].trim()
                    });
                }

                if (cantidadProductosRequerida >= 2 && productosNoVacios >= 2) {
                    datosFiltrados.add(new Object[]{
                        fila[0].trim(),
                        fila[1].trim(),
                        fila[2].trim(),
                        fila[3].trim(),
                        fila[4].trim(), fila[5].trim(), fila[6].trim(),
                        fila[7].trim(), fila[8].trim(), fila[9].trim(), fila[10].trim()
                    });
                }
            }

            return datosFiltrados.toArray(new Object[0][]);
        }
    }
}