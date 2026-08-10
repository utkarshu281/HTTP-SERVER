package org.example;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.function.BiFunction;
public class ClientHandler {
    Map<String, String> httpValue;
    List<String> arr;
    FileHandling fileHandler;
    private final Map<String, String> mimeTypes = Map.of(
            ".html", "text/html",
            ".css", "text/css",
            ".js", "application/javascript",
            ".txt", "text/plain",
            ".json", "application/json"
    );

    ClientHandler() {
        httpValue = new HashMap<>();
        arr = new ArrayList<>();
    }

    public void parsing(Scanner scanner, Socket clientSocket, FileHandling fileHandler, PrintWriter outPut) {
        this.fileHandler = fileHandler;
        //String[] arr = new String[30];
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            if (message.isEmpty()) break;
            arr.add(message);
        }
        parsingMethod(arr, outPut, fileHandler);
        arr.clear();
    }

    public void parsingMethod(List<String> arr, PrintWriter output, FileHandling fileHandler) {
        String[] arrOfMethods = arr.get(0).split(" ");
        String fileName = arrOfMethods[1];
        int statusCode = handleRequest(arrOfMethods, fileName);
        serverHandlingResponse(output, statusCode, fileHandler, arr, fileName);
    }

    public enum HttpMethod {
        GET, POST, DELETE, PUT, PATCH
    }

    public int handleRequest(String[] arrayOfMethods, String fileName) {
        HttpMethod method = HttpMethod.valueOf(arrayOfMethods[0]);
        BiFunction<HttpMethod, String, Integer> handler = (m, nameOfFile) -> {
            switch (m) {
                case GET:
                    boolean fileExistOrNot = fileHandler.fileExistOrNot(nameOfFile);
                    if (fileExistOrNot) return 200;
                    return 404;
                case POST, DELETE, PUT, PATCH:
                    return 405;
                default:
                    return 404;
            }
        };
        return handler.apply(method, fileName);
    }

    public void parsingHeader(List<String> arr) {
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).trim().isEmpty()) {
                continue;
            }
            String[] parsingArr = arr.get(i).split(":", 2);
            httpValue.put(parsingArr[0], parsingArr[1]);
        }
        //printingValue();
    }

    //    public void printingValue(PrintWriter out){
//        httpValue.forEach((key,value)-> out.println(key+":"+value));
//        httpValue.clear();
//    }
    public void serverHandlingResponse(PrintWriter output, int statusCode, FileHandling fileHandler, List<String> arr, String fileName) {
        String response="";
        String body="";
        String contentType=findContentType(fileName);
        parsingHeader(arr);
        switch (statusCode) {
            case 200 -> {
                body=fileHandler.returnContentFromFile(fileName);
                response="OK";
                //serverOutput(output,fileHandler,fileName);
            }
            case 405 -> {
                body="Method Not Allowed";
                response=body;
            }
            case 404 -> {
                body="Not Found";
                response=body;
            }
        }
        writeResponse(output,body,statusCode,response,contentType);
    }
    public String findContentType(String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "application/octet-stream";
        }

        String extension = fileName.substring(dotIndex).toLowerCase();

        return mimeTypes.getOrDefault(
                extension,
                "application/octet-stream"
        );
    }

//    public void serverOutput(PrintWriter output, FileHandling fileHandler, String fileName) {
//        int contentLength = fileHandler.readingFileContentsAndLength(fileName);
//            output.printf("HTTP/1.1 %d %s\r\n", 200, "OK");
//            output.printf("Content-Type: text/plain\r\n");
//            output.printf("Content-Length: %d\r\n", contentLength);
//            //a mandatory blank line to separate headers from the body
//            output.print("\r\n");
//            ArrayList<String> contentsOfFile = fileHandler.returngContent();
//            for (String str : contentsOfFile) {
//                output.println(str);
//            }
//    }
    public void writeResponse(PrintWriter output,String body, int statusCode,String response,String contentType){
            output.printf("HTTP/1.1 %d %s\r\n",statusCode, response);
            output.printf("Content-Type: %s\r\n",contentType);
            output.printf("Content-Length: %d\r\n", body.length()+1);
            output.print("\r\n");
            output.printf("%s\n",body);
            //output.flush();
    }
}