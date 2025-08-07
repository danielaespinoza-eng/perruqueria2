package main.java.pageObjects;

public interface CC_Localizadores {
    // Lista de productos individuales
    String listProductos = "//*[@id='catalog-container']/div";

    // Botón dinámico (INDEX será reemplazado)
    String buttonAgregarCarro = "//*[@id='catalog-container']/div[INDEX]/button";
    
    // Botón dinámico (INDEX será reemplazado)
    String buttonBuscar = "//button[contains(text(),'Buscar')]";
    
    // Contador del carrito
    String labelNumeroCarrito = "//*[@id='cart-summary']";
    
    // Botón para ver carrito (popup)
    String botonVerCarrito = "///*[@id=\"cart-summary\"]";
    
    // Botón "Ir a la bolsa de compra" en el home
    String botonIrABolsa = "//*[@id=\"main-content\"]/button";
    
 // Parte base del XPath para el contenedor del producto
    String contenedorProductos = "//div[@class='cart-item-container' and .//p[contains(text(),'%s')]]";

    // XPath del botón "+" relativo al contenedor
    String botonSumarProducto = ".//button[text()='+']";

    // XPath del input de cantidad relativo al contenedor (opcional)
    String inputCantidadProducto = ".//input[@type='number']";
    
    //eliminar producto
    String botonEliminarProducto = ".//button[.//i[contains(@class,'fa-trash-alt')]]";
    
    //ir a finalizar reserva
    String btnIrFinalizarReserva = "//button[contains(text(),'Ir a la Finalización de la Reserva')]";
    
    //titulo resumen servicio
    String tituloResumenServicio = "//h2[text()='Resumen Servicios']";
    
    //input nombre cliente
    String inputNombreCliente = "//form[@id='reservation-form']//label[text()='Nombre del Cliente:']/following-sibling::input[1]";
    
    //input fecha servicio
    String inputFechaServicio = "//form[@id='reservation-form']//label[text()='Fecha del Servicio:']/following-sibling::input[1]";
    
    //seleccionar profesional
    String selectProfesional = "//form[@id='reservation-form']//select[1]";
    
    //input nombre cliente
    String inputNombreMascota = "//form[@id='reservation-form']//label[text()='Nombre de la Mascota:']/following-sibling::input[1]";
    
    //input edad mascota
    String inputEdadMascota = "//form[@id='reservation-form']//label[text()='Edad de la Mascota:']/following-sibling::input[1]";
    
    String selectEdadMascota ="//form[@id='reservation-form']//label[text()='Edad de la Mascota:']/following-sibling::select[1]";
    
    //input nombre cliente
    String inputRazaMascota = "//form[@id='reservation-form']//label[text()='Raza de la Mascota:']/following-sibling::input[1]";
    
    //btn finalizar reserva
    String btnFinalizarReserva = "//button[normalize-space()='Finalizar Reserva']";
}
