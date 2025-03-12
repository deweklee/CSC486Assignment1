package model;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONException;

import java.io.IOException;

public class Subscriber implements Runnable, MqttCallback {

    private boolean wait = true;
    private final static String STATUS_KEY = "subPanel";
    private final static String BROKER = "tcp://test.mosquitto.org:1883";
    private final static String TOPIC = "jgs/unity/test";
    private final static String CLIENT_ID = "subscriber-" + System.currentTimeMillis();
    private MqttClient client;
    @Override
    public void run() {

    }

    public void pause(boolean wait) {
        this.wait = wait;
        if (wait) {
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Message Displaying Paused");
        } else {
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Message Displaying Started");
        }
    }

    public void connect() {
        if (client != null && client.isConnected()) {
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Subscriber is already connected");
        } else {
            try {
                client = new MqttClient(BROKER, CLIENT_ID);
                client.setCallback(this);
                client.connect();
                client.subscribe(TOPIC);
                Blackboard.getInstance().statusUpdate(STATUS_KEY, "Connection Successful");
            } catch (MqttException e) {
                Blackboard.getInstance().statusUpdate(STATUS_KEY, "Connection Failed");
            }
        }

    }

    public void disconnect() throws MqttException {
        if (client == null || (client != null && !client.isConnected())) {
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Subscriber is already disconnected");
        } else {
            client.disconnect();
            Blackboard.getInstance().statusUpdate(STATUS_KEY, "Successfully Disconnected");
        }
    }
    @Override
    public void connectionLost(Throwable throwable) {
        Blackboard.getInstance().statusUpdate(STATUS_KEY, "Connection Lost");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws JSONException, IOException {
//        if (!wait) {
//            Blackboard.getInstance().statusUpdate(STATUS_KEY, "New Message");
//        }
        Blackboard.getInstance().addValue(wait, new String(mqttMessage.getPayload()));

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivered complete: " + iMqttDeliveryToken.getMessageId());
    }
}