package model;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.io.*;

public class Publisher implements Runnable {

    private final static String BROKER = "tcp://test.mosquitto.org:1883";
    private final static String TOPIC = "csc486/assgn1";
    private final static String CLIENT_ID = "publisher" + System.currentTimeMillis();
    private final static String STATUS_KEY = "pubPanel";
    private static volatile BufferedReader file = null;
    private boolean wait = true;
    private MqttClient client;
    @Override
    public void run() {
        while (true) {
            try {
                if (file != null && !wait && client != null){
                    String line = file.readLine();
                    while (line != null && !wait && client != null) {
                        MqttMessage message = new MqttMessage(line.getBytes());
                        message.setQos(2);
                        if (client.isConnected()) {
                            client.publish(TOPIC, message);
                            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Publishing message: " + message);
                        } else {
                            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Client not connected");
                            System.out.println(client);
                        }
                        // Prepare for next loop iteration
                        line = file.readLine();
                        Thread.sleep(10);
                    }
                }

            } catch (InterruptedException | IOException | MqttException e) {
                System.out.println("error " + e.getLocalizedMessage());
            }
        }
    }
    public void connect(){
        if (client != null && client.isConnected()) {
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Publisher is already connected");
        } else {
            try {
                client = new MqttClient(BROKER, CLIENT_ID);
                client.connect();
                Blackboard.getInstance().statusUpdate(STATUS_KEY, "Connection Successful");
            } catch (MqttException e) {
                Blackboard.getInstance().statusUpdate(STATUS_KEY, "Connection Failed");
            }
        }
    }

    public void disconnect() throws MqttException {
        if (client == null || (client != null && !client.isConnected())) {
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Publisher is already disconnected");
        } else {
            client.disconnect();
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Successfully Disconnected");
        }
    }

    public void pause(boolean wait) {
        if (!wait && file == null) {
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Cannot publish because file is not loaded");
        } else if (!wait && client == null) {
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Cannot publish because no connection");
        } else {
            this.wait = wait;
            if (wait) {
                Blackboard.getInstance().statusUpdate(STATUS_KEY, "Publishing Paused");
            } else {
                Blackboard.getInstance().statusUpdate(STATUS_KEY, "Publishing Started");
            }
        }
    }
    public void loadFile(String filepath){
        try {
            file = new BufferedReader(new FileReader(filepath));
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "File Successfully Loaded from " + filepath);
        } catch(FileNotFoundException e) {
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "File Failed to Load");
        }


    }

}