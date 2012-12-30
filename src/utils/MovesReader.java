package utils;

import com.jhlabs.map.proj.MercatorProjection;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 12/11/12
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class MovesReader {
    private static int capacity;
    private static boolean reading_coordinates;
    private Graph g;
    protected static final String styleSheet = "node {\n" +
            "shape: box;" +
            "size: 5px, 5px;" +
            "text-color: red; " +
            "text-size: 40; " +
            "text-background-mode: rounded-box; " +
            "text-background-color: #222C; " +
            "text-padding: 5px, 4px; " +
            "text-offset: 0px, 5px;" +
            "}";

    private static boolean reading_demand;
    private static boolean reading_services;
    private static boolean reading_depot;
    private BufferedReader reader;
    private static Vector<Point2D> vector;

    public MovesReader(){
        setG(new DefaultGraph("g"));
        getG().addAttribute("ui.stylesheet", styleSheet);
        getG().addAttribute("ui.quality");
        getG().addAttribute("ui.antialias");

    }

    public boolean load(String file) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String strLine;
            vector = new Vector<Point2D>();
            try {
                while ((strLine = reader.readLine()) != null)   {
                    // Print the content on the console
                    proccesLine (getG(), strLine);
                    //System.out.println(strLine);
                }
                //writeImage();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }



    private static void proccesLine(Graph g, String strLine) {
        //To change body of created methods use File | Settings | File Templates.

        if ( strLine.startsWith("--------")){
            reading_services = true;
        }else if (reading_services)
        {
            //# customer, serviceReference, dayOfWeek, minHour, minMinute, maxHour, maxMinute, duration, latitude, longitude

            String [] datos = strLine.split(",");
            String id = datos[1];
            double lat = Double.parseDouble(datos[8]);
            double longi = Double.parseDouble(datos[9]);
            Point2D p = new Point2D.Double();
            p.setLocation(lat,longi);
            lat *= Math.PI /180.0;
            longi *= Math.PI /180.0;
            MercatorProjection projection = new MercatorProjection();
            Point2D.Double punto = projection.project(lat, longi, new Point2D.Double());

            vector.add(p);
            try{
                g.addNode(id);
                g.getNode(id).setAttribute("xyz",punto.y,punto.x,0);
                g.getNode(id).setAttribute("ini",datos[3]+":"+datos[4]);
                g.getNode(id).setAttribute("end",datos[5]+":"+datos[6]);
                g.getNode(id).setAttribute("duration",datos[7]);
                g.getNode(id).setAttribute("latitude",Double.parseDouble(datos[8]));
                g.getNode(id).setAttribute("longitude",Double.parseDouble(datos[9]));

            }catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }
    }

    private static  void writeImage()
    {
        try {
            int limit =2000;
            //Static google map apis doesn't allow to put more than certain number of markers.
            String url = "http://maps.google.com/maps/api/staticmap?&size=512x512&maptype=roadmap&sensor=false";

            for (Point2D aVector : vector) {
                if (url.length() < 2000)
                    url += "&markers=color:green%7Clabel:G%7C" +
                            String.valueOf(aVector.getX()) + "," + String.valueOf(aVector.getY());
            }
            String [] aux = {url};
        } catch (Exception ex) {
            System.out.println("Error!" + ex);
        }
    }


    public static void navega(String[] args) {

        if( !java.awt.Desktop.isDesktopSupported() ) {

            System.err.println( "Desktop is not supported (fatal)" );
            System.exit( 1 );
        }

        if ( args.length == 0 ) {

            System.out.println( "Usage: OpenURI [URI [URI ... ]]" );
            System.exit( 0 );
        }

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {

            System.err.println( "Desktop doesn't support the browse action (fatal)" );
            System.exit( 1 );
        }

        for ( String arg : args ) {

            try {
                java.net.URI uri = new java.net.URI( arg );
                desktop.browse( uri );
            }
            catch ( Exception e ) {

                System.err.println( e.getMessage() );
            }
        }
    }

    public Graph getG() {
        return g;
    }

    public void setG(Graph g) {
        this.g = g;
    }
}
