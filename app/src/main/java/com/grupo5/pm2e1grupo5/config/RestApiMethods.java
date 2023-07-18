package com.grupo5.pm2e1grupo5.config;

public class RestApiMethods {
    public static final String ipaddress = "192.168.100.9";
    public static final String webapi = "ApiRestGrupo5";
    public static final String separador = "/";
    //Routing
    public static final String postRouting = "ContactoCreate.php";
    public static final String getRouting = "ContactoRead.php";
    public static final String upRouting = "ContactoUpd.php";
    public static final String delRouting = "ContactoDel.php";

    //EndPoint
    public static final String apiUpd = "http://"+ipaddress+separador+webapi+separador+upRouting;
    public static final String apiGet = "http://"+ipaddress+separador+webapi+separador+getRouting;
    public static final String apiPost = "http://"+ipaddress+separador+webapi+separador+postRouting;
    public static final String apiDel = "http://"+ipaddress+separador+webapi+separador+delRouting;
}
