package dataModel;

public class Result {

    private String label;
    private Integer correct;
    private Integer incorect;
    private Double error;

    public Result(String label, Integer correct, Integer incorect, Double error) {
        this.label = label;
        this.correct = correct;
        this.incorect = incorect;
    }

    public Result(String label) {
        this.label = label;
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

    public Double getError() {
        return error;
    }

    public void setError(Double error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Result{" +
                "label='" + label + '\'' +
                ", correct=" + correct +
                ", incorect=" + incorect +
                ", error=" + error +
                '}';
    }
}
