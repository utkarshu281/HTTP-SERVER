package org.example;

import com.fasterxml.jackson.databind.JsonSerializable;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ClientHandler {
    HashMap<String,String> httpValue;
    ArrayList<String> arr;
    ClientHandler(){
        httpValue=new HashMap<>();
        arr=new ArrayList<>();
    }
    public int parsing(Scanner scanner, Socket clientSocket){
        //String[] arr = new String[30];
        while(scanner.hasNextLine()){
            String message = scanner.nextLine();
            if(message.isEmpty())break;
            arr.add(message);
        }
        int statusCode=parsingMethod(arr);
        arr.clear();
        return statusCode;
    }
    public int parsingMethod(ArrayList<String> arr){
        String[] arrOfMethods=arr.get(0).split(" ");
        parsingBody(arr);
        return switch (arrOfMethods[0]) {
            case "GET" -> 200;
            case "POST" -> 405;
            case "DELETE" -> 405;
            default -> 404;
        };
    }
    public void parsingBody(ArrayList<String> arr){
        for(int i=1;i<arr.size();i++){
            if (arr.get(i).trim().isEmpty()) {
                continue;
            }
            String[] parsingArr=arr.get(i).split(":",2);
            httpValue.put(parsingArr[0],parsingArr[1]);
        }
        //printingValue();
    }
    public void printingValue(PrintWriter out){
        httpValue.forEach((key,value)-> out.println(key+":"+value));
        httpValue.clear();
    }
    public void serverOutput(PrintWriter output,int statusCode){
        String content="Hello from java HTTP server developed by utkarsh upadhyay\n";
        int contentLength=content.length();
        String response;
        if(statusCode==200){
            response="OK";
            output.printf("HTTP/1.1 %d %s\r\n",statusCode,response);
        } else if (statusCode==405) {
            response="Method Not Allowed";
            output.printf("HTTP/1.1 %d %s\r\n",statusCode,response);
        } else if (statusCode==404) {
            response="Not Found";
            output.printf("HTTP/1.1 %d %s\r\n",statusCode,response);
        }
        output.printf("Content-Type: text/plain\r\n");
        output.printf("Content-Length: %d\r\n",contentLength);
        //a mandatory blank line to separate headers from the body
        output.print("\r\n");
        output.print(content);
    }
}
