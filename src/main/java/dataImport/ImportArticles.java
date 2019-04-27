package dataImport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;

public class ImportArticles {

    private File[] sgmFiles;
    private ArrayList<Article> articles;
    public ImportArticles(ArrayList<Article> articles) {
        this.articles=articles;
    }

    public void extract(Path fileDir) {
        File folder =new File(fileDir.toString());

        sgmFiles = folder.listFiles();
        if (sgmFiles != null && sgmFiles.length > 0) {
            for (File sgmFile : sgmFiles) {
                extractFile(articles, sgmFile);
            }
        } else {
            System.err.println("No .sgm files in " + fileDir);
        }
    }

    protected void extractFile(ArrayList<Article> articles, File sgmFile) {
        TreeMap<String, ArrayList<String>> articleData = new TreeMap<>();
        Document doc = null;
        try {
            doc = Jsoup.parse(sgmFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements e = new Elements();
        Elements ed = new Elements();
        ArrayList<String> s = new ArrayList<>();
        Elements singleArticle = doc.getElementsByTag("REUTERS");
        for (Element text: singleArticle) {
            for (tags t : tags.values()) {
                s = new ArrayList<>();
                e = new Elements();
                e = text.getElementsByTag(t.name());
                if(t.name().equals("TEXT")){
                    ArrayList<String> ss = new ArrayList<>();
                        for(textTags tt: textTags.values()){
                            ss = new ArrayList<>();
                            for (int i=0;i<e.size();++i) {
                                if(e.get(i).getElementsByTag(tt.name()).hasText()){
                                ss.add(e.get(i).getElementsByTag(tt.name()).text());
                                articleData.put(tt.name(), ss);}
                            }
                        }
                        ss = new ArrayList<>();
                        //Node n = e.select(t.name()).last().data();
                        ss.add(e.select(t.name()).text());
                        articleData.put("BODY",ss);
                }
                else{
                    if (e.select("D").hasText()) {
                        s.addAll(e.select("D").eachText());
                    }
                    else {
                        s.add(e.tagName(t.name()).text());
                    }
                    articleData.put(t.name(), s);
                }
            }
            articles.add(new Article(articleData));
        }
    }

    enum tags{
        DATE,
        TOPICS,
        PLACES,
        PEOPLE,
        ORGS,
        EXCHANGES,
        COMPANIES,
        TEXT,
        UNKNOWN,
    }
    enum textTags{
        TITLE,
        BODY,
        DATELINE,
        AUTHOR
    }
}
