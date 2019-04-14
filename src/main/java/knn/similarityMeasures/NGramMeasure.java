package knn.similarityMeasures;

import java.util.ArrayList;

/**
 * Miara n-Gramów z ograniczeniami w wersji do liczenia odlwgłości słów
 */

public class NGramMeasure {
    public Double termSimilarity(String firstWord, String secondWord){
        double similarity = 0;
        double occurance = 0;
        ArrayList<String> firstWordGrams = new ArrayList<>();
        ArrayList<String> secondWordGrams = new ArrayList<>();
        int max = 0;
        int min = Math.min(firstWord.length(),secondWord.length());
        for(int i=2;i<=min;++i){
            firstWordGrams.addAll(generateGrams(firstWord,i,min));
            secondWordGrams.addAll(generateGrams(secondWord,i,min));
        }
        for(String fgram:firstWordGrams){
            for(String sgrams: secondWordGrams){
                if(fgram.equals(sgrams)){
                    occurance +=1;
                }
            }
        }
        if(firstWord.length()<secondWord.length()){
           max=secondWordGrams.size();
            similarity=(2/((max-firstWord.length()+1)*(max - firstWord.length()+2)-(max-secondWord.length()+1)
                    *(max-secondWord.length())));
        }else{
            max=firstWordGrams.size();
            similarity=(2/((max-secondWord.length()+1)*(max - secondWord.length()+2)-(max-firstWord.length()+1)
                    *(max-firstWord.length())));
        }
        similarity= similarity*occurance;
        return similarity;
    }

    public ArrayList<String>generateGrams(String word, int size, int maxSize){
        ArrayList<String> wordGrams= new ArrayList<>();
        for(int j =0;j<word.length();++j){
            if((j+size)<=maxSize) {
                wordGrams.add(word.substring(j, (j + size)).trim());
            }
        }
        return wordGrams;
    }
}
