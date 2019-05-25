package dataModel;

import dataModel.Feature;

import java.io.Serializable;
import java.util.ArrayList;

public class FeaturesList implements Serializable {

    Integer id;
    private ArrayList<Feature> features;

    public FeaturesList( ArrayList<Feature> features) {
        this.id = hashCode();
        this.features = features;
    }

    public FeaturesList() {
        this.id=hashCode();
        features=new ArrayList<>();
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Feature> features) {
        this.features = features;
    }

    public void addFeature (Feature feature) {
        this.features.add(feature);
    }

    public Feature getFeature(int i)
    {
        return features.get(i);
    }

    public Feature getFeature(String name)
    {
        for(Feature f:features){
            if(f.getName().equals(name)){
                return f;
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "FeaturesList{" +
                "id='" + id +'\'' +
                "\n features=" + features +
                '}';
    }
}
