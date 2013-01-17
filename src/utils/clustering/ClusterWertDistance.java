package utils.clustering;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 07/01/13
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
public class ClusterWertDistance implements Comparable {
    private ClusterWert origin;
    private SortedMap<Double, ClusterWert> distances;
    private SortedMap<ClusterWert, ClusterWert> original;
    private double best_distance = -1;

    public ClusterWertDistance(ClusterWert or) {
        setOrigin(or);
        setDistances(new TreeMap<Double, ClusterWert>());
        setOriginal(new TreeMap<ClusterWert, ClusterWert>());
    }

    public ClusterWertDistance() {

    }

    public boolean addElement(double distance, ClusterWert other, ClusterWert originalCluster) {
        boolean res = true;
        if (other.equals(getOrigin()))
            res = false;
        else {

            getDistances().put(distance, other);

            this.addOriginal(other, originalCluster);

            setBest_distance(getDistances().firstKey());
        }
        return res;
    }

    public boolean removeOriginalCluster(ClusterWert originalC) {
        boolean res = false;

        for (Map.Entry<ClusterWert, ClusterWert> entry : original.entrySet()) {
            if (originalC.equals(entry.getValue())) {
                res = true;
                while (distances.values().remove(entry.getKey())) ;
                while (distances.values().remove(entry.getValue())) ;
                break;
            }
        }
        if (distances.size() != 0)
            setBest_distance(distances.firstKey());
        else
            setBest_distance(-1);
        return res;
    }

    public double getBest_distance() {
        return best_distance;
    }

    public void setBest_distance(double best_distance) {
        this.best_distance = best_distance;
    }

    /*
    @Override
    public int compareTo(ClusterWertDistance o) {

        return o.origin.getId().compareTo(origin.getId());

    } */

    @Override
    public int hashCode() {
        return origin.getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClusterWertDistance))
            return false;
        else {
            boolean res = ((ClusterWertDistance) o).getOrigin().getId().equals(getOrigin().getId());
            return res;
        }
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof ClusterWertDistance))
            return -1;
        else
            return ((ClusterWertDistance) o).getOrigin().getId().compareTo(getOrigin().getId());
    }

    public ClusterWert getOrigin() {
        return origin;
    }

    public void setOrigin(ClusterWert origin) {
        this.origin = origin;
    }

    public SortedMap<Double, ClusterWert> getDistances() {
        return distances;
    }

    public void setDistances(SortedMap<Double, ClusterWert> distances) {
        this.distances = distances;
    }

    public void addOriginal(ClusterWert aux, ClusterWert clusterWert) {
        //To change body of created methods use File | Settings | File Templates.
        getOriginal().put(aux, clusterWert);
    }

    public SortedMap<ClusterWert, ClusterWert> getOriginal() {
        return original;
    }

    public void setOriginal(SortedMap<ClusterWert, ClusterWert> original) {
        this.original = original;
    }
}
