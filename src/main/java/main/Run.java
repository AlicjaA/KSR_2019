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
        ArrayList<Integer> choosenFeatures;
        ArrayList<String>keyLabels= new ArrayList<>();
        ArrayList<String> customKeys = new ArrayList<>();
        selectingMethod = ui.getAsCharArray("Wybierz metodę selekcji słów kluczowych: t-average TF*IDF, i-inverse document frequency, c- własna lista")[0];
        if(selectingMethod=='c'){
            customKeys = ui.getAsStringArrayList("Podaj własne słowa kluczowe oddzielone spacją:");
        }
        startingTag=ui.getAsString("Podaj tag oddzielający artykuły (np. dla reuters jest to 'REUTERS'):");
        keyLabels = ui.getAsStringArrayList("Podaj etykiety oddzielone spacją:");
        labelTag = ui.getAsString("Podaj tag etykiety:");
        choosenFeatures=ui.getAsIntList("Podaj numery cech, których chcesz użyć: 1-śr liczba wystąpień słów kluczowych " +
                "2-średnia liczba wystąpień słów kluczowych  3-suma PI 4-suma SPI 5-suma TF 6- string");
        if(choosenFeatures.contains(6)){
            featureTag = ui.getAsString("Podaj tag do wybrania cechy:");
        }
        Double percent = ui.getAsFraction("Podaj wielkość zbioru testowego w procentach:");
        char distanceMeasure = ui.getAsString("Podaj metrykę: m-manhattan, c-chebrzyszev, 5-minkowski z paramertami=5," +
                "\n p-power distance z parametrami, e-euklidesowa").charAt(0);
        int k = ui.getAsIntList("Podaj k:").get(0);
        DataManager dataManager = new DataManager(path,selectingMethod,keyLabels, labelTag, featureTag,customKeys, choosenFeatures,percent, distanceMeasure, k, startingTag);
        //System.in.read();

    }
}
