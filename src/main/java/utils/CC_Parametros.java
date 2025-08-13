package main.java.utils;

import java.io.File;

public interface CC_Parametros {
    String url = "https://franciscavalenzuelasalazar.github.io/mascopet/catalog.html";

    String nombreAutomatizador = "[Flujo-Automatizado]";
    String nombreProyecto = "[*Proyecto*]";

    String gloDir = "C:" + File.separator + "Users" + File.separator + "TG" + File.separator +
            "eclipse-workspace" + File.separator + "Estructura-Base-New_Perruqueria" + File.separator +
            "CC_Logs_Perruqueria";

    int gloColumnas = 2;
    String gloNombreCSV = "CSVParametersCarritoCompras.csv";

    default String getRutaCSV() {
        return gloDir + File.separator + "data" + File.separator + gloNombreCSV;
    }
}
