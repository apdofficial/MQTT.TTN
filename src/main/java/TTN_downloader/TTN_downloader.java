package TTN_downloader;

import Database.DBAccessor;
import Database.DBmanager;
import Database.LoadDatabaseProperties;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * TTN_downloader class.
 *
 * This class makes a connection to TTN and retrieves the data from it's MQTT server using a paho client
 *
 * @author Group 3
 */

public class TTN_downloader {

    public void startListeningTTN() {
        //Load the properties for the database from the data.properties file.
        //Change the settings in that file to match your database
        LoadDatabaseProperties properties = new LoadDatabaseProperties("data.properties");
        //Persistence that uses memory to store data
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            //Paho client that can talk to an MQTT server
            MqttClient sampleClient = new MqttClient(
                    properties.getProperty("ttn.broker"),
                    properties.getProperty("ttn.clientId"), persistence);

            //Holds the set of options that control how the client connects to a server
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            //Retrieve data from property file.
            //Fill in your TTN app ID and Access key
            connOpts.setUserName(properties.getProperty("ttn.app_id"));
            connOpts.setPassword(properties.getProperty("ttn.access_key").toCharArray());

            //Make a dbmanager and make a connection with a running database on localhost:3306
            DBmanager dbManager = new DBmanager();
            dbManager.makeConnection();
            DBAccessor DBAccessor = new DBAccessor(dbManager);

            //Sets the callback listener to use for events that happen asynchronously
            sampleClient.setCallback(new MqttCallback() {

                public void connectionLost(Throwable cause) {
                }
                //function that prints a line to the terminal and stores the data via the DBAccessor class
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if(message.toString().startsWith("{")) System.out.println("Data received and inserted into database");
                    if(message.toString().startsWith("{")) DBAccessor.insertData(message);
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });
            //connect to an MQTT server
            sampleClient.connect(connOpts);
            //subscribe to the TTN app with corresponding app ID
            sampleClient.subscribe(properties.getProperty("ttn.app_id") + "/#");
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}