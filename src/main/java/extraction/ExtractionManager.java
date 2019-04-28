package extraction;

import com.sun.org.apache.xpath.internal.functions.FuncSubstringBefore;
import dataImport.Article;
import extraction.documentProcessing.ExtractionOperations;
import extraction.importanceMeasurment.LocalImportanceMeasures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class ExtractionManager {
    ExtractionOperations exOperate;

    public ExtractionManager(){ exOperate = new ExtractionOperations();}

    public void tokenization (ArrayList<Article> articles) throws IOException {
        for (Article article : articles){
            ArrayList<String> wordList = new ArrayList<>();
            wordList= exOperate.makeDataList(article.getDateline());
            wordList.addAll(exOperate.makeDataList(article.getTitle()));
            wordList.addAll(exOperate.makeDataList(article.getAuthor()));
            wordList.addAll(exOperate.makeDataList(article.getBody()));
            wordList.addAll(exOperate.makeDataList(article.getUnknown()));
            wordList.addAll(exOperate.makeDataList(article.getDate()));
            wordList= exOperate.deleteStopwords(wordList);
            wordList= exOperate.cleanWordList(wordList);
            wordList= exOperate.toLowerCase(wordList);
            wordList= exOperate.stemmer(wordList);
            article.setWords(wordList);
            wordList= exOperate.deduplicate(wordList);
            article.setBasicTerms(wordList);
        }
    }

    public void selectKeyWords(ArrayList<Article> articles, ArrayList<String> keyWords, String label){
        switch (label){
            case "places":{
                ArrayList<String[]> tmp = new ArrayList<>();
                String[] t= new String[2];
                for(Article article:articles){
                    for(places place: places.values()){
                        if(article.getPlaces().size()==1 && article.getPlaces().contains(place.name())){
                            String[] subP = article.getDateline().split(",");
                            String[] subA = article.getAuthor().split(",");
                            keyWords.add(subA[0]);
                            keyWords.add(subP[0]);
                        }
                    }
                }
                keyWords=exOperate.cleanWordList(keyWords);
                keyWords=exOperate.toLowerCase(keyWords);
                keyWords = exOperate.stemmer(keyWords);
                keyWords=exOperate.deduplicate(keyWords);
            }
            default:{}
        }
    }

    public void measureImportance(ArrayList<Article> articles, ArrayList<String> keyWords){
        LocalImportanceMeasures localImp = new LocalImportanceMeasures();

        for(Article article:articles){
            TreeMap<String,ArrayList<Double>> terms = new TreeMap<>();
            for(String keyWord: keyWords){
                ArrayList<Double> measures = new ArrayList<>();
                measures.add(localImp.binaryImportance(article.getWords(),keyWord));
                measures.add(localImp.quantitativeImportance(article.getWords(), keyWord));
                measures.add(localImp.termFrequency(article.getWords(),keyWord));
                measures.add(localImp.probabilisticImportance(article.getWords(),keyWords,keyWord));
                //measures.add(localImp.probabilisticSimilarityImportance(article.getWords(),keyWords,keyWord));
                terms.put(keyWord,measures);
            }
            article.setTerms(terms);
        }
    }

    enum places{
        westgermany, usa, france, uk, canada, japan
    }
}
