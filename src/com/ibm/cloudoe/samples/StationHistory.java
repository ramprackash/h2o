package com.ibm.cloudoe.samples;
import java.io.*;
import java.util.*;
import java.text.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mongodb.*;
import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONArray;

//public class StationHistory implements Runnable {
public class StationHistory {
    // Gathers historical data for a station using
    // http://cdec.water.ca.gov/cgi-progs/queryMonthly?<id>&d=<date in dd-Mon-yyyy+hh:mm>&span=10years
    // ex: http://cdec.water.ca.gov/cgi-progs/queryMonthly?APN&d=16-Apr-2014+18:17&span=10years
    // Creates a table "StationHistory" in mongodb with key id-year-month
    // containing reservoir level data. Monthly average is already in "Station"
    // object
    // KEY: Start
    private String id;
    private String year;
    private String month;
    // KEY: End
    private String level;
    
    private String state;
    private MongoClient mongo;
    private String histURL;

    public String id() {
        return this.id;
    }

    public String year() {
        return this.year;
    }

    public String month() {
        return this.month;
    }

    public String level() {
        return this.level;
    }

    public String initStationHistory(Stations stns, String in_state, String in_id) {
        String dbgString;
        //switch(state) {
        //    case "CA":
                Calendar c = new GregorianCalendar();
                Date d1 = c.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy+hh:mm");
               stns.debugString = this.histURL = "http://cdec.water.ca.gov/cgi-progs/queryMonthly?" + in_id + "&d=" + sdf.format(d1) + "&span=10years";
         //       break;
         //   default:
         //       break;
        //}
        dbgString = this.histURL;
        this.id = in_id;
        this.state = in_state;
        this.mongo = stns.mongo();
        try {
            Document htmldoc = Jsoup.connect(this.histURL).get();
            Elements tableElements = htmldoc.select("table");
            //dbgString = tableElements.toString();
            Elements tableRowElements = tableElements.select("tr");
            for (int k = 2; k < tableRowElements.size(); k++) {
                Element row = tableRowElements.get(k);
                Elements rowItems = row.select("td");
                String date = null, level = null, month = null, year = null;
                for (int j = 0; j < rowItems.size(); j++) {
                    switch(j) {
                        case 0:
                            date = rowItems.get(j).text();
                            month = date.substring(0, 2);
                            year = date.substring(3, 7);
                            break;
                        case 2:
                            level = rowItems.get(j).text();
                            break;
                    }
                }
                if (year == null) {
                    continue;
                }

                DB db;
                db = this.mongo.getDB("db");
                DBCollection stationHistTable = db.getCollection("stationHistory");
                BasicDBObject doc = new BasicDBObject();
                BasicDBObject q   = new BasicDBObject();

                // query
                q.put("id", id);
                q.put("year", year);
                q.put("month", month);

                // update record
                doc.put("id", id);
                doc.put("year", year);
                doc.put("month", month);
                doc.put("level", level);

                // call update with upsert = true
                stationHistTable.update(q, doc, true, false);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return dbgString;
    }

    // if id==0, then dump all records
    // if id==valid && year==0 dump all years for id
    // if id==valid && year==valid dump all months for id & year
    // if id==valid && year==valid && month==valid dump the exact record
    public static ArrayList<StationHistory> getStationHistory(Stations stns, 
            String id,
            String year,
            String month) {

        DB db;
        db = stns.mongo().getDB("db");
        DBCollection stationHistTable = db.getCollection("stationHistory");
        BasicDBObject doc = new BasicDBObject();
        BasicDBObject q   = new BasicDBObject();

        // query
        if (id != null) {
            q.put("id", id);
        }
        if (year != null) {
            q.put("year", year);
        }
        if (month != null) {
            q.put("month", month);
        }

        DBCursor c = stationHistTable.find(q);
        ArrayList<StationHistory> arrayOfStnHist = 
            new ArrayList<StationHistory>();
        try {
            while (c.hasNext()) {
                DBObject o = c.next();
                StationHistory sh = new StationHistory();
                sh.id = (String)o.get("id");
                sh.year = (String)o.get("year");
                sh.month = (String)o.get("month");
                sh.level = (String)o.get("level");
                arrayOfStnHist.add(sh);
            } 
        } finally {
            c.close();
        }

        return arrayOfStnHist;
    }

    public static void dumpStationHistoryDB(Stations stns, PrintWriter out) {
        DB db;
        db = stns.mongo().getDB("db");
        DBCollection stationHistTable = db.getCollection("stationHistory");
        DBCursor cursor = stationHistTable.find();
        try {
            while(cursor.hasNext()) {
                DBObject o = cursor.next();
                String id = (String) o.get("id");
                String janAvg = (String) o.get("year");
                String febAvg = (String) o.get("month");
                String marAvg = (String) o.get("level");
                out.println("Id = " + id + " year = " + janAvg + " month = " + febAvg + " level = " + marAvg + "<p>");
            }
        } finally {
            cursor.close();
        }
    }

    // if id==0, then dump all records
    // if id==valid && year==0 dump all years for id
    // if id==valid && year==valid dump all months for id & year
    // if id==valid && year==valid && month==valid dump the exact record
    public static String getStationHistoryInJSON(Stations stns, 
            String id,
            String year,
            String month,
            PrintWriter out) {

        DB db;
        db = stns.mongo().getDB("db");
        DBCollection stationHistTable = db.getCollection("stationHistory");
        BasicDBObject doc = new BasicDBObject();
        BasicDBObject q   = new BasicDBObject();
        JSONArray arr = new JSONArray();
        String jsonText = null;

        // query
        if (id != null) {
            q.put("id", id);
        }
        if (year != null) {
            q.put("year", year);
        }
        if (month != null) {
            q.put("month", month);
        }

        DBCursor c = stationHistTable.find(q);
        ArrayList<StationHistory> arrayOfStnHist = 
            new ArrayList<StationHistory>();
        try {
            while (c.hasNext()) {
                DBObject o = c.next();
                StationHistory sh = new StationHistory();
                JSONObject obj = new JSONObject();
                sh.id = (String)o.get("id");
                sh.year = (String)o.get("year");
                sh.month = (String)o.get("month");
                sh.level = (String)o.get("level");
                obj.put("id", new String(sh.id));
                obj.put("year", new String(sh.year));
                //obj.put("level", Double.parseDouble(sh.level));
                if (sh.level.matches("[0-9]+") && sh.level.length() >= 1) {
                    obj.put("level", Double.parseDouble(sh.level));
                } else {
                    continue;
                }
                arr.add(obj);
                arrayOfStnHist.add(sh);
            } 
            jsonText = arr.toString();
            out.println(jsonText);
        }
        catch (NumberFormatException e ) {
            out.println("Numberformat exception");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            c.close();
        }

        return jsonText;
    }
}
