package org.httpserver;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
       HttpServer httpServer = new HttpServer(8080,100);
       try{
       httpServer.start();
       }catch (IOException e){
           System.out.println("Could not start server : " + e.getMessage());
       }
    }
}