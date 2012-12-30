package utils;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 27/12/12
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */

import utils.clustering.ClusterWert;
import utils.clustering.Service;

import java.awt.geom.Point2D;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 11/12/12
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 * @web http://java-buddy.blogspot.com/
 */
public class GoogleMaps  {


    private static Vector<String []> vector;


    /**
     * @param args the command line arguments
     */




    private boolean reading_services;
    /*
   public MyBrowser(){

       final URL urlGoogleMaps = getClass().getResource("html.html");
       //webEngine.loadContent(genHtml());
       webEngine.loadContent(urlGoogleMaps.toExternalForm());
       getChildren().add(webView);
   } */

    public GoogleMaps(){
        //read();
        //generateHtml();
        //final URL urlGoogleMaps = getClass().getResource("html.html");
        //webEngine.load(urlGoogleMaps.toExternalForm());
        //webEngine.load(genHtml());


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

            BufferedReader reader = new BufferedReader(new FileReader("/Users/emilio/Desktop/GraphStreamTest/files/export_lunes.txt"));
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





    public  void generateHtml (ArrayList<ClusterWert> lista, String filename)
    {

        URL resourceUrl = getClass().getResource("html.html");
        File file = null;
        try {
            file = new File(filename);
            // navega(aux);
            FileWriter writer = new FileWriter(file,false);
            BufferedWriter output = new BufferedWriter(writer);
            output.write(genHtml(lista));
            output.flush();
            output.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private String genHtml(ArrayList<ClusterWert> lista) {
        //To change body of created methods use File | Settings | File Templates.
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
                "var barr = [ ] ;" +
                "        function initialize() {\n" +
                "\n" +
                "\n" +
                "        var mapDiv = document.getElementById('map-canvas');\n" +
                "\n" +
                "\n" +
               // "        var latlng = new google.maps.LatLng("+vector.get(0)[8]+","+vector.get(0)[9]+");\n" +
                "        var latlng = new google.maps.LatLng("+lista.get(0).getServices().get(0).getX()+","+lista.get(0).getServices().get(0).getY()+");\n" +

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
        for(int i =0;i<lista.size();i++){
            String color = get_random_color();
            for (int j=0;j<lista.get(i).getServices().size();j++)
            {
                Service s = lista.get(i).getServices().get(j);
                String [] aux = s.toString().replaceAll("\t"," ").split("\n");
                tHtml.append(" ['"+s.getId()+"'," +s.getX()+","+s.getY()+",3,"+color+","+"'Cluster "+i+"','"+aux[3]+"','"+aux[4]+"','"+aux[5]+"',"+ (j+1) +","+s.getDuration()+ "]");
                if(j!=lista.get(i).getServices().size()-1 || i!=lista.size()-1)
                    tHtml.append(",\n");

            }
        }
        /*
        for(int i=0;i<vector.size();i++)
        {
            tHtml.append(" ['"+vector.get(i)[1]+"'," +vector.get(i)[8]+","+vector.get(i)[9]+",3]");
            if(i!=vector.size()-1)
                tHtml.append(",\n");
        } */

        /*
"        ['ibis Birmingham Airport', 52.452656, -1.730548, 4],\n" +
"        ['ETAP Birmingham Airport', 52.452527, -1.731644, 3],\n" +
"        ['ibis Birmingham City Centre', 52.475162, -1.897208, 2]\n" +  */
        tHtml.append("        ];\n" +
                "\n" +
                "        for (var i = 0; i < hotels.length; i++) {\n" +
                "\n" +
                "barr[i] = new Object ;" +

                "        var hotel = hotels [i]\n" +
                "var pinColor = hotel[4] ;\n" +
                "    var icon = new google.maps.MarkerImage(\"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|\" + pinColor,\n" +
                "        new google.maps.Size(21, 34),\n" +
                "        new google.maps.Point(0,0),\n" +
                "        new google.maps.Point(10, 34));\n" +
                //
                 "icon.scaledSize = new google.maps.Size(15,26);\n" +
                //      "        var icon = new google.maps.MarkerImage(\"http://web4support.com/images/red_icon.png\"); \n" +
                //             "icon.scaledSize = new google.maps.Size(8,8);\n" +
                "var marker = new google.maps.Marker({\n" +
                "        position: new google.maps.LatLng (hotel[1], hotel[2]),\n" +
                "        map: map,\n" +
                "icon: icon," +
                "        title: hotel[5] +'_'+ hotel[9],\n" +
                "        zIndex: hotel[3]\n" +
                "        });\n" +
                "\n" +
                " barr[i].marker = marker ;" +
                " barr[i].html = '<div id=\"content\">'+\n" +
                "            '<div id=\"siteNotice\">'+\n" +
                "            '</div>'+\n" +
                "            '<div id=\"bodyContent\">'+\n" +
                "            '<p> ' +hotel [5]  +'<br />'" +
                "+'Servicio: '+ hotel[0]  +'<br />' " +
                "+'Orden de visita: '+ hotel[9]  +'<br />' " +
                "+hotel[6]+ '<br />'+" +
                "hotel[7]+ '<br />'+ " +
                "hotel[8]+ '<br />'+ " +
                "'Duraci&oacuten: '+hotel[10] +'</p>'+\n" +
              //  "            '<p> ' + hotel[6] +'</p>'+\n" +
              //  "            '<p> ' + hotel[7] +'</p>'+\n" +
              //  "            '<p> ' + hotel[8] +'</p>'+\n" +
                "            '</div>'+\n" +
                "            '</div>';\n" +
                "\n" +
                "barr[i].infoWindow = new google.maps.InfoWindow({\n" +
                "            content: barr[i].html,\n" +
                "            maxWidth: 230\n" +
                "        });" +
                "barr[i].listener = makeClosure(i, barr[i].marker) ;" +
                /*
                "google.maps.event.addListener(marker, 'click', function() {\n" +
                "infowindow.setContent(this.content);\n" +
                "infowindow.open(map, this);" +
                "        });" +
                */
                "        }\n" +
                "\n" +
                "        }\n" +
                "// Make a simple closure with the listener...\n" +
                "\n" +
                "  function makeClosure( i, marker )\n" +
                "  {\n" +
                "   var listener = google.maps.event.addListener(marker, 'click', function() {\n" +
                "    openInfoWindow(i) ;\t\t// <-- this is the key to making it work\n" +
                "   });\n" +
                "   return listener ;\n" +
                "  }\n" +
                "\n" +
                "  // Open the infoWindow - called from the closure...\n" +
                "\n" +
                "  function openInfoWindow(i)\n" +
                "  {\n" +
                "   if ( typeof(lastI) == 'number' && typeof(barr[lastI].infoWindow) == 'object' )\n" +
                "   { \n" +
                "    barr[lastI].infoWindow.close() ;\n" +
                "   }\n" +
                "   lastI = i ;    \n" +
                "   barr[i].infoWindow.open(map,barr[i].marker) ;    \n" +
                "  }" +
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




    private String  get_random_color() {

        String[] letters = new String[15];
        letters = "0123456789ABCDEF".split("");
        String code ="'";
        for(int i=0;i<6;i++)
        {
            double ind = 1+ (Math.random() * 15);
            int index = (int)Math.round(ind);
            code += letters[index];
        }
        if(code.length()<6)
        {
            System.out.println("ASDJASLKD");
        }
        return code+"'";
    }


}
