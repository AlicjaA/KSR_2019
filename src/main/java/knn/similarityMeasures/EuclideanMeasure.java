package knn.similarityMeasures;

import java.util.TreeMap;

public class EuclideanMeasure {

    public double getEuclideanDistance( double[] features1,  double[] features2) {
        double sum = 0;
        for (int i = 0; i < features1.length; i++)
        {  //System.out.println(features1[i]+" "+features2[i]);
            //applied Euclidean distance formula
            sum += Math.pow(features1[i] - features2[i], 2);
        }return Math.sqrt(sum);
    }
}
