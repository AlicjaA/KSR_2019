package dataImport;

import java.util.*;

public class Article {

    private String date;
    private List<String> topics;
    private List<String> places;
    private List<String> people;
    private List<String> orgs;
    private List<String> exchanges;
    private List<String> companies;
    private String unknown;
    private String title;
    private String dateline;
    private String body;
    private ArrayList<String> words;
    private Map<String, Integer> terms;
    private ArrayList<String> basicTerms;
    public Article(TreeMap<String, ArrayList<String>> articleData){
        this.topics= new ArrayList<>();
        this.places=new ArrayList<>();
        this.people=new ArrayList<>();
        this.orgs=new ArrayList<>();
        this.exchanges=new ArrayList<>();
        this.companies=new ArrayList<>();
        if(articleData.containsKey("DATE")){
            this.date=articleData.get("DATE").get(0);}
        else{
            this.date=" ";}
        if(articleData.containsKey("TOPICS")){
            for(String value:articleData.get("TOPICS")){
                this.topics.add(value);}} else{
            this.topics.add(" ");}
        if(articleData.containsKey("PLACES")){
            for(String value:articleData.get("PLACES")){
                this.places.add(value);}} else{
            this.places.add(" ");}
        if(articleData.containsKey("PEOPLE")){
            for(String value:articleData.get("PEOPLE")){
                this.people.add(value);}} else{
            this.people.add(" ");}
        if(articleData.containsKey("ORGS")){
            for(String value:articleData.get("ORGS")){
                this.orgs.add(value);}} else{
            this.orgs.add(" ");}
        if(articleData.containsKey("EXCHANGES")){
            for(String value:articleData.get("EXCHANGES")){
                this.exchanges.add(value);}} else{
            this.exchanges.add(" ");}
        if(articleData.containsKey("COMPANIES")){
            for(String value:articleData.get("COMPANIES")){
                this.companies.add(value);}} else{
            this.companies.add(" ");}
        if(articleData.containsKey("UNKNOWN")){this.unknown = articleData.get("UNKNOWN").get(0);} else{this.unknown=" ";}
        if(articleData.containsKey("TITLE")){this.title = articleData.get("TITLE").get(0);} else{this.title=" ";}
        if(articleData.containsKey("DATELINE")){this.dateline = articleData.get("DATELINE").get(0);} else{this.dateline=" ";}
        if(articleData.containsKey("BODY")){this.body = articleData.get("BODY").get(0);} else{
        if(articleData.containsKey("TEXT TYPE=\"UNPROC\"")){this.body = articleData.get("TEXT TYPE=\"UNPROC\"").get(0);} else{this.body=" ";}}

    }


    public String getDate() {
        return date;
    }
    public List<String> getTopics() {
        return topics;
    }
    public List<String> getPlaces() {
        return places;
    }
    public List<String> getPeople() {
        return people;
    }
    public List<String> getOrgs() {
        return orgs;
    }
    public List<String> getExchanges() {
        return exchanges;
    }
    public List<String> getCompanies() {
        return companies;
    }
    public String getUnknown() {
        return unknown;
    }
    public String getTitle() { return title; }
    public String getDateline() {
        return dateline;
    }
    public String getBody() {
        return body;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words=new ArrayList<>();
        for(String word: words){ this.words.add(word);}
    }

    public Map<String, Integer> getTerms() {
        return terms;
    }

    public void setTerms(Map<String, Integer> terms) {
        this.terms = new TreeMap<>();
        this.terms.putAll(terms);
    }

    public ArrayList<String> getBasicTerms() {
        return basicTerms;
    }

    public void setBasicTerms(ArrayList<String> basicTerms) {
        this.basicTerms = new ArrayList<>();
        for(String word: basicTerms){ this.basicTerms.add(word);}
    }


    @Override
    public String toString() {
       return "Article{" +
                "\n date='" + date + '\'' +
                "\n topics=" + topics +
                "\n places=" + places +
                "\n title='" + title + '\'' +
                "\n dateline='" + dateline + '\'' +
                "\n body='" + body + '\'' +
                "\n words=" + words +
                "\n terms=" + terms +
                "\n basicTerms=" + basicTerms +
                '}';
    }
}