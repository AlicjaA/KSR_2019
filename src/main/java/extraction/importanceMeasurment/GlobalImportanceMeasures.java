package extraction.importanceMeasurment;

import dataModel.Article;

import java.util.ArrayList;


public class GlobalImportanceMeasures {




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

    public Double TFIDFMethod(ArrayList<Article> articles, String word){
        LocalImportanceMeasures l = new LocalImportanceMeasures();
        Double[] tf =new Double[articles.size()];
        for(int i=0;i<articles.size();++i){
            tf[i]=l.termFrequency(articles.get(i).getWords(),word);
        }
        Double idf = inverseDocumentFrequency(articles,word);
        Double tfidfSum=0.0;
        for(Double d:tf){
            tfidfSum+=d;
        }
        
        return tfidfSum*idf;
    }


}
