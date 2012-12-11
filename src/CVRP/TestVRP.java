package CVRP;


import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import sun.net.www.content.text.plain;

import java.io.*;
import java.util.StringTokenizer;

import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

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
    public static void main (String args[])
    {
        Graph g = new DefaultGraph("g");
        g.addAttribute("ui.stylesheet", styleSheet);
        g.addAttribute("ui.quality");
        g.addAttribute("ui.antialias");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/emilio/Desktop/GraphStreamTest/A-VRP/prueba.vrp"));
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
        for (Node node : g) {
            node.addAttribute("ui.label", node.getId());
        }
        addDistances(g);

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
            if(!id.equals("1"))                 {
            g.getNode(id).addAttribute("ui.size", capacity);
                g.getNode(id).setAttribute("ui.class","normal");
            }
           // g.getNode(id).addAttribute("ui.text", id);

        }else if (reading_depot){
            StringTokenizer str = new StringTokenizer(strLine," \t",false);
            String id = str.nextToken();
            g.getNode(id).setAttribute("ui.class","depot");
            reading_depot = false;
        }

    }

    public static void addDistances(Graph g){
        for ( Node n : g){
            for ( Node m : g){
                if(!m.equals(n)) {
                    Object a = n.getAttribute("xyz");
                    double distance = Math.sqrt(
                            Math.pow( nodePosition(n)[0] - nodePosition(m)[0],2 ) +
                            Math.pow( nodePosition(n)[1] - nodePosition(m)[1],2 )
                            );
                    try{

                    g.addEdge(n.getId()+"-"+m.getId(),n.getId(),m.getId(),false);
                        g.getEdge(n.getId()+"-"+m.getId()).setAttribute("ui.label", String.valueOf(distance).substring(0,3));
                    }catch(EdgeRejectedException e)
                        {

                        }


                }
            }
        }
    }



}
