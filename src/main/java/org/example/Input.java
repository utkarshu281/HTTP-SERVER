package org.example;

import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name="HTTP SERVER",mixinStandardHelpOptions = true)
public class Input implements Runnable {
    HttpServer httpServerObject;
    ClientHandler clientHandler;
    Input(HttpServer httpServer,ClientHandler clientHandler){
        this.httpServerObject = httpServer;
        this.clientHandler=clientHandler;
    }
    @CommandLine.Option(names = "--port",description = "port for socket to conect")
    public int port;
    @CommandLine.Option(names="--dir",description = "path to file")
    public String directory;
    @Override
    public void run() {
        try {
            httpServerObject.initializeServer(port,directory,clientHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        ClientHandler clientHandler = new ClientHandler();
        HttpServer httpServerObject = new HttpServer();
        int exitCode = new CommandLine(new Input(httpServerObject,clientHandler)).execute(args);
        System.exit(exitCode);
    }
}
