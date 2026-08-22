package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.function.BiFunction;

public class ClientHandler {
  Map<String, String> httpValue;
  List<String> arr;
  FileHandling fileHandler;
  MethodHandler requestRouter = new MethodHandler();
  private final Map<String, String> mimeTypes = Map.of(
      ".html", "text/html",
      ".css", "text/css",
      ".js", "application/javascript",
      ".txt", "text/plain",
      ".json", "application/json");

  ClientHandler() {
    httpValue = new HashMap<>();
    arr = new ArrayList<>();
  }

  public void parsing(Scanner scanner, Socket clientSocket, FileHandling fileHandler, PrintWriter outPut,
      BufferedReader reader) throws IOException {
    this.fileHandler = fileHandler;
    // String[] arr = new String[30];
    /*
     * while (scanner.hasNextLine()) {
     * String message = scanner.nextLine();
     * if (message.isEmpty())
     * break;
     * arr.add(message);
     * }
     */
    int contentLength = 0;
    String line;
    while ((line = reader.readLine()) != null && !line.isEmpty()) {
      if (line.toLowerCase().contains("content-length:")) {
        String[] parts = line.split(":", 2);
        contentLength = Integer.parseInt(parts[1].trim());
      }
      arr.add(line);
      // System.out.println(line);
    }
    /*
     * StringBuilder body = new StringBuilder();
     * if (contentLength > 0) {
     * String content;
     * while ((content = reader.readLine()) != null) { //this will kepp reading
     * until curl stopitself(end of stream)
     * body.append(content);
     * body.append(System.lineSeparator()); // for new line character
     * }
     * }
     * System.out.println(body.toString());
     * parsingMethod(arr, outPut, fileHandler);
     * arr.clear();
     * body.setLength(0);
     */
    char[] body = new char[contentLength];
    if (contentLength > 0) {
      int total = 0;
      while (total < contentLength) {
        int currentChar = reader.read(body, total, contentLength - total);
        if (currentChar == -1)
          break;
        total += currentChar;
      }
    }
    String requestBody = new String(body);
    System.out.println(requestBody);
    parsingMethod(arr, outPut, fileHandler, requestBody);
    arr.clear();
  }

  public void parsingMethod(List<String> arr, PrintWriter output, FileHandling fileHandler, String requestBody) {
    String[] arrOfMethods = arr.get(0).split(" ");
    boolean requestValidateAnswer = validatingRequest(arrOfMethods);
    if (requestValidateAnswer) {
      String fileName = arrOfMethods[1];
      int statusCode = handleRequest(arrOfMethods, fileName);
      serverHandlingResponse(output, statusCode, fileHandler, arr, fileName, HttpMethod.valueOf(arrOfMethods[0]));
    } else {
      int statusCode = 400;
      String fileName = "";
      serverHandlingResponse(output, statusCode, fileHandler, arr, fileName, HttpMethod.valueOf(arrOfMethods[0]));
    }
  }

  public boolean validatingRequest(String[] arrOfMethods) {
    // METHOD FILE_NAME HTTP_TYPE
    String[] arrMethods = arrOfMethods; // pointing at same object
    if (!(arrMethods.length > 3 || arrMethods.length < 3)) {
      try {
        HttpMethod method = HttpMethod.valueOf(arrMethods[0]);
        String File = arrMethods[1];
        String httpType = arrMethods[2];
        boolean enumCheck = false;
        boolean fileCheck = false;
        for (HttpMethod m : HttpMethod.values()) {
          enumCheck = false;
          if (method == m) {
            enumCheck = true;
          }
          if (enumCheck)
            break;
        }
        if (enumCheck) {
          if (!(File.isEmpty()))
            fileCheck = true;
          if (fileCheck) {
            if (httpType.equals("HTTP/1.1")) {
              return true;
            } else {
              return false;
            }
          } else {
            return false;
          }
        } else {
          return false;
        }
      } catch (Exception e) {

      }
    }
    return false;
  }

  public enum HttpMethod {
    GET, POST, DELETE, PUT, PATCH
  }

  public int handleRequest(String[] arrayOfMethods, String fileName) {
    try {
      HttpMethod method = HttpMethod.valueOf(arrayOfMethods[0]);
      BiFunction<HttpMethod, String, Integer> handler = (m, nameOfFile) -> {
        switch (m) {
          case GET, POST:
            boolean fileExistOrNot = fileHandler.fileExistOrNot(nameOfFile);
            if (fileExistOrNot)
              return 200;
            return 404;
          case DELETE, PUT, PATCH:
            return 405;
          default:
            return 404;
        }
      };
      return handler.apply(method, fileName);
    } catch (Exception e) {
      return 404;
    }
  }

  public void parsingHeader(List<String> arr) {
    for (int i = 1; i < arr.size(); i++) {
      if (arr.get(i).trim().isEmpty()) {
        continue;
      }
      String[] parsingArr = arr.get(i).split(":", 2);
      httpValue.put(parsingArr[0], parsingArr[1]);
    }
    // printingValue();
  }

  // public void printingValue(PrintWriter out){
  // httpValue.forEach((key,value)-> out.println(key+":"+value));
  // httpValue.clear();
  // }

  public void methodHandling(HttpMethod method) {
    switch (method) {
      case GET:
        requestRouter.handleGET();
        break;
      case POST:
        requestRouter.handlePOST();
        break;
      case PUT:
        requestRouter.handlePUT();
        break;
      case DELETE:
        requestRouter.handleDELETE();
        break;
      case PATCH:
        requestRouter.handlePATCH();
        break;
      default:
        break; /// this case will nevere happen due to the previous checking
    }
  }

  public void serverHandlingResponse(PrintWriter output, int statusCode, FileHandling fileHandler, List<String> arr,
      String fileName, HttpMethod method) {
    String response = "";
    String body = "";
    String contentType = findContentType(fileName);
    parsingHeader(arr);
    switch (statusCode) {
      case 200 -> {
        body = fileHandler.returnContentFromFile(fileName);
        response = "OK";
        // serverOutput(output,fileHandler,fileName);
      }
      case 405 -> {
        body = "Method Not Allowed";
        response = body;
      }
      case 404 -> {
        body = "Not Found";
        response = body;
      }
      case 400 -> {
        body = "bad request";
        response = body;
      }
    }
    writeResponse(output, body, statusCode, response, contentType);
  }

  public String findContentType(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    if (dotIndex == -1) {
      return "application/octet-stream";
    }

    String extension = fileName.substring(dotIndex).toLowerCase();

    return mimeTypes.getOrDefault(
        extension,
        "application/octet-stream");
  }

  // public void serverOutput(PrintWriter output, FileHandling fileHandler, String
  // fileName) {
  // int contentLength = fileHandler.readingFileContentsAndLength(fileName);
  // output.printf("HTTP/1.1 %d %s\r\n", 200, "OK");
  // output.printf("Content-Type: text/plain\r\n");
  // output.printf("Content-Length: %d\r\n", contentLength);
  // //a mandatory blank line to separate headers from the body
  // output.print("\r\n");
  // ArrayList<String> contentsOfFile = fileHandler.returngContent();
  // for (String str : contentsOfFile) {
  // output.println(str);
  // }
  // }

  public void writeResponse(PrintWriter output, String body, int statusCode, String response, String contentType) {
    output.printf("HTTP/1.1 %d %s\r\n", statusCode, response);
    output.printf("Content-Type: %s\r\n", contentType);
    output.printf("Content-Length: %d\r\n", body.length() + 1);
    output.print("\r\n");
    output.printf("%s\n", body);
    // output.flush();
  }
}
