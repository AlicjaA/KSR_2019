package knn.similarityMeasures;

import java.util.ArrayList;

public class ManhattanMetric {

    public double manhattan (ArrayList<Double> fvalue, ArrayList<Double> svalue){
        double sum = 0;
        for (int i = 0; i < fvalue.size(); i++)
            sum += Math.abs(fvalue.get(i) - svalue.get(i));
        return sum;
    }
}
