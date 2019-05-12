package extraction;

import dataImport.Article;
import extraction.documentProcessing.ExtractionOperations;
import extraction.importanceMeasurment.GlobalImportanceMeasures;
import extraction.importanceMeasurment.LocalImportanceMeasures;

import java.io.IOException;
import java.util.*;

public class ExtractionManager {
    ExtractionOperations exOperate;
    GlobalImportanceMeasures glImpMeass;
    LocalImportanceMeasures localImp;

    public ExtractionManager(){ exOperate = new ExtractionOperations();
        this.glImpMeass = new GlobalImportanceMeasures();
        this.localImp = new LocalImportanceMeasures();}

    public void tokenization (ArrayList<Article> articles) throws IOException {
        for (Article article : articles){
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(article.getFeature());
            ArrayList<String> wordList = new ArrayList<>();
            //wordList= exOperate.makeDataList(article.getDateline());
            //wordList.addAll(exOperate.makeDataList(article.getTitle()));
            //wordList.addAll(exOperate.makeDataList(article.getAuthor()));
            /*wordList.addAll(exOperate.makeDataList(article.getBody()));
            wordList.addAll(exOperate.makeDataList(article.getUnknown()));
            wordList.addAll(exOperate.makeDataList(article.getDate()));
            wordList.addAll(article.getPlaces());
            wordList.addAll(article.getPeople());
            wordList.addAll(article.getTopics());
            wordList.addAll(article.getCompanies());
            wordList.addAll(article.getExchanges());
            wordList.addAll(article.getOrgs());*/
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
            if(tmp.size()>0){article.setFeature(tmp.get(0));}else{article.setFeature(" ");}
        }
    }

    /*public TreeMap<String, ArrayList<Article>> generateLabeledArticles(ArrayList<Article> articles, ArrayList<String> labels, boolean equal){
        for(String label: labels){
            ArrayList<Article> tmp = new ArrayList<>();
            for (Article article: articles){
                if(equal){
                    if(article.)
                }
            }
        }
    }*/

    public ArrayList<String> selectKeyWords(ArrayList<Article> articles, char selectingMethod){
        ArrayList<String> candidateKeys = new ArrayList<>();
        ArrayList<String> allArticlesWords = new ArrayList<>();
        ArrayList<String> finalKays = new ArrayList<>();
        TreeMap<String, Double> keys = new TreeMap<String, Double>();
        for(Article article:articles){
            candidateKeys.addAll(article.getTerms());
            allArticlesWords.addAll(article.getWords());
        }
        candidateKeys = exOperate.deduplicate(candidateKeys);
        ArrayList<Double> measureList = new ArrayList<>();
        for(String word: candidateKeys){
            Double measure=0.0;
            switch (selectingMethod){
                case 'i':{
                    measure= glImpMeass.inverseDocumentFrequency(articles,word);
                    measureList.add(measure);
                    keys.put(word,measure);
                    break;
                }
                case 't':{
                    measure= glImpMeass.inverseDocumentFrequency(articles,word);
                    measureList.add(measure);
                    keys.put(word,measure);
                    break;
                }
                default:{
                    break;
                }
            }
        }
        Collections.sort(measureList);
        /*
        LocalImportanceMeasures localImp = new LocalImportanceMeasures();
        for(Article article: articles){

            ArrayList<Double> measureList = new ArrayList<>();
            Double measure=0.0;
            switch (selectingMethod){
                case 'q':
                    for(String word: article.getTerms()){
                        measure = Double.valueOf(localImp.quantitativeImportance(article.getWords(),word));
                        measureList.add(measure);
                        keys.put(word,measure);
                    }
                    Collections.sort(measureList);
                    break;
                case 't':
                    for(String word: article.getTerms()){
                        measure = localImp.termFrequency(article.getWords(),word);
                        measureList.add(measure);
                        keys.put(word,measure);
                    }
                    Collections.sort(measureList);
                    break;
                case 'p':
                    for(String word: article.getTerms()){
                        measure = localImp.probabilisticImportance(article.getWords(), article.getTerms(), word);
                        measureList.add(measure);
                        keys.put(word,measure);
                    }
                    Collections.sort(measureList,Collections.reverseOrder());
                    break;
            }*/
        if(selectingMethod !='c') {
            double standardDeviation = localImp.standardDeviation(measureList)*2;
            double borderValue = measureList.get(0) + standardDeviation;
            double minValue = measureList.get(0);
            /*if(selectingMethod=='p'){ borderValue = measureList.get(0)-(standardDeviation*2.0); }
            else {borderValue = measureList.get(0)+(standardDeviation/4.0);}*/

            ArrayList<String> iterator = new ArrayList<>();
            for (String key : keys.navigableKeySet()) {
                boolean check = false;
                /*if(selectingMethod=='p'){ check=(keys.get(key)<borderValue); }
                else {check=(keys.get(key)>borderValue);}*/
                check = (keys.get(key) > borderValue && keys.get(key) > minValue);
                if (check) {
                    iterator.add(key);
                }
            }
            for (String key : iterator) {
                keys.remove(key);
            }

            for(String key:keys.navigableKeySet()){
                finalKays.add(key);
            }
        }
            //article.setKeys(finalKays);
        return finalKays;
    }

