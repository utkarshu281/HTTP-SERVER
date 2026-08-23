package org.example;

import java.io.PrintWriter;

public class MethodHandler {
  public void handleGET(PrintWriter writer,FileHandling fileOperation, String fileName) {
      String response = "Ok";
      String body = fileOperation.returnContentFromFile(fileName);
      String contentType=ClientHandler.findContentType(fileName);
      int statusCode=200;
      ResponseWriter.writingResposneGET(statusCode, response, body, contentType, writer);
  }
  /* NOTE:- FOR FUTURE UPDATES 
  public void handlePUT(PrintWriter writer,FileHandling fileOperation, String fileName) {}
  public void handlePATCH(PrintWriter writer,FileHandling fileOperation, String fileName) {}
  
  */

  public void handlePOST(PrintWriter writer,FileHandling fileOperation, String fileName,String requestBody) {
    boolean existorNot=fileOperation.writeFile(fileName, requestBody);
    int statusCode=201;
    if(existorNot){
      String response="Created";
      String contentType=ClientHandler.findContentType(fileName);
      ResponseWriter.writingResposnePOST(statusCode,contentType,response,writer);
    }else{
      ResponseWriter.failedResponseWriter(statusCode, writer);
    }
  }

  public void handleDELETE(PrintWriter writer,FileHandling fileOperation, String fileName) {
      String response = "No Content";
      String contentType=ClientHandler.findContentType(fileName);
      int statusCode=204;
      boolean fileDelted = fileOperation.deleteFile(fileName);
      if(fileDelted){
        ResponseWriter.writingResposneDELETE(statusCode,contentType,response,writer);
      }else{
        ResponseWriter.failedResponseWriter(statusCode, writer);
      }
  }

}
