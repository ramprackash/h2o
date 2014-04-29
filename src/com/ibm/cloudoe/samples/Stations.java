package com.ibm.cloudoe.samples; 
import java.io.*;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mongodb.*;
//import org.cloudfoundry.runtime.env.*;

public class Stations {

    ArrayList<Station> stationArray;
    private int initStatus;
    public int initProgress;
    public String debugString;
    private String state;
    private MongoClient mongo;

    /*
    public static void main(String[] args) 
    {
        System.out.println("Getting station info");
        Stations caStations = initStations("CA");
        System.out.println("Done getting station info");
        if (caStations == null) {
            System.out.println("Error retrieving Station Info");
        }
        for (Station st : caStations.stationArray) {
            if (st.isStationId("KLM")) {
                st.dumpStation();
            }
        }
    }

    private static int getStationsCount(String all_stations_url) {
        try {
            Document doc = Jsoup.connect(all_stations_url).get();
            Elements tableElements = doc.select("table");
            Elements tableRowElements = 
                tableElements.select(":not(thead) tr");
            return tableRowElements.size();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    */
    public MongoClient mongo()
    {
        return(this.mongo);
    }
    public int initProgress()
    {
        return(this.initProgress);
    }

    public boolean isInitDone()
    {
        return(this.initStatus == 2);
    }

    public boolean isInitStarted()
    {
        return(this.initStatus == 1);
    }

