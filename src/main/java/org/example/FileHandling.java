package org.example;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class FileHandling {
    Path dir;
    ArrayList<String> fileReader;
    StringBuilder content;
    FileHandling(String path){
        dir =  Paths.get(path);
        fileReader=new ArrayList<>();
        content = new StringBuilder();
    }
    public boolean fileCheckInDirectory(String fileName){
        Path documentRoot = dir.normalize();
        Path requestedPath = dir.resolve(fileName).normalize();
        if (requestedPath.startsWith(documentRoot)) {
            return true;
        }
        return false;
    }
    public boolean fileExistOrNot(String fileName){
            boolean check = fileCheckInDirectory(fileName);
           if(check){
               Path cleanPath=cleaningPath(fileName);
               return Files.exists(cleanPath);
           }
           return false;
    }
    public Path cleaningPath(String fileName){
        if(fileName.startsWith("/") && fileName.length()>1){
            fileName = fileName.substring(1);
            Path rawPath = dir.resolve(fileName);
            return rawPath.normalize();
        }
        Path rawPath = dir.resolve("index.html"); ///default for /
        return rawPath.normalize();
    }
    public void readingFileContents(Path pathToFile){
        content.setLength(0);
       File fileToRead = pathToFile.toFile();
        try(BufferedReader reader = new BufferedReader(new FileReader(fileToRead))){
            String currrentLine;
            while((currrentLine=reader.readLine())!=null){
                content.append(currrentLine);
                content.append("\n");
                fileReader.add(currrentLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<String> returngContent(){
        return fileReader;
    }
    public String returnContentFromFile(String fileName){
        Path cleanPath = cleaningPath(fileName);
        readingFileContents(cleanPath);
        return content.toString();}

}
