package extraction;

import dataImport.Article;
import extraction.documentProcessing.ExtractionOperations;

import java.io.IOException;
import java.util.ArrayList;

public class ExtractionManager {
    ExtractionOperations exOperate;

    public ExtractionManager(){ exOperate = new ExtractionOperations();}

    public void tokenization (ArrayList<Article> articles) throws IOException {
        for (Article article : articles){
            ArrayList<String> wordList = new ArrayList<>();
            wordList= exOperate.makeDataList(article.getBody());
            wordList= exOperate.cleanWordList(wordList);
            wordList= exOperate.toLowerCase(wordList);
            wordList= exOperate.stemmer(wordList);
            article.setWords(wordList);
            wordList= exOperate.deduplicate(wordList);
            wordList= exOperate.deleteStopwords(wordList);
            article.setBasicTerms(wordList);
        }
}
}
