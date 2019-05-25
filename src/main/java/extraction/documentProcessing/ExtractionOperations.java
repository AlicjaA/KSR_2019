package extraction.documentProcessing;


import java.util.Comparator;
import dataModel.Article;
import dataModel.Feature;
import extraction.Features.QLFeatures;
import extraction.Features.QNFeatures;
import extraction.importanceMeasurment.LocalImportanceMeasures;
import extraction.importanceMeasurment.GlobalImportanceMeasures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ExtractionOperations {
    private class ValueComparator<K,V extends Comparable> implements Comparator<K>
    {
        private Map<K,V> map;

        public ValueComparator(Map<K,V> map) {
            this.map = new HashMap<>(map);
        }

        @Override
        public int compare(K s1, K s2) {
            return map.get(s1).compareTo(map.get(s2));
        }
    }

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

    private void featuresNormalization(ArrayList<Feature> values){
        Double min= 0.0;
        Double max=0.0;
        for(Feature v: values){
            if(v.getValue() != (-1.0)) {
                max = (v.getValue() > max) ? v.getValue() : max;
                min = (min == 0.0) ? v.getValue() : (v.getValue() < min) ? v.getValue() : min;
            }
        }

        for(Feature v: values){
            if(v.getValue() != (-1.0)) {
                double normalizedValue = 0.0;
                normalizedValue = (v.getValue() - min) / (max - min);
                v.setValue(normalizedValue);
            }
        }

    }

    public ArrayList<Double> doublesNormalization(ArrayList<Double> values){
        ArrayList<Double> result = new ArrayList<>();
        Double min= 0.0;
        Double max=0.0;
        for(Double v: values){

                max = (v > max) ? v : max;
                min = (min == 0.0) ? v : (v < min) ? v : min;
        }

        for(Double v: values){
                double normalizedValue = 0.0;
                normalizedValue = (v - min) / (max - min);
                result.add(normalizedValue);
        }
        return result;
    }

    public ArrayList<Feature> calculateFeatures(Article article, ArrayList<Integer> chosenFeatures, ArrayList<String> keys) {
        LocalImportanceMeasures localImp = new LocalImportanceMeasures();
        ArrayList<Feature> features = new ArrayList<>();
        if (chosenFeatures.contains(1)) {

            Feature feature = new Feature(QNFeatures.AVG_QI.name(), QNFeatures.AVG_QI.calculate(article,keys,localImp));
            features.add(feature);
        }
        if (chosenFeatures.contains(2)) {

            Feature feature = new Feature(QNFeatures.AVG_SUM_BI.name(),QNFeatures.AVG_SUM_BI.calculate(article,keys,localImp));
            features.add(feature);
        }
        if (chosenFeatures.contains(3)) {


            Feature feature = new Feature(QNFeatures.SUM_PI.name(),QNFeatures.SUM_PI.calculate(article,keys,localImp));
            features.add(feature)
            ;
        }
        if (chosenFeatures.contains(4)) {

            Feature feature = new Feature(QNFeatures.SUM_PSI.name(), QNFeatures.SUM_PSI.calculate(article,keys,localImp));
            features.add(feature);

        }
        if (chosenFeatures.contains(5)) {

            Feature feature = new Feature(QNFeatures.SUM_TF.name(),QNFeatures.SUM_TF.calculate(article,keys,localImp));
            features.add(feature);

        }
        if (chosenFeatures.contains(6)) {
            QLFeatures.STRING.name(); QLFeatures.STRING.calculate(article);

            Feature feature = new Feature(QLFeatures.STRING.name(), QLFeatures.STRING.calculate(article));
            features.add(feature);
        }

        if (chosenFeatures.contains(7) && chosenFeatures.contains(6) ) {

            Feature feature = new Feature(QNFeatures.PSI_STRING_FEATURE.name(),QNFeatures.PSI_STRING_FEATURE.calculate(article,keys,localImp));
            features.add(feature);

        }

        if (chosenFeatures.contains(8) && chosenFeatures.contains(6)) {

            Feature feature = new Feature(QNFeatures.TF_STRING_FEATURE.name(),QNFeatures.TF_STRING_FEATURE.calculate(article,keys,localImp));
            features.add(feature);

        }

        if (chosenFeatures.contains(9)) {

            Feature feature = new Feature(QNFeatures.SUM_BI_F10P_KEYS.name(),QNFeatures.SUM_BI_F10P_KEYS.calculate(article,keys,localImp));
            features.add(feature);

        }

        if (chosenFeatures.contains(10)) {

            Feature feature = new Feature(QNFeatures.AVG_DIST_KEY_TXT_BEGIN.name(),QNFeatures.AVG_DIST_KEY_TXT_BEGIN.calculate(article,keys,localImp));
            features.add(feature);

        }

        if (chosenFeatures.contains(11)) {

            Feature feature = new Feature(QNFeatures.SUM_QI_F20P.name(),QNFeatures.SUM_QI_F20P.calculate(article,keys,localImp));
            features.add(feature);

        }

        if (chosenFeatures.contains(12)) {

            Feature feature = new Feature(QNFeatures.AVG_TF.name(),QNFeatures.AVG_TF.calculate(article,keys,localImp));
            features.add(feature);

        }

        if (chosenFeatures.contains(13)) {

            Feature feature = new Feature(QNFeatures.AVG_PSI.name(),QNFeatures.AVG_PSI.calculate(article,keys,localImp));
            features.add(feature);

        }

        if (chosenFeatures.contains(14)) {

            Feature feature = new Feature(QNFeatures.AVG_PI.name(),QNFeatures.AVG_PI.calculate(article,keys,localImp));
            features.add(feature);

        }

        if (chosenFeatures.contains(15)) {

            Feature feature = new Feature(QNFeatures.SUM_QI.name(),QNFeatures.SUM_QI.calculate(article,keys,localImp));
            features.add(feature);

        }


        featuresNormalization(features);
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
                    measure= glImpMeass.TFIDFMethod(articles,word);
                    measureList.add(measure);
                    keys.put(word,measure);
                    break;
                }
                default:{
                    break;
                }
            }
        }


        if(selectingMethod !='c') {
            Collections.sort(measureList, Collections.reverseOrder());
            Map<String,Double> sorted = sortByValue(keys);
            int i=sorted.size();

            Set <Map.Entry<String, Double>> set = sorted.entrySet();
            List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(set);
            int border = 0;
            if(list.size()>20 && list.get(i-20).getValue()>0.0){
                border = i-20;
            }
            else{
                border = i - (int) (i*0.2);
            }

            for (int j = sorted.size()-1; j>=border;--j) {
                finalKays.add(list.get(j).getKey());
            }

        }
        else{
            finalKays=candidateKeys;
        }
        return finalKays;
    }

    private static Map<String, Double> sortByValue(Map<String, Double> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
