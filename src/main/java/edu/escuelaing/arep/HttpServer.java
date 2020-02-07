package edu.escuelaing.arep;

import org.apache.commons.io.FilenameUtils;

import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP Server class, returns requested resources
 */
public class HttpServer {

    /**
     * The server socket that the Http server will use
     */
    private ServerSocket serverSocket;

    /**
     * Socket for communication between client and server.
     */
    private Socket clientSocket;

    /**
     * Text-output stream for communication
     */
    private PrintWriter out;

    /**
     * Text-input stream for communication
     */
    private BufferedReader in;

    /**
     * Creates an instance of the Http Server object
     */
    public HttpServer() {
        int port = getPort();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(1);
        }
        clientSocket = null;
        out = null;
        in = null;
    }

    /**
     * Starts the server, begins to listen to connections
     *
     * @throws IOException
     */
    public void start() throws IOException {
        while (true) {
            try {
                System.out.println("Ready to receive");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            Pattern pattern = Pattern.compile("GET (/[^\\s]*)");
            Matcher matcher = null;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                stringBuilder.append(inputLine);
                if (!in.ready()) {
                    matcher = pattern.matcher(stringBuilder.toString());
                    if (matcher.find()) {
                        String fileRequested = matcher.group().substring(5);
                        System.out.println("VALUE: " + fileRequested);
                        handleRequest(fileRequested);
                    }
                    break;
                }
            }

            out.close();
            in.close();
            clientSocket.close();
        }
    }

    /**
     * Handles how to send back a requested resource
     *
     * @param fileRequested
     * @throws IOException
     */
    private void handleRequest(String fileRequested) throws IOException {
        String filePath = "src/main/resources/";
        String ext = FilenameUtils.getExtension(fileRequested);
        boolean isImage = false;
        switch (ext) {
            case "png":
                filePath += "images/" + fileRequested;
                isImage = true;
                break;
            case "js":
                filePath += "js/" + fileRequested;
                break;
            case "html":
                filePath += "web-pages/" + fileRequested;
                break;
        }

        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            String header = generateHeader(isImage, ext, file.length());
            if (isImage) {
                FileInputStream fileIn = new FileInputStream(filePath);
                OutputStream os = clientSocket.getOutputStream();
                for (char c : header.toCharArray()) {
                    os.write(c);
                }
                int a;
                while ((a = fileIn.read()) > -1) {
                    os.write(a);
                }
                os.flush();
                fileIn.close();
                os.close();
            } else {
                out.println(header);
                BufferedReader br = new BufferedReader(new FileReader(file));

                StringBuilder stringBuilder = new StringBuilder();
                String st;
                while ((st = br.readLine()) != null) {
                    stringBuilder.append(st);
                }
                out.println(stringBuilder.toString());
                br.close();
            }
        } else {
            out.println("HTTP/1.1 404\r\nAccess-Control-Allow-Origin: *\r\n\r\n<html><body><h1>404 NOT FOUND ("+fileRequested+")</h1></body></html>");
        }
    }

    /**
     * Generates a header for the browser
     *
     * @param isImage tells if the requested resource is an image
     * @param ext the extension of the resource
     * @param length the length of the resource
     * @return the header
     */
    private String generateHeader(boolean isImage, String ext, long length) {
        String header = null;
        if (isImage) {
            header = "HTTP/1.1 200 \r\nAccess-Control-Allow-Origin: *\r\nContent-Type: image/" + ext + "\r\nConnection: close\r\nContent-Length:" + length + "\r\n\r\n";
        } else {
            header = "HTTP/1.1 200 \r\nAccess-Control-Allow-Origin: *\r\nContent-Type: text/html\r\n\r\n";
        }
        return header;
    }


    /**
     * This method reads the default port as specified by the PORT variable in
     * the environment.
     *
     * @return The port variable if set, else 4567 as default
     */
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    /**
     * Main method that starts the HTTP Server
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new HttpServer().start();
    }

}
