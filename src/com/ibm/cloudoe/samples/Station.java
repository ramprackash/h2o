package com.ibm.cloudoe.samples;
//package h2o;
import java.io.*;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Station {

    // fields for each station from http://cdec.water.ca.gov/misc/resinfo.html
    private String id; // A code for ex: APN for ALPINE LAKE
    private String dam;
    private String lake;
    private String stream;
    private String capacity;
    // fields for each station from  http://cdec.water.ca.gov/cgi-progs/stationInfo?station_id=<id>
    private double elevInFeet;
    private String county;
    private String riverBasin;
    private String latitude; // of the format 37.940000N
    private String longitude;
    // fields for each station from
    // http://cdec.water.ca.gov/cgi-progs/profile?s=<id>&type=res
    private String avgJan;
    private String avgFeb;
    private String avgMar;
    private String avgApr;
    private String avgMay;
    private String avgJun;
    private String avgJul;
    private String avgAug;
    private String avgSep;
    private String avgOct;
    private String avgNov;
    private String avgDec;

    public void setCounty(String s)
    {
        this.county = s;
    }

    public void setRiverBasin(String s)
    {
        this.riverBasin = s;
    }

    public void setLatitude(String s)
    {
        this.latitude = s;
    }

    public void setLongitude(String s)
    {
        this.longitude = s;
    }

    public void setAvgJan(String s)
    {
        this.avgJan = s;
    }

    public void setAvgFeb(String s)
    {
        this.avgFeb = s;
    }

    public void setAvgMar(String s)
    {
        this.avgMar = s;
    }

    public void setAvgApr(String s)
    {
        this.avgApr = s;
    }

    public void setAvgMay(String s)
    {
        this.avgMay = s;
    }

    public void setAvgJun(String s)
    {
        this.avgJun = s;
    }

    public void setAvgJul(String s)
    {
        this.avgJul = s;
    }

    public void setAvgAug(String s)
    {
        this.avgAug = s;
    }

    public void setAvgSep(String s)
    {
        this.avgSep = s;
    }

    public void setAvgOct(String s)
    {
        this.avgOct = s;
    }

    public void setAvgNov(String s)
    {
        this.avgNov = s;
    }

    public void setAvgDec(String s)
    {
        this.avgDec = s;
    }

    public static Station initStation(String id, 
                                      String dam, 
                                      String lake, 
                                      String stream,
                                      String capacity) {

        Station stn = new Station();
        stn.id = id;
        stn.dam = dam;
        stn.stream = stream;
        stn.capacity = capacity;
        return stn;
    }

    public void updateStationAverages(String state) {
        String station_info_url = 
            "http://cdec.water.ca.gov/cgi-progs/profile?s=" + this.id + "&type=res";

        try {
            Document doc = Jsoup.connect(station_info_url).get();
            Elements tableElements = doc.select("table");

            Elements tableRowElements = tableElements.select(":not(thead) tr");

            for (int k = 0; k < tableRowElements.size(); k++) {
                int lat = 0, lon = 0;
                Element row = tableRowElements.get(k);
                Elements rowItems = row.select("td");
                for (int j = 0; j < rowItems.size(); j++) {
                    switch(rowItems.get(j).text()) {
                        case "January":
                            this.avgJan = rowItems.get(j+1).text();
                            break;
                        case "February":
                            this.avgFeb = rowItems.get(j+1).text();
                            break;
                        case "March":
                            this.avgMar = rowItems.get(j+1).text();
                            break;
                        case "April":
                            this.avgApr = rowItems.get(j+1).text();
                            break;
                        case "May":
                            this.avgMay = rowItems.get(j+1).text();
                            break;
                        case "June":
                            this.avgJun = rowItems.get(j+1).text();
                            break;
                        case "July":
                            this.avgJul = rowItems.get(j+1).text();
                            break;
                        case "August":
                            this.avgAug = rowItems.get(j+1).text();
                            break;
                        case "September":
                            this.avgSep = rowItems.get(j+1).text();
                            break;
                        case "October":
                            this.avgOct = rowItems.get(j+1).text();
                            break;
                        case "November":
                            this.avgNov = rowItems.get(j+1).text();
                            break;
                        case "December":
                            this.avgDec = rowItems.get(j+1).text();
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        //out.println(" Jan Avg =  " + this.avgJan + " Feb Avg =  " + this.avgFeb);
    }

    public void updateStationLocation(String state) {
        //System.out.println("Getting location for the station: " + this.id);
        String station_info_url = 
            "http://cdec.water.ca.gov/cgi-progs/stationInfo?station_id=";

        station_info_url = station_info_url + this.id;

        try {
            Document doc = Jsoup.connect(station_info_url).get();
            Elements tableElements = doc.select("table");

            Elements tableRowElements = tableElements.select(":not(thead) tr");

            for (int k = 0; k < tableRowElements.size(); k++) {
                int lat = 0, lon = 0;
                Element row = tableRowElements.get(k);
                Elements rowItems = row.select("td");
                for (int j = 0; j < rowItems.size(); j++) {
                    if (rowItems.get(j).text().equals("Latitude")) {
                        lat = 1;
                        continue;
                    }
                    if (rowItems.get(j).text().equals("Longitude")) {
                        lon = 1;
                        continue;
                    }
                    if (lat == 1) {
                        lat = 0;
                        this.latitude = rowItems.get(j).text();
                    }
                    if (lon == 1) {
                        this.longitude = rowItems.get(j).text();
                        lon = 0;
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        //System.out.println(" Lat " + this.latitude + " Lon " + this.longitude);
    }

    public boolean isStationId(String id)
    {
        return this.id.equals(id);
    }

    public String id()
    {
        return id;
    }

    public String dam()
    {
        return dam;
    }

    public String lake()
    {
        return lake;
    }

    public String stream()
    {
        return stream;
    }

    public String capacity()
    {
        return capacity;
    }

    public String elevation()
    {
        return Double.toString(elevInFeet);
    }

    public String county()
    {
        return county;
    }

    public String riverBasin()
    {
        return riverBasin;
    }

    public String latitude()
    {
        return latitude;
    }

    public String longitude()
    {
        return longitude;
    }

    public String avgJan()
    {
        return avgJan;
    }

    public String avgFeb()
    {
        return avgFeb;
    }

    public String avgMar()
    {
        return avgMar;
    }

    public String avgApr()
    {
        return avgApr;
    }

    public String avgMay()
    {
        return avgMay;
    }

    public String avgJun()
    {
        return avgJun;
    }

    public String avgJul()
    {
        return avgJul;
    }

    public String avgAug()
    {
        return avgAug;
    }

    public String avgSep()
    {
        return avgSep;
    }

    public String avgOct()
    {
        return avgOct;
    }

    public String avgNov()
    {
        return avgNov;
    }

    public String avgDec()
    {
        return avgDec;
    }

    public static float googleLatitude(String s)
    {
        if (s != null && (s.length() > 2)) {
            int l = s.length();
            String lat = s.substring(0, l-2);
            return Float.parseFloat(lat);
        } else {
            // Some stations do not have lat/long filled
            // Do not add markers for them.
            // Check for return value from this API
            return Float.parseFloat("0.0");
        }
    }

    public static float googleLongitude(String s)
    {
        if (s != null && (s.length() > 2)) {
            int l = s.length();
            String lon = s.substring(0, l-2);
            return -(Float.parseFloat(lon));
        } else {
            // Some stations do not have lat/long filled
            // Do not add markers for them.
            // Check for return value from this API
            return Float.parseFloat("0.0");
        }
    }

    public String dumpStation()
    {
        /*
        System.out.println("id = " + this.id);
        System.out.println("dam= " + this.dam);
        System.out.println("lake= " + this.lake);
        System.out.println("stream= " + this.stream);
        System.out.println("capacity= " + this.capacity);
        System.out.println("elevation= " + this.elevInFeet);
        System.out.println("county= " + this.county);
        System.out.println("river= " + this.riverBasin);
        System.out.println("latitude= " + this.latitude);
        System.out.println("longitude= " + this.longitude);
        */
        String s;
        s = "id = " + this.id + "dam= " + this.dam + "lake= " + this.lake + "stream= ";
        return s;

    }

    public String getStationInJSON()
    {
        /*
        System.out.println("id = " + this.id);
        System.out.println("dam= " + this.dam);
        System.out.println("lake= " + this.lake);
        System.out.println("stream= " + this.stream);
        System.out.println("capacity= " + this.capacity);
        System.out.println("elevation= " + this.elevInFeet);
        System.out.println("county= " + this.county);
        System.out.println("river= " + this.riverBasin);
        System.out.println("latitude= " + this.latitude);
        System.out.println("longitude= " + this.longitude);
        */
        String s;
        s = "id = " + this.id + "dam= " + this.dam + "lake= " + this.lake + "stream= ";
        return s;

    }


}
