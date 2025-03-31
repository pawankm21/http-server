package org.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.httpserver.api.routes.Router;
import org.httpserver.connection.handlers.ClientHandler;
import org.httpserver.connection.socket.SocketFactory;
import org.httpserver.utils.config.ServerConstants;

public class HttpServer {
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getName());

    private final int port;
    private final int backlog;
    private final SocketFactory socketFactory;
    private ServerSocket serverSocket;
    private final AtomicBoolean running;
    private final ExecutorService threadPool;
    private  Router router;
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
        running = new AtomicBoolean(false);
        threadPool = Executors.newFixedThreadPool(ServerConstants.threadPoolSize);
        //TODO: create router
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
        threadPool.execute(this::acceptConnections);
    }

    private void acceptConnections(){
        while(running.get() && !serverSocket.isClosed()){
            try {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("New connection accepted from: " + clientSocket.getInetAddress());
                threadPool.execute(new ClientHandler(clientSocket,router));
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
