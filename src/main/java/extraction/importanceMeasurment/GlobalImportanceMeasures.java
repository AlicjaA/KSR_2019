package extraction.importanceMeasurment;

import dataModel.Article;

import java.util.ArrayList;

/**
 * @autor: Alicja Anszpergier
 * 5. IDF (inverse dokument frequency) - log(|D|/DF(ti)) stosunek liczby dokumentów do liczby dokumentów, w których wystąpiło słowo
 * 6. TF*IDF - TF(ti, d) • IDF(ti) - ważność słowa tym większa im częściej występuje w dokumencie i im żadziej występuje w innych dokumentach
 * 7. BM25 - uzależnia wynik dla słowa od relatywnej długości artykułu na tle innych artykułów ze zbioru: IDF * ((k + 1) * tf) / (k * (1.0 - b + b * (|d|/avgDl)) + tf)
 */

public class GlobalImportanceMeasures {


    /**
     *
     *
     * @param articles: lista artykułów
     * @param word: słowo, dla którego liczymy DF
     * @return quantitativeMap:TreeMap<String, Integer>
     */

    public Double documentFrequency (ArrayList<Article> articles, String word ){
       Double counter = 0.0;
        for (Article article:articles){
            if(article.getWords().contains(word)){
                ++counter;
            }
        }
        return  counter;
    }

    public Double inverseDocumentFrequency (ArrayList<Article> articles, String word){
        double df = documentFrequency(articles,word);
        return Math.log(articles.size()/df);
    }

    public Double averageTFIDFMethod (ArrayList<Article> articles, String word){
        LocalImportanceMeasures l = new LocalImportanceMeasures();
        Double[] tf =new Double[articles.size()];
        for(int i=0;i<articles.size();++i){
            tf[i]=l.termFrequency(articles.get(i).getWords(),word);
        }
        Double idf = inverseDocumentFrequency(articles,word);
        Double tfidfSum=0.0;
        for(Double d:tf){
            tfidfSum+=(d/idf);
        }
        return tfidfSum/tf.length;
    }


}
