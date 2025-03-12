package view;

import model.Blackboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReadingComprehensionGamePanel extends JPanel implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    private class ReadingItem {
        String passage;
        String question;
        String[] options;
        int correctOption;

        ReadingItem(String passage, String question, String[] options, int correctOption) {
            this.passage = passage;
            this.question = question;
            this.options = options;
            this.correctOption = correctOption;
        }
    }

    // CardLayout for switching between screens.
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Color selection components.
    private JPanel colorSelectionPanel;
    private JRadioButton rbWhiteOnBlack, rbBlackOnWhite, rbBlackOnWarm;
    private ButtonGroup colorGroup;
    private JButton startButton;

    // Combined reading and question panel components.
    private JPanel readingAndQuestionPanel;
    private JTextArea passageTextArea;
    private JButton finishedReadingButton;

    // Components for displaying question and answer choices.
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JButton submitButton;
    private JLabel resultLabel;
    private JButton backToHomeButton;

    // Game data: a list of reading items.
    private List<ReadingItem> readingItems;
    private ReadingItem currentItem;

    // Color scheme variables.
    private Color bgColor = Color.white;
    private Color fgColor = Color.black;

    public ReadingComprehensionGamePanel() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize reading items.
        initializeReadingItems();

        // Create the individual panels/screens.
        createColorSelectionPanel();
        createReadingAndQuestionPanel();

        // Add panels to the card panel.
        cardPanel.add(colorSelectionPanel, "COLOR_SELECTION");
        cardPanel.add(readingAndQuestionPanel, "READING_AND_QUESTION");

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
    }

    // Build the color selection screen.
    private void createColorSelectionPanel() {
        colorSelectionPanel = new JPanel();
        colorSelectionPanel.setLayout(new BoxLayout(colorSelectionPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Select Color Scheme", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        rbWhiteOnBlack = new JRadioButton("White on Black");
        rbWhiteOnBlack.setFont(new Font("Verdana", Font.PLAIN, 15));
        rbBlackOnWhite = new JRadioButton("Black on White");
        rbBlackOnWhite.setFont(new Font("Verdana", Font.PLAIN, 15));
        rbBlackOnWarm = new JRadioButton("Black on Warm");
        rbBlackOnWarm.setFont(new Font("Verdana", Font.PLAIN, 15));

        // Set default selection.
        rbWhiteOnBlack.setSelected(true);

        // Group radio buttons.
        colorGroup = new ButtonGroup();
        colorGroup.add(rbWhiteOnBlack);
        colorGroup.add(rbBlackOnWhite);
        colorGroup.add(rbBlackOnWarm);

        rbWhiteOnBlack.setAlignmentX(Component.CENTER_ALIGNMENT);
        rbBlackOnWhite.setAlignmentX(Component.CENTER_ALIGNMENT);
        rbBlackOnWarm.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button applies the chosen color scheme and switches to the reading screen.
        startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Object> values = new ArrayList<>();
                values.add(System.currentTimeMillis());
                values.add("chose color option");

                applyColorScheme();
                values.add(colorToString(bgColor));
                values.add(colorToString(fgColor));

                // Randomly select a reading item.
                Random random = new Random();
                int index = random.nextInt(readingItems.size());
                currentItem = readingItems.get(index);
                // Set the passage text.
                passageTextArea.setText(currentItem.passage);

                values.add(index);
                try {
                    Blackboard.getInstance().addValue(true, values.toArray());
                } catch (Exception ex) {
                    System.out.println("failed to send color option value to blackboard");
                }

                // Reset UI components.
                finishedReadingButton.setVisible(true);
                questionPanel.setVisible(false);
                resultLabel.setText("");
                submitButton.setEnabled(true);
                backToHomeButton.setVisible(false);
                // Switch to the reading and question panel.
                cardLayout.show(cardPanel, "READING_AND_QUESTION");
            }
        });

        // Add components with spacing.
        colorSelectionPanel.add(Box.createVerticalStrut(20));
        colorSelectionPanel.add(titleLabel);
        colorSelectionPanel.add(Box.createVerticalStrut(20));
        colorSelectionPanel.add(rbWhiteOnBlack);
        colorSelectionPanel.add(rbBlackOnWhite);
        colorSelectionPanel.add(rbBlackOnWarm);
        colorSelectionPanel.add(Box.createVerticalStrut(20));
        colorSelectionPanel.add(startButton);
    }

    // Build a combined panel that displays the passage and, upon finishing reading, shows the question and answer choices.
    private void createReadingAndQuestionPanel() {
        readingAndQuestionPanel = new JPanel();
        readingAndQuestionPanel.setLayout(new BoxLayout(readingAndQuestionPanel, BoxLayout.Y_AXIS));

        // Passage text area with scroll support.
        passageTextArea = new JTextArea(10, 40);
        passageTextArea.setLineWrap(true);
        passageTextArea.setWrapStyleWord(true);
        passageTextArea.setEditable(false);
        passageTextArea.setFont(new Font("Verdana", Font.PLAIN, 25));
        passageTextArea.setOpaque(true); // Ensure background color is visible.
        JScrollPane scrollPane = new JScrollPane(passageTextArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Finished Reading button.
        finishedReadingButton = new JButton("Finished Reading");
        finishedReadingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        finishedReadingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Set up the question panel with the current item's question and answer options.
                ArrayList<Object> values = new ArrayList<>();
                values.add(System.currentTimeMillis());
                values.add("finished reading text");
                values.add(colorToString(bgColor));
                values.add(colorToString(fgColor));

                try {
                    Blackboard.getInstance().addValue(true, values.toArray());
                } catch (Exception ex) {
                    System.out.println("failed to send finished reading text value to blackboard");
                }

                questionLabel.setText(currentItem.question);
                questionLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
                for (int i = 0; i < optionButtons.length; i++) {
                    if (i < currentItem.options.length) {
                        optionButtons[i].setText(currentItem.options[i]);
                        optionButtons[i].setFont(new Font("Verdana", Font.PLAIN, 18));
                        optionButtons[i].setVisible(true);
                    } else {
                        optionButtons[i].setVisible(false);
                    }
                }
                optionsGroup.clearSelection();
                // Hide the "Finished Reading" button and display the question panel below the passage.
                finishedReadingButton.setVisible(false);
                questionPanel.setVisible(true);
            }
        });

        // Create the question panel.
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionLabel = new JLabel("Question will appear here", SwingConstants.CENTER);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        questionPanel.add(questionLabel);
        questionPanel.add(Box.createVerticalStrut(10));

        // Create radio buttons for answer options (assume a maximum of 4 options).
        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton("Option " + (i + 1));
            optionButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            optionsGroup.add(optionButtons[i]);
            questionPanel.add(optionButtons[i]);
        }

        // Submit Answer button.
        submitButton = new JButton("Submit Answer");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ArrayList<Object> values = new ArrayList<>();
                values.add(System.currentTimeMillis());
                values.add("answer submitted");
                values.add(colorToString(bgColor));
                values.add(colorToString(fgColor));

                int selectedIndex = -1;
                for (int i = 0; i < optionButtons.length; i++) {
                    if (optionButtons[i].isSelected()) {
                        selectedIndex = i;
                        break;
                    }
                }
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(ReadingComprehensionGamePanel.this, "Please select an answer.");
                    return;
                }
                if (selectedIndex == currentItem.correctOption) {
                    resultLabel.setText("Correct!");
                    values.add("correct");
                } else {
                    resultLabel.setText("Incorrect. The correct answer is: "
                            + currentItem.options[currentItem.correctOption]);
                    values.add("incorrect");
                }
                resultLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
                // Disable further submissions and reveal the back button.
                submitButton.setEnabled(false);
                backToHomeButton.setVisible(true);
                try {
                    Blackboard.getInstance().addValue(true, values.toArray());
                } catch (Exception ex) {
                    System.out.println("failed to send answer submission value to blackboard");
                }
            }
        });

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Back to Home button.
        backToHomeButton = new JButton("Back to Home");
        backToHomeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToHomeButton.setVisible(false);
        backToHomeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Return to the color selection screen.
                cardLayout.show(cardPanel, "COLOR_SELECTION");
            }
        });

        // Arrange components in the question panel.
