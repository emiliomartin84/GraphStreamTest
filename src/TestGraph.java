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
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swingViewer.Viewer;

public class TestGraph {
    public static void main (String [] args) throws InterruptedException {
        //Graph graph = new SingleGraph("Tutorial 1");
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Graph graph = new MultiGraph("Live long and proser");
        SpriteManager sman = new SpriteManager(graph);
        Sprite s = sman.addSprite("S1");


        Viewer viewer = graph.display();

        viewer.disableAutoLayout();
        graph.addNode("A");
        graph.getNode("A").setAttribute("xyz", 1, 0, 0);

        graph.addNode("B");
        graph.getNode("B").setAttribute("x",1);
        graph.getNode("B").setAttribute("xyz",2,20,3);


        graph.addNode("C");
        graph.getNode("C").setAttribute("x",1);
        graph.getNode("C").setAttribute("xyz",3,0,0);
           graph.addEdge("AB", "A", "B");

        graph.addEdge("BC", "B", "C", true);
        graph.addEdge("CA", "C", "A");


        s.attachToEdge("BC");
        s.setPosition(0.5);
        for(Node n:graph) {
            System.out.println(n.getId());
        }
        for(Edge e:graph.getEachEdge()) {
            System.out.println(e.getId());
        }




    }
}
