package org.httpserver.api.routes;

import java.util.HashMap;
import java.util.Map;
import org.httpserver.api.handlers.RequestHandler;
import org.httpserver.protocol.http.request.HttpRequest;
import org.httpserver.protocol.http.response.HttpResponse;

public class SimpleRouter implements Router {
    private final Map<String, Map<String, RequestHandler>> routes = new HashMap<>();
    private final RequestHandler notFoundHandler;

    public SimpleRouter() {
        // Default handler for 404 Not Found
        this.notFoundHandler = request -> {
            return HttpResponse.builder()
                    .statusCode(404)
                    .statusMessage("Not Found")
                    .header("Content-Type", "text/plain")
                    .body("404 Not Found: " + request.getPath())
                    .build();
        };
    }

    @Override
    public RequestHandler resolveHandler(HttpRequest request) {
        Map<String, RequestHandler> methodHandlers = routes.get(request.getPath());
        if (methodHandlers != null) {
            RequestHandler handler = methodHandlers.get(request.getMethod());
            if (handler != null) {
                return handler;
            }
        }
        return notFoundHandler;
    }

    @Override
    public void addRoute(String path, String method, RequestHandler handler) {
        routes.computeIfAbsent(path, k -> new HashMap<>()).put(method, handler);
    }
}