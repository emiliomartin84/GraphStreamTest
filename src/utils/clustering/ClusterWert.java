package utils.clustering;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 16/12/12
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class ClusterWert implements Comparable {

    private ArrayList<Service> services;
    private static final double MAX_DISTANCE = -1;
    private double distance = MAX_DISTANCE;
    private double travelTime;


    public ClusterWert() {
        services = new ArrayList<Service>();
    }

    public ClusterWert(Service miFirstService) {
        services = new ArrayList<Service>();
        services.add(miFirstService);
    }

    public String getOrderedId(ClusterWert other) {
        String result = "";
        for (Service s : services) {
            result += s.getId() + "-";
        }
        for (Service s : other.getServices()) {
            result += s.getId() + "-";
        }
        String[] splitedString = result.split("-");
        ArrayList<String> aux = new ArrayList<String>();
        Collections.addAll(aux, splitedString);
        Collections.sort(aux);
        result = "";
        for (String s : aux)
            result += s + "-";
        return result;
    }


    public String getId() {
        String result = "";
        for (Service s : services) {
            result += s.getId() + "-";
        }

        return result;
    }


    public double peekServiceOther(Service s) {
        CachedDistance cache = CachedDistance.getInstance();
        double distance = -1;
        getServices().add((Service) s.clone());

        if (services.size() < 8) {
            String id = Service.getOrderedID(getServices());

            ArrayList<ArrayList<Service>> permutations = getMyPermutation();
            int best = 0;

            double bestDistance = Double.MAX_VALUE;


            for (int i = 0; i < permutations.size(); i++) {
                double auxDistance = calculateDistance(permutations.get(i));

                if (auxDistance != MAX_DISTANCE && auxDistance < bestDistance) {
                    bestDistance = auxDistance;
                    distance = auxDistance;
                    best = i;
                }
            }
            if (distance != MAX_DISTANCE) {
                ArrayList<Service> copyA = new ArrayList<Service>();
                for (int i = 0; i < permutations.get(best).size(); i++) {

                    copyA.add((Service) permutations.get(best).get(i).clone());
                }
                setServices(copyA);
                this.distance = calculateMyOwnDistance();
                distance = this.distance;
            }


        } else {
            //calculateMyOwnDistance();
            this.distance = -1;

        }
        return distance;

    }


    public double peekService(Service s) {
        CachedDistance cache = CachedDistance.getInstance();
        double distance = -1;
        getServices().add((Service) s.clone());

        if (services.size() < 7) {
            String id = Service.getOrderedID(getServices());
            if (!cache.getCache().containsKey(id)) {
                ArrayList<ArrayList<Service>> permutations = getMyPermutation();
                int best = 0;

                double bestDistance = Double.MAX_VALUE;


                for (int i = 0; i < permutations.size(); i++) {
                    double auxDistance = calculateDistance(permutations.get(i));

                    if (auxDistance != MAX_DISTANCE && auxDistance < bestDistance) {
                        bestDistance = auxDistance;
                        distance = auxDistance;
                        best = i;
                    }
                }
                if (distance != MAX_DISTANCE) {


                    ArrayList<Service> copy = new ArrayList<Service>();
                    ArrayList<Service> copyA = new ArrayList<Service>();
                    for (int i = 0; i < permutations.get(best).size(); i++) {
                        copy.add((Service) permutations.get(best).get(i).clone());
                        copyA.add((Service) permutations.get(best).get(i).clone());
                    }

                    id = Service.getOrderedID(permutations.get(best));
                    setServices(copyA);
                    calculateDistance(copy);
                    if (copy.size() > 3)
                        cache.getCache().put(id, copy);
                    this.distance = calculateMyOwnDistance();
                    for (Service sa : getServices()) {
                        Date h = sa.getH();
                        Date fin = sa.getFin();
                        if (h.after(fin))
                            System.out.println("AA");
                    }

                    cache.setFallos(cache.getFallos() + 1);
                    distance = this.distance;
                }
            } else //
            {
                ArrayList<Service> copy = new ArrayList<Service>();
                for (int i = 0; i < cache.getCache().get(id).size(); i++)
                    copy.add((Service) cache.getCache().get(id).get(i).clone());
                setServices(copy);
                this.distance = calculateMyOwnDistance();

                for (Service sa : getServices()) {
                    Date h = sa.getH();
                    Date fin = sa.getFin();
                    if (h.after(fin))
                        System.out.println("AA");
                }
                cache.setAciertos(cache.getAciertos() + 1);
                distance = this.distance;
            }

        } else {
            //calculateMyOwnDistance();
            this.distance = -1;

        }
        return distance;

    }

    public double calculateMyOwnDistance() {
        this.distance = calculateDistance(getServices());
        return this.distance;
    }

    public double calculateDistance(ArrayList<Service> services) {

        double distance = 0;
        Service previous = services.get(0);
        previous.setH(previous.getIni());
        this.travelTime = 0;
        for (int i = 1; i < services.size() && distance != MAX_DISTANCE; i++) {
            //It returns at [0] distance plus wait time, at [1] just travel time.
            double[] aux = services.get(i).calculateDistanceFromToMe(previous);

            if (aux[0] == -1) {
                this.travelTime = -1;
                distance = -1;
                break;
            } else {
                this.travelTime += aux[1];
                distance += aux[0];
            }
            previous = services.get(i);
        }
        return distance;

    }

    public ArrayList<ArrayList<Service>> getMyPermutation() {
        return permutationsOf(this.getServices());
    }

    public static ArrayList<ArrayList<Service>> permutationsOf(ArrayList<Service> s) {
        ArrayList<ArrayList<Service>> result = new ArrayList<ArrayList<Service>>();

        if (s.size() == 1) { // base case
            // return a new ArrayList containing just s
            ArrayList<Service> aux = new ArrayList<Service>();
            aux.add((Service) s.get(0).clone());
            result.add(aux);
            return result;
        } else {
            // separate the first character from the rest
            Service first = s.get(0);
            ArrayList<Service> rest = new ArrayList<Service>(s.subList(1, s.size()));
            // get all permutationsOf the rest of the characters
            ArrayList<ArrayList<Service>> simpler = permutationsOf(rest);  // recursive step
            // for each permutation,
            for (ArrayList<Service> permutation : simpler) { // extra work            // add the first character in all possible positions, and
                ArrayList<ArrayList<Service>> additions = insertAtAllPositions(first, permutation);            // put each result into a new ArrayList
                result.addAll(additions);
            }
            return result;
        }
    }

    private static ArrayList<ArrayList<Service>> insertAtAllPositions(Service ch, ArrayList<Service> s) {
        ArrayList<ArrayList<Service>> result = new ArrayList<ArrayList<Service>>();
        for (int i = 0; i <= s.size(); i++) {
            ArrayList<Service> inserted = new ArrayList<Service>();
            ArrayList<Service> first = new ArrayList<Service>(s.subList(0, i));
            inserted.addAll(first);
            inserted.add(ch);
            ArrayList<Service> rest = new ArrayList<Service>(s.subList(i, s.size()));
            inserted.addAll(rest);
            result.add(inserted);
        }
        return result;
    }

    public String toCSV() {
        String result = "";
        double serviceTime = 0;
        double awaitTime = 0;
        for (Service s : services) {
            serviceTime += s.getDuration();
            awaitTime += ((s.getIni().getTime() - s.getH().getTime()) > 0) ? s.getIni().getTime() - s.getH().getTime() : 0;
        }
        SimpleDateFormat sDF = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(services.get(services.size() - 1).getH());
        result += this.getId() + "\t" +
                services.size() + "\t" +
                sDF.format(services.get(0).getH()) + "\t" +
                sDF.format(cal.getTime()) + "\t";

        cal.add(Calendar.MINUTE, (int) services.get(services.size() - 1).getDuration());
        result += sDF.format(cal.getTime()) + "\t" +
                String.valueOf(getDistance() / (60 * 1000) + "\t" +
                        String.valueOf(serviceTime).replace(',', '.') + "\t" +
                        travelTime / (60 * 1000) + "\t" +
                        awaitTime / (60 * 1000);
        return result;
    }

    public static void main(String args[]) {
        ArrayList<Service> services1 = new ArrayList<Service>();
        for (int i = 0; i < 3; i++) {
            Service s = new Service(String.valueOf(i));
            services1.add(s);
        }
        ArrayList<ArrayList<Service>> permutation = permutationsOf(services1);

        for (ArrayList<Service> aPermutation : permutation) {
            String id = "";
            String aux = Service.getOrderedID(aPermutation);
            for (int j = 0; j < aPermutation.size(); j++) {
                id += aPermutation.get(j).getId() + "-";
            }

            System.out.println("LOL " + aux);
        }

    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String toString() {
        String resultado = "";
        resultado += " Cluster " + String.valueOf(hashCode()) + "\n";
        resultado += " numServices " + services.size() + "\n";
        for (int i = 0; i < services.size(); i++) {
            resultado += "\t" + services.get(i).toString() + "\n";
        }
        return resultado;

    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof ClusterWert))
            return this.compareTo(o);
        else {
            return getId().compareTo(((ClusterWert) o).getId());
        }


    }
}

