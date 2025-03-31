package org.httpserver.connection.handlers;

import org.httpserver.api.routes.Router;

import java.net.Socket;
import java.util.logging.Logger;

public class ClientHandler implements Runnable{
    public static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
    private final Socket clientSocket;
    private final Router router;
    public ClientHandler(Socket clientSocket, Router router){
        this.clientSocket = clientSocket;
        this.router = router;
    }
    @Override
    public void run() {
        //TODO: parser request
        //TODO: get a response
        //TODO:write the response back to the client

    }
}
