package Server;

import Protocol.CommandProcessor;

import java.io.*;
import java.net.Socket;

public class ClientProcessor implements Runnable {
    private Socket client;
    private CommandProcessor commandProcessor;

    public ClientProcessor(Socket client, CommandProcessor commandProcessor) {
        this.client = client;
        this.commandProcessor = commandProcessor;
    }

    public void kill(){
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        try (BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter outToClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))){

            String request = inFromClient.readLine();
            while (request != null) {
                    String serverResponse = commandProcessor.processRequest(request);
                        outToClient.write(serverResponse);
                        outToClient.newLine();
                        outToClient.flush();
                        request = inFromClient.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
