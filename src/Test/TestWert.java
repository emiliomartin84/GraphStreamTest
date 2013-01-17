package Test;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import utils.GoogleMaps;
import utils.MovesReader;
import utils.clustering.CachedDistance;
import utils.clustering.ClusterWert;
import utils.clustering.ClusterWertDistance;
import utils.clustering.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 26/12/12
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class TestWert {
    public static void main(String[] args) {
        MovesReader reader = new MovesReader();
        //reader.setNewFomat(true);
        //reader.setFilterDays("0,1,2,3,4,5");
        reader.load("/Users/emilio/Desktop/GraphStreamTest/files/export_lunesCOPY.txt");

        Graph g = reader.getG();
        Iterator<Node> it = reader.getG().iterator();
        ArrayList<ClusterWert> services = new ArrayList<ClusterWert>();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("resultado"));


            //Transforming data stored into GraphStream into ClusterWert data structure
            while (it.hasNext()) {
                Node next = it.next();
                Service s = new Service(next.getId());
                if (s.fillIn(next)) {
                    ClusterWert cluster = new ClusterWert(s);
                    services.add(cluster);
                } else
                    System.out.println("Error al procesar " + s.toString());
            }

            CachedDistance cache = CachedDistance.getInstance();
            double lastBestdistance = Double.MAX_VALUE;
            //HashSet<ClusterWertDistance> distances = new HashSet<ClusterWertDistance>();
            TreeMap<ClusterWertDistance, Double> distances = new TreeMap<ClusterWertDistance, Double>();
            do {
                int bestPosI = 0;
                int bestPosJ = 0;
                double bestDistance = Double.MAX_VALUE;
                ClusterWertDistance best = new ClusterWertDistance();
                int skiped = 0;
                if (services.size() == 7)
                    System.out.println("SDA");
                for (int j = 0; j < services.size(); j++) {

                    ClusterWert cIni = services.get(j);
                    ClusterWertDistance distance = new ClusterWertDistance(cIni);


                    if (!distances.containsKey(distance))//|| (distances.containsKey(distance) && distances.))
                    {
                        for (int i = j + 1; i < services.size(); i++) {
                            ClusterWert aux = cache.getMinimumDistanceOther(cIni, services.get(i));
                            double auxDistance = aux.getDistance();
                            if (auxDistance != -1) {
                                distance.addElement(auxDistance, aux, services.get(i));

                                /*if(auxDistance < bestDistance){
                                    bestDistance = auxDistance;
                                    bestPosI = i;
                                    bestPosJ = j;
                                    best = distance;
                                } */
                            }
                        }
                        if (distance.getBest_distance() != -1)
                            distances.put(distance, distance.getBest_distance());
                    } else
                        skiped++;
                }
                Iterator<Map.Entry<ClusterWertDistance, Double>> iter = distances.entrySet().iterator();


                while (iter.hasNext()) {
                    Map.Entry<ClusterWertDistance, Double> pair = iter.next();
                    ClusterWertDistance aux = pair.getKey();
                    double auxDistance = aux.getBest_distance();
                    if (auxDistance != -1 && auxDistance < bestDistance) {
                        if (services.contains(aux.getOrigin())) {
                            bestDistance = auxDistance;
                            best = pair.getKey();
                        } else
                            distances.remove(pair.getKey());
                    }
                }

                if (bestDistance < lastBestdistance)
                    System.out.println("ASDAS");


                ArrayList<ClusterWert> toDelete = new ArrayList<ClusterWert>();
                if (bestDistance != Double.MAX_VALUE) {
                    Double firstKey = best.getDistances().firstKey();
                    ClusterWert original = best.getOriginal().get(best.getDistances().get(firstKey));

                    System.out.println("MIx " + services.indexOf(best.getOrigin()) + " con " + services.indexOf(original) + " currentSize " + services.size() + " distance: " + bestDistance + " skipped: " + skiped);
                    System.out.println("Cache Size " + cache.getCache1().size() + " A: " + cache.getAciertos() + " F: " + cache.getFallos());

                    services.remove(best.getOrigin());
                    toDelete.add(best.getOrigin());
                    services.remove(original);
                    toDelete.add(original);
                    Double bestKey = best.getDistances().firstKey();
                    services.add(best.getDistances().get(bestKey));
                    distances.remove(best);
                    lastBestdistance = bestDistance;
                } else {
                    System.out.println("TODOS LEJOS");
                    break;

                }

                iter = distances.entrySet().iterator();
                /*
              while(iter.hasNext())
              {
                  Map.Entry<ClusterWertDistance,Double> pair = iter.next();
                  ClusterWertDistance aux = pair.getKey();
                  double auxDistance = aux.getBest_distance();
                  if(auxDistance!=-1 && auxDistance < bestDistance){
                      if(services.contains(aux.getOrigin())){
                          bestDistance = auxDistance;
                          best = pair.getKey();
                      }else
                          distances.remove(pair);
                  }
              }  */

                while (iter.hasNext()) {
                    Map.Entry<ClusterWertDistance, Double> pair = iter.next();
                    double auxDistance = pair.getValue();
                    ClusterWertDistance aux = pair.getKey();
                    for (ClusterWert w : toDelete)
                        aux.removeOriginalCluster(w);
                    if (!services.contains(aux.getOrigin()) || aux.getDistances().size() == 0)
                        iter.remove();
                }

                //equals.remove(i);


                //TODO iterate over equals in order to mix all other elements up.
            } while (services.size() > 1);

            String strLine;
            int i = 1;
            for (ClusterWert c : services) {
                writer.write(i + " " + c.toString());
                writer.newLine();
                i++;
            }
            writer.flush();
            writer.close();

            GoogleMaps google = new GoogleMaps();
            Date date = new Date();
            date.setTime(System.currentTimeMillis());
            google.generateHtml(services, "prueba" + date.toString() + ".html");

            System.out.print("ASDASD");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
