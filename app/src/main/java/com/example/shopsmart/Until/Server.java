package com.example.shopsmart.Until;

public class Server {
    private static String localhost = "https://tucaomypham.000webhostapp.com";
    private static String urlGetCategory = localhost + "/shop_smart/getselect_catelory.php";
    private static String urlGetProduct = localhost + "/shop_smart/getselect_product.php";

    public static String getLocalhost() {
        return localhost;
    }

    public static String getUrlGetCategory() {
        return urlGetCategory;
    }

    public static String getUrlGetProduct() {
        return urlGetProduct;
    }
}
