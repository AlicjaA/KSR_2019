package main;

import dataModel.Article;
import dataImport.ImportArticles;
import extraction.ExtractionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataManager {
    private ArrayList<Article> articleList;
    private ArrayList<Article> trainingArticleList;
    private ArrayList<Article> testingArticleList;
    private ExtractionManager extractionManager;
    //private KNN
    private ArrayList<String> keyLabels;
    ArrayList<String> keyWords;

    public DataManager(Path path, char selectingMethod, ArrayList<String> keyLabels, String labelsTag, String featureTag,
                       ArrayList<String> customKeys, ArrayList<Integer> choosenfeatures, Double trainingPercent, char distanceMeasure, int k, String startingTag) throws IOException {
        extractionManager = new ExtractionManager();
        this.keyLabels=keyLabels;
        this.keyLabels.add("unknown");
        labelsTag.toUpperCase();
        if(selectingMethod=='c'){
            this.keyWords=customKeys;
        }
        else{
            keyWords = new ArrayList<>();
        }
        importArticles(path, featureTag.toUpperCase(), labelsTag.toUpperCase(), trainingPercent, startingTag);
        extraction(selectingMethod, choosenfeatures);

        saveData();
    }

    private void importArticles(Path path, String featureTag, String labelTag, Double trainingPercent, String startingTag){
        articleList = new ArrayList<>();
        testingArticleList =new ArrayList<>();
        trainingArticleList = new ArrayList<>();
        ImportArticles importer = new ImportArticles(articleList);
        importer.extract(path, labelTag, featureTag, startingTag);
        for(Article article:articleList){
            if(!keyLabels.contains(article.getLabel())){
                article.setLabel("unknown");
            }
        }
        Double tmp = Double.valueOf(articleList.size())*trainingPercent;
        Integer trainingSetSize = tmp.intValue();
        for(int i=0;i<articleList.size();++i){
            if(i<trainingSetSize) {
                trainingArticleList.add(articleList.get(i));
            }
            else{
                testingArticleList.add(articleList.get(i));
            }
        }
    }

    private void extraction (char selectingMethod, ArrayList<Integer> chosenFeatures) throws IOException {
        extractionManager.tokenization(articleList);
        keyWords=extractionManager.selectKeyWords(trainingArticleList,selectingMethod);
        extractionManager.setFeatures(articleList,chosenFeatures,keyWords);
    }

    private void saveData() throws IOException {
        ArrayList<String> dataToSave = new ArrayList<>();
        for(Article article: articleList){
            dataToSave.add(article.toString()+"\n");
        }
        Files.write (Paths.get(ClassLoader.getSystemClassLoader().getResource("results.txt").toString().substring(6).trim()), dataToSave);
    }



}
