package extraction.importanceMeasurment;

import knn.similarityMeasures.NGramMeasure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

/**
 * @author Alicja Anszpergier
 * Użyte metody ekstrakcji cech do reprezentacji przestrzenno-wektorowej:
 *1. ilościowa (quantitative): liczba wystąpień w dokumencie
 *2. TF (term frequency)-liczba wystąpień słowa do liczby słów w dokumencie
 *3. probabilistyczna - częstotliwość występowania termu do sumy częstotliwości występowania wszystkich termów.
 *4. własna
 */

public class LocalImportanceMeasures {

    /**
     * Metoda realizuje tworzenie ilościowej reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param articlesDict: lista słów z artykułu po deduplikacji, stemizacji i stopliście
     * @return quantitativeMap:TreeMap<String, Integer>
     */
    public TreeMap<String, Integer> quantitativeImportance(ArrayList<String> articleWords,  ArrayList<String> articlesDict){
        TreeMap<String, Integer> quantitativeMap = new TreeMap<String, Integer>();
        for (String word: articlesDict){
            int count=0;
            for (String aword: articleWords){
                if (aword.equals(word)){
                    count++;
                }
            }
            quantitativeMap.put(word,count);
        }
        return quantitativeMap;
    }

    /**
     * Metoda realizuje tworzenie reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu w oparciu o częstotliwość występowania słowa w artykule
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param articlesDict: lista słów z artykułu po deduplikacji, stemizacji i stopliście
     * @return termFrequencyMap:TreeMap<String, Integer>
     */
    public TreeMap<String, Integer> termFrequency (ArrayList<String> articleWords,  ArrayList<String> articlesDict){
        TreeMap<String, Integer> termFrequencyMap = new TreeMap<String, Integer>();
        TreeMap<String, Integer> quantitativeMap  =  new TreeMap<String, Integer>();
        quantitativeMap=quantitativeImportance(articleWords,articlesDict);
        for (String word: articlesDict ){
            termFrequencyMap.put(word,(quantitativeMap.get(word).intValue()/articleWords.size()) );
        }
        return termFrequencyMap;
    }
.
    /**
     * Metoda realizuje tworzenie reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu w oparciu
     * o częstotliwość występowania słowa na tle częstotliwości występowania pozostałych słów.
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param articlesDict: lista słów z artykułu po deduplikacji, stemizacji i stopliście
     * @return probabilistic:TreeMap<String, Integer>
     */
    public TreeMap<String, Integer> probabilisticImportance (ArrayList<String> articleWords,  ArrayList<String> articlesDict){
        TreeMap<String, Integer> termFrequencyMap = new TreeMap<String, Integer>();
        TreeMap<String, Integer> probabilisticMap  =  new TreeMap<String, Integer>();
        termFrequencyMap=termFrequency(articleWords,articlesDict);
        for (String word: articlesDict ){
            int termFrequencySum = 0;
            for(String w:articlesDict){
                if(!w.equals(word)){
                    termFrequencySum+=termFrequencyMap.get(word).intValue();
                }

            }
            probabilisticMap.put(word,(termFrequencyMap.get(word).intValue()/termFrequencySum) );
        }
        return probabilisticMap;
    }

    /**
     * Metoda realizuje tworzenie reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu w oparciu
     * o częstotliwość występowania słowa na tle częstotliwości występowania pozostałych słów.
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param articlesDict: lista słów z artykułu po deduplikacji, stemizacji i stopliście
     * @return probabilistic:TreeMap<String, Integer>
     */
    public TreeMap<String, Integer> probabilisticSimilarityImportance (ArrayList<String> articleWords,  ArrayList<String> articlesDict){
        TreeMap<String, Integer> termFrequencyMap = new TreeMap<String, Integer>();
        TreeMap<String, Integer> probabilisticMap  =  new TreeMap<String, Integer>();
        termFrequencyMap=termFrequency(articleWords,articlesDict);
        for (String word: articlesDict ){
            ArrayList<String> similarWords = getSimilarWords(word, articleWords);
            int termFrequencySumOther = 0;
            int termFrequencySum = 0;
            for(String sword:similarWords){
                termFrequencySum +=termFrequencyMap.get(sword).intValue();
            }
            for(String w:articlesDict){
                if(!similarWords.contains(w)){
                    termFrequencySumOther+=termFrequencyMap.get(word).intValue();
                }
            }
            probabilisticMap.put(word,(termFrequencySum/termFrequencySumOther) );
        }
        return probabilisticMap;
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
        int stDeviation = standardDeviation((ArrayList<Double>)similarityMap.values());
        for(String w:articleWords){
            similarityMap.put(w, nGram.termSimilarity(word, w));
        }
    }

    private Double standardDeviation(ArrayList<Double> values){
        double sum=0.0;
        for(Double value:values){
            sum+=value;
        }
        double sd = 0.0;
        for (int i = 0; i < values.size(); i++)
        {
            sd += Math.pow(values.get(i) - (sum/values.size()),2) / values.size();
        }
        return Math. sqrt(sd);
    }
}
