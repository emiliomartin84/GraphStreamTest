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
 * @web http://java-buddy.blogspot.com/
 */
public class GoogleMaps {


    private static Vector<String[]> vector;


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

    public GoogleMaps() {
        //read();
        //generateHtml();
        //final URL urlGoogleMaps = getClass().getResource("html.html");
        //webEngine.load(urlGoogleMaps.toExternalForm());
        //webEngine.load(genHtml());


    }

    public void proccesLine(String strLine) {
        if (strLine.startsWith("--------")) {
            reading_services = true;
        } else if (reading_services) {
            String[] datos = strLine.split(",");
            String id = datos[1];
            double lat = Double.parseDouble(datos[8]);
            Double longi = Double.parseDouble(datos[9]);
            Point2D p = new Point2D.Double();
            vector.add(datos);
        }
    }

    public void read() {
        try {

            BufferedReader reader = new BufferedReader(new FileReader("/Users/emilio/Desktop/GraphStreamTest/files/export_lunes.txt"));
            String strLine;
            vector = new Vector<String[]>();
            try {
                while ((strLine = reader.readLine()) != null) {
                    // Print the content on the console
                    proccesLine(strLine);
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


    public void generateHtml(ArrayList<ClusterWert> lista, String filename) {

        URL resourceUrl = getClass().getResource("html.html");
        File file = null;
        try {
            file = new File(filename);
            // navega(aux);
            FileWriter writer = new FileWriter(file, false);
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
                "var markers = [];\n" +
                "var polylines = [];" +
                "        function initialize() {\n" +
                "\n" +
                "\n" +
                "        var mapDiv = document.getElementById('map-canvas');\n" +
                "\n" +
                "\n" +
                // "        var latlng = new google.maps.LatLng("+vector.get(0)[8]+","+vector.get(0)[9]+");\n" +
                "        var latlng = new google.maps.LatLng(" + lista.get(0).getServices().get(0).getX() + "," + lista.get(0).getServices().get(0).getY() + ");\n" +

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
        for (int i = 0; i < lista.size(); i++) {
            String color = get_random_color();
            for (int j = 0; j < lista.get(i).getServices().size(); j++) {
                Service s = lista.get(i).getServices().get(j);
                String[] aux = s.toString().replaceAll("\t", " ").split("\n");
                tHtml.append(" ['" + s.getId() + "'," + s.getX() + "," + s.getY() + ",3," + color + "," + "'Cluster " + i + "','" + s.getH().toLocaleString() + "','" + s.getIni().toLocaleString() + "','" + s.getFin().toLocaleString() + "'," + (j + 1) + "," + s.getDuration() + "]");
                if (j != lista.get(i).getServices().size() - 1 || i != lista.size() - 1)
                    tHtml.append(",\n");

            }
        }
        tHtml.append("        ];\n" +
                "\n" +
                " for (var i = 0; i < hotels.length; i++) {\n" +
                "\n" +
                "var hotel = hotels [i]\n" +
                "var pinColor = hotel[4] ;\n" +
                "    var icon = new google.maps.MarkerImage(\"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|\" + pinColor,\n" +
                "        new google.maps.Size(21, 34),\n" +
                "        new google.maps.Point(0,0),\n" +
                "        new google.maps.Point(10, 34));\n" +
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
                "marker.html = '<div id=\"content\">' + '<div id=\"siteNotice\">' + '</div>' + '<div id=\"bodyContent\">' + '<p> ' + hotel[5] + '<br />' + 'Servicio: ' + hotel[0] + '<br />' + 'Orden de visita: ' + hotel[9] + '<br />' + hotel[6] + '<br />' + hotel[7] + '<br />' + hotel[8] + '<br />' + 'Duraci&oacuten: ' + hotel[10] + '</p>' + '</div>' + '</div>';\n" +
                "\n" +
                "\t\t\t\t\tmarker.infoWindow = new google.maps.InfoWindow({\n" +
                "\t\t\t\t\t\tcontent : marker.html,\n" +
                "\t\t\t\t\t\tmaxWidth : 230\n" +
                "\t\t\t\t\t});\n" +
                "\t\t\t\t\tmarker.cluster = hotel[5];\n" +
                "\t\t\t\t\tmarker.orden = hotel[9];\n" +
                "\t\t\t\t\tmarker.color = pinColor;\n" +
                "\t\t\t\t\tmarker.listener = makeClosure(i, marker);\n" +
                "\t\t\t\t\tmarkers.push(marker);\n" +
                "\t\t\t\t}" +
                "google.maps.event.addListener(map, \"rightclick\", redrawAll);\n" +
                "\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t// Make a simple closure with the listener...\n" +
                "\n" +
                "\t\t\tfunction makeClosure(i, marker) {\n" +
                "\t\t\t\tvar listener = google.maps.event.addListener(marker, 'click', function() {\n" +
                "\n" +
                "\t\t\t\t\topenInfoWindow(i);\n" +
                "\t\t\t\t\t// <-- this is the key to making it work\n" +
                "\t\t\t\t});\n" +
                "\t\t\t\treturn listener;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t\n" +
                "\n" +
                "\t\t\tfunction openInfoWindow(i) {\n" +
                "\n" +
                "\t\t\t\tif ( typeof (lastI) == 'number' && typeof (markers[lastI].infoWindow) == 'object') {\n" +
                "\t\t\t\t\tmarkers[lastI].infoWindow.close();\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\tlastI = i;\n" +
                "\t\t\t\tmarkers[i].infoWindow.open(map, markers[i]);\n" +
                "\t\t\t\tvar cluster = markers[i].cluster;\n" +
                "\t\t\t\tvar flightPlanCoordinates = [];\n" +
                "\t\t\t\tvar color = \"#\" + markers[i].color;\n" +
                "\t\t\t\tfor (var i = 0; i < markers.length; i++) {\n" +
                "\n" +
                "\t\t\t\t\tif (markers[i].cluster == cluster) {\n" +
                "\t\t\t\t\t\tif(flightPlanCoordinates.indexOf(markers[i].getPosition()) == -1 )\n" +
                "\t\t\t\t\t\tflightPlanCoordinates.push(markers[i].getPosition());\n" +
                "\t\t\t\t\t} else {\n" +
                "\t\t\t\t\t\tmarkers[i].setMap(null);\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tvar flightPath = new google.maps.Polyline({\n" +
                "\t\t\t\t\tpath : flightPlanCoordinates,\n" +
                "\t\t\t\t\tstrokeColor : color,\n" +
                "\t\t\t\t\tstrokeOpacity : 0.8,\n" +
                "\t\t\t\t\tstrokeWeight : 2.5\n" +
                "\t\t\t\t});\n" +
                "\t\t\t\tflightPath.cluster = cluster;\n" +
                "\t\t\t\tvar find = false;\n" +
                "\t\t\t\tfor(var i =0; i< polylines.length;i++){\n" +
                "\t\t\t\t\tif(polylines[i].cluster != cluster){\n" +
                "\t\t\t\t\t\tpolylines[i].setMap(null);\n" +
                "\t\t\t\t\t}else{\n" +
                "\t\t\t\t\t\tpolylines[i].setMap(map);\n" +
                "\t\t\t\t\t\tfind=true;\n" +
                "\t\t\t\t\t}\t\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tif(!find)\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\tflightPath.setMap(map);\n" +
                "\t\t\t\t\tpolylines.push(flightPath);\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\tfunction redrawAll(){\n" +
                "\t\t\t\tfor (var i = 0; i < markers.length; i++) {\n" +
                "\t\t\t\t\tmarkers[i].setMap(map);\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\tfor (var i = 0; i < polylines.length; i++) {\n" +
                "\t\t\t\t\tpolylines[i].setMap(null);\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\t\t\tgoogle.maps.event.addDomListener(window, 'load', initialize);\n" +
                "\t\t</script>\n" +
                "\t</head>\n" +
                "\t<body onload=\"initialize()\">\n" +
                "\n" +
                "\t\t<div id=\"map-canvas\" tabindex=\"0\" style=\"width:100%; height:100%\"></div>\n" +
                "\n" +
                "\t</body>\n" +
                "</html>");

        //    System.out.println(tHtml.toString());
        return tHtml.toString();
    }


    private String get_random_color() {

        String[] letters = new String[15];
        letters = "0123456789ABCDEF".split("");
        String code = "'";
        for (int i = 0; i < 6; i++) {
            double ind = 1 + (Math.random() * 15);
            int index = (int) Math.round(ind);
            code += letters[index];
        }
        if (code.length() < 6) {
            System.out.println("ASDJASLKD");
        }
        return code + "'";
    }


}
