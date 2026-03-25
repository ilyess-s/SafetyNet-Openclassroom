package com.example.SafetyNet.model;

public class Firestations {
    private String address = "";
    private int station;
    //constructeur vide pour jackson
    public Firestations() {
    }
    public Firestations(String address, int station) {
        this.address = address;
        this.station = station;
    }
    public String getAddress() {
        return address;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
