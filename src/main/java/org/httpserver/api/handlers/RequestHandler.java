package org.httpserver.api.handlers;

import org.httpserver.protocol.http.request.HttpRequest;
import org.httpserver.protocol.http.response.HttpResponse;

@FunctionalInterface
public interface RequestHandler {
    HttpResponse handle(HttpRequest request);
}