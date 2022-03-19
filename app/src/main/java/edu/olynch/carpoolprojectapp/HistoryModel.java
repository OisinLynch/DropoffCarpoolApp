package edu.olynch.carpoolprojectapp;

public class HistoryModel {
    String name;
    String destination;
    String journeyLength;
    int image;

    public HistoryModel(String name, String destination, String journeyLength, int image) {
        this.name = name;
        this.destination = destination;
        this.journeyLength = journeyLength;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

    public String getJourneyLength() {
        return journeyLength;
    }

    public int getImage() {
        return image;
    }
}
