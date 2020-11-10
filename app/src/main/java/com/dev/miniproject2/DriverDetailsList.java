package com.dev.miniproject2;

public class DriverDetailsList {


    String Mobile,Name,Source,Destination,Fare, Stop1, Stop2, Stop3, Stop4, Vehicle, ID;
    Long CurrentHour;



    public DriverDetailsList(String mobile, String name, String source, String stop1, String stop2, String stop3, String stop4, String destination, String fare, String vehicle, Long currentHour, String id) {
        Mobile=mobile;
        Name=name;
        Source = source;
        Destination = destination;
        Fare = fare;
        Stop1 = stop1;
        Stop2 = stop2;
        Stop3 = stop3;
        Stop4 = stop4;
        Vehicle = vehicle;
        CurrentHour = currentHour;
        ID = id;
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


    public String getStop1() {
        return Stop1;
    }

    public void setStop1(String stop1) {
        Stop1 = stop1;
    }

    public String getStop2() {
        return Stop2;
    }

    public void setStop2(String stop2) {
        Stop2 = stop2;
    }

    public String getStop3() {
        return Stop3;
    }

    public void setStop3(String stop3) {
        Stop3 = stop3;
    }

    public String getStop4() {
        return Stop4;
    }

    public void setStop4(String stop4) {
        Stop4 = stop4;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Long getCurrentHour() {
        return CurrentHour;
    }

    public void setCurrentHour(Long currentHour) {
        CurrentHour = currentHour;
    }
}
