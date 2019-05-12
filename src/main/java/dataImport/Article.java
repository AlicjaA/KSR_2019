package dataImport;

import java.util.*;

public class Article {


    private String text;
    private String label;
    private String feature;
    private ArrayList<String> words;
    private ArrayList<String> terms;
    public static class Features{
        private String name;
        private Double value;
        private String sValue;
        public Features(String name){ this.name=name; this.value=null; this.sValue=null;}
        public Double getValue() {
            return value;
        }
        public void setValue(Double value) {
            this.value = value;
        }
        public String getsValue() {
            return sValue;
        }
        public void setsValue(String sValue) {
            this.sValue = sValue;
        }

        @Override
        public String toString() {
            return "{name='" + name +"' sValue='"+sValue +"' value='"+value+"}";
        }
    };
    ArrayList<Features>features;

    public Article(TreeMap<String, ArrayList<String>> articleData){
        features = new ArrayList<>();
        if(articleData.containsKey("LABEL")){
            this.label=articleData.get("LABEL").get(0);}
        else{
            this.label="unknown";}

        if(articleData.containsKey("TEXT")){
            this.text = articleData.get("TEXT").get(0);}
        else{
            this.text=" ";}
        if(articleData.containsKey("FEATURE")){
            this.feature = articleData.get("FEATURE").get(0);}
        else{
            this.feature=" ";}
    }

    public String getText() {
        return text;
    }

    public String getLabel() {
        return label;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words=new ArrayList<>();
        for(String word: words){ this.words.add(word);}
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<String> terms) {
        this.terms = new ArrayList<>();
        for(String word: terms){ this.terms.add(word);}
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<Features> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Features> features) {
        for(Features f:features){ this.features.add(f);}
    }

    @Override
    public String toString() {
       return "Article{" +
                "\n label='" + label + '\'' +
                "\n text='" + text + '\'' +
                "\n words='" + words +'\'' +
                "\n terms='" + terms +'\'' +
               "\n feature='" + feature +'\'' +
               "\n features='" + features +
                '}';
    }
}