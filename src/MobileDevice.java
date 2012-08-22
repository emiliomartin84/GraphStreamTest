import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

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


    public MobileDevice(Graph graph, String name) {
        this.graph = graph;
        this.x = Math.random();
        this.y = Math.random();
        nextTarget();
        node = graph.addNode( name );// Associate the device with a node.
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
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    protected void nextTarget() {
        targetx = Math.random();            // New random destination.
        targety = Math.random();
        speed   = Math.random()*0.1f;       // New random speed.
        pause   = (int)(Math.random()*10);  // New random pause time at
    }                                       // target.

    protected void move() {
        x += (targetx-x)*speed;     // Move a little.
        y += (targety-y)*speed;
        node.setAttribute("x", x);  // Modify the node attributes to move
        node.setAttribute("y", y);  // it in the graph viewer.
    }
    protected void checkEdges() {
        // To be done ...
    }

}
