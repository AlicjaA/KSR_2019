package dataModel;

public class Result {

    private String label;
    private Integer correct;
    private Integer incorect;
    private Double accuracy;

    public Result(String label, Integer correct, Integer incorect, Double error) {
        this.label = label;
        this.correct = correct;
        this.incorect = incorect;
        this.accuracy =0.0;
    }

    public Result(String label) {
        this.label = label;
        this.correct=0;
        this.incorect=0;
        this.accuracy =0.0;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Integer getIncorect() {
        return incorect;
    }

    public void setIncorect(Integer incorect) {
        this.incorect = incorect;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setError() {
        this.accuracy = accuracy;
    }

    public void addCorect(){
        this.correct +=1;
    }

    public void addIncorrect(){
        this.incorect +=1;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "Result{" +
                "label='" + label + '\'' +
                ", correct=" + correct +
                ", incorect=" + incorect +
                ", accuracy=" + accuracy +
                '}';
    }
}
