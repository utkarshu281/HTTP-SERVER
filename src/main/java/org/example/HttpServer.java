package org.example;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
public class HttpServer {
    public void initializeServer(int port, String directory, ClientHandler clientHandler) throws IOException {
        FileHandling fileHandler = new FileHandling(directory);
        ServerSocket server;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("server is started....");
        serverListening(server,port,clientHandler,directory,fileHandler);
        System.out.print("Server is closed.....");
        server.close();
    }
    public void serverListening(ServerSocket server, int port,ClientHandler clientHandler,String directory,FileHandling fileHandler){
        while(true){
            try {
                System.out.println("server is listening on port "+port);
                Socket clientSocket = server.accept();
                /*I have left this comment, the reason another way to read the client input
                    BufferedReader reader =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String message=reader.readLine();
                    System.out.println("Received: "+message); */
                    Scanner scanner = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(),true);
                    clientHandler.parsing(scanner,clientSocket,fileHandler,output);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}