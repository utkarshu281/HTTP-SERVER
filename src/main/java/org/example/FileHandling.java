package org.example;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class FileHandling {
    Path dir;
    ArrayList<String> fileReader;
    FileHandling(){
        dir =  Paths.get("www");
        fileReader=new ArrayList<>();
    }
    public int readingFileContentsAndLength(String filename){
        int statuCode=0;
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
        } catch (FileNotFoundException e) {
            return -1;//denotes that there is some problem with program
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
        }
        return lengthOfContent;
    }
    public ArrayList<String> returngContent(){
        return fileReader;
    }

}
