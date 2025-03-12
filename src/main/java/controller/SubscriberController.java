package controller;

import org.eclipse.paho.client.mqttv3.MqttException;
import view.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubscriberController implements ActionListener {
    private Main viewMain;
    public SubscriberController(Main viewMain) {
        this.viewMain = viewMain;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("About")) {
            viewMain.about();
        } else if (e.getActionCommand().equals("Start")) {
            System.out.println("Start");
            viewMain.pauseThread(false);
        } else if (e.getActionCommand().equals("Stop")) {
            System.out.println("Stop");
            viewMain.pauseThread(true);
        } else if (e.getActionCommand().equals("Connect")) {
            System.out.println("Connect");
            try {
                viewMain.connect();
            } catch (MqttException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getActionCommand().equals("Disconnect")) {
            try {
                viewMain.disconnect();
            } catch (MqttException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
