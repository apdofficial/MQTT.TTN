package Server;

import Protocol.*;
import TTN_downloader.TTN_downloader;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Server class.
 *
 * Following class implements Server which can handle multiple Clients.
 *
 * Server  is also listening to the incoming messages from The Thing Network
 *
 * @author Group 3
 * @version 0.1
 */
public class Server {
    public static void main(String[] argv) {
        ServerSocket serverSocket;

        try {
            //Create a new server socket for the clients to speak with
            serverSocket = new ServerSocket(49975);
            System.out.println("Running Server on port: 49975");
            //Initiate concurrency multithreading
            ExecutorService exec = Executors.newCachedThreadPool();
            //Create list for the connecting clients
            List<ClientProcessor> clients = Collections.synchronizedList(new ArrayList<>());

            //Creating Protocol

            //Create the command processor from CommandProcessor.java that handles the commands
            CommandProcessor commands = new CommandProcessor();

            //Create the ttn_downloader from TTN_downloader.java
            TTN_downloader ttn_downloader = new TTN_downloader();

            //Start to listen to TTN
            ttn_downloader.startListeningTTN();

            //Assigning commands to Protocol
            commands.addCommand(new DeviceNames());
            commands.addCommand(new Data());
            commands.addCommand(new Temperature());
            commands.addCommand(new Luminosity());
            commands.addCommand(new Humidity());
            commands.addCommand(new Pressure());

            //submit to a new thread
            exec.submit(() -> {
                //Read input to stop the program. The program can be stopped by parsing the letter "q"
                Scanner input = new Scanner(System.in);
                while(true){
                    String str = input.nextLine();
                    if (str.equalsIgnoreCase("q"))
                        break;
                }

                //When program is stopped, kill all the clients
                clients.forEach(client -> client.kill());
                System.out.println("should have killed");

               System.exit(0);
            });

            while (true) {
                ClientProcessor clientHandler = new ClientProcessor(serverSocket.accept(), commands);
                exec.submit(clientHandler);
                clients.add(clientHandler);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
