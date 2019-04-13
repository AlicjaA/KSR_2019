package extraction;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author Alicja Anszpergier
 * Użyte metody ekstrakcji cech do reprezentacji przestrzenno-wektorowej:
 *1. binarna (boolowska): jest, nie ma
 *2. ilościowa (quantitative): liczba wystąpień w dokumencie
 *3. TF (term frequency)-liczba wystąpień słowa do liczby słów w dokumencie
 * 4. odległość w słowach od początku tekstu - im bliżej tym bardziej znacząca
 */

public class LocalImportanceMeasures {

    /**
     * Metoda realizuje tworzenie binarnej reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param allArticlesDict: lista słów ze wszystkich artykułów po deduplikacji, stemizacji i stopliście
     * @return binaryMap: TreeMap<String, Integer>
     */
    public TreeMap<String, Integer> binaryImportance(ArrayList<String> articleWords,  ArrayList<String> allArticlesDict){
        TreeMap<String, Integer> binaryMap = new TreeMap<String, Integer>();
        for (String word: allArticlesDict){
            if(articleWords.equals(word)){
                binaryMap.put(word,1);
            }
            else{
                binaryMap.put(word,0);
            }
        }
        return binaryMap;
    }

    /**
     * Metoda realizuje tworzenie ilościowej reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu
     *
     * @param articleWords: reprezentacja artykułu w postaci stringu po stemizacji i stopliście
     * @param allArticlesDict: lista słów ze wszystkich artykułów po deduplikacji, stemizacji i stopliście
     * @return quantitativeMap:TreeMap<String, Integer>
     */
    public TreeMap<String, Integer> quantitativeImportance(ArrayList<String> articleWords,  ArrayList<String> allArticlesDict){
        TreeMap<String, Integer> quantitativeMap = new TreeMap<String, Integer>();
        for (String word: allArticlesDict){
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
     * @param allArticlesDict: lista słów ze wszystkich artykułów po deduplikacji, stemizacji i stopliście
     * @return termFrequencyMap:TreeMap<String, Integer>
     */
    public TreeMap<String, Integer> termFrequency (ArrayList<String> articleWords,  ArrayList<String> allArticlesDict){
        TreeMap<String, Integer> termFrequencyMap = new TreeMap<String, Integer>();
        TreeMap<String, Integer> quantitativeMap  =  new TreeMap<String, Integer>();
        quantitativeMap=quantitativeImportance(articleWords,allArticlesDict);
        for (String word: allArticlesDict ){
            termFrequencyMap.put(word,(quantitativeMap.get(word).intValue()/articleWords.size()) );
        }
        return termFrequencyMap;
    }
}
