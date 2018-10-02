package com.example.leena.mypills;

public class Event {
    private String Description;
    private String timing;
    private String imageURL;

    public String getOrganiser() {
        return Organiser;
    }

    public void setOrganiser(String organiser) {
        Organiser = organiser;
    }

    private String Organiser;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    private String Type;

}
