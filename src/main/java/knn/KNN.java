package knn;

import dataModel.*;
import knn.distanceMeasures.PowerDistanceMeasuses;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class KNN {
    ArrayList<Label> lablesStructList;
    char distanceMeasure;

    public KNN(ArrayList<Article> trainingSet, ArrayList<String> keyLabels, char distanceMeasure){
        this.distanceMeasure = distanceMeasure;
        lablesStructList = new ArrayList<>();
        for(String label: keyLabels){
            lablesStructList.add(new Label(label));
        }
        addToCoolStart(trainingSet);

    }

    public void addToCoolStart(ArrayList<Article> trainingSet){
        for(Label label: lablesStructList){
            for(Article article:trainingSet){
                if(article.getLabel().equals(label.getLabel())) {
                    label.addFeaturesList(article.getFeaturesList());
                }
            }
        }
    }

    public void classification(ArrayList<Article> testingSet, int k){
        setDistanceMeasure(testingSet);
        for(Article article: testingSet){
            Map<Integer,Double> tmp = article.getTrainingSetDistances();

            TreeMap<Integer,Double> distances = new TreeMap<>();
                    tmp.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEachOrdered(x -> distances.put(x.getKey(), x.getValue()));

            NavigableSet<Integer> distanceKeys= distances.navigableKeySet();
            ArrayList<Integer> neighboursIds = new ArrayList<>();

            for(int j=0;j<k;++j){
                neighboursIds.add(distanceKeys.pollLast());
            }

            ArrayList<Integer> labelsOccurance = new ArrayList<>();

            for(int i=0;i<k;++i){
                for(Label label:lablesStructList){
                    int counter=0;
                    for(FeaturesList featuresList: label.getFeaturesLists()){
                        if(neighboursIds.contains(featuresList.getId())){
                            ++counter;
                        }
                    }
                    labelsOccurance.add(counter);
                }
            }

            Integer maxOccurance = Collections.max(labelsOccurance);

            for(int g=0; g<lablesStructList.size();++g){
                if(labelsOccurance.get(g).equals(maxOccurance)){
                    article.setKnnLabel(lablesStructList.get(g).getLabel());
                }
            }
        }

    }

    //TODO policzyć error i zwrócić wynik
    /*private ArrayList<Result> calculateError(ArrayList<Article> testingSet){

    }*/

    private void setDistanceMeasure(ArrayList<Article> testingSet)
    {
        PowerDistanceMeasuses distance =new PowerDistanceMeasuses();
        for(Article article: testingSet){
            TreeMap<Integer,Double> articleToTrainingSetDistances = new TreeMap<>();
            ArrayList<Double> articleFeatures = getDoubleFeatures(article.getFeaturesList());

            for(Label label: lablesStructList){
                for(FeaturesList featuresList: label.getFeaturesLists()){
                    ArrayList<Double> labelFeatures = getDoubleFeatures(featuresList);
                    double result=0.0;
                    switch (distanceMeasure){
                        case'm':{
                            result=distance.manhattanDistanceMeasure(articleFeatures,labelFeatures);
                            break;
                        }
                        case'c':{
                            result=distance.chebyshevDistanceMeasure(articleFeatures,labelFeatures);
                            break;
                        }
                        case'5':{
                            result=distance.minkowski5DistanceMeasure(articleFeatures,labelFeatures);
                            break;
                        }
                        case'p':{
                            result=distance.powerDistanceMeasure(articleFeatures,labelFeatures);
                            break;
                        }
                        default:{
                            result= distance.euclideanDistanceMeasure(articleFeatures,labelFeatures);
                            break;
                        }
                    }
                    articleToTrainingSetDistances.put(featuresList.getId(),result);
                }
            }



        }
    }

    private ArrayList<Double> getDoubleFeatures(FeaturesList featuresList){
        ArrayList<Double> doubleFeatures = new ArrayList<>();
        for(Feature feature : featuresList.getFeatures()){

            if(!feature.getValue().equals(null)){
                doubleFeatures.add(feature.getValue());
            }
        }
        return doubleFeatures;
    }


}
