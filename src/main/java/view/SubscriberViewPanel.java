package view;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;

public class SubscriberViewPanel extends JPanel implements PropertyChangeListener {

    private JTextArea textArea;
    private JPanel colorPanel;
    private JLabel pleasureLabel, arousalLabel, dominanceLabel;
    private JLabel mmm, mpm, mpp, ppp, pmm, pmp, mmp, ppm;

    private int mmma = 255, mpma = 255, mppa = 255, pppa = 255, pmma = 255, pmpa = 255, mmpa = 255, ppma = 255;

    public SubscriberViewPanel() {
        setLayout(new GridLayout(1, 2));

        // TextArea with scrollable pane
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Panel to hold the three color squares
        colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(4, 2));

        pleasureLabel = createColoredLabel("Pleasure");
        arousalLabel = createColoredLabel("Arousal");
        dominanceLabel = createColoredLabel("Dominance");

        mmm = createColoredLabel("---");
        mpm = createColoredLabel("-+-");
        mpp = createColoredLabel("-++");
        ppp = createColoredLabel("+++");
        pmm = createColoredLabel("+--");
        pmp = createColoredLabel("+-+");
        mmp = createColoredLabel("--+");
        ppm = createColoredLabel("++-");

        updateLabelColors();

        colorPanel.add(mmm);
        colorPanel.add(mpm);
        colorPanel.add(mpp);
        colorPanel.add(ppp);
        colorPanel.add(pmm);
        colorPanel.add(pmp);
        colorPanel.add(mmp);
        colorPanel.add(ppm);

        add(scrollPane);
        add(colorPanel);

        startAlphaReduction();
    }

    private JLabel createColoredLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.GRAY); // Default color
        label.setPreferredSize(new Dimension(100, 50)); // Set size
        return label;
    }

    private void updateLabelColors() {
        mmm.setBackground(new Color(0, 255, 0, mmma));
        mpm.setBackground(new Color(0, 255, 0, mpma));
        mpp.setBackground(new Color(0, 255, 0, mppa));
        ppp.setBackground(new Color(0, 255, 0, pppa));
        pmm.setBackground(new Color(0, 255, 0, pmma));
        pmp.setBackground(new Color(0, 255, 0, pmpa));
        mmp.setBackground(new Color(0, 255, 0, mmpa));
        ppm.setBackground(new Color(0, 255, 0, ppma));
    }

    private void startAlphaReduction() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                reduceAlpha();
                System.out.println("alpha decreased");
                updateLabelColors();
                repaint();
            }
        }, 1000, 1000);
    }

    private void reduceAlpha() {
        if (mmma >= 10 ) mmma -= 10;
        if (mpma >= 10) mpma -= 10;
        if (mppa >= 10) mppa -= 10;
        if (pppa >= 10) pppa -= 10;
        if (pmma >= 10) pmma -= 10;
        if (pmpa >= 10) pmpa -= 10;
        if (mmpa >= 10) mmpa -= 10;
        if (ppma >= 10) ppma -= 10;

        updateLabelColors();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        textArea.append(evt.getNewValue().toString() + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());

        String[] values = evt.getNewValue().toString().split(",");
        if (values.length >= 29) {
            try {
                double pleasure = Double.parseDouble(values[26]);
                double arousal = Double.parseDouble(values[27]);
                double dominance = Double.parseDouble(values[28]);

                if (pleasure < 0 && arousal < 0 && dominance < 0) {
                    mmma = 255;
                    updateLabelColors();
                    repaint();
                } else if (pleasure < 0 && arousal > 0 && dominance < 0) {
                    mpma = 255;
                    updateLabelColors();
                    repaint();
                } else if (pleasure < 0 && arousal > 0 && dominance > 0) {
                    mppa = 255;
                    updateLabelColors();
                    repaint();
                } else if (pleasure > 0 && arousal > 0 && dominance > 0) {
                    pppa = 255;
                    updateLabelColors();
                    repaint();
                } else if (pleasure > 0 && arousal < 0 && dominance < 0) {
                    pmma = 255;
                    updateLabelColors();
                    repaint();
                } else if (pleasure > 0 && arousal < 0 && dominance > 0) {
                    pmpa = 255;
                    updateLabelColors();
                    repaint();
                } else if (pleasure < 0 && arousal < 0 && dominance > 0) {
                    mmpa = 255;
                    updateLabelColors();
                    repaint();
                } else if (pleasure > 0 && arousal > 0 && dominance < 0) {
                    ppma = 255;
                    updateLabelColors();
                    repaint();
                }

            } catch (NumberFormatException e) {
                System.err.println("Invalid numerical values received.");
            }
        }
    }
}
