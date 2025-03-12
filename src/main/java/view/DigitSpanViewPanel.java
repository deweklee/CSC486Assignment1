package view;

import model.Blackboard;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DigitSpanViewPanel extends JPanel {
    private JLabel instructionLabel;
    private JLabel numberLabel;
    private JTextField inputField;
    private JButton startButton, submitButton;
    private List<Integer> numbers;
    private int level;
    private Timer timer;

    public DigitSpanViewPanel() {
        setLayout(new GridLayout(4, 1));

        instructionLabel = new JLabel("Click Start to begin.", SwingConstants.CENTER);
        numberLabel = new JLabel("", SwingConstants.CENTER);
        inputField = new JTextField();
        startButton = new JButton("Start");
        submitButton = new JButton("Submit");
        submitButton.setEnabled(false);

        add(instructionLabel);
        add(numberLabel);
        add(inputField);
        add(startButton);
        add(submitButton);

        level = 1;
        numbers = new ArrayList<>();

        startButton.addActionListener(e -> startGame());

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkAnswer();
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void startGame() {
        numbers.clear();
        Random random = new Random();

        for (int i = 0; i < level; i++) {
            numbers.add(random.nextInt(10));
        }

        numberLabel.setText(numbers.toString().replaceAll("[\\[\\],]", ""));
        instructionLabel.setText("Level " + level +  ", Memorize the numbers!");
        inputField.setText("");
        inputField.setEnabled(false);
        submitButton.setEnabled(false);

        timer = new Timer((level + 1) * 500, e -> {
            numberLabel.setText("?");
            inputField.setEnabled(true);
            submitButton.setEnabled(true);
            instructionLabel.setText("Level " + level +  ", Enter the numbers:");
        });

        timer.setRepeats(false);
        timer.start();
    }

    private void checkAnswer() throws JSONException, IOException {
        String input = inputField.getText().trim();
        List<Integer> userNumbers = new ArrayList<>();

        ArrayList<Object> values = new ArrayList<>();
        values.add(System.currentTimeMillis());
        values.add(level);

        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                userNumbers.add(Character.getNumericValue(c));
            }
        }

        if (numbers.equals(userNumbers)) {
            instructionLabel.setText("Correct! Level up!");
            values.add(1);
            level++;
        } else {
            instructionLabel.setText("Incorrect. Game Over! You reached level " + level);
            values.add(0);
            level = 1;
        }

        Blackboard.getInstance().addValue(true, values.toArray());
        numberLabel.setText("");
        inputField.setText("");
        inputField.setEnabled(false);
        submitButton.setEnabled(false);
    }
}
