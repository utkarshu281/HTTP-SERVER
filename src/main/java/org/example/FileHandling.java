package org.example;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class FileHandling {
    Path dir;
    ArrayList<String> fileReader;
    FileHandling(String path){
        dir =  Paths.get(path);
        fileReader=new ArrayList<>();
    }
    public boolean fileExistOrNot(String fileName){
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        Path file = dir.resolve(fileName);
        return Files.exists(file);
    }
    public int readingFileContentsAndLength(String filename){
        if (filename.startsWith("/")) {
            filename = filename.substring(1);
        }
        File fileToRead= dir.resolve(filename).toFile();
        int lengthOfContent=0;
        try(BufferedReader reader = new BufferedReader(new FileReader(fileToRead))){
            String currrentLine;
            while((currrentLine=reader.readLine())!=null){
                lengthOfContent=lengthOfContent+currrentLine.length();
                fileReader.add(currrentLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lengthOfContent;
    }
    public ArrayList<String> returngContent(){
        return fileReader;
    }

}
