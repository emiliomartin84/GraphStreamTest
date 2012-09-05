package CVRP;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import sun.net.www.content.text.plain;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 23/08/12
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public class TestVRP {

    private static int capacity;
    private static boolean reading_coordinates;
    private static boolean reading_demands;
    private static boolean reading_depot;

    protected static final String styleSheet = "node.depot {\n" +
            "shape: box;" +
            "size: 15px, 15px;" +
            "}" +
    "node { size-mode: dyn-size; size: 10px;}";
    public static void main (String args[])
    {
        Graph g = new DefaultGraph("g");
        g.addAttribute("ui.stylesheet", styleSheet);
        g.addAttribute("ui.quality");
        g.addAttribute("ui.antialias");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/emilio/Desktop/GraphStreamTest/A-VRP/A-n80-k10.vrp"));
            String strLine;
            try {
                while ((strLine = reader.readLine()) != null)   {
                    // Print the content on the console
                    proccesLine (g, strLine);
                }
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

        if ( strLine.startsWith("CAPACITY")){
            StringTokenizer str = new StringTokenizer(strLine," :",false);
            str.nextToken();
            capacity = Integer.parseInt(str.nextToken()) ;
        }else if (strLine.startsWith("NODE_COORD_SECTION"))
            reading_coordinates = true;
        else if (strLine.startsWith("DEMAND_SECTION")){
            reading_coordinates = false;
            reading_demands = true;
        }else if (strLine.startsWith("DEPOT_SECTION"))
        {
            reading_demands = false;
            reading_depot = true;
        }
        else if ( reading_coordinates)
        {
            StringTokenizer str = new StringTokenizer(strLine," \t",false);
            String id = str.nextToken();
            int x = Integer.parseInt(str.nextToken()) ;
            int y = Integer.parseInt(str.nextToken()) ;
            g.addNode(id);
            g.getNode(id).setAttribute("xyz",x,y,0);
            //if(!id.equals("1"))
              //  g.addEdge("1"+id,"1",id);
        }else if (reading_demands){
            StringTokenizer str = new StringTokenizer(strLine," \t",false);
            String id = str.nextToken();
            int capacity = Integer.parseInt(str.nextToken());
            g.getNode(id).setAttribute("capacity", capacity);
            if(!id.equals("1"))
            g.getNode(id).addAttribute("ui.size", capacity);

        }else if (reading_depot){
            StringTokenizer str = new StringTokenizer(strLine," \t",false);
            String id = str.nextToken();
            g.getNode(id).setAttribute("ui.class","depot");
            reading_depot = false;
        }

    }



}
