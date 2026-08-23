package org.example;

import java.io.PrintWriter;

public class MethodHandler {
  MethodHandler() {
  }

  public void handleGET(PrintWriter writer,FileHandling fileOperation, String fileName) {
      String response = "Ok";
      String body = fileOperation.returnContentFromFile(fileName);
      String contentType=ClientHandler.findContentType(fileName);
      int statusCode=200;
      ResponseWriter.writingResposneGET(statusCode, response, body, contentType, writer);
  }

  public void handlePUT(PrintWriter writer,FileHandling fileOperation, String fileName) {
    
  }

  public void handlePATCH(PrintWriter writer,FileHandling fileOperation, String fileName) {
  }

  public void handlePOST(PrintWriter writer,FileHandling fileOperation, String fileName,String requestBody) {
    boolean existorNot=fileOperation.writeFile(fileName, requestBody);
    if(existorNot){
      int statusCode=201;
      String response="Created";
      String contentType=ClientHandler.findContentType(fileName);
      ResponseWriter.writingResposnePOST(statusCode,contentType,response,writer);
    }else{
      throw new IllegalArgumentException("File somehow doesn't exist");
    }
  }

  public void handleDELETE(PrintWriter writer,FileHandling fileOperation, String fileName) {
  }

}
