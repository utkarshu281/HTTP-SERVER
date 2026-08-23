package org.example;

import java.io.PrintWriter;

public class ResponseWriter{
    public static void writingResposneGET(int statusCode,String response,String body,String contentType,PrintWriter writer){
        writer.printf("HTTP/1.1 %d %s\r\n", statusCode, response);
        writer.printf("Content-Type: %s\r\n", contentType);
        writer.printf("Content-Length: %d\r\n", body.length() + 1);
        writer.print("\r\n");
        writer.printf("%s\n", body);
    // output.flush();
    }
    public static void writingResposnePOST(int statusCode,String contentType,String response,PrintWriter writer){
        String body="Resource successfully created.";
        writer.printf("HTTP/1.1 %d %s\r\n", statusCode, response);
        writer.printf("Content-Type: %s\r\n", contentType);
        writer.printf("Content-Length: %d\r\n", body.length() + 1);
        writer.print("\r\n");
        writer.printf("%s\n", body);
    }
    public static void writingResposneDELETE(int statusCode,String contentType,String response,PrintWriter writer){
        writer.printf("HTTP/1.1 %d %s\r\n", statusCode, response);
        writer.print("\r\n");
        writer.flush();
    }
    public static void writingResposnePUT(){} //future updates
    public static void writingResposnePATCH(){} //future update
    public static void failedResponseWriter(int statusCode,PrintWriter writer){
        String body="An unexpected server error occurred";
        writer.printf("HTTP/1.1 %d %s\r\n",500,"internal server error");
        writer.printf("Content-Type: %s\r\n","text/plain");
        switch (statusCode) {
            case 201->{
                body="Failed To Update Resource";
            }
            case 204->{
                body="Failed To Delete Resource";
            }   
        }
        writer.printf("Content-Length: %d\r\n", body.length() + 1);
        writer.print("\r\n");
        writer.printf("%s\n", body);
    } 
}