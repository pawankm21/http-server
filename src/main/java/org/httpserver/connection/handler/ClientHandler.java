package org.httpserver.connection.handler;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;
import org.httpserver.protocol.http.request.HttpRequestParser;
import org.httpserver.protocol.http.response.HttpResponseWriter;
import org.httpserver.api.routes.Router;

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
    private final Socket clientSocket;
    private final Router router;

    public ClientHandler(Socket clientSocket, Router router) {
        this.clientSocket = clientSocket;
        this.router = router;
    }

    @Override
    public void run() {
        try {

            var request = HttpRequestParser.parse(clientSocket.getInputStream());

            var handler = router.resolveHandler(request);

            var response = handler.handle(request);

            HttpResponseWriter.write(response, clientSocket.getOutputStream());

        } catch (Exception e) {
            LOGGER.severe("Error handling client request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.warning("Error closing client socket: " + e.getMessage());
            }
        }
    }
}