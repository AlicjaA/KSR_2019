package knn.distanceMeasures;

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
        ArrayList<Double> n = new ArrayList<>();
        n.add(Double.valueOf(firstWordGrams.size()));
        n.add(Double.valueOf(secondWordGrams.size()));
        ArrayList<Double> occ = new ArrayList<>();
        occ.add(occurance);
        occ.add(occurance);


        Double result=0.0;
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
        return result;
    }
}
