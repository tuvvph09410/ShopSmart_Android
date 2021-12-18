package com.example.shopsmart.Until;

public class Server {
    private static String localhost = "https://tucaomypham.000webhostapp.com";
    private static String urlGetCategory = localhost + "/shop_smart/getselect_catelory.php";
    private static String urlGetProduct = localhost + "/shop_smart/getselect_product.php";
    private static String urlGetProductByIDCateGory = localhost + "/shop_smart/getselect_byIdcatergory_product.php";
    private static String urlGetProductByIDmanufacturer = localhost + "/shop_smart/getselect_byIdmanufacturer_product.php";
    private static String urlGetProductByIDmanufacturerAndCategory = localhost + "/shop_smart/getselect_byidcategory_and_manufacturer_product.php";
    private static String urlGetColorByIDProduct = localhost + "/shop_smart/getselect_byIdProduct_color_and_image.php";

    public static String getUrlGetProductByIDmanufacturerAndCategory() {
        return urlGetProductByIDmanufacturerAndCategory;
    }

    public static String getUrlGetColorByIDProduct() {
        return urlGetColorByIDProduct;
    }

    public static String getUrlGetProductByIDmanufacturer() {
        return urlGetProductByIDmanufacturer;
    }

    public static String getLocalhost() {
        return localhost;
    }

    public static String getUrlGetCategory() {
        return urlGetCategory;
    }

    public static String getUrlGetProduct() {
        return urlGetProduct;
    }

    public static String getUrlGetProductByIDCateGory() {
        return urlGetProductByIDCateGory;
    }
}
