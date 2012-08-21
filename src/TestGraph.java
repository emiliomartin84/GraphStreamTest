/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 12/08/12
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.Viewer;

public class TestGraph {
    public static void main (String [] args)
    {
        //Graph graph = new SingleGraph("Tutorial 1");
        Graph graph = new MultiGraph("embedded");
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        graph.addAttribute("ui.stylesheet", "graph { fill-color: red; }");
        graph.setAutoCreate( true );

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        graph.display(true);

        for(Node n:graph) {
            System.out.println(n.getId());
        }
        for(Edge e:graph.getEachEdge()) {
            System.out.println(e.getId());
        }




    }
}
