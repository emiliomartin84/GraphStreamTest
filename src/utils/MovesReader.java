package utils;

import com.jhlabs.map.proj.MercatorProjection;
import com.jhlabs.map.proj.TransverseMercatorProjection;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
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
    protected static final String styleSheet = "node {\n" +
            "shape: box;" +
            "size: 15px, 15px;" +
            "text-color: red; " +
            "text-size: 40; " +
            "text-style: bold; " +
            "text-background-mode: rounded-box; " +
            "text-background-color: #222C; " +
            "text-padding: 5px, 4px; " +
            "text-offset: 0px, 5px;" +
            "}";

    private static boolean reading_demand;
    private static boolean reading_services;
    private static boolean reading_depot;
    private BufferedReader reader;


    private static Vector<Point2D> vector;  public MovesReader(String path, String file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path+file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public static Graph loadFile(String path)
    {
          return null;
    }
    public static void main (String args[])
    {
        Graph g = new DefaultGraph("g");
        g.addAttribute("ui.stylesheet", styleSheet);
        g.addAttribute("ui.quality");
        g.addAttribute("ui.antialias");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/emilio/Desktop/GraphStreamTest/files/export_lunes.txt"));
            String strLine;
            vector = new Vector<Point2D>();
            try {
                while ((strLine = reader.readLine()) != null)   {
                    // Print the content on the console
                    proccesLine (g, strLine);
                    System.out.println(strLine);
                }
                writeImage();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        g.display(false);
    }

    private static void proccesLine(Graph g, String strLine) {
        //To change body of created methods use File | Settings | File Templates.

        if ( strLine.startsWith("--------")){
            reading_services = true;
        }else if (reading_services)
        {
            String [] datos = strLine.split(",");

            String id = datos[1];
            double lat = Double.parseDouble(datos[8]);
            Double longi = Double.parseDouble(datos[9]);
            Point2D p = new Point2D.Double();
            p.setLocation(lat,longi);
            lat *= Math.PI /180.0;
            longi *= Math.PI /180.0;
            TransverseMercatorProjection projection = new TransverseMercatorProjection();
            Point2D.Double punto = projection.project(lat, longi, new Point2D.Double());

            vector.add(p);
            try{
            g.addNode(id);
            g.getNode(id).setAttribute("xyz",punto.y,punto.x,0);
            }catch (Exception e)
            {

            }
            //if(!id.equals("1"))
              //  g.addEdge("1"+id,"1",id);
        }
    }

    private static  void writeImage()
    {
        try {
            String url = "http://maps.google.com/maps/api/staticmap?&size=512x512&maptype=roadmap&sensor=false";

            for(int i=0;i<vector.size();i++)
            {
                if(url.length()<2000)
               url+= "&markers=color:green%7Clabel:G%7C" +
                       String.valueOf(vector.get(i).getX())+","+String.valueOf(vector.get(i).getY());
            }


           // BufferedImage img = ImageIO.read(new URL("http://maps.googleapis.com/maps/api/staticmap?center=New+York,NY&zoom=13&size=600x300&key=AIzaSyCQXIE3yBRjyhTHjvZYNsJJKn2mfWxub_M"));

           // File outputfile = new File("map.png");
            //ImageIO.write(img, "png", outputfile);
            //System.out.println("Saved!");
            String [] aux = {url, "http://maps.googleapis.com/maps/api/staticmap?center=New+York,NY&zoom=13&sensor=false&size=600x300&key=AIzaSyCQXIE3yBRjyhTHjvZYNsJJKn2mfWxub_M"};
            navega(aux);

        } catch (Exception ex) {
            System.out.println("Error!" + ex);
        }
    }


    public static void navega(String [] args) {

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
    private void proccesLine()
    {

    }
}
