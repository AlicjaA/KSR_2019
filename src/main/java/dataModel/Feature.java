package dataModel;

import java.io.Serializable;

public class Feature  implements Serializable {
    private String name;
    private Double value;
    private String sValue;
    public Feature(String name){ this.name=name; this.value=-1.0; this.sValue="empty";}
    public Feature(String name, Double value){
        this.name=name;
        this.value=value;
        this.sValue="empty";}
    public Feature(String name, String sValue){
        this.name=name;
        this.value=(-1.0);
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
