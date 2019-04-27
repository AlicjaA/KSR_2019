package main;


import dataImport.Article;
import dataImport.ImportArticles;
import extraction.ExtractionManager;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Run {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException, IOException {

        final Path[] path = new Path[1];

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                Window w =new Window("dir");
                path[0] = w.getPath();
            }
        });
        ArrayList<Article> articleList = new ArrayList<>();

        ImportArticles importer = new ImportArticles(articleList);
        importer.extract(path[0]);

        ExtractionManager extractionManager = new ExtractionManager();
        extractionManager.tokenization(articleList);

        ArrayList<String> dataToSave = new ArrayList<>();
        for(Article article: articleList){
            dataToSave.add(article.toString()+"\n");
        }
        Files.write (Paths.get(ClassLoader.getSystemClassLoader().getResource("results.txt").toString().substring(6).trim()), dataToSave);
        System.in.read();


    }
}
