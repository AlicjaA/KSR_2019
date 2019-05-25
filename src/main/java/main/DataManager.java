package main;

import dataModel.Article;
import dataImport.ImportArticles;
import dataModel.Label;
import dataModel.Result;
import extraction.ExtractionManager;
import knn.KNN;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataManager {
    private ArrayList<Article> articleList;
    private ArrayList<Article> trainingArticleList;
    private ArrayList<Article> testingArticleList;
    private ArrayList<Result> results;
    private ExtractionManager extractionManager;
    private KNN knn;
    private ArrayList<String> keyLabels;
    ArrayList<String> keyWords;
    long seed;

    public DataManager(Path path, char selectingMethod, ArrayList<String> keyLabels, String labelsTag, String featureTag,
                       ArrayList<String> customKeys, ArrayList<Integer> choosenfeatures, Double trainingPercent, char distanceMeasure,
                       int k, String startingTag, String splitter) throws IOException {
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
        importArticles(path, featureTag.toUpperCase(), labelsTag.toUpperCase(), trainingPercent, startingTag, splitter);
        extraction(selectingMethod, choosenfeatures);
        knn = new KNN(trainingArticleList,keyLabels,distanceMeasure);
        results= new ArrayList<>();
        results = knn.classification(testingArticleList,k);
        ArrayList<String> extraData = new ArrayList<>();
        extraData.add("keyLabels: "+keyLabels+"\n extractionMethod: "+selectingMethod+"\n startingTag: "+startingTag+"\n labelsTag: "+labelsTag+"\n featureTag: "+featureTag+"\n customKeys: "+customKeys+
                "\n choosenFeatures: "+choosenfeatures+"\n distanceMeasure: "+distanceMeasure+"\n trainingSet%: "+trainingPercent+
                "\n k: "+k);
        saveData(extraData);
    }

    private void importArticles(Path path, String featureTag, String labelTag, Double trainingPercent, String startingTag, String splitter){
        articleList = new ArrayList<>();
        testingArticleList =new ArrayList<>();
        trainingArticleList = new ArrayList<>();
        ImportArticles importer = new ImportArticles(articleList);
        importer.extract(path, labelTag, featureTag, startingTag, splitter);
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

    @Override
    public String toString() {
        return "DataManager{" +
                "trainingArticleList=" + trainingArticleList.size() +"\n"+
                " testingArticleList=" + testingArticleList.size() +"\n"+
                " keyLabels=" + keyLabels +"\n"+
                " keyWords=" + keyWords +"\n"+
                '}';
    }

    private void saveData(ArrayList<String>extraData) throws IOException {
        ArrayList<String> dataToSave = new ArrayList<>();
        dataToSave.addAll(extraData);
        dataToSave.add("\n"+extraData+"\n");
        for(Result result: results){
            dataToSave.add(result.toString());
        }
        dataToSave.add("\n"+knn.toString()+"\n");
        dataToSave.add(this.toString()+"\n");
        for(Article article: articleList){
            dataToSave.add(article.toString()+"\n");
        }
        dataToSave.add("\n GENERATE KEYS MEASURES:\n");
        dataToSave.addAll(extractionManager.getToSave());

        //Serializer serializer  = new Serializer();
        //serializer.netSaver(this, ClassLoader.getSystemClassLoader().getResource("serialization.txt").toString().substring(6).trim());
        //Files.createFile(Paths.get(ClassLoader.getSystemClassLoader().getResource("results.txt").toString().substring(6).trim()));
        Files.write (Paths.get(ClassLoader.getSystemClassLoader().getResource("results.txt").toString().substring(6).trim()), dataToSave);

    }



}
