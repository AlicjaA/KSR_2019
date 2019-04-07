package extraction;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @autor: Alicja Anszpergier
 * 4. DF (document frequency)- liczba dokumentów, w których wystąpiło słowo
 * 5. IDF (inverse dokument frequency) - log(|D|/DF(ti)) stosunek liczby dokumentów do liczby wystąpień słowa we wszystkich dokumentach
 * 6. TF*IDF - TF(ti, d) • IDF(ti) - ważność słowa tym większa im częściej występuje w dokumenci i im żadziej występuje w innych dokumentach
 * 7. BM25 - uzależnia wynik dla słowa od relatywnej długości artykułu na tle innych artykułów ze zbioru: IDF * ((k + 1) * tf) / (k * (1.0 - b + b * (|d|/avgDl)) + tf)
 */

public class globalImportanceMeasures {

    /**
     * Metoda realizuje tworzenie ilościowej reprezentacji przestrzenno-wektorowej dla pojedyńczego artykułu
     *
     * @param binaryImportanceForAllArticle: Lista wektorów licalImportanceMeasures.binaryImportance dla wszystkich artyków
     * @param allArticlesDict: lista słów ze wszystkich artykułów po deduplikacji, stemizacji i stopliście
     * @return quantitativeMap:TreeMap<String, Integer>
     */

    public TreeMap<String, Integer> documentFrequency (ArrayList<TreeMap<String, Integer>> binaryImportanceForAllArticle, ArrayList<String> allArticlesDict ){
        TreeMap<String,Integer> docFrequency = new TreeMap<String, Integer>();
        for (String word:allArticlesDict){
            int counter=0;
            for (TreeMap<String, Integer> binaryImportance: binaryImportanceForAllArticle){
                if(binaryImportance.containsKey(word)){
                    ++counter;
                }
            }
            docFrequency.put(word,counter);
        }
        return  docFrequency;
    }

    
}
