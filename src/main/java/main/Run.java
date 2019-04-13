package main;


import dataOperations.Article;
import dataOperations.ImportArticles;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

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

        System.in.read();

    }
}