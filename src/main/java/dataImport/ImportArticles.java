package dataImport;

import dataModel.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ImportArticles {

    private File[] sgmFiles;
    private ArrayList<Article> articles;
    public ImportArticles(ArrayList<Article> articles) {
        this.articles=articles;
    }

    public void extract(Path fileDir, String labelTag, String featureTag, String startingTag) {
        File folder =new File(fileDir.toString());

        sgmFiles = folder.listFiles();
        if (sgmFiles != null && sgmFiles.length > 0) {
            for (File sgmFile : sgmFiles) {
                extractFile(articles, sgmFile, labelTag, featureTag, startingTag);
            }
        } else {
            System.err.println("No .sgm files in " + fileDir);
        }
    }

    protected void extractFile(ArrayList<Article> articles, File sgmFile, String labelTag, String featureTag, String startingTag) {
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
        Elements singleArticle = doc.getElementsByTag(startingTag);

        for (Element text: singleArticle) {
            List<Node> nodes = text.childNodes();
            String ss = "";
            s = new ArrayList<>();
            for (Node n : text.childNodes()) {
                if (n.childNodes().size() > 1) {
                    ArrayList<String> lastNodeNames= new ArrayList<>();
                    for (Node nn: n.childNodes()) {
                        if (!lastNodeNames.contains(nn.nodeName())) {
                            ss += text.getElementsByTag(nn.nodeName()).text() + " ";
                            lastNodeNames.add(nn.nodeName());
                        }
                    }
                    Element ee = text.getElementsByTag(n.nodeName()).first();
                    List<TextNode> tmp = ee.textNodes();
                    for (Node t : ee.textNodes()) {
                        ss += t.toString() + " ";
                    }
                } else {
                    ss += text.getElementsByTag(n.nodeName()).text() + " ";
                }
            }
            s.add(ss);
            articleData.put("TEXT", s);

            if (!(text.getElementsByTag(labelTag).first().childNodes().size() > 1)) {
                String sa = text.getElementsByTag(labelTag).text();
                ArrayList<String> sss = new ArrayList<>();
                sss.add(sa);
                articleData.put("LABEL", sss);
            }

            if(!featureTag.equals("empty")) {
                String[] sb = text.getElementsByTag(featureTag).text().split(", ");
                ArrayList<String> sss = new ArrayList<>();
                sss.add(sb[0]);
                articleData.put("FEATURE", sss);
                articles.add(new Article(articleData));
            }
        }
    }
}
