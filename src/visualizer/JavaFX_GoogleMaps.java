package visualizer;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 11/12/12
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Vector;

/**
 *
 * @web http://java-buddy.blogspot.com/
 */
public class JavaFX_GoogleMaps extends Application {

    private Scene scene;
    private static Vector<String []> vector;
    MyBrowser myBrowser;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("moves");


        myBrowser = new MyBrowser();
        scene = new Scene(myBrowser);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    class MyBrowser extends Region{

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        private boolean reading_services;
        /*
        public MyBrowser(){

            final URL urlGoogleMaps = getClass().getResource("html.html");
            //webEngine.loadContent(genHtml());
            webEngine.loadContent(urlGoogleMaps.toExternalForm());
            getChildren().add(webView);
        } */

        public MyBrowser(){
            read();
            generateHtml();
            final URL urlGoogleMaps = getClass().getResource("html.html");
            webEngine.load(urlGoogleMaps.toExternalForm());
            //webEngine.load(genHtml());
            getChildren().add(webView);

        }

        public void proccesLine(String strLine)
        {


        if ( strLine.startsWith("--------")){
            reading_services = true;
        }else if (reading_services)
        {
            String [] datos = strLine.split(",");

            String id = datos[1];
            double lat = Double.parseDouble(datos[8]);
            Double longi = Double.parseDouble(datos[9]);
            Point2D p = new Point2D.Double();
            vector.add(datos);
        }
        }
    public void read ()
    {
        try{

        BufferedReader reader = new BufferedReader(new FileReader("/Users/emilio/Desktop/GraphStreamTest/files/export_lunes_c11.txt"));
        String strLine;
        vector = new Vector<String[]>();
        try {
            while ((strLine = reader.readLine()) != null)   {
                // Print the content on the console
                proccesLine ( strLine);
                System.out.println(strLine);
            }

            System.out.print(vector.size());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    }


    }
    private void generateHtml()
    {
        URL resourceUrl = getClass().getResource("html.html");

        File file = null;
        try {
            file = new File(resourceUrl.toURI());
            String [] aux = {resourceUrl.toURI().toString()};
           // navega(aux);
            FileWriter writer = new FileWriter(file,false);
            BufferedWriter output = new BufferedWriter(writer);
            output.write(genHtml());
            output.flush();
            output.close();

        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    private String genHtml(){

        StringBuilder tHtml = new StringBuilder();

        tHtml.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />\n" +
                "    <style type=\"text/css\">\n" +
                "        html { height: 100% }\n" +
                "        body { height: 100%; margin: 0; padding: 0 }\n" +
                "        #map_canvas { height: 100% }\n" +
                "    </style>\n" +
                "    <script type=\"text/javascript\"\n" +
                "\n" +
                "            src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCQXIE3yBRjyhTHjvZYNsJJKn2mfWxub_M&sensor=true\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "\n" +
                "        var map;\n" +
                "function get_random_color() {\n" +
                "    var letters = '0123456789ABCDEF'.split('');\n" +
                "    var color = '';\n" +
                "    for (var i = 0; i < 6; i++ ) {\n" +
                "        color += letters[Math.round(Math.random() * 15)];\n" +
                "    }\n" +
                "    return color;\n" +
                "}" +
                "        function initialize() {\n" +
                "\n" +
                "\n" +
                "        var mapDiv = document.getElementById('map-canvas');\n" +
                "\n" +
                "\n" +
                "        var latlng = new google.maps.LatLng("+vector.get(0)[8]+","+vector.get(0)[9]+");\n" +
                "\n" +
                "        var myOptions = {\n" +
                "        zoom: 11,\n" +
                "        center: latlng,\n" +
                "        mapTypeId: google.maps.MapTypeId.ROADMAP\n" +
                "        };\n" +
                "\n" +
                "        map = new google.maps.Map(mapDiv,myOptions);\n" +
                "\n" +
                "        google.maps.event.addListenerOnce(map, 'tilesloaded', addMarkers);\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        function addMarkers() {\n" +
                "        var bounds = map.getBounds();\n" +
                "        var southWest = bounds.getSouthWest();\n" +
                "        var northEast = bounds.getNorthEast();\n" +
                "        var lngSpan = northEast.lng() - southWest.lng();\n" +
                "        var latSpan = northEast.lat() - southWest.lat();\n" +
                "\n" +
                "        var hotels = [\n");
                for(int i=0;i<vector.size();i++)
                {
                    tHtml.append(" ['"+vector.get(i)[1]+"'," +vector.get(i)[8]+","+vector.get(i)[9]+",3,get_random_color()]");
                    if(i!=vector.size()-1)
                        tHtml.append(",\n");
                }

                                                                                      /*
                "        ['ibis Birmingham Airport', 52.452656, -1.730548, 4],\n" +
                "        ['ETAP Birmingham Airport', 52.452527, -1.731644, 3],\n" +
                "        ['ibis Birmingham City Centre', 52.475162, -1.897208, 2]\n" +  */
                tHtml.append("        ];\n" +
                "\n" +
                "        for (var i = 0; i < hotels.length; i++) {\n" +
                "\n" +

                "        var hotel = hotels [i]\n" +
                        "var pinColor = hotel[4] ;\n" +
                        "    var icon = new google.maps.MarkerImage(\"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|\" + pinColor,\n" +
                        "        new google.maps.Size(21, 34),\n" +
                        "        new google.maps.Point(0,0),\n" +
                        "        new google.maps.Point(10, 34));" +
                        "icon.scaledSize = new google.maps.Size(12,18);\n" +
          //      "        var icon = new google.maps.MarkerImage(\"http://web4support.com/images/red_icon.png\"); \n" +
           //             "icon.scaledSize = new google.maps.Size(8,8);\n" +
                        "var marker = new google.maps.Marker({\n" +
                "        position: new google.maps.LatLng (hotel[1], hotel[2]),\n" +
                "        map: map,\n" +
                        "icon: icon," +
                "        title: hotel[0],\n" +
                "        zIndex: hotel[3]\n" +
                "        });\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        google.maps.event.addDomListener(window, 'load', initialize);\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload=\"initialize()\">\n" +
                "<div id=\"map-canvas\" style=\"width:100%; height:100%\"></div>\n" +
                "</body>\n" +
                "</html>");
              //    System.out.println(tHtml.toString());
        return tHtml.toString();
    }
}