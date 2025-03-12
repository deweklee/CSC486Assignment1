package model;

import org.json.JSONObject;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Objects;

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

    public void addValue(Boolean paused, Object value) {
        try {
            JSONObject obj = new JSONObject(value.toString());
            String type = obj.getString("Type");
            if (Objects.equals(type, "PAD")) {
                addPADData(obj);
            } else if (Objects.equals(type, "AFFECT")) {
                addAffectData(obj);
            }
            samples.add(obj);
        } catch(Exception e) {
            // if json fails, value could be coming from GUI instead
            System.out.println("blackboard failed add json");
            try {
                addValueArray(value);
            } catch (Exception ex) {
                System.out.println("blackboard failed to add value array");
            }
        }
    }

    private void addValueArray(Object value) {
        try {
            Object[] valueArray = (Object[]) value;
            StringBuilder csvString = new StringBuilder();
            for (Object o : valueArray) {
                csvString.append(o).append(",");
            }

            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(csvString + System.lineSeparator());
            fileWriter.close();
        } catch (Exception ex) {
            System.out.println("Error processing quiz input");
        }
    }

    private void addPADData(JSONObject obj) {
        try {
            String Type = obj.getString("Type");
            double p = obj.getDouble("P");
            double a = obj.getDouble("A");
            double d = obj.getDouble("D");
            BigDecimal time = BigDecimal.valueOf(obj.getDouble("time"));

            String csvString =
                    Type + "," +
                            time + "," +
                            (Double.isNaN(p) ? "NaN" : p) + "," +
                            (Double.isNaN(a) ? "NaN" : a) + "," +
                            (Double.isNaN(d) ? "NaN" : d) + ",";

            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(csvString + System.lineSeparator());
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("failed to read PAD data");
        }
    }

    private void addAffectData(JSONObject obj) {
        try {
            String Type = obj.getString("Type");
            String activeInterest = obj.getString("Active Interest");
            String interest = obj.getString("Interest");
            String activeRelaxation = obj.getString("Active Relaxation");
            String relaxation = obj.getString("Relaxation");
            String activeAttentionOrFocus = obj.getString("Active Attention or Focus");
            String attentionOrFocus = obj.getString("Attention or Focus");
            String activeEngagement = obj.getString("Active Engagement");
            String engagement = obj.getString("Engagement");
            String activeExcitement = obj.getString("Active Excitement");
            String excitement = obj.getString("Excitement");
            String activeStress = obj.getString("Active Stress");
            String stress = obj.getString("Stress");
            BigDecimal time = BigDecimal.valueOf(obj.getDouble("time"));

            String csvString =
                    Type + "," +
                            time + "," +
                            activeInterest + "," + interest + "," +
                            activeRelaxation + "," + relaxation + "," +
                            activeAttentionOrFocus + "," + attentionOrFocus + "," +
                            activeEngagement + "," + engagement + "," +
                            activeExcitement + "," + excitement + "," +
                            activeStress + "," + stress + ",";

            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(csvString + System.lineSeparator());
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("failed to read Affect data");
        }
    }

    public void setOutputFile(String filename) {
        this.outputFile = filename;
    }

    public void statusUpdate(String key, String update) {
        firePropertyChange(key, null, update);
    }
}
