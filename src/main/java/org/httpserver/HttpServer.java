package org.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.httpserver.api.handlers.RequestHandler;
import org.httpserver.api.routes.Router;
import org.httpserver.api.routes.SimpleRouter;
import org.httpserver.connection.handler.ClientHandler;
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
    private final Router router;

    /**
     * Creates a new HttpServer instance with the specified port and backlog.
     *
     * @param port   the port number on which the server will listen for incoming connections
     * @param backlog the maximum number of queued connections
     */
    public HttpServer(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
        this.socketFactory = new SocketFactory();
        this.running = new AtomicBoolean(false);
        this.threadPool = Executors.newFixedThreadPool(ServerConstants.threadPoolSize);
        this.router = new SimpleRouter();
    }

    /**
     * Starts the HTTP server.
     *
     * @throws IOException if an error occurs while starting the server
     */
    public void start() throws IOException {
        serverSocket = socketFactory.createServerSocket(port, backlog);
        running.set(true);
        LOGGER.info("HTTP server started on port: " + port);
        threadPool.execute(this::acceptConnections);
    }

    private void acceptConnections() {
        while (running.get() && !serverSocket.isClosed()) {
            try {Socket clientSocket = serverSocket.accept();
                LOGGER.info("New connection accepted from: " + clientSocket.getInetAddress());
                threadPool.execute(new ClientHandler(clientSocket, router));
            } catch (IOException e) {
                if (running.get()) {
                    LOGGER.severe("Error accepting connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Adds a new route to the server.
     *
     * @param path the URL path
     * @param method the HTTP method (GET, POST, etc.)
     * @param handler the handler for this route
     */
    public void addRoute(String path, String method, RequestHandler handler) {
        router.addRoute(path, method, handler);
    }

    /**
     * Stops the HTTP server.
     */
    public void stop() {
        running.set(false);
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                LOGGER.info("HTTP server stopped.");
            } catch (IOException e) {
                LOGGER.severe("Error stopping server: " + e.getMessage());
            }
        }
        threadPool.shutdown();
    }
}