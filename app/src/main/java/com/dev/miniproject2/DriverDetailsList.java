package com.dev.miniproject2;

public class DriverDetailsList {


    String Mobile,Name,Source,Destination,Fare;


    public DriverDetailsList(String mobile,String name,String source, String destination,String fare) {
        Mobile=mobile;
        Name=name;
        Source = source;
        Destination = destination;
        Fare = fare;
    }

    public DriverDetailsList() {
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSource() { return Source; }

    public void setSource(String source) { Source = source; }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

}
