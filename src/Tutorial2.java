import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 22/08/12
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class Tutorial2 {
    public static void main(String args[]) {
        Graph graph = new SingleGraph("Tutorial2");
        graph.display(false);
        graph.addAttribute("ui.antialias");
        try {
            FileSource source = FileSourceFactory.sourceFor(
                    "tutorial2.dgs");
            source.addSink(graph);
            source.begin("tutorial2.dgs");
            while(source.nextEvents()){
                Thread.sleep(1000);
            }
            source.end();

        } catch(Exception e) { e.printStackTrace(); }
    }
}