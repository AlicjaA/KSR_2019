package dataModel;

import java.util.ArrayList;

public class Label {
    private String label;
    private ArrayList<FeaturesList> featuresLists;


    public Label(String label) {
        this.label = label;
        featuresLists =new ArrayList<>();
    }

    public ArrayList<FeaturesList> getFeaturesLists() {
        return featuresLists;
    }

    public FeaturesList getFeaturesList( int i) {
        return featuresLists.get(i);
    }

    public void addFeaturesList(FeaturesList featuresList) {
        this.featuresLists.add(featuresList);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {

        return "Label{" +
                "label='" + label + '\'' +
                ", featuresLists=" + featuresLists.size() +
                '}';
    }
}

