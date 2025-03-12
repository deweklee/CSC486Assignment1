package model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import org.json.CDL;

public class Blackboard extends PropertyChangeSupport {

    private String outputFile;
    private static Blackboard instance;
    private LinkedList<JSONObject> samples;

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

    public void addValue(Boolean paused, Object value) throws JSONException, IOException {
        System.out.println(value.toString());
        try {
            JSONObject obj = new JSONObject(value.toString());
            if(!paused){
                firePropertyChange("subPanel", samples.getLast(), obj);
            }
            samples.add(obj);
        } catch(Exception e) {
            System.out.println("blackboard failed add json value");
            try {
                addValueArray(value);
            } catch (Exception ex) {
                System.out.println("blackboard failed to add value array");
            }
        }

//        addEyeTrackerJSON(value);
//        addValueArray(value);
//        addEEGData(value);
//        addFaceData(value);
    }

    private void addValueArray(Object value) {
        try {
            Object[] valueArray = (Object[]) value;
            String csvString = "";
            for(int i = 0; i< valueArray.length; i++) {
                csvString += valueArray[i] + ",";
            }
            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(file, true);

            // Write CSV string followed by a newline
            fileWriter.write(csvString + System.lineSeparator());

            // Close file writer
            fileWriter.close();
        } catch (Exception ex) {
            System.out.println("Error processing game input");
        }
    }
    private void addEEGData(Object value) throws JSONException, IOException {
        try {
            JSONObject obj = new JSONObject(value.toString());

            String Type = obj.getString("Type");
            double counter = obj.getDouble("Counter");
            double t7 = obj.getDouble("T7");
            double t8 = obj.getDouble("T8");
            double time = obj.getDouble("Time");
            BigDecimal d = new BigDecimal(time);
            double Pz = obj.getDouble("Pz");
            double contactQuality = obj.getDouble("Contact Quality");
            boolean interpolated = obj.getBoolean("Interpolated");
            double af3 = obj.getDouble("AF3");
            double af4 = obj.getDouble("AF4");

            String csvString =
                    Type + "," +
                            (Double.isNaN(counter) ? "NaN" : counter) + "," +
                            (Double.isNaN(t7) ? "NaN" : t7) + "," +
                            (Double.isNaN(t8) ? "NaN" : t8) + "," +
                            d + "," +
                            (Double.isNaN(Pz) ? "NaN" : Pz) + "," +
                            (Double.isNaN(contactQuality) ? "NaN" : contactQuality) + "," +
                            interpolated + "," +
                            (Double.isNaN(af3) ? "NaN" : af3) + "," +
                            (Double.isNaN(af4) ? "NaN" : af4) + ",";

                            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(file, true);

            // Write CSV string followed by a newline
            fileWriter.write(csvString + System.lineSeparator());

            // Close file writer
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("failed");
        }
    }
    private void addFaceData(Object value) throws JSONException, IOException {
        try {
            JSONObject obj = new JSONObject(value.toString());

            String Type = obj.getString("Type");
            double powerUpperFace = obj.getDouble("powerUpperFace");
            String actionEye = obj.getString("actionEye");
            String actionUpperFace = obj.getString("actionUpperFace");
            double time = obj.getDouble("time");
            BigDecimal d = new BigDecimal(time);
            double powerLowerFace = obj.getDouble("powerLowerFace");
            String actionLowerFace = obj.getString("actionLowerFace");


            String csvString =
                    Type + "," +
                            (Double.isNaN(powerUpperFace) ? "NaN" : powerUpperFace) + "," +
                            actionEye + "," +
                            actionUpperFace + "," +
                            d + "," +
                            (Double.isNaN(powerLowerFace) ? "NaN" : powerLowerFace) + "," +
                            actionLowerFace + ",";

            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(file, true);

            // Write CSV string followed by a newline
            fileWriter.write(csvString + System.lineSeparator());

            // Close file writer
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("failed");
        }
    }
    private void addEyeTrackerJSON(Object value) {
        try {
            JSONObject obj = new JSONObject(value.toString());

            long systemTime = obj.getLong("system_time");
            long deviceTime = obj.getLong("device_time");

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
            System.out.println("Error processing eye tracking JSON");
        }
    }
    public void setOutputFile(String filename) {
        this.outputFile = filename;
    }
    public void statusUpdate(String key, String update) {
        firePropertyChange(key, null, update);
    }
}


