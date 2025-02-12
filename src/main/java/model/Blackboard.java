package model;


import org.json.JSONObject;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

public class Blackboard extends PropertyChangeSupport {

    private String outputFile = "./output/output-" + System.currentTimeMillis() + ".csv";
    private static Blackboard instance;
    private LinkedList<String> samples;

    private Blackboard() {
        super(new Object());
        samples = new LinkedList<>();
    }

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public void addValue(Boolean paused, Object value)  {
        samples.add(value.toString());
        if(!paused){
            firePropertyChange("subPanel", null, value);
        }
        try {
            JSONObject obj = new JSONObject(value.toString());

            int systemTime = obj.getInt("system_time");
            int deviceTime = obj.getInt("device_time");

            // Use optDouble with NaN as default value
            double leftX = obj.getJSONObject("left_eye").optDouble("x", Double.NaN);
            double leftY = obj.getJSONObject("left_eye").optDouble("y", Double.NaN);
            double rightX = obj.getJSONObject("right_eye").optDouble("x", Double.NaN);
            double rightY = obj.getJSONObject("right_eye").optDouble("y", Double.NaN);

            // Convert NaN values to a string "NaN" for CSV output
            String csvString =
                    systemTime + "," +
                            deviceTime + "," +
                            (Double.isNaN(leftX) ? "NaN" : leftX) + "," +
                            (Double.isNaN(leftY) ? "NaN" : leftY) + "," +
                            (Double.isNaN(rightX) ? "NaN" : rightX) + "," +
                            (Double.isNaN(rightY) ? "NaN" : rightY);

            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(file, true);

            // Write CSV string followed by a newline
            fileWriter.write(csvString + System.lineSeparator());

            // Close file writer
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Error processing JSON: " + e.getLocalizedMessage());
            e.printStackTrace();  // Print full stack trace for debugging
        }
    }

    public void statusUpdate(String key, String update) {
        firePropertyChange(key, null, update);
    }
}


