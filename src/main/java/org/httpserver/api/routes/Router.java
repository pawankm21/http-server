package org.httpserver.api.routes;

import lombok.Getter;
import lombok.Setter;
import org.httpserver.api.handlers.RequestHandler;

import java.net.http.HttpRequest;

public interface Router {
    RequestHandler resolveHandler(HttpRequest request);
    void addRoute(String path, String method, RequestHandler handler);
}
