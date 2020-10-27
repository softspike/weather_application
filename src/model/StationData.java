package model;

import java.util.List;


public class StationData {

    private String name;
    private String year;

    private List<String> inputtempMaxi;
    private List<String> inputtempMini;
    private List<String> inputairFrost;
    private List<String> inputrainfall;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getInputtempMaxi() {
        return inputtempMaxi;
    }

    public void setInputtempMaxi(List<String> inputtempMaxi) {
        this.inputtempMaxi = inputtempMaxi;
    }

    public List<String> getInputtempMini() {
        return inputtempMini;
    }

    public void setInputtempMini(List<String> inputtempMini) {
        this.inputtempMini = inputtempMini;
    }

    public List<String> getInputairFrost() {
        return inputairFrost;
    }

    public void setInputairFrost(List<String> inputairFrost) {
        this.inputairFrost = inputairFrost;
    }

    public List<String> getInputrainfall() {
        return inputrainfall;
    }

    public void setInputrainfall(List<String> inputrainfall) {
        this.inputrainfall = inputrainfall;
    }

}
