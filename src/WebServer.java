import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class WebServer {



    // *USAGE!!!! ONCE YOU HAVE EVERYTHING TOGETHER you must run javac src*.java to complile all of the source files then run
    // "java -cp src WebServer" in terminal
    // once this is done, if everything is working you will see it says "Server running on http://localhost:8080"
    // After which the index shoudl be able to link to the stdDB.txt and write the passwords into it
    // once done you can return to the terminal and use ctrl + c to stop the server

    public static void main(String[] args) throws IOException {
        
        PasswordManager pm = new PasswordManager();

        
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        System.out.println("Server running on http://localhost:8080");

        
        server.createContext("/", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1); // checks for invalid methods
                return;
            }

            try {
                
                byte[] bytes = Files.readAllBytes(Paths.get("index.html")); // Load index.html 
                Headers headers = exchange.getResponseHeaders();
                headers.add("Content-Type", "text/html; charset=utf-8");
                exchange.sendResponseHeaders(200, bytes.length); // sends OK code
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } catch (IOException e) {
                String html = withBackLink("<h1>Error</h1><p>Could not load index.html</p>");
                sendHtml(exchange, html);
            }
        });

        // Handle user login and addition
        server.createContext("/user-login", exchange -> {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }

            String body = readBody(exchange.getRequestBody());
            Map<String, String> params = parseForm(body);

            String mode     = params.get("mode");
            String username = params.get("username");
            String password = params.get("password");

            String html;

            // Checks if user actually did everything correctly 
            if (mode == null || username == null || password == null) {
                html = withBackLink("<h1>Error</h1><p>Missing parameters.</p>");
            } else if (!username.matches("[A-Za-z0-9_]{1,16}")) {
                html = withBackLink("<h1>Error</h1><p>Invalid username.</p>");
            } else if (!password.matches("[A-Za-z0-9_!@#$%^&*()]{1,16}")) {
                html = withBackLink("<h1>Error</h1><p>Invalid password.</p>");
            } else if ("add".equals(mode)) {
                pm.addUser(username, password);
                html = withBackLink("<h1>User added</h1><p>User '" + username + "' added.</p>");
            } else if ("login".equals(mode)) {
                boolean ok = pm.verifyPassword(username, password);
                if (ok) {
                    html = withBackLink("<h1>Login successful</h1><p>Welcome, " + username + ".</p>");
                } else {
                    html = withBackLink("<h1>Login failed</h1><p>Incorrect username or password.</p>");
                }
            } else {
                html = withBackLink("<h1>Error</h1><p>Mode must be 'add' or 'login'.</p>");
            }

            sendHtml(exchange, html);
        });

        server.start();
    }

    // Read the request body into a String
    private static String readBody(InputStream is) throws IOException {
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    // Parse application and decodes annoying URL-encoded form data given the way the html was setup
    private static Map<String, String> parseForm(String body) throws IOException {
        Map<String, String> map = new HashMap<>();
        if (body == null || body.isEmpty()) return map;

        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String val = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                map.put(key, val);
            }
        }
        return map;
    }

    // Helper to send an HTML string as response
    private static void sendHtml(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Basic HTML to head back to the login 
    private static String withBackLink(String innerHtml) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Password Manager</title></head>"
                + "<body style='font-family: Arial, sans-serif; background: #f3f3f3; margin: 0; padding: 40px;'>"
                + "<div style='max-width: 400px; margin: 0 auto; background: #ffffff; padding: 20px 24px; "
                + "border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);'>"
                + innerHtml
                + "<p style='margin-top: 20px; font-size: 14px;'>"
                + "<a href=\"/\">Back to login page</a>"
                + "</p>"
                + "</div>"
                + "</body></html>";
    }
}
