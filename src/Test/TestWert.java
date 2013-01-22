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

        Date date = new Date();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("resultado" + date.toString() + ".csv"));


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
            long startTime = System.nanoTime();
            if (true) {

                //HashSet<ClusterWertDistance> distances = new HashSet<ClusterWertDistance>();
                TreeMap<ClusterWertDistance, Double> distances = new TreeMap<ClusterWertDistance, Double>();
                do {
                    if (services.size() == 497)
                        System.out.println("a");

                    int bestPosI = 0;
                    int bestPosJ = 0;
                    double bestDistance = Double.MAX_VALUE;
                    ClusterWertDistance best = new ClusterWertDistance();
                    int skiped = 0;


                    for (int j = 0; j < services.size(); j++) {

                        ClusterWert cIni = services.get(j);
                        ClusterWertDistance distance = new ClusterWertDistance(cIni);
                        //Distance measured in milliseconds.
                        double lowerBoundDistance = (cIni.getDistance() == -1) ? cIni.getServices().get(0).getDuration() * 60 * 1000 : cIni.getDistance();
                        double lowerDistance = (distances.firstEntry() != null) ? distances.firstEntry().getValue() : Double.MAX_VALUE;

                        if (!distances.containsKey(distance) && lowerBoundDistance < lowerDistance) {

                            for (int i = j + 1; i < services.size(); i++) {
                                //lowerBoundDistance += (services.get(j).getDistance()==-1) ? services.get(j).getServices().get(0).getDuration()*60*1000 : services.get(j).getDistance();
                                //if(lowerBoundDistance < lowerDistance){
                                ClusterWert aux = cache.getMinimumDistanceOther(cIni, services.get(i));
                                double auxDistance = aux.getDistance();
                                if (auxDistance != -1) {
                                    distance.addElement(auxDistance, aux, services.get(i));
                                }
                                //}
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
                        System.out.println("ERRROR ");


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
                        services.add(0, best.getDistances().get(bestKey));
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
                        pair.setValue(pair.getKey().getBest_distance());
                    }

                    //equals.remove(i);


                    //TODO iterate over equals in order to mix all other elements up.
                } while (services.size() > 1);
            }
            String strLine;
            int i = 1;
            writer.write("ID\tIDServices\tNum Services\tH first \tH last\tLast Service  Time (includes duration)\tDistance\tService Time\tTravel Time\tWaiting time");
            writer.newLine();
            for (ClusterWert c : services) {
                writer.write(i + "\t" + c.toCSV());
                writer.newLine();
                i++;
            }
            long elapsedTime = System.nanoTime() - startTime;

            writer.write(String.valueOf(elapsedTime / 1000000));
            writer.flush();
            writer.close();

            GoogleMaps google = new GoogleMaps(false);

            date = new Date();
            google.generateHtml(services, "prueba" + date.toString() + ".html");

            System.out.print("ASDASD");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
