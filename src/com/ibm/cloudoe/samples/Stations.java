package com.ibm.cloudoe.samples; 

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.servlet.*;
import javax.servlet.http.*;


import java.io.*;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mongodb.*;
import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONArray;
//import org.json.simple.JSONObject;
//import org.cloudfoundry.runtime.env.*;

@Path(value="/stations")
public class Stations extends HttpServlet {

    public static Stations caStations = null;

    ArrayList<Station> stationArray;
    private int initStatus;
    private int initProgress;
    public String debugString;
    private String state;
    private MongoClient mongo;

    public static Stations getStationsObject()
    {
        return caStations;
    }
    public MongoClient mongo()
    {
        return(this.mongo);
    }
    public int initProgress()
    {
        return(this.initProgress);
    }
    public void setInitProgress(int i)
    {
        this.initProgress = i;
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

    public void initStations(String state, boolean forceRefresh, PrintWriter out)
    {
        String all_stations_url;
        String connURL;
        DB db;

        if ((this.initStatus >= 2) && (forceRefresh == false)) {
            // Nothing to do
            return;
        }

        if ((this.initStatus == 1) && (forceRefresh == false)) {
            // An init is already in progress
            return;
        }

        if (this.initStatus == 0)
            this.initStatus = 1;

        try {
            connURL = getServiceURI();
            if (this.mongo == null) {
                this.mongo = new MongoClient(new MongoClientURI(connURL));
            }
            db = this.mongo.getDB("db");
            boolean stationTableExists = db.collectionExists("stations");
            boolean stationHistoryTableExists = db.collectionExists("stationHistory");
            if (stationHistoryTableExists && stationTableExists && !forceRefresh) {
                updateStationsFromDB();
                this.initStatus = 3;
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //out.println("Trying resinfo <p>");
        //TODO: Have a dictionary of States => stationinfo urls
        if (state.equals("CA")) {
            this.initStatus = 1;
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
                    Thread locThread = stn.launchLocThread(state);
                    locThreads.add(locThread);
                    Thread avgThread = stn.launchAvgThread(state);
                    avgThreads.add(avgThread);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
            for (int i = 0; i < locThreads.size(); i++)
            {
                try {
                    ((Thread)locThreads.get(i)).join();
                } catch (InterruptedException e) {
                    System.out.println("Main thread Interrupted");
                }
            }
            for (int i = 0; i < avgThreads.size(); i++)
            {
                try {
                    ((Thread)avgThreads.get(i)).join();
                    this.initProgress += i;
                } catch (InterruptedException e) {
                    System.out.println("Main thread Interrupted");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //out.println("Done getting stations info for : " + 
                           //this.stationArray.size());
        this.createDBEntries(state, out);
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

    private void createDBEntries(String state, PrintWriter out)
    {
        int i = 0;
        DB db;
        try {
            db = this.mongo.getDB("db");
            DBCollection stationTable = db.getCollection("stations");
            this.setInitProgress(0);
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

                this.setInitProgress(i);
                stationTable.update(q, doc, true, false);

                StationHistory sh = new StationHistory();
                this.debugString = sh.initStationHistory(this, state, st.id());
            }
        } catch (Exception e) {
            e.printStackTrace(out);
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

    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String init = request.getParameter("initdb");
        String initstatus = request.getParameter("initstatus");

        if (init != null && init.equals("1")) {
            if (caStations == null) {
                caStations = new Stations();
            }
            JSONObject obj=new JSONObject();
            try {
                obj.put("Refresh", "1");

                out.write(obj.toString());
                // Force refresh for DB
                caStations.initStations("CA", true, out);
                return;
            } catch (Exception e) {
                e.printStackTrace(out);
                return;
            }
        }

        if (initstatus != null && initstatus.equals("1")) {
            try {
                JSONObject obj1=new JSONObject();

                if (caStations == null) {
                    caStations = new Stations();
                    caStations.initStations("CA", false, out);
                }

                obj1.put("initstatus", new Double(caStations.initStatus));
                obj1.put("progress", new Double(caStations.initProgress()));
                if (caStations.debugString == null) {
                    obj1.put("url", new String("NULL"));
                } else {
                    obj1.put("url", new String(caStations.debugString));
                }
                out.write(obj1.toString());
                return;
            }
            catch (Exception e) {
                e.printStackTrace(out);
                return;
            }
        }

        if (caStations == null) {
            caStations = new Stations();
            caStations.initStations("CA", false, out);
        }

        String st = caStations.getStationsInJSON(out);
        out.write(st);
        //out.println(st);
        out.close();
    }


    public String getStationsInJSON(PrintWriter out)
    {
        DB db = null;
        try {
            if (this.mongo != null) {
                db = this.mongo.getDB("db");
            } else {
                out.println("mongo is null. Initializing it now"); 
                String connURL = getServiceURI();
                this.mongo = new MongoClient(new MongoClientURI(connURL));
                db = this.mongo.getDB("db");
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        DBCollection stationTable = db.getCollection("stations");
        DBCursor cursor = stationTable.find();
        JSONArray arr=new JSONArray();
        String jsonText = null;

        try {
            while(cursor.hasNext()) {
                JSONObject obj=new JSONObject();
                DBObject o = cursor.next();
                String lat    = (String) o.get("latitude");
                String lon    = (String) o.get("longitude");
                Double lt = new Double(Station.googleLatitude(lat));
                Double ln = new Double(Station.googleLongitude(lon));
                int donotadd = 0;
                if (lt == 0.0 || ln == 0.0) {
                    // skip adding unknown reservoirs
                    donotadd = 1;
                } else {
                    // If there is no historical data for a reservoir, skip
                    // adding that to the map too. No use showing it...
                    ArrayList<StationHistory> sha = 
                        StationHistory.getStationHistory(caStations, 
                                (String)o.get("id"), null, null);
                    int count = 0;
                    for (StationHistory st : sha) {
                        count++;
                        if (new Double(st.level()) != 0) {
                            donotadd = 0;
                            break;
                        }
                    }
                    if (count == sha.size()) {
                        donotadd = 1;
                    }
                }
                if (donotadd == 0) {
                    obj.put("id", (String)o.get("id"));
                    obj.put("dam", (String)o.get("dam"));
                    obj.put("lat", lt);
                    obj.put("lon", ln);
                    obj.put("Jan", (String)o.get("avgJan"));
                    obj.put("Feb", (String)o.get("avgFeb"));
                    obj.put("Mar", (String)o.get("avgMar"));
                    obj.put("Apr", (String)o.get("avgApr"));
                    obj.put("May", (String)o.get("avgMay"));
                    obj.put("Jun", (String)o.get("avgJun"));
                    obj.put("Jul", (String)o.get("avgJul"));
                    obj.put("Aug", (String)o.get("avgAug"));
                    obj.put("Sep", (String)o.get("avgSep"));
                    obj.put("Oct", (String)o.get("avgOct"));
                    obj.put("Nov", (String)o.get("avgNov"));
                    obj.put("Dec", (String)o.get("avgDec"));
                    arr.add(obj);
                } else {
                }
            }
            jsonText = arr.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        if (jsonText == null) {
            // Catchall - if nothing, return empty json array
            return "[]";
        }
        return jsonText;
    }
}
