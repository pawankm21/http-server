package org.httpserver.api.routes;

import lombok.Getter;
import lombok.Setter;
import org.httpserver.api.handlers.RequestHandler;
import org.httpserver.protocol.http.request.HttpRequest;

public interface Router {
    RequestHandler resolveHandler(HttpRequest request);
    void addRoute(String path, String method, RequestHandler handler);
}
