package knn.distanceMeasures;

import extraction.documentProcessing.ExtractionOperations;
import extraction.importanceMeasurment.NGramMeasure;

import java.util.ArrayList;

public class NGramDistanceMeasure {

    public Double nGramDistanceMeasure (String firstWord, String secondWord, char distanceMeasure){
        NGramMeasure nmeasure = new NGramMeasure();
        PowerDistanceMeasuses distance = new PowerDistanceMeasuses();
        Double occurance = 0.0;
        ArrayList<String> firstWordGrams = new ArrayList<>();
        ArrayList<String> secondWordGrams = new ArrayList<>();
        int max = 0;
        int min = Math.min(firstWord.length(),secondWord.length());
        for(int i=2;i<=min;++i){
            firstWordGrams.addAll(nmeasure.generateGrams(firstWord,i,min));
            secondWordGrams.addAll(nmeasure.generateGrams(secondWord,i,min));
        }
        for(String fgram:firstWordGrams){
            for(String sgrams: secondWordGrams){
                if(fgram.equals(sgrams)){
                    occurance +=1;
                }
            }
        }
        ArrayList<Double> distanceParams = new ArrayList<>();
        distanceParams.add(Double.valueOf(firstWordGrams.size()));
        distanceParams.add(Double.valueOf(secondWordGrams.size()));
        distanceParams.add(occurance);
        distanceParams.add(occurance);

        ExtractionOperations extractionOperations = new ExtractionOperations();
        distanceParams=extractionOperations.doublesNormalization(distanceParams);
        ArrayList<Double> grams = new ArrayList<Double>();
        grams.addAll(distanceParams.subList(0,1));
        ArrayList<Double> occ = new ArrayList<>();
        occ.addAll(distanceParams.subList(2,3));

        Double result=0.0;
        switch (distanceMeasure){
            case'm':{
                result=distance.manhattanDistanceMeasure(grams,occ);
                break;
            }
            case'c':{
                result=distance.chebyshevDistanceMeasure(grams,occ);
                break;
            }
            case'5':{
                result=distance.minkowski5DistanceMeasure(grams,occ);
                break;
            }
            case'p':{
                result=distance.powerDistanceMeasure(grams,occ);
                break;
            }
            default:{
                result= distance.euclideanDistanceMeasure(grams,occ);
                break;
            }
        }
        return result;
    }
}
