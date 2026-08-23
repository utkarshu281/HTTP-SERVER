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
        String body="Successfully created post";
        writer.printf("HTTP/1.1 %d %s\r\n", statusCode, response);
        writer.printf("Content-Type: %s\r\n", contentType);
        writer.printf("Content-Length: %d\r\n", body.length() + 1);
        writer.print("\r\n");
        writer.printf("%s\n", body);
    }
    public static void writingResposneDELETE(){}
    public static void writingResposnePUT(){}
    public static void writingResposnePATCH(){}
}