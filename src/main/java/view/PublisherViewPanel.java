package view;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PublisherViewPanel extends JPanel implements PropertyChangeListener {

    private JTextArea textArea;

    public PublisherViewPanel() {
        setLayout(new GridLayout(1, 1));

        textArea = new JTextArea();
        textArea.setEditable(false); // Optional: Prevents user editing
        textArea.setLineWrap(true);  // Wraps text to avoid horizontal scrolling
        textArea.setWrapStyleWord(true); // Wraps at word boundaries

        // Wrap JTextArea in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling

        add(scrollPane); // Add scrollPane instead of textArea
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        textArea.append(evt.getNewValue().toString() + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength()); // Auto-scroll to bottom
    }
}
