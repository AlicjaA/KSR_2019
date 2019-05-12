package knn.similarityMeasures;


import dataImport.Article;

import java.util.ArrayList;
import java.util.TreeMap;

public class powerDistanceMeasuses {

    private Double powerDistanceMeasuse(ArrayList<Double> first, ArrayList<Double> second,
                                                        Double p, Double r){
        Double result=0.0;
        Double newResult=0.0;
        for(int i=0;i<first.size();++i){
            newResult=Math.abs(first.get(i)-second.get(i));
            if(p==0){
                result=(result<newResult)? newResult:result;
            }
            else{
                result+=Math.pow(newResult,p);
            }
        }

        Double power = 1/r;
        return Math.pow(result,power);
    }

    public Double manhattanDistanceMeasure(ArrayList<Double> first, ArrayList<Double> second){
        return powerDistanceMeasuse(first,second,1.0,1.0);
    }
    public Double euclideanDistanceMeasure(ArrayList<Double> first, ArrayList<Double> second){
        return powerDistanceMeasuse(first,second,2.0,2.0);
    }

    public Double chebyshevDistanceMeasure(ArrayList<Double> first, ArrayList<Double> second){
        return powerDistanceMeasuse(first,second,0.0,1.0);
    }

    public Double minkowski5DistanceMeasure(ArrayList<Double> first, ArrayList<Double> second){
        return powerDistanceMeasuse(first,second,5.0,5.0);
    }

    public Double powerDistanceMeasure(ArrayList<Double> first, ArrayList<Double> second){
        return powerDistanceMeasuse(first,second,2.0,1.0);
    }

}
