package main;

import dataImport.Article;
import dataImport.ImportArticles;
import extraction.ExtractionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

public class DataManager {
    ArrayList<String> keyWords;
    ArrayList<Article> articleList;
    ExtractionManager extractionManager;

    public DataManager(Path[] path) throws IOException {
        keyWords = new ArrayList<>();
        articleList = new ArrayList<>();
        extractionManager = new ExtractionManager();
        importArticles(path);
        extraction();
        extractionManager.measureImportance(articleList,keyWords);
        saveData();
    }

    private void importArticles(Path[] path){
        ImportArticles importer = new ImportArticles(articleList);
        importer.extract(path[0]);
    }

    private void extraction () throws IOException {
        extractionManager.tokenization(articleList);
        extractionManager.selectKeyWords(articleList, keyWords, "places");
    }

    private void saveData() throws IOException {
        ArrayList<String> dataToSave = new ArrayList<>();
        for(Article article: articleList){
            dataToSave.add(article.toString()+"\n");
        }
        Files.write (Paths.get(ClassLoader.getSystemClassLoader().getResource("results.txt").toString().substring(6).trim()), dataToSave);
    }



}
