package view;
import controller.SubscriberController;
import model.Blackboard;
import model.Subscriber;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private Subscriber subscriber;

    private JMenuBar createSubscriberMenuBar() {
        // controller
        SubscriberController controller = new SubscriberController(this);
        // construct
        JMenuBar menuBar = new JMenuBar();
        JMenu connectMenu = new JMenu("Connection");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        JMenuItem startMenuItem = new JMenuItem("Start");
        JMenuItem stopMenuItem = new JMenuItem("Stop");
        JMenuItem connectMenuItem = new JMenuItem("Connect");
        JMenuItem disconnectMenuItem = new JMenuItem("Disconnect");
        connectMenu.add(connectMenuItem);
        connectMenu.add(disconnectMenuItem);
        connectMenu.add(startMenuItem);
        connectMenu.add(stopMenuItem);
        helpMenu.add(aboutMenuItem);
        startMenuItem.addActionListener(controller);
        stopMenuItem.addActionListener(controller);
        aboutMenuItem.addActionListener(controller);
        connectMenuItem.addActionListener(controller);
        disconnectMenuItem.addActionListener(controller);
        connectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.SHIFT_DOWN_MASK));
        disconnectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_DOWN_MASK));
        menuBar.add(connectMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }


    public Main() throws InterruptedException {
        subscriber = new Subscriber();
        Thread subThread = new Thread(subscriber);
        subThread.start();

        ReadingComprehensionGamePanel subCentralPanel = new ReadingComprehensionGamePanel();

        setJMenuBar(createSubscriberMenuBar());

        setLayout(new BorderLayout());
        add(subCentralPanel, BorderLayout.CENTER);
        Blackboard.getInstance().addPropertyChangeListener("subPanel", subCentralPanel);

    }
    public void about() {
        // view
        JOptionPane.showMessageDialog(this, "About Skeleton");
    }

    public void pauseThread(boolean b) {
        subscriber.pause(b);
    }

    public void connect() throws MqttException {
        subscriber.connect();
    }

    public void disconnect() throws MqttException {
        subscriber.disconnect();
    }
    public static void main(String[] args) throws InterruptedException {
        Main main1 = new Main();
        Blackboard.getInstance().setOutputFile("./output/output-" + System.currentTimeMillis() + ".csv");
        main1.setSize(800, 600);
        main1.setLocationRelativeTo(null);
        main1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main1.setVisible(true);
        main1.setTitle("Subscriber");
    }


}