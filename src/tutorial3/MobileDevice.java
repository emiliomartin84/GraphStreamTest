package tutorial3;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.Iterator;

import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 22/08/12
 * Time: 19:55
 * To change this template use File | Settings | File Templates.
 */
public class MobileDevice {
    protected double x, y;              // Current position.
    protected double targetx, targety;  // Destination.
    protected double speed;             // Speed factor.
    protected int pause;                // How many time to wait.
    protected Graph graph;              // The graph.
    protected Node node;                // The node for this device.
    private static final double MIN_DISTANCE_TO_STABLSIH_CONNECTION = 1350;
    private static final double MIN_DISTANCE_TO_TARGET =0.1 ;
    private static final double PAUSE_TIME = 0;
    private static final double MAX_SPEED = 0.05;
    private static final double MAX_Y = 10000;
    private static final double MAX_X = 10000;


    public MobileDevice(Graph graph, String name) {
        this.graph = graph;
        this.x = Math.random()* MAX_X;
        this.y = Math.random()* MAX_Y;
        nextTarget();
        node = graph.addNode(name);// Associate the device with a node.
        node.setAttribute("x",this.x);
        node.setAttribute("y",this.y);
    }

    public void next() {        // Our three behaviours.
        if (pause > 0) {
            pause--;            // Stand still.
        } else if(atTarget()) {
            nextTarget();       // Choose a next target, speed, and wait.
        } else {
            move();             // Move a little toward the target.
        }
    }

    private boolean atTarget() {
        double distance_to_target = Math.sqrt( (x*x -targetx*targetx) + (y*y -targety*targety) );
        return distance_to_target < MIN_DISTANCE_TO_TARGET;
    }

    protected void nextTarget() {
        targetx = Math.random()*MAX_X;            // New random destination.
        targety = Math.random()*MAX_Y;
        speed   = Math.random()*MAX_SPEED;       // New random speed.
        pause   = (int)(Math.random()*PAUSE_TIME);  // New random pause time at
    }                                       // target.

    protected void move() {
        x += (targetx-x)*speed;     // Move a little.
        y += (targety-y)*speed;
        node.setAttribute("x", x);  // Modify the node attributes to move
        node.setAttribute("y", y);  // it in the graph viewer.
    }
    protected void checkEdges() {
        // Look at edges to remove :
        Iterator<?extends Edge> edges = node.getEdgeIterator();
        ArrayList<String> toRemove = new ArrayList<String>();
        if(!edges.hasNext())
            node.setAttribute("ui.class","noconnected");
        else
            node.setAttribute("ui.class","connected");

        while( edges.hasNext()) {
            Edge edge = edges.next();
            if(!closeTo(edge.getOpposite(node))) {
                toRemove.add(edge.getId());
            }
        }

        for(String edge: toRemove)
            graph.removeEdge(edge);

        for(Node other: graph) {
            // If close enough but not too much :
            if(other != node && closeTo(other)) {
                // If not already connected :
                if(!node.hasEdgeToward(other.getId())) {
                    // Edge creation in the graph :
                    graph.addEdge(
                            String.format("%s-%s", node.getId(), other.getId()),
                            node.getId(), other.getId());
                }
            }
        }
    }

    private boolean closeTo(Node other) {

        double[] pos = nodePosition(other);
        double dx = pos[0] - x;
        double dy = pos[1] - y;
        return (Math.sqrt(dx * dx + dy * dy) < MIN_DISTANCE_TO_STABLSIH_CONNECTION);

    }

}
