package org.example;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Stack;

public class ClientHandler {
    HashMap<String,String> httpValue;
    ArrayList<String> arr;
    ClientHandler(){
        httpValue=new HashMap<>();
        arr=new ArrayList<>();
    }
    public void parsing(Scanner scanner, Socket clientSocket){
        //String[] arr = new String[30];
        while(scanner.hasNextLine()){
            String message = scanner.nextLine();
            if(message.isEmpty())break;
            arr.add(message);
        }
        parsingMethod(arr);
        arr.clear();
    }
    public void parsingMethod(ArrayList<String> arr){
        String[] arrOfMethods=arr.get(0).split(" ");
        switch (arrOfMethods[0]){
            case "GET":
                System.out.printf("Method : %s%n Path : %s%n Version: %s%n Status %s%n",arrOfMethods[0],arrOfMethods[1],arrOfMethods[2],"supported");
                break;
            case "POST":
                System.out.printf("Method : %s%n Path : %s%n Version: %s%n Status %s%n",arrOfMethods[0],arrOfMethods[2],arrOfMethods[2],"unsupported");
                break;
            case "DELETE":
                System.out.printf("Method : %s%n Path : %s%n Version: %s%n Status %s%n",arrOfMethods[0],arrOfMethods[2],arrOfMethods[2],"unsupported");
                break;
            default:
                break;
        }
        parsingBody(arr);
    }
    public void parsingBody(ArrayList<String> arr){
        for(int i=1;i<arr.size();i++){
            if (arr.get(i).trim().isEmpty()) {
                continue;
            }
            String[] parsingArr=arr.get(i).split(":",2);
            httpValue.put(parsingArr[0],parsingArr[1]);
        }
        printingValue();
    }
    public void printingValue(){
        httpValue.forEach((key,value)-> System.out.println(key+":"+value));
        httpValue.clear();
    }
}
