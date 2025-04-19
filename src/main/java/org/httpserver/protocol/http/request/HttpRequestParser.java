package org.httpserver.protocol.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    public static HttpRequest parse(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // Parse request line
        String requestLine = reader.readLine();
        if (requestLine == null) {
            throw new IOException("Empty request");
        }

        String[] requestLineParts = requestLine.split(" ");
        if (requestLineParts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }

        String method = requestLineParts[0];
        String path = requestLineParts[1];
        String version = requestLineParts[2];

        // Parse headers
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            int colonIndex = headerLine.indexOf(':');
            if (colonIndex > 0) {
                String name = headerLine.substring(0, colonIndex).trim();
                String value = headerLine.substring(colonIndex + 1).trim();
                headers.put(name, value);
            }
        }

        // Parse body if present
        StringBuilder body = new StringBuilder();
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[1024];
            int bytesRead;
            int totalBytesRead = 0;

            while (totalBytesRead < contentLength &&
                    (bytesRead = reader.read(buffer, 0, Math.min(buffer.length, contentLength - totalBytesRead))) != -1) {
                body.append(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }
        }

        return new HttpRequest(method, path, version, headers, body.toString());
    }
}