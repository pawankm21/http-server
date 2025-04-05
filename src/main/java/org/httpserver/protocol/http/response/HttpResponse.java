package org.httpserver.protocol.http.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class HttpResponse {
    private final int statusCode;
    private String statusMessage;
    private final Map<String, Object> headers;
    private final String body;

    public static class Builder {
        private int statusCode = 200;
        private String statusMessage = "OK";
        private final Map<String, Object> headers = new HashMap<>();
        private String body = "";

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder statusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
            return this;
        }

        public Builder header(String name, Object value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(statusCode, statusMessage, headers, body);
        }
    }
}
