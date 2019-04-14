package extraction.documentProcessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class ExtractionOperations {

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

}
