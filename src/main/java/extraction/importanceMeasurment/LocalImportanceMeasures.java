package extraction.importanceMeasurment;

import java.util.*;


public class LocalImportanceMeasures {


    public Integer quantitativeImportance(ArrayList<String> articleWords,  String word){
        int count=0;
        for (String aword: articleWords){
            if (aword.equals(word)){
                count++;
            }
        }
        return count;
    }


    public double termFrequency (ArrayList<String> articleWords,  String word){
        double quantitative=quantitativeImportance(articleWords,word);
        double termFrequency = quantitative/articleWords.size();
        return termFrequency;
    }


    public double probabilisticImportance (ArrayList<String> articleWords,  ArrayList<String> terms, String word){
        TreeMap<String, Double> termFrequencyMap = new TreeMap<String, Double>();
        double wordTF=termFrequency(articleWords,word);
        double probabilistic =0;
        for(int i=0;i<terms.size();++i){
            termFrequencyMap.put(terms.get(i), termFrequency(articleWords,terms.get(i)));
        }
        double termFrequencySum = 0;
        for(int j=0;j<terms.size();++j){
            if(!(terms.get(j).equalsIgnoreCase(word))){
                termFrequencySum+=termFrequencyMap.get(terms.get(j));
            }

        }
        if(termFrequencySum>0) {
            probabilistic = wordTF / termFrequencySum;
        }
        return probabilistic;
    }


    public Double probabilisticSimilarityImportance (ArrayList<String> articleWords,  ArrayList<String> terms, String word){
        TreeMap<String, Double> termFrequencyMap = new TreeMap<String, Double>();
        double probabilistic  = 0.0;
        termFrequencyMap.put(word,termFrequency(articleWords, word));
        for(String aword:terms) {
            termFrequencyMap.put(aword, termFrequency(articleWords, aword));
        }
        ArrayList<String> similarWords = getSimilarWords(word, articleWords);
        for(String sword:articleWords){
                termFrequencyMap.put(sword, termFrequency(articleWords, sword));
        }
        double termFrequencySumOther = 0.0;
        double termFrequencySum = 0.0;
        for(String sword:similarWords){
            termFrequencySum +=termFrequencyMap.get(sword);
        }
        for(String w:articleWords){
            if(!similarWords.contains(w)){
                termFrequencySumOther+=termFrequencyMap.get(w);
            }
        }
        if(termFrequencySumOther>0) {
            probabilistic = termFrequencySum / termFrequencySumOther;
        }
        return probabilistic;
    }

    private ArrayList<String> getSimilarWords(String word, ArrayList<String> articleWords){
        NGramMeasure nGram = new NGramMeasure();
        ArrayList<String> similarWords = new ArrayList<>();
        TreeMap<String,Double> similarityMap = new TreeMap<>();
        for(String w:articleWords){
            if(!w.equals(word)) {
                similarityMap.put(w, nGram.termSimilarity(word, w));
            }
        }
        ArrayList<Double> values = new ArrayList<>();
        for(double val: similarityMap.values()){
            values.add(val);
        }
        double stDeviation = standardDeviation(values);
        values.sort(Comparator.reverseOrder());
        double borderValue = values.get(0)-stDeviation;
        for(String w:similarityMap.keySet()){
            if(similarityMap.get(w)>=borderValue && similarityMap.get(w)>0){similarWords.add(w);}
        }
        return similarWords;
    }

    public Double average(ArrayList<Double> values){
        double sum=0.0;
        for(Double value:values){
            sum+=value;
        }
        return sum/values.size();
    }

    public Double standardDeviation(ArrayList<Double> values){

        double sd = 0.0;
        for (int i = 0; i < values.size(); i++)
        {
            sd += Math.pow(values.get(i) - (average(values)),2) / values.size();
        }
        return Math.sqrt(sd);
    }


    public double binaryImportance(ArrayList<String> articleWords,  String word){
        double binary = 0;
        if(articleWords.contains(word)){
            binary=1;
        }
        return binary;
    }
}
