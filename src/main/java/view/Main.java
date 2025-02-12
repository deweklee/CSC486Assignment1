package view;
import controller.PublisherController;
import controller.SubscriberController;
import model.Blackboard;
import model.Publisher;
import model.Subscriber;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class Main extends JFrame {

    private Publisher publisher;
    private Subscriber subscriber;

    private JFrame secondFrame;

    private JMenuBar createPublisherMenuBar() {
        // controller
        PublisherController controller = new PublisherController(this);
        // construct
        JMenuBar menuBar = new JMenuBar();
        JMenu connectMenu = new JMenu("Connection");
        JMenu helpMenu = new JMenu("Help");
        JMenu fileMenu = new JMenu("File");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        JMenuItem startMenuItem = new JMenuItem("Start");
        JMenuItem stopMenuItem = new JMenuItem("Stop");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        JMenuItem connectMenuItem = new JMenuItem("Connect");
        JMenuItem disconnectMenuItem = new JMenuItem("Disconnect");
        connectMenu.add(connectMenuItem);
        connectMenu.add(disconnectMenuItem);
        connectMenu.add(startMenuItem);
        connectMenu.add(stopMenuItem);
        helpMenu.add(aboutMenuItem);
        fileMenu.add(loadMenuItem);
        startMenuItem.addActionListener(controller);
        stopMenuItem.addActionListener(controller);
        aboutMenuItem.addActionListener(controller);
        loadMenuItem.addActionListener(controller);
        connectMenuItem.addActionListener(controller);
        disconnectMenuItem.addActionListener(controller);
        connectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.SHIFT_DOWN_MASK));
        disconnectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_DOWN_MASK));
        menuBar.add(connectMenu);
        menuBar.add(helpMenu);
        menuBar.add(fileMenu);
        return menuBar;
    }

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
        publisher = new Publisher();
        Thread modelThread = new Thread(publisher);
        modelThread.start();

        subscriber = new Subscriber();
        Thread subThread = new Thread(subscriber);
        subThread.sleep(1000);
        subThread.start();

        setJMenuBar(createPublisherMenuBar());
        PublisherViewPanel pubCentralPanel = new PublisherViewPanel();
        setLayout(new BorderLayout());
        add(pubCentralPanel, BorderLayout.CENTER);
        Blackboard.getInstance().addPropertyChangeListener("pubPanel", pubCentralPanel);

        secondFrame = new JFrame("Second Window");

        secondFrame.setJMenuBar(createSubscriberMenuBar());
        SubscriberViewPanel subCentralPanel = new SubscriberViewPanel();
        secondFrame.setLayout(new BorderLayout());
        secondFrame.add(subCentralPanel, BorderLayout.CENTER);
        Blackboard.getInstance().addPropertyChangeListener("subPanel",subCentralPanel);
        secondFrame.setSize(800, 600);
        secondFrame.setLocationRelativeTo(null);
        secondFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        secondFrame.setVisible(true);
        secondFrame.setTitle("Subscriber");

    }
    public void about() {
        // view
        JOptionPane.showMessageDialog(this, "About Skeleton");
    }

    public void pauseThread(Boolean isPublisher, boolean b) {
        // controller
        if (isPublisher) {
            publisher.pause(b);
        } else {
            subscriber.pause(b);
        }

    }

    public void load(String filepath) throws FileNotFoundException {
        publisher.loadFile(filepath);
    }

    public void connect(Boolean isPublisher) throws MqttException {
        // controller
        if (isPublisher) {
            publisher.connect();
        } else {
            subscriber.connect();
        }
    }

    public void disconnect(Boolean isPublisher) throws MqttException {
        // controller
        if (isPublisher) {
            publisher.disconnect();
        } else {
            subscriber.disconnect();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Main main1 = new Main();
        main1.setSize(800, 600);
        main1.setLocationRelativeTo(null);
        main1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main1.setVisible(true);
        main1.setTitle("Publisher");
    }


}
