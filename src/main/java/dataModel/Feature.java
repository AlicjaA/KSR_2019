package dataModel;

public class Feature {
    private String name;
    private Double value;
    private String sValue;
    public Feature(String name){ this.name=name; this.value=null; this.sValue="empty";}
    public Feature(String name, Double value){
        this.name=name;
        this.value=value;
        this.sValue="empty";}
    public Feature(String name, String sValue){
        this.name=name;
        this.value=null;
        this.sValue=sValue;}
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
    public String getName(){return name;}

    @Override
    public String toString() {
        return "{name='" + name +"' sValue='"+sValue +"' value='"+value+"}";
    }
}
