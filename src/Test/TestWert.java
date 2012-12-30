package Test;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import utils.GoogleMaps;
import utils.MovesReader;
import utils.clustering.CachedDistance;
import utils.clustering.ClusterWert;
import utils.clustering.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 26/12/12
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class TestWert {
    public static void main (String [] args){
        MovesReader reader = new MovesReader();
        reader.load("/Users/emilio/Desktop/GraphStreamTest/files/export_lunes.txt");
        Graph g = reader.getG();
        Iterator<Node> it = reader.getG().iterator();
        ArrayList<ClusterWert> services = new ArrayList<ClusterWert>();

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("resultado"));

            
            //Transforming data stored into GraphStream into ClusterWert data structure
        while (it.hasNext()){
            Node next = it.next();
            Service s = new Service (next.getId());
            if(s.fillIn(next)){
            ClusterWert cluster = new ClusterWert(s);
            services.add(cluster);
            }else
                System.out.println("Error al procesar "+s.toString());
        }

        CachedDistance cache = CachedDistance.getInstance();

        do{

            int bestPosI =0;
            int bestPosJ =0;
            double bestDistance=Double.MAX_VALUE;
            ClusterWert best = new ClusterWert();
            for(int j=0;j<services.size();j++)
            {
                ClusterWert cIni = services.get(j);
                //System.out.println(" CIni " +cIni.getServices().size());

                for(int i=j+1;i<services.size();i++ )
                {

                    ClusterWert aux = cache.getMinimumDistance(cIni,services.get(i));
                    double auxDistance = aux.getDistance();
                    if(auxDistance!= -1 && auxDistance<bestDistance){
                        bestDistance= auxDistance;
                        bestPosI = i;
                        bestPosJ = j;
                        best = aux;
                        //System.out.println("BESt distance "+bestDistance);
                    }
                }
            }
            //Mix the cluster.
            if(bestDistance!=Double.MAX_VALUE)
            {
                System.out.println("MIx "+ bestPosJ +" con "+bestPosI + " currentSize "+services.size());
                System.out.println("Cache Size "+ cache.getCache().size() + " A: "+cache.getAciertos()+" F: "+cache.getFallos());
                services.remove(bestPosI);
                services.remove(bestPosJ);
                services.add(best);
            }else {
                System.out.println("TODOS LEJOS");
                break;

            }
        }while (services.size()>1);

        String strLine;
            int   i = 1;
        for(ClusterWert c : services)
        {
            writer.write(i+" "+c.toString());
            writer.newLine();
            i++;
        }
            writer.flush();
            writer.close();

            GoogleMaps google  = new GoogleMaps();
            google.generateHtml(services,"prueba.html");
        
        System.out.print("ASDASD");
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