//        questionPanel.add(Box.createVerticalStrut(10));
//        questionPanel.add(questionLabel);
        questionPanel.add(Box.createVerticalStrut(10));
        questionPanel.add(submitButton);
        questionPanel.add(Box.createVerticalStrut(10));
        questionPanel.add(resultLabel);
        questionPanel.add(Box.createVerticalStrut(10));
        questionPanel.add(backToHomeButton);
        questionPanel.setVisible(false);

        // Add all components to the combined reading and question panel.
        readingAndQuestionPanel.add(Box.createVerticalStrut(10));
        readingAndQuestionPanel.add(scrollPane);
        readingAndQuestionPanel.add(Box.createVerticalStrut(10));
        readingAndQuestionPanel.add(finishedReadingButton);
        readingAndQuestionPanel.add(Box.createVerticalStrut(10));
        readingAndQuestionPanel.add(questionPanel);
    }

    // Apply the selected color scheme to both text and background for all components.
    private void applyColorScheme() {
        if (rbWhiteOnBlack.isSelected()) {
            bgColor = Color.black;
            fgColor = Color.white;
        } else if (rbBlackOnWhite.isSelected()) {
            bgColor = Color.white;
            fgColor = Color.black;
        } else if (rbBlackOnWarm.isSelected()) {
            bgColor = new Color(255, 218, 185); // Warm, peach-puff-like color.
            fgColor = Color.black;
        }
        setComponentColors(colorSelectionPanel);
        setComponentColors(readingAndQuestionPanel);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // Recursively set background and foreground colors.
    // Also, if the component is an AbstractButton (e.g., JButton, JRadioButton), we force its properties.
    private void setComponentColors(Component comp) {
        if (comp instanceof JButton) return;
        comp.setBackground(bgColor);
        comp.setForeground(fgColor);
        if (comp instanceof AbstractButton) {
            AbstractButton btn = (AbstractButton) comp;
            btn.setOpaque(true);
            btn.setBackground(bgColor);
            btn.setForeground(fgColor);
            btn.setContentAreaFilled(true);
        }
        if (comp instanceof JComponent) {
            ((JComponent) comp).setOpaque(true);
        }
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                setComponentColors(child);
            }
        }
    }
    private String colorToString(Color color) {
        // Check for black.
        if (color.equals(Color.black)) {
            return "black";
        }
        // Check for white.
        else if (color.equals(Color.white)) {
            return "white";
        }
        // Check for the warm color.
        else if (color.equals(new Color(255, 218, 185))) {
            return "warm";
        }
        // Fallback for any other colors.
        return "unknown";
    }
    private void initializeReadingItems() {
        readingItems = new ArrayList<>();
        // Sample list of 20 coding questions

        readingItems.add(new ReadingItem(
                "public class ConcatTest {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int a = 2;\n" +
                        "        int b = 3;\n" +
                        "        System.out.println(a + b + \" equals \" + a + b);\n" +
                        "    }\n" +
                        "}",
                "What is printed when this code is executed?",
                new String[] {
                        "5 equals 23",
                        "23 equals 23",
                        "5 equals 5",
                        "23 equals 5"
                },
                0
        ));

        readingItems.add(new ReadingItem(
                "public class LoopComplex {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int sum = 0;\n" +
                        "        for (int i = 1; i < 5; i++) {\n" +
                        "            for (int j = i; j < 5; j++) {\n" +
                        "                if (j % 2 == 0) continue;\n" +
                        "                sum += i * j;\n" +
                        "            }\n" +
                        "        }\n" +
                        "        System.out.println(sum);\n" +
                        "    }\n" +
                        "}",
                "What is printed when this code is executed?",
                new String[] {
                        "14",
                        "19",
                        "21",
                        "25"
                },
                1
        ));

        readingItems.add(new ReadingItem(
                "public class RecursionTest {\n" +
                        "    public static int mystery(int n) {\n" +
                        "        if (n <= 1) return n;\n" +
                        "        return mystery(n-1) + mystery(n-2);\n" +
                        "    }\n" +
                        "    public static void main(String[] args) {\n" +
                        "        System.out.println(mystery(5));\n" +
                        "    }\n" +
                        "}",
                "What is printed when this code is executed?",
                new String[] {
                        "3",
                        "8",
                        "5",
                        "13"
                },
                2
        ));

        readingItems.add(new ReadingItem(
                "public class PrecedenceTest {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int a = 2;\n" +
                        "        int b = 3;\n" +
                        "        System.out.println(a + b * 2 + \" \" + (a + b) * 2);\n" +
                        "    }\n" +
                        "}",
                "What is printed when this code is executed?",
                new String[] {
                        "10 8",
                        "8 8",
                        "10 10",
                        "8 10"
                },
                3
        ));

        readingItems.add(new ReadingItem(
                """
                        public class NestedTernary {
                            public static void main(String[] args) {
                                int x = 5;
                                int y = 10;
                                System.out.println(x > 3 ? (y < 15 ? 1 : 2) : (y > 15 ? 3 : 4));
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "1",
                        "2",
                        "3",
                        "4"
                },
                0
        ));

        readingItems.add(new ReadingItem(
                """
                        public class ArraySwap {
                            public static void main(String[] args) {
                                int[] arr = {1, 2, 3, 4};
                                for (int i = 0; i < arr.length / 2; i++) {
                                    int temp = arr[i];
                                    arr[i] = arr[arr.length - 1 - i];
                                    arr[arr.length - 1 - i] = temp;
                                }
                                for (int i : arr) {
                                    System.out.print(i + " ");
                                }
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "1 2 3 4 ",
                        "4 3 2 1 ",
                        "1 2 3 4",
                        "4 3 2 1"
                },
                1
        ));

        readingItems.add(new ReadingItem(
                """
                        public class ForEachTest {
                            public static void main(String[] args) {
                                int[] nums = {1, 2, 3, 4};
                                for (int n : nums) {
                                    n *= 2;
                                }
                                System.out.println(nums[2]);
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "6",
                        "4",
                        "3",
                        "2"
                },
                2
        ));

        readingItems.add(new ReadingItem(
                """
                        public class SwitchFallThrough {
                            public static void main(String[] args) {
                                int value = 2;
                                switch(value) {
                                    case 1: System.out.print("A"); break;
                                    case 2: System.out.print("B");
                                    case 3: System.out.print("C"); break;
                                    default: System.out.print("D");
                                }
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "B",
                        "ABC",
                        "BD",
                        "BC"
                },
                3
        ));

        readingItems.add(new ReadingItem(
                """
                        public class StringTest {
                            public static void main(String[] args) {
                                String s = "Hello";
                                s.concat(" World");
                                System.out.println(s);
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "Hello",
                        "Hello World",
                        "World",
                        "Error"
                },
                0
        ));

        readingItems.add(new ReadingItem(
                """
                        public class ComplexRecursion {
                            public static int mystery(int n) {
                                if(n <= 1) return 1;
                                return n * mystery(n - 2);
                            }
                            public static void main(String[] args) {
                                System.out.println(mystery(6));
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "24",
                        "48",
                        "36",
                        "120"
                },
                1
        ));

        readingItems.add(new ReadingItem(
                """
                        public class OverloadTest {
                            public static void print(int x) {
                                System.out.print("int ");
                            }
                            public static void print(double x) {
                                System.out.print("double ");
                            }
                            public static void main(String[] args) {
                                print(5);
                                print(5.0);
                                print(5.0f);
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "int int double ",
                        "double double double ",
                        "int double double ",
                        "int double int "
                },
                2
        ));

        readingItems.add(new ReadingItem(
                """
                        public class ExceptionTest {
                            public static void main(String[] args) {
                                try {
                                    System.out.print("A");
                                    int x = 5 / 0;
                                    System.out.print("B");
                                } catch (ArithmeticException e) {
                                    System.out.print("C");
                                } finally {
                                    System.out.print("D");
                                }
                                System.out.print("E");
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "ACBDE",
                        "ABCDE",
                        "AD",
                        "ACDE"
                },
                3
        ));

        readingItems.add(new ReadingItem(
                """
                        public class AutoboxTest {
                            public static void main(String[] args) {
                                Integer a = 100, b = 100;
                                Integer c = 200, d = 200;
                                System.out.println(a == b);
                                System.out.println(c == d);
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "true false",
                        "false false",
                        "true true",
                        "false true"
                },
                0
        ));

        readingItems.add(new ReadingItem(
                """
                        public class ModuloNegative {
                            public static void main(String[] args) {
                                System.out.println(-7 % 3);
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "0",
                        "-1",
                        "1",
                        "-7"
                },
                1
        ));

        readingItems.add(new ReadingItem(
                """
                        public class StringEquality {
                            public static void main(String[] args) {
                                String s1 = "hello";
                                String s2 = new String("hello");
                                System.out.println(s1 == s2);
                                System.out.println(s1.equals(s2));
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "true false",
                        "true true",
                        "false true",
                        "false false"
                },
                2
        ));

        readingItems.add(new ReadingItem(
                """
                        public class BitwiseTest {
                            public static void main(String[] args) {
                                int a = 5;
                                int b = 3;
                                System.out.println(a & b);
                                System.out.println(a | b);
                                System.out.println(a ^ b);
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "5 3 2",
                        "1 3 6",
                        "1 7 2",
                        "1 7 6"
                },
                3
        ));

        readingItems.add(new ReadingItem(
                """
                        public class IncrementTest {
                            public static void main(String[] args) {
                                int a = 5;
                                int b = a++ + a++;
                                System.out.println(b);
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "11",
                        "10",
                        "12",
                        "13"
                },
                0
        ));

        readingItems.add(new ReadingItem(
                """
                        public class ComplexLoop {
                            public static void main(String[] args) {
                                int count = 0;
                                for (int i = 0; i < 5; i++) {
                                    for (int j = 0; j < 5; j++) {
                                        if (i == j) continue;
                                        if (j == 3) break;
                                        count++;
                                    }
                                }
                                System.out.println(count);
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "15",
                        "13",
                        "10",
                        "12"
                },
                1
        ));

        readingItems.add(new ReadingItem(
                """
                        import java.util.Arrays;
                        public class LambdaTest {
                            public static void main(String[] args) {
                                String[] words = {"apple", "banana", "cherry"};
                                Arrays.sort(words, (a, b) -> b.length() - a.length());
                                System.out.println(Arrays.toString(words));
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "[apple, banana, cherry]",
                        "[banana, apple, cherry]",
                        "[banana, cherry, apple]",
                        "[cherry, apple, banana]"
                },
                2
        ));

        readingItems.add(new ReadingItem(
                """
                        public class OverloadConversion {
                            public static void method(int a) {
                                System.out.print("int");
                            }
                            public static void method(long a) {
                                System.out.print("long");
                            }
                            public static void main(String[] args) {
                                byte b = 10;
                                method(b);
                            }
                        }""",
                "What is printed when this code is executed?",
                new String[] {
                        "long",
                        "byte",
                        "Error",
                        "int"
                },
                3
        ));
    }
}
