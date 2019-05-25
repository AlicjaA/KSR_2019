package main;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Run {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException, IOException {
        UiManager ui = new UiManager();
        Path path=ui.getPath();
        char selectingMethod;
        String labelTag;
        String startingTag;
        String featureTag="empty";
        String splitter="";
        ArrayList<Integer> choosenFeatures;
        ArrayList<String>keyLabels= new ArrayList<>();
        ArrayList<String> customKeys = new ArrayList<>();


        selectingMethod = ui.getAsCharArray("Wybierz metodę selekcji słów kluczowych: t-average TF*IDF, i-inverse document frequency,  c- własna lista")[0];
        if(selectingMethod=='c'){
            customKeys = ui.getAsStringArrayList("Podaj własne słowa kluczowe oddzielone spacją:");
        }
        startingTag=ui.getAsString("Podaj tag oddzielający artykuły (np. dla reuters jest to 'REUTERS'):");
        keyLabels = ui.getAsStringArrayList("Podaj etykiety oddzielone spacją:");
        labelTag = ui.getAsString("Podaj tag etykiety:");
        choosenFeatures=ui.getAsIntList("Podaj oddzielone przecinkami numery cech, których chcesz użyć: \n" +
                " 1-śr liczba obecnpości słów kluczowych,\n " +
                "2-średnia liczba wystąpień słów kluczowych,\n  3-suma PI,\n 4-suma PSI,\n 5-suma TF,\n 6- string, \n 7-PSI dla 6,\n" +
                " 8-TF dla 6,\n" +
                "9-suma BI dla 10% pierwszych słów kluczowych,\n 10- średna odległość słów kluczowych od początku tekstu,\n" +
                " 11-suma ilości wystąpień słów kluczowych w pierwszych 20% tekstu,\n" +
                "12-średnie TF,\n 13-śrenia PSI,\n 14-średnia PI,\n 15-suma QI\n");
        if(choosenFeatures.contains(6)){
            featureTag = ui.getAsString("Podaj tag do wybrania cechy:");
            splitter=ui.getAsString("Podaj splitter:");
        }
        else{
            featureTag="empty";
        }
        Double percent = ui.getAsFraction("Podaj wielkość zbioru treningowego w procentach:");
        char distanceMeasure = ui.getAsString("Podaj metrykę: m-manhattan, c-chebrzyszev, 5-minkowski z paramertami=5," +
                "\n p-power distance z parametrami:p3:r2, e-euklidesowa").charAt(0);
        int k = ui.getAsIntList("Podaj k:").get(0);
        DataManager dataManager = new DataManager(path,selectingMethod,keyLabels, labelTag, featureTag,customKeys, choosenFeatures,percent, distanceMeasure, k, startingTag, splitter);
        System.exit(0);

    }
}
