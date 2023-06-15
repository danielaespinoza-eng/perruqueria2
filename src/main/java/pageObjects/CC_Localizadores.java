package main.java.pageObjects;
//Dentro de esta clase se almacenaran todos los localizadores que se ocuparan.
public interface CC_Localizadores {

	// input
	String inputUsuario = "//*[@id=\"user-name\"]"; //XPATH
	String inputContrasena = "//*[@id=\"password\"]"; //XPATH

	// button
	String buttonIniciarSesion = "//*[@id=\"login-button\"]"; //XPATH
	String buttonAgregarCarro = "//html/body/div/div/div/div[2]/div/div/div/div[0]/div[2]/div[2]/button";
	String buttonCarroCompras = "//*[@id=\"shopping_cart_container\"]/a";
	
	//label
	String labelPaginaProductos = "//*[@id=\"header_container\"]/div[2]/span";
	String labelNumeroCarrito = "//*[@id=\"shopping_cart_container\"]/a/span";
	
	//list
	String listProductos = "//html/body/div/div/div/div[2]/div/div/div/div/div/div/a/div";

}