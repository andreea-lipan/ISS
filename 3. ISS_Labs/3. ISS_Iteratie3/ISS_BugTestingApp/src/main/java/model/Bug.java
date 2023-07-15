package model;

public class Bug {

    private int id;
    private String name;
    private String summary;
    private Functionality functionality;
    private String description;
    private String stepsToReproduce;

    public Bug(String name, String summary, Functionality functionality, String description, String stepsToReproduce) {
        this.name = name;
        this.summary = summary;
        this.functionality = functionality;
        this.description = description;
        this.stepsToReproduce = stepsToReproduce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Functionality getFunctionality() {
        return functionality;
    }

    public void setFunctionality(Functionality functionality) {
        this.functionality = functionality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStepsToReproduce() {
        return stepsToReproduce;
    }

    public void setStepsToReproduce(String stepsToReproduce) {
        this.stepsToReproduce = stepsToReproduce;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bug() {
    }

    @Override
    public String toString() {
        return "Bug{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", functionality=" + functionality +
                ", description='" + description + '\'' +
                ", stepsToReproduce='" + stepsToReproduce + '\'' +
                '}';
    }
}
