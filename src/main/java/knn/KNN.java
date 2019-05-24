package knn;

import dataModel.*;
import extraction.Features.QLFeatures;
import knn.distanceMeasures.NGramDistanceMeasure;
import knn.distanceMeasures.PowerDistanceMeasuses;

import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.*;


public class KNN {
    ArrayList<Label> lablesStructList;
    char distanceMeasure;

    private class ValueComparator<K,V extends Comparable> implements Comparator<K>
    {
        private Map<K,V> map;

        public ValueComparator(Map<K,V> map) {
            this.map = new HashMap<>(map);
        }

        @Override
        public int compare(K s1, K s2) {
            return map.get(s1).compareTo(map.get(s2));
        }
    }

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

    public ArrayList<Result> classification(ArrayList<Article> testingSet, int k){
        setDistanceMeasure(testingSet);
        for(Article article: testingSet){
            Map<Integer,Double> tmp = article.getTrainingSetDistances();

            Comparator<Integer> comparator = new ValueComparator(tmp);

            TreeMap<Integer, Double> distances = Maps.newTreeMap(comparator);
            distances.putAll(tmp);

            NavigableSet<Integer> distanceKeys= ((TreeMap<Integer, Double>) distances).navigableKeySet();
            ArrayList<Integer> neighboursIds = new ArrayList<>();

            for(int j=0;j<k;++j){
                neighboursIds.add(distanceKeys.pollFirst());
            }

            ArrayList<Integer> labelsOccurance = new ArrayList<>();


                for(Label label:lablesStructList){
                    int counter=0;
                    for(FeaturesList featuresList: label.getFeaturesLists()){
                        if(neighboursIds.contains(featuresList.getId())){
                            ++counter;
                        }
                    }
                    labelsOccurance.add(counter);
                }


            Integer maxOccurance = Collections.max(labelsOccurance);

            for(int g=0; g<lablesStructList.size();++g){
                if(labelsOccurance.get(g).equals(maxOccurance)){
                    article.setKnnLabel(lablesStructList.get(g).getLabel());
                }
            }
        }

        return calculateError(testingSet);

    }

    private ArrayList<Result> calculateError(ArrayList<Article> testingSet){
        ArrayList<Result> results = new ArrayList<>();
        for(Label label:lablesStructList){
            results.add(new Result(label.getLabel()));
        }

        for (Article article: testingSet){
            Result r = results.stream()
                    .filter(result -> article.getKnnLabel().equals(result.getLabel()))
                    .findAny()
                    .orElse(null);
            if(article.getLabel()==article.getKnnLabel()){
                r.addCorect();
            }
            else{
                r.addIncorrect();
            }
        }

        for(Result result: results){
            result.setError();
        }
        return results;
    }

    private void setDistanceMeasure(ArrayList<Article> testingSet)
    {
        PowerDistanceMeasuses distance =new PowerDistanceMeasuses();
        for(Article article: testingSet){
            TreeMap<Integer,Double> articleToTrainingSetDistances = new TreeMap<>();
            ArrayList<Double> articleDoubleFeatures = getDoubleFeatures(article.getFeaturesList());
            Double finalResult=0.0;

            for(Label label: lablesStructList){
                for(FeaturesList featuresList: label.getFeaturesLists()){
                    ArrayList<Double> labelDoubleFeatures= getDoubleFeatures(featuresList);
                    double doubleResult=0.0;
                    switch (distanceMeasure){
                        case'm':{
                            doubleResult=distance.manhattanDistanceMeasure(articleDoubleFeatures,labelDoubleFeatures);
                            break;
                        }
                        case'c':{
                            doubleResult=distance.chebyshevDistanceMeasure(articleDoubleFeatures,labelDoubleFeatures);
                            break;
                        }
                        case'5':{
                            doubleResult=distance.minkowski5DistanceMeasure(articleDoubleFeatures,labelDoubleFeatures);
                            break;
                        }
                        case'p':{
                            doubleResult=distance.powerDistanceMeasure(articleDoubleFeatures,labelDoubleFeatures);
                            break;
                        }
                        default:{
                            doubleResult= distance.euclideanDistanceMeasure(articleDoubleFeatures,labelDoubleFeatures);
                            break;
                        }
                    }
                    if(article.getFeatureString()=="empty"){
                        finalResult=doubleResult;
                    }
                    else {
                        NGramDistanceMeasure nGramDist = new NGramDistanceMeasure();
                        Double stringResult= nGramDist.nGramDistanceMeasure(featuresList.getFeature(QLFeatures.STRING.name()).getsValue(), article.getFeatureString(),
                                distanceMeasure);
                        finalResult= calculateWeightedAverageResult(doubleResult,stringResult,articleDoubleFeatures.size());
                    }
                    articleToTrainingSetDistances.put(featuresList.getId(),finalResult);
                }
            }

            article.setTrainingSetDistances(articleToTrainingSetDistances);

        }
    }

    private ArrayList<Double> getDoubleFeatures(FeaturesList featuresList){
        ArrayList<Double> doubleFeatures = new ArrayList<>();
        for(Feature feature : featuresList.getFeatures()){

            if(!(feature.getValue()==null) || (feature.getValue()==0.0)){
                doubleFeatures.add(feature.getValue());
            }
        }
        return doubleFeatures;
    }

    private Double calculateWeightedAverageResult(Double doubleResult, Double stringResult, Integer doubleFeatureAmount){
        return (doubleResult*doubleFeatureAmount+stringResult)/(doubleFeatureAmount+1);
    }
}