    private void updateStationsFromDB() 
    {
        DB db = this.mongo.getDB("db");
        DBCollection stationTable = db.getCollection("stations");
        DBCursor cursor = stationTable.find();
        this.stationArray = new ArrayList<Station>();
        try {
            while(cursor.hasNext()) {
                DBObject o = cursor.next();
                String id = (String) o.get("id");
                String dam = (String) o.get("dam");
                String lake = (String) o.get("lake");
                String stream = (String) o.get("stream");
                String capacity = (String) o.get("capacity");
                String lat    = (String) o.get("latitude");
                String lon    = (String) o.get("longitude");
                String county = (String)o.get("county");
                String riverBasin = (String)o.get("riverBasin");
                String latitude = (String)o.get("latitude");
                String longitude = (String)o.get("longitude");
                String avgJan = (String)o.get("avgJan");
                String avgFeb = (String)o.get("avgFeb");
                String avgMar = (String)o.get("avgMar");
                String avgApr = (String)o.get("avgApr");
                String avgMay = (String)o.get("avgMay");
                String avgJun = (String)o.get("avgJun");
                String avgJul = (String)o.get("avgJul");
                String avgAug = (String)o.get("avgAug");
                String avgSep = (String)o.get("avgSep");
                String avgOct = (String)o.get("avgOct");
                String avgNov = (String)o.get("avgNov");
                String avgDec = (String)o.get("avgDec");

                Station stn = Station.initStation(id, dam, lake,
                                                  stream, capacity);
                stn.setLatitude(lat);
                stn.setLongitude(lon);
                stn.setAvgJan(avgJan);
                stn.setAvgFeb(avgFeb);
                stn.setAvgMar(avgMar);
                stn.setAvgApr(avgApr);
                stn.setAvgMay(avgMay);
                stn.setAvgJun(avgJun);
                stn.setAvgJul(avgJul);
                stn.setAvgAug(avgAug);
                stn.setAvgSep(avgSep);
                stn.setAvgOct(avgOct);
                stn.setAvgNov(avgNov);
                stn.setAvgDec(avgDec);

                try {
                    this.stationArray.add(stn);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            cursor.close();
        }
    }

    public void initStations(String state, boolean forceRefresh)
    {
        String all_stations_url;
        String connURL;
        DB db;

        if (this.initStatus == 2)
            return;

        if (this.initStatus == 0)
            this.initStatus = 1;

        try {
            connURL = getServiceURI();
            this.mongo = new MongoClient(new MongoClientURI(connURL));
            db = this.mongo.getDB("db");
            boolean stationTableExists = db.collectionExists("stations");
            boolean stationHistoryTableExists = db.collectionExists("stationHistory");
            if (stationHistoryTableExists && stationTableExists && !forceRefresh) {
                updateStationsFromDB();
                this.initStatus = 2;
                return;
            }
        } catch (Exception e) {
                this.initProgress = 999;
            e.printStackTrace();
        }

        //out.println("Trying resinfo <p>");
        //TODO: Have a dictionary of States => stationinfo urls
        if (state.equals("CA")) {
                this.initProgress = 222;
            all_stations_url = 
                "http://cdec.water.ca.gov/misc/resinfo.html";
        }
        else {
            return;
        }
        this.stationArray = new ArrayList<Station>();
        try {
            Document doc = Jsoup.connect(all_stations_url).get();
            Elements tableElements = doc.select("table");
            Elements tableRowElements = 
            tableElements.select(":not(thead) tr");

            //out.println("Retrieved resinfo <p>");
            ArrayList<Thread> locThreads = new ArrayList<Thread>();
            ArrayList<Thread> avgThreads = new ArrayList<Thread>();

            for (int k = 1; k < tableRowElements.size(); k++) {
                int i = k - 1;
                String id = "", dam = "", lake = "";
                String stream = "", capacity = "";
                Element row = tableRowElements.get(k);
                Elements rowItems = row.select("td");
                for (int j = 0; j < rowItems.size(); j++) {

                    switch(j) {
                        case 0:
                            id = rowItems.get(j).text();
                            break;
                        case 1:
                            dam = rowItems.get(j).text();
                            break;
                        case 2:
                            lake = rowItems.get(j).text();
                            break;
                        case 3:
                            stream = rowItems.get(j).text();
                            break;
                        case 4:
                            capacity = rowItems.get(j).text();
                            break;

                        default:
                            //out.println("Unknown data in table");
                            return;
                    }

                }
                Station stn = Station.initStation(id, dam, lake,
                        stream, capacity);
                try {
                    this.stationArray.add(stn);
                    //Thread locThread = new Thread(stn);
                    //locThread.start();
                    //locThreads.add(locThread);
                    //Thread avgThread = stn.launchAvgThread();
                    //avgThreads.add(avgThread);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                //out.println("Getting location <p>");
                stn.updateStationLocation(state);
                //out.println("Getting Averages <p>");
                stn.updateStationAverages(state);
                this.initProgress = k;
            }
            for (int i = 0; i < locThreads.size(); i++)
            {
                try {
                    this.initProgress += i;
                    ((Thread)locThreads.get(i)).join();
                } catch (InterruptedException e) {
                    System.out.println("Main thread Interrupted");
                }
            }
            for (int i = 0; i < avgThreads.size(); i++)
            {
                try {
                    this.initProgress += i;
                    ((Thread)avgThreads.get(i)).join();
                } catch (InterruptedException e) {
                    System.out.println("Main thread Interrupted");
                }
            }
        } catch (IOException e) {
            this.initProgress = 333;
            e.printStackTrace();
        }
        //out.println("Done getting stations info for : " + 
                           //this.stationArray.size());
        this.createDBEntries(state);
        this.initStatus = 2;
    }

    private static String getServiceURI() throws Exception 
    {
        String jdbcurl = null;
        String envServices = null;
        String vcapServices = System.getenv("VCAP_SERVICES");
        envServices = 
            vcapServices.substring(vcapServices.indexOf("url"));
        jdbcurl = 
            envServices.substring(envServices.indexOf("mongodb"));
        jdbcurl = 
            jdbcurl.substring(jdbcurl.indexOf("mon"), 
                              jdbcurl.lastIndexOf('\"'));
        return jdbcurl;
    }

    private void createDBEntries(String state)
    {
        int i = 0;
        DB db;
        db = this.mongo.getDB("db");
        DBCollection stationTable = db.getCollection("stations");
        this.initProgress = 5000;
        for (Station st : this.stationArray) {
            i++;
            BasicDBObject doc = new BasicDBObject();
            BasicDBObject q   = new BasicDBObject();

            // query
            q.put("id", st.id());

            // update
            doc.put("id", st.id());
            doc.put("dam", st.dam());
            doc.put("lake", st.lake());
            doc.put("stream", st.stream());
            doc.put("capacity", st.capacity());
            doc.put("elevInFeet", st.elevation());
            doc.put("county", st.county());
            doc.put("riverBasin", st.riverBasin());
            doc.put("latitude", st.latitude()); 
            doc.put("longitude", st.longitude());
            doc.put("avgJan", st.avgJan());
            doc.put("avgFeb", st.avgFeb());
            doc.put("avgMar", st.avgMar());
            doc.put("avgApr", st.avgApr());
            doc.put("avgMay", st.avgMay());
            doc.put("avgJun", st.avgJun());
            doc.put("avgJul", st.avgJul());
            doc.put("avgAug", st.avgAug());
            doc.put("avgSep", st.avgSep());
            doc.put("avgOct", st.avgOct());
            doc.put("avgNov", st.avgNov());
            doc.put("avgDec", st.avgDec());

            stationTable.update(q, doc, true, false);

            StationHistory sh = new StationHistory();
            this.debugString = sh.initStationHistory(this, state, st.id());
            this.initProgress += i;
        }
    }

    public String debugString()
    {
        return this.debugString;
    }

    public void dumpDBEntries(PrintWriter out)
    {
        DB db;
        db = this.mongo.getDB("db");
        DBCollection stationTable = db.getCollection("stations");
        DBCursor cursor = stationTable.find();
        try {
        while(cursor.hasNext()) {
            DBObject o = cursor.next();
            String id = (String) o.get("id");
            String janAvg = (String) o.get("avgJan");
            String febAvg = (String) o.get("avgFeb");
            String marAvg = (String) o.get("avgMar");
            String aprAvg = (String) o.get("avgApr");
            String lat    = (String) o.get("latitude");
            String lon    = (String) o.get("longitude");
            out.println("Id = " + id + " jan = " + janAvg + " feb = " + febAvg + " mar = " + marAvg + " apr = " + aprAvg + " lat = " + Station.googleLatitude(lat) + " lon = " + Station.googleLongitude(lon));
            out.println("<p>");
        }
        } finally {
            cursor.close();
        }
    }

    public String dumpStation(String id)
    {
        for (Station st : this.stationArray) {
            if (st.isStationId(id)) {
                return st.dumpStation();
            }
        }

        return "Not Found";
    }
}
