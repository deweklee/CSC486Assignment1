package controller;

import org.eclipse.paho.client.mqttv3.MqttException;
import view.Main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class PublisherController implements ActionListener {

    private Main viewMain;

    public PublisherController(Main viewMain) {
        this.viewMain = viewMain;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("About")) {
            viewMain.about();
        } else if (e.getActionCommand().equals("Start")) {
            viewMain.pauseThread(true, false);
        } else if (e.getActionCommand().equals("Stop")) {
            viewMain.pauseThread(true, true);
        } else if (e.getActionCommand().equals("Load")) {
            try {
                JFileChooser fileChooser = new JFileChooser();

                FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files", "csv");
                fileChooser.setFileFilter(csvFilter);
                fileChooser.setAcceptAllFileFilterUsed(false);

                int result = fileChooser.showOpenDialog(viewMain);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    viewMain.load(selectedFile.getAbsolutePath());
                }

            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getActionCommand().equals("Connect")) {
            try {
                System.out.println("connect");
                viewMain.connect(true);
            } catch (MqttException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getActionCommand().equals("Disconnect")) {
            try {
                System.out.println("disconnect");
                viewMain.disconnect(true);
            } catch (MqttException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}