package com.swingtech.app.tabletracker.service.experiment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class TcpServerDebugServer {
	protected static final int HTTP_SERVER_PORT_NUMBER = 9090;

	public static void main(String[] args) throws Exception {
		 handleManualSockets();
//		handleHttpServer();
	}

	public static void handleManualSockets() throws Exception {
		System.out.println("The capitalization server is running.");
		int clientNumber = 0;
		ServerSocket listener = new ServerSocket(HTTP_SERVER_PORT_NUMBER);
		try {
			System.out.println("Creating new debugger.");
			while (true) {
				new TcpServerDebugger(listener.accept(), clientNumber++).start();
			}
		} finally {
			System.out.println("Finally1  Finally in main progoram reached.  cosing listener and exiting.");
			listener.close();
		}
	}

	public static void handleHttpServer() throws Exception {
		System.out.println("The Http Server is running.");

		HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_SERVER_PORT_NUMBER), 0);
		server.createContext("/info", new InfoHandler());
		server.createContext("/get", new GetHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	static class InfoHandler implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {
		    	System.out.println("\n\nHandling /info");
		      String response = "Use /get to download a PDF";
		      
		      printRequestinfo(httpExchange);
		      
		      httpExchange.sendResponseHeaders(200, response.length());
		      OutputStream os = httpExchange.getResponseBody();
		      os.write(response.getBytes());
		      os.close();
		    }
		
		public void printRequestinfo(HttpExchange httpExchange) {
			System.out.println("\nPrinting Request Info");
			
			System.out.println("  URL:  " + httpExchange.getRequestURI());
			System.out.println("  Method:  " + httpExchange.getRequestMethod());
			System.out.println("  Http Context Attributes:  " + httpExchange.getHttpContext().getAttributes());
			System.out.println("  Headers:  " + httpExchange.getRequestHeaders());
			System.out.println("  URL:  " + httpExchange.getRequestURI());
			System.out.println("  URL:  " + httpExchange.getRequestURI());
			System.out.println("  URL:  " + httpExchange.getRequestURI());
		}
	}
	

	static class GetHandler implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {
			System.out.println("handling /get");

			// add the required response header for a PDF file
			Headers h = httpExchange.getResponseHeaders();
			h.add("Content-Type", "application/pdf");

			// a PDF (you provide your own!)
			File file = new File("c:/temp/doc.pdf");
			byte[] bytearray = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(bytearray, 0, bytearray.length);

			// ok, we are ready to send the response.
			httpExchange.sendResponseHeaders(200, file.length());
			OutputStream os = httpExchange.getResponseBody();
			os.write(bytearray, 0, bytearray.length);
			os.close();
		}
	}
}
