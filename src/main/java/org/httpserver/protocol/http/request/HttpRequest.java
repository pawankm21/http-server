package org.httpserver.protocol.http.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.httpserver.protocol.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class HttpRequest {
    private final String  uri;
    private final Map<String,Object> headers = new HashMap<>();
    private final String body;
    private final HttpMethod method;
    private final String version;

}
