package extraction.documentProcessing;

import dataModel.Article;
import dataModel.Feature;
import extraction.importanceMeasurment.LocalImportanceMeasures;
import extraction.importanceMeasurment.GlobalImportanceMeasures;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.TreeMap;

public class ExtractionOperations {

    public ArrayList<String> makeDataList(String data){
        ArrayList<String> list = new ArrayList<>();
        for (String s:data.split(" ")){
            if(!s.isEmpty() && !(s==null)) {
                list.add(s);
            }
        }
        return list;
    }

    public String stemmer (String data){
        PorterStemmer st = new PorterStemmer();
        return st.stem(data);
    }

    public ArrayList<String> stemmer (ArrayList<String> wordList){
        PorterStemmer st = new PorterStemmer();
        for (int i=0; i<wordList.size();++i){
            wordList.set(i,st.stem(wordList.get(i)));
        }
        return wordList;
    }

    public ArrayList<String> deduplicate (ArrayList<String> wordList){
        for (int i=0; i<wordList.size();++i){
            for (int j=wordList.size()-1;j>=0;--j){
                if(wordList.get(i).equals(wordList.get(j)) && (i != j)){
                    wordList.remove(j);
                }

            }
        }
        return wordList;
    }

    public ArrayList<String> cleanWordList (ArrayList<String> wordList){
        for(int i=0;i<wordList.size();++i){
            String word = wordList.get(i);
            word = word.replaceAll("(&#[1-9]\\d*;)", "");
            word = word.replaceAll("\\p{IsPunctuation}", "");
            word = word.replaceAll("\\s+"," ");
            word = word.replaceAll("(<.>)", "");
            word = word.replaceAll("(<.><.>)", "");
            word = word.replaceAll("<", "");
            word = word.replaceAll(">", "");
            word = word.replaceAll("('s)", "");
            word = word.replaceAll("('d)", "");
            word = word.replaceAll("('re)", "");
            word = word.replaceAll("('m)", "");
            word = word.replaceAll("('ve)", "");
            word = word.replaceAll("\\d", "");
            if(word.length()==1){word = word.replaceAll(".{1}", "");}
            if(word.contains("f\u001Freut")){word = word.replaceAll(".", "");}
            word=word.replaceAll("(\\p{Z})","");
            word=word.replaceAll("(\\p{So})","");
            word=word.replaceAll(" ","");
            word=word.replaceAll("\\p{C}","");
            wordList.set(i, word);
        }
        wordList.removeAll(Collections.singleton(""));
        return wordList;
    }

    public ArrayList<String> toLowerCase (ArrayList<String> wordList){
        for(int i=0;i<wordList.size();++i){
            String word = wordList.get(i);
            word = word.toLowerCase(Locale.getDefault());
            wordList.set(i, word);
        }
        return wordList;
    }

    public String toLowerCase (String data){
        data=data.toLowerCase();
        return data;
    }

    public ArrayList<String> deleteStopwords (ArrayList<String> wordList) throws IOException {
        ArrayList<String> stopWords = new ArrayList<>();
        stopWords=(ArrayList<String>) Files.readAllLines (
                Paths.get(ClassLoader.getSystemClassLoader().getResource("stopwords").toString().substring(6).trim()));

        wordList.removeAll(stopWords);
        return wordList;
    }

    private void normalization(ArrayList<Feature> values){
        Double min= null;
        Double max=0.0;
        for(Feature v: values){
            if(v.getValue() != null) {
                max = (v.getValue() > max) ? v.getValue() : max;
                min = (min == null) ? v.getValue() : (v.getValue() < min) ? v.getValue() : min;
            }
        }

        for(Feature v: values){
            if(v.getValue() != null) {
                double normalizedValue = 0.0;
                normalizedValue = (v.getValue() - min) / (max - min);
                v.setValue(normalizedValue);
            }
        }

    }

    public ArrayList<Feature> calculateFeatures(Article article, ArrayList<Integer> chosenFeatures, ArrayList<String> keys) {
        LocalImportanceMeasures localImp = new LocalImportanceMeasures();
        ArrayList<Feature> features = new ArrayList<>();
        if (chosenFeatures.contains(1)) {
            double sum = 0.0;
            for (String key : keys) {
                sum += localImp.quantitativeImportance(article.getWords(), key);
            }
            Feature feature = new Feature("AVGQI");
            feature.setValue(sum / keys.size());
            features.add(feature)
            ;
        }
        if (chosenFeatures.contains(2)) {
            double sum = 0.0;
            for (String key : keys) {
                sum += localImp.binaryImportance(article.getWords(), key);
            }
            Feature feature = new Feature("AVGSBI");
            feature.setValue(sum / keys.size());
            features.add(feature)
            ;
        }
        if (chosenFeatures.contains(3)) {
            double sum = 0.0;
            for (String key : keys) {
                sum += localImp.probabilisticImportance(article.getWords(), keys, key);
            }

            Feature feature = new Feature("SPI");
            feature.setValue(sum);
            features.add(feature)
            ;
        }
        if (chosenFeatures.contains(4)) {
            double sum = 0.0;
            for (String key : keys) {
                sum += localImp.probabilisticSimilarityImportance(article.getWords(), keys, key);
            }
            Feature feature = new Feature("SPSI");
            feature.setValue(sum);
            features.add(feature)
            ;
        }
        if (chosenFeatures.contains(5)) {
            double sum = 0.0;
            for (String key : keys) {
                sum += localImp.termFrequency(article.getWords(), key);
            }
            Feature feature = new Feature("STF");
            feature.setValue(sum);
            features.add(feature);
        }
        if (chosenFeatures.contains(6)) {
            Feature feature = new Feature("STRING");
            feature.setsValue(article.getFeatureString());
            features.add(feature);
        }

        normalization(features);
        return features;
    }

    public ArrayList<String> generateKeys(ArrayList<Article> articles, ArrayList<String> candidateKeys, char selectingMethod)
    {
        GlobalImportanceMeasures glImpMeass = new GlobalImportanceMeasures();
        LocalImportanceMeasures localImp = new LocalImportanceMeasures();
        ArrayList<String> finalKays = new ArrayList<>();
        TreeMap<String, Double> keys = new TreeMap<String, Double>();
        candidateKeys = deduplicate(candidateKeys);
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

        if(selectingMethod !='c') {
            double standardDeviation = localImp.standardDeviation(measureList)*2;
            double borderValue = measureList.get(0) + standardDeviation;
            double minValue = measureList.get(0);

            ArrayList<String> iterator = new ArrayList<>();
            for (String key : keys.navigableKeySet()) {
                boolean check = false;

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
        return finalKays;
    }
}
