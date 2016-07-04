package com.swingtech.app.tabletracker.service.experiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swingtech.app.tabletracker.service.service.TableTrackerService;

public class TcpServerDebugger extends Thread {
	public final static Logger logger = LoggerFactory.getLogger(TableTrackerService.class);;
	
    private Socket socket;
    private int clientNumber;

    public TcpServerDebugger(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        log("New connection with client# " + clientNumber + " at " + socket);
    }
    
    public void run() {
        try {

            System.out.println("starting TcpServerDebugger.run()");
            // Decorate the streams so we can send characters
            // and not just bytes.  Ensure output is flushed
            // after every newline.
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Send a welcome message to the client.
            out.println("Hello, you are client #" + clientNumber + ".");
            out.println("Enter a line with only a period to quit\n");

            System.out.println("Hello, you are client #" + clientNumber + ".");
            System.out.println("Enter a line with only a period to quit\n");
            
            System.out.println("entering deamon loop\n\n\n");
            
            // Get messages from the client, line by line; return them
            // capitalized
            while (true) {
                String input = in.readLine();

                System.out.println("  readline -->     " + input);
                
                if (input == null) {
                    break;
                }
                out.println(input.toUpperCase());
            }
        } catch (IOException e) {
            log("Error handling client# " + clientNumber + ": " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log("Couldn't close a socket, what's going on?");
            }
            log("Connection with client# " + clientNumber + " closed");
        }
    }

    /**
     * Logs a simple message.  In this case we just write the
     * message to the server applications standard output.
     */
    private void log(String message) {
        System.out.println(message);
    }
}
