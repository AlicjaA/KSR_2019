package knn;

import dataImport.Article;

import java.util.ArrayList;
import java.util.TreeMap;

public class KNN {
    ArrayList<Article> trainingSet;
    ArrayList<Article> testingSet;
    private class Label{
        private String label;
        private ArrayList<Article.Features> vectors;


        public Label(String label) {
            this.label = label;
            vectors=new ArrayList<>();
        }

        public ArrayList<Article.Features> getVectors() {
            return vectors;
        }

        public void setVectors(ArrayList<Article.Features> vectors) {
            this.vectors = vectors;
        }

        public String getLabel() {
            return label;
        }
    }
    ArrayList<Label> lablesStructList;

    public KNN(ArrayList<Article> trainingSet, ArrayList<Article> testingSet, ArrayList<String> keyLabels){
        this.trainingSet=trainingSet;
        this.testingSet= testingSet;
        lablesStructList = new ArrayList<>();
        for(String label: keyLabels){
            lablesStructList.add(new Label(label));
        }
    }

    private void generateCoolStart(){
        for(Label l: lablesStructList){
            for(Article article:trainingSet){
                if(article.getLabel().equals(l.getLabel())){
                    l.setVectors(article.getFeatures());
                }
            }
        }
    }

    switch (distanceMeasure){
        case'm':{
            result=distance.manhattanDistanceMeasure(n,occ);
            break;
        }
        case'c':{
            result=distance.chebyshevDistanceMeasure(n,occ);
            break;
        }
        case'5':{
            result=distance.minkowski5DistanceMeasure(n,occ);
            break;
        }
        case'p':{
            result=distance.powerDistanceMeasure(n,occ);
            break;
        }
        default:{
            result= distance.euclideanDistanceMeasure(n,occ);
            break;
        }
    }
}
