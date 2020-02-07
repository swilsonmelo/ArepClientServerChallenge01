package edu.escuelaing.arep;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.junit.Assert.fail;

/**
 * Test class for the Http server
 */
public class HttpServerTest {

    private static HttpServer httpServer;

    private Socket echoSocket;

    private PrintWriter out;

    private BufferedReader in;

    @Before
    public void init() {
        if (httpServer == null) {
            httpServer = new HttpServer();
            Thread serverThread = new Thread(() -> {
                try {
                    httpServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    fail();
                }
            });
            serverThread.start();
        }
        echoSocket = null;
        out = null;
        in = null;

        try {
            echoSocket = new Socket("127.0.0.1", 4567);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don’t know about host!.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn’t get I/O for "
                    + "the connection to: localhost.");
            System.exit(1);
        }
    }

    @After
    public void close() throws IOException {
        out.close();
        in.close();
        echoSocket.close();
    }


    @Test
    public void shouldReturn200() throws IOException {
        out.println("GET /index.html");
        String response = in.readLine();
        Assert.assertTrue(response.contains("200"));
    }

    @Test
    public void shouldReturn404() throws IOException {
        out.println("GET /not.not");
        String response = in.readLine();
        Assert.assertTrue(response.contains("404"));
    }

    @Test
    public void shouldReturnImageHeader() throws IOException {
        out.println("GET /java.png");
        StringBuilder response = new StringBuilder();
        String ln;
        while ((ln = in.readLine()) != null) {
            response.append(ln);
        }
        Assert.assertTrue(response.toString().contains("image/png"));
    }

    @Test
    public void shouldReturnTextHeader() throws IOException {
        out.println("GET /index.html");
        StringBuilder response = new StringBuilder();
        String ln;
        while ((ln = in.readLine()) != null) {
            response.append(ln);
        }
        Assert.assertTrue(response.toString().contains("text/"));
    }

}
