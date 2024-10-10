package Nashtech;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PredictiveModel {
    private static final Logger LOGGER = Logger.getLogger(PredictiveModel.class.getName());
    private Classifier classifier;

    public PredictiveModel() {
        this.classifier = new J48(); // Using a decision tree classifier
    }

    public void trainModel(String dataPath) throws Exception {
        Instances data = loadData(dataPath);
        if (data == null || data.numInstances() == 0) {
            throw new IllegalArgumentException("Failed to load data from path: " + dataPath);
        }
        data.setClassIndex(data.numAttributes() - 1); // Set the class index to the last attribute
        classifier.buildClassifier(data);
    }

    public double predict(Instances instance) throws Exception {
        if (instance == null || instance.numInstances() == 0) {
            throw new IllegalArgumentException("Instance data is invalid or empty");
        }
        instance.setClassIndex(instance.numAttributes() - 1); // Set the class index to the last attribute
        return classifier.classifyInstance(instance.instance(0));
    }

    public static Instances loadData(String dataPath) throws Exception {
        InputStream inputStream = PredictiveModel.class.getClassLoader().getResourceAsStream(dataPath);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + dataPath);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Instances data = null;
        try {
            data = new Instances(reader);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load data from path: " + dataPath, e);
            throw e;
        } finally {
            reader.close();
        }
        if (data.numInstances() == 0) {
            LOGGER.log(Level.WARNING, "No data found in file: " + dataPath);
        }
        return data;
    }
}
