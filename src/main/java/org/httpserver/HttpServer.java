package org.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.httpserver.socket.SocketFactory;

public class HttpServer {
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getName());
    private final int port;
    private final int backlog;
    private final SocketFactory socketFactory;
    private ServerSocket serverSocket;
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * Creates a new HttpServer instance with the specified port and backlog.
     *
     * @param port   the port number on which the server will listen for incoming connections
     * @param backlog the maximum number of queued connections
     */
    public HttpServer(int port,int backlog){
        this.port = port;
        this.backlog = backlog;
        this.socketFactory = new SocketFactory();
    }
    /**
     * Starts the HTTP server.
     *
     * @throws Exception if an error occurs while starting the server
     */
    public void start() throws IOException {
        serverSocket = socketFactory.createServerSocket(port, 100);
        running.set(true);
        LOGGER.info("HTTP server started on port: " + port);
        new Thread(this::acceptConnections).start();
    }
    private void acceptConnections(){
        while(running.get() && !serverSocket.isClosed()){
            try {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("New connection accepted from: " + clientSocket.getInetAddress());
                clientSocket.close();
            } catch (IOException e) {
                if (running.get()) {
                    LOGGER.severe("Error accepting connection: " + e.getMessage());
                }
            }
        }
    }
    public void stop(){
        running.set(false);
        if(serverSocket !=null && !serverSocket.isClosed()){
            try {
                serverSocket.close();
                LOGGER.info("HTTP server stopped.");
            } catch (IOException e) {
                LOGGER.severe("Error stopping server: " + e.getMessage());
            }
        }
    }
}
