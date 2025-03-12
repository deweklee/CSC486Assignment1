package view;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CircleViewPanel extends JPanel implements PropertyChangeListener {
    private int x;
    private int y;
    private int diameter = 50;
    private Color circleColor = Color.GREEN;

    public CircleViewPanel() {
        this.setSize(new Dimension(800,600));
        x = (this.getWidth() / 2) - 25;
        y = (this.getHeight() / 2) - 25;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(circleColor);
        g.fillOval(x, y, diameter, diameter);
    }

    private Boolean checkRightHandRaised(JSONObject oldValue, JSONObject newValue) throws JSONException {
        return newValue.getJSONObject("rightArmUp").getDouble("y") >
                oldValue.getJSONObject("rightArmUp").getDouble("y");
    }

    private Boolean checkLeftHandRaised(JSONObject oldValue, JSONObject newValue) throws JSONException {
        return newValue.getJSONObject("leftArmUp").getDouble("y") >
                oldValue.getJSONObject("leftArmUp").getDouble("y");
    }

    private Boolean checkHeadLowered(JSONObject oldValue, JSONObject newValue) throws JSONException {
        return newValue.getJSONObject("head").getDouble("y") <
                oldValue.getJSONObject("head").getDouble("y");
    }

    private Boolean checkHeadRaised(JSONObject oldValue, JSONObject newValue) throws JSONException {
        return newValue.getJSONObject("head").getDouble("y") >
                oldValue.getJSONObject("head").getDouble("y");
    }

    private Boolean checkEyesRight(JSONObject oldValue, JSONObject newValue) throws JSONException {
        return newValue.getJSONObject("eyeFixationPoint").getDouble("x") > 0;
    }

    private Boolean checkEyesLeft(JSONObject oldValue, JSONObject newValue) throws JSONException {
        return newValue.getJSONObject("eyeFixationPoint").getDouble("x") < 0;
    }

    private Boolean checkEyesUp(JSONObject oldValue, JSONObject newValue) throws JSONException {
        return newValue.getJSONObject("eyeFixationPoint").getDouble("y") > 0;
    }

    private Boolean checkEyesDown(JSONObject oldValue, JSONObject newValue) throws JSONException {
        return newValue.getJSONObject("eyeFixationPoint").getDouble("y") < 0;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        try {
            System.out.println( evt.getNewValue());
            JSONObject newValue = (JSONObject) evt.getNewValue();
            JSONObject prevValue = (JSONObject) evt.getOldValue();
//            System.out.println(newValue);
//            System.out.println(prevValue);
            if (checkRightHandRaised(prevValue, newValue)) {
                System.out.println("right hand raised");
                circleColor = Color.BLUE;
            }
            if (checkLeftHandRaised(prevValue, newValue)) {
                System.out.println("left hand raised");
                circleColor = Color.RED;
            }
            if (checkHeadLowered(prevValue, newValue)) {
                System.out.println("head lowered");
                if (diameter > 10) {
                    diameter -= 1;
                }
            }
            if (checkHeadRaised(prevValue, newValue)) {
                System.out.println("head raised");
                if (diameter < 300) {
                    diameter += 1;
                }
            }
            if (checkEyesRight(prevValue, newValue)) {
                System.out.println("eyes right");
                if (x + diameter + 100 < this.getWidth()) {
                    x += 100;
                    repaint();
                }
            }
            if (checkEyesLeft(prevValue, newValue)) {
                System.out.println("eyes left");
                if (x - 100 > 0) {
                    x -= 100;
                    repaint();
                }
            }
            if (checkEyesUp(prevValue, newValue)) {
                System.out.println("eyes up");
                if (y - 100 > 0) {
                    y -= 100;
                    repaint();
                }
            }
            if (checkEyesDown(prevValue, newValue)) {
                System.out.println("eyes down");
                if (y + diameter + 100 < this.getHeight()) {
                    y += 100;
                    repaint();
                }
            }
        } catch (Exception e) {
            System.out.println("CircleViewPanel failed");
            System.out.println(e);
        }
        repaint();
    }
}
