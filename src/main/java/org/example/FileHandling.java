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
    public boolean fileExistOrNot(String fileName){
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        Path file = dir.resolve(fileName);
        return Files.exists(file);
    }
    public void readingFileContents(String filename){
        content.setLength(0);
        if (filename.startsWith("/")) {
            filename = filename.substring(1);
        }
        File fileToRead= dir.resolve(filename).toFile();
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
        readingFileContents(fileName);
        return content.toString();}

}
