package extraction.importanceMeasurment;

import knn.similarityMeasures.NGramMeasure;

import java.util.*;

/**
 * @author Alicja Anszpergier
 * Użyte metody ekstrakcji cech do reprezentacji przestrzenno-wektorowej:
 *1. ilościowa (quantitative): liczba wystąpień w dokumencie
 *2. TF (term frequency)-liczba wystąpień słowa do liczby słów w dokumencie
 *3. probabilistyczna - częstotliwość występowania termu do sumy częstotliwości występowania wszystkich termów.
 *4. własna
 * 5. binarna - jest,  nie ma
 */

public class LocalImportanceMeasures {

    /**
     * Metoda realizuje tworzenie ilościowej reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param word: słowo kluczowe
     * @return count: double
     */
    public Integer quantitativeImportance(ArrayList<String> articleWords,  String word){
        int count=0;
        for (String aword: articleWords){
            if (aword.equals(word)){
                count++;
            }
        }
        return count;
    }

    /**
     * Metoda realizuje tworzenie reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu w oparciu o częstotliwość występowania słowa w artykule
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param word: słowo kluczowe
     * @return count: double
     */
    public double termFrequency (ArrayList<String> articleWords,  String word){
        double quantitative=quantitativeImportance(articleWords,word);
        double termFrequency = quantitative/articleWords.size();
        return termFrequency;
    }

    /**
     * Metoda realizuje tworzenie reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu w oparciu
     * o częstotliwość występowania słowa na tle częstotliwości występowania pozostałych słów.
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param articleDict: słowa kluczowe
     * @param word: słowo kluczowe, dla którego liczymy
     * @return probabilistic: double
     */
    public double probabilisticImportance (ArrayList<String> articleWords,  ArrayList<String> articleDict, String word){
        TreeMap<String, Double> termFrequencyMap = new TreeMap<String, Double>();
        double probabilistic =0;
        for(int i=0;i<articleDict.size();++i){
            termFrequencyMap.put(articleDict.get(i), termFrequency(articleWords,articleDict.get(i)));
        }
        double termFrequencySum = 0;
        for(int j=0;j<articleDict.size();++j){
            if(!(articleDict.get(j).equalsIgnoreCase(word))){
                termFrequencySum+=termFrequencyMap.get(articleDict.get(j));
            }

        }
        if(termFrequencySum>0) {
            probabilistic = termFrequencyMap.get(word) / termFrequencySum;
        }
        return probabilistic;
    }

    /**
     * Metoda realizuje tworzenie reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu w oparciu
     * o częstotliwość występowania słowa i słów podobnych na tle częstotliwości występowania pozostałych słów.
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param keys: lista słów z artykułu po deduplikacji, stemizacji i stopliście
     * @return probabilisticSimilarityImportance:TreeMap<String, double>
     */
    public Double probabilisticSimilarityImportance (ArrayList<String> articleWords,  ArrayList<String> keys, String word){
        TreeMap<String, Double> termFrequencyMap = new TreeMap<String, Double>();
        double probabilistic  = 0.0;
        for(String aword:keys) {
            termFrequencyMap.put(aword, termFrequency(articleWords, aword));
        }
        ArrayList<String> similarWords = getSimilarWords(word, articleWords);
        for(String sword:articleWords){
            if(!keys.contains(sword)){
                termFrequencyMap.put(sword, termFrequency(articleWords, sword));
            }
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

    public Double standardDeviation(ArrayList<Double> values){
        double sum=0.0;
        for(Double value:values){
            sum+=value;
        }
        double sd = 0.0;
        for (int i = 0; i < values.size(); i++)
        {
            sd += Math.pow(values.get(i) - (sum/values.size()),2) / values.size();
        }
        return Math.sqrt(sd);
    }

    /**
     * Metoda realizuje tworzenie binarnej reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param word: słowo ze słów kluczowych
     * @return binary: double
     */
    public double binaryImportance(ArrayList<String> articleWords,  String word){
        double binary = 0;
        if(articleWords.contains(word)){
            binary=1;
        }
        return binary;
    }
}
