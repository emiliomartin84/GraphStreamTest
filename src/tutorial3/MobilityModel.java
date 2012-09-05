package tutorial3;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 22/08/12
 * Time: 20:01
 * To change this template use File | Settings | File Templates.
 */
public class MobilityModel {
    protected ArrayList<MobileDevice> devices;// Set of mobile devices.
    protected int deviceCount = 5000;          // N devices.
    protected int steps = 100000000;              // Simulate N steps.
    protected Graph graph;                    // The dyn. ad-hoc net.


    protected static final String styleSheet = "node.noconnected { size: 5px; fill-color: black;  }"+
            "node.connected { size: 5px; fill-color: green;  }"
            + "edge  { fill-color: blue; }";

    public MobilityModel() {
        graph = new DefaultGraph("My graphs");     // The network.
        graph.display( false );
        //System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", styleSheet);
        addMobileDevices(deviceCount);  // Add N mobile devices.
        for( int i=0; i<steps; i++ ) {   // For N steps do:
            runMobileDevices();           // Make each device move or stand.
            checkEdges();                 // Check the connectivity, that is
            sleep();                      // which device can “see” others.
        }

    }

    protected void runMobileDevices() {
        for( MobileDevice device: devices )
            device.next();
    }

    protected void checkEdges() {
        for( MobileDevice device: devices )
            device.checkEdges();
    }
    protected void addMobileDevices(int number){
        devices = new ArrayList<MobileDevice>(number);
        for(int i =0; i<number;i++){
            devices.add(new MobileDevice(graph,String.valueOf(i)));
        }
    }


    protected void sleep(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}