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
    public void printingValue(){
        httpValue.forEach((key,value)-> System.out.println(key+":"+value));
        httpValue.clear();
    }
    public void serverOutput(PrintWriter output,int statusCode){
        System.out.printf("HTTP/1.1 %d OK\nHello from http server\n",statusCode);
    }
}