    public void setFeatures(ArrayList<Article> articles, ArrayList<Integer> chosenFeatures, ArrayList<String> keys){
        for(Article article:articles){
            ArrayList<Article.Features> features = new ArrayList<>();
            if(chosenFeatures.contains(1)){
                double sum=0.0;
                for(String key: keys){
                    sum += localImp.quantitativeImportance(article.getWords(), key);
                }
                Article.Features feature = new Article.Features("AVGQI");
                feature.setValue(sum/keys.size());
                features.add(feature)
                ;}
            if(chosenFeatures.contains(2)){
                double sum=0.0;
                for(String key: keys){
                    sum += localImp.binaryImportance(article.getWords(), key);
                }
                Article.Features feature = new Article.Features("AVGSBI");
                feature.setValue(sum/keys.size());
                features.add(feature)
                ;}
            if(chosenFeatures.contains(3)){
                double sum=0.0;
                for(String key: keys){
                    sum += localImp.probabilisticImportance(article.getWords(),keys, key);
                }
                Article.Features feature = new Article.Features("SPI");
                feature.setValue(sum);
                features.add(feature)
                ;}
            if(chosenFeatures.contains(4)){
                double sum=0.0;
                for(String key: keys){
                    sum += localImp.probabilisticSimilarityImportance(article.getWords(),keys, key);
                }
                Article.Features feature = new Article.Features("SPSI");
                feature.setValue(sum);
                features.add(feature)
                ;}
            if(chosenFeatures.contains(5)){
                double sum=0.0;
                for(String key: keys){
                    sum += localImp.termFrequency(article.getWords(), key);
                }
                Article.Features feature = new Article.Features("STF");
                feature.setValue(sum);
                features.add(feature);}
            if(chosenFeatures.contains(6)){
                Article.Features feature = new Article.Features("STRING");
                feature.setsValue(article.getFeature());
                features.add(feature);
            }

            normalization( features);
            article.setFeatures(features);
        }
    }

    private void normalization(ArrayList<Article.Features> values){
        Double min= null;
        Double max=0.0;
        for(Article.Features v: values){
            if(v.getValue() != null) {
                max = (v.getValue() > max) ? v.getValue() : max;
                min = (min == null) ? v.getValue() : (v.getValue() < min) ? v.getValue() : min;
            }
        }

        for(Article.Features v: values){
            if(v.getValue() != null) {
                double normalizedValue = 0.0;
                normalizedValue = (v.getValue() - min) / (max - min);
                v.setValue(normalizedValue);
            }
        }

    }
}
