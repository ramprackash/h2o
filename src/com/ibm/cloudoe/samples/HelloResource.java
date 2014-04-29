package com.ibm.cloudoe.samples;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import java.io.*;
import java.util.*;
import java.text.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.wink.json4j.JSONObject;

//This class define the /hello RESTful API to fetch all system environment information.

//@Path("/hello")
public class HelloResource extends HttpServlet {
    static Stations caStations = null;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String button = request.getParameter("RESET");

        //initDB(out);
        if (button != null) {
            if (caStations != null) {
                if (caStations.isInitDone()) {
                    out.println("Init is done." + caStations.initProgress() + " Trying to dump DB<p>");
                    out.println("Init Progress: " + caStations.initProgress() + " URL: " + caStations.debugString() + "<p>");
                    caStations.dumpDBEntries(out);
                    StationHistory.dumpStationHistoryDB(caStations, out);
                    /*
                    ArrayList<StationHistory> sh =
                        StationHistory.getStationHistory(caStations, "ANT", 
                                                         "2009", "02");
                    for (StationHistory st : sh) {
                        out.println("Id: " + st.id() +
                                    " Year: " + st.year() +
                                    " Month: " + st.month() +
                                    " Level: " + st.level() + "<p>");
                    }
                    */
                }
                else {
                    out.println("Database still being populated <p>");
                    out.println("Init Progress: " + caStations.initProgress() + " URL: " + caStations.debugString() + "<p>");
                }
            }
        } else {
            // "Add this note" button has been clicked

            //out.println("<title>Example</title>" +
            //        "<body bgcolor=FFFFFF>");
            //out.println("<h2>Button Clicked</h2>");

            String DATA = request.getParameter("DATA");
            DATA = DATA.replaceAll("'", "''");

            //if(DATA != null){
            //    out.println(DATA);
            //} else {
            //    out.println("No text entered.");
            //}

            //addNoteToDB(DATA, out);

            //out.println("<P>Return to <A HREF=\"index.html\">Form</A>");
        }
        doGet(request, response);
        out.close();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>MongoDB of Reservoirs</title>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> <link rel=\"stylesheet\" href=\"style.css\" /> </head>");

        out.println("<body><center>");
        out.println("<h1>Reservoirs of CA</h1>");

        out.println("<TABLE BORDER=\"2\" CELLPADDING=\"2\">");
        out.println("    <TR><TD WIDTH=\"275\" ALIGN=\"center\">");
        out.println("            Pressing the button will dump the list of reservoirs that are present in the stations table of MongoDB");
        out.println("            <FORM METHOD=\"POST\" ACTION=\"HelloResource\"> ");
        //out.println("            <INPUT TYPE=\"TEXT\" NAME=\"DATA\" SIZE=30> ");
        out.println("                <P> ");
        //out.println("                <INPUT TYPE=\"SUBMIT\" VALUE=\"Add this note\">");
        out.println("                <INPUT TYPE=\"SUBMIT\" NAME=\"RESET\" VALUE=\"List all reservoir stations\">");
        out.println("            </FORM> ");
        out.println("    </TD></TR> ");
        out.println("</TABLE>");
        out.println("<P>");
        out.println("Current Entries:");
        out.println("<TABLE BORDER=\"2\" CELLPADDING=\"2\">");
        //out.println(selectAllFromDB(out));
        out.println("</TABLE> </center>");
        out.println("</body> </html>");
        out.close();
        if (caStations == null) {
            caStations = new Stations();
            caStations.initStations("CA", false);
        }
    }


    /*
	@GET
	public String getInformation() {
            String err = "Hello  ";
		// load all system environments
		JSONObject sysEnv = new JSONObject(System.getenv());

		// 'VCAP_APPLICATION' is in JSON format, it contains useful information about a deployed application
		// String envApp = System.getenv("VCAP_APPLICATION");

		// 'VCAP_SERVICES' contains all the credentials of services bound to this application.
		// String envServices = System.getenv("VCAP_SERVICES");
		// TODO Get service credentials and communicate with BlueMix services
                if (caStations == null)
                {
                    err = err + "Calling initStations";
                    caStations = Stations.initStations("CA");
                    err = err + "... Done";
                }

                //return err;
                //return (err + caStations.dumpStation("APN"));

		return sysEnv.toString();
	}
        */
}
