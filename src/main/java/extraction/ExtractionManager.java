package extraction;

import dataModel.Article;
import dataModel.Feature;
import extraction.documentProcessing.ExtractionOperations;



import java.io.IOException;
import java.util.*;

public class ExtractionManager {
    ExtractionOperations exOperate;

    public ExtractionManager()
    {
        exOperate = new ExtractionOperations();
    }

    public void tokenization (ArrayList<Article> articles) throws IOException {
        for (Article article : articles){
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(article.getFeatureString());
            ArrayList<String> wordList = new ArrayList<>();
            wordList.addAll(exOperate.makeDataList(article.getText()));
            wordList= exOperate.deleteStopwords(wordList);
            wordList= exOperate.toLowerCase(wordList);
            tmp= exOperate.toLowerCase(tmp);
            wordList= exOperate.stemmer(wordList);
            tmp=exOperate.stemmer(tmp);
            wordList= exOperate.cleanWordList(wordList);
            tmp= exOperate.cleanWordList(tmp);
            wordList= exOperate.deleteStopwords(wordList);
            article.setWords(wordList);
            wordList= exOperate.deduplicate(wordList);
            article.setTerms(wordList);
            if(tmp.size()>0){article.setFeatureString(tmp.get(0));}else{article.setFeatureString("empty");}
        }
    }

    public ArrayList<String> selectKeyWords(ArrayList<Article> articles, char selectingMethod){
        ArrayList<String> candidateKeys = new ArrayList<>();

        for(Article article:articles){
            candidateKeys.addAll(article.getTerms());
        }

        return exOperate.generateKeys(articles,candidateKeys,selectingMethod);
    }

    public void setFeatures(ArrayList<Article> articles, ArrayList<Integer> chosenFeatures, ArrayList<String> keys){
        for(Article article:articles){
            ArrayList<Feature> features = exOperate.calculateFeatures(article,chosenFeatures,keys);
            article.setFeatures(features);
        }
    }

    public ArrayList<Double> normalize(ArrayList<Double> params){
        return exOperate.doublesNormalization(params);
    }

}
