package utils.clustering;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 19/12/12
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class CachedDistance {

    private Hashtable<String, ArrayList<Service>> cache;
    private static volatile CachedDistance instance = null;
    private long fallos = 0;
    private long aciertos = 0;
    private Hashtable<String, String> cache1;

    private CachedDistance() {
        cache = new Hashtable<String, ArrayList<Service>>();
        setCache1(new Hashtable<String, String>());

    }

    public static CachedDistance getInstance() {
        if (instance == null) {
            synchronized (CachedDistance.class) {
                if (instance == null) {
                    instance = new CachedDistance();
                }
            }
        }
        return instance;
    }


    public ClusterWert getMinimumDistance(ClusterWert c1, ClusterWert c2) {

        ArrayList<Service> srvC1 = c1.getServices();
        ArrayList<Service> srvC2 = c2.getServices();
        ClusterWert intermediate = new ClusterWert();
        //Copy all services.
        for (Service s : srvC1)
            intermediate.getServices().add((Service) s.clone());

        //Insert iteratively each service into the intermediate array,
        // if one of the inserted service get max distance then distance(C1,C2) = maxDistance
        boolean maxDistance = false;
        Iterator<Service> it = srvC2.iterator();
        do {
            Service next = it.next();
            if (intermediate.peekService((Service) next.clone()) == -1) {
                maxDistance = true;
                /*while (it.hasNext()){
                    intermediate.getServices().add(it.next());
                    String key = Service.getOrderedID(intermediate.getServices());
                    cache.put(key,intermediate.getServices());
                }    */

            }

        } while (!maxDistance && it.hasNext());


        double aux = intermediate.calculateMyOwnDistance();

        /*
    if(!cache.containsKey(id))
    {

    }else
        result.setServices(cache.get(id));*/
        return intermediate;
    }

    public ClusterWert getMinimumDistanceOther(ClusterWert c1, ClusterWert c2) {

        ArrayList<Service> srvC1 = c1.getServices();
        ArrayList<Service> srvC2 = c2.getServices();
        ClusterWert intermediate = new ClusterWert();
        //Copy all services.

        String id = c1.getOrderedId(c2);
        if (getCache1().containsKey(id)) {
            String idordered = (String) getCache1().get(id);
            setAciertos(getAciertos() + 1);

            if (!idordered.equals("-1")) {
                intermediate = getResult(c1, c2, idordered);
                intermediate.calculateMyOwnDistance();
                return intermediate;
            } else
                return intermediate;

        } else {
            setFallos(getFallos() + 1);
            for (Service s : srvC1)
                intermediate.getServices().add((Service) s.clone());
            //Insert iteratively each service into the intermediate array,
            // if one of the inserted service get max distance then distance(C1,C2) = maxDistance
            boolean maxDistance = false;
            Iterator<Service> it = srvC2.iterator();
            do {
                Service next = it.next();
                if (intermediate.peekServiceOther((Service) next.clone()) == -1) {
                    maxDistance = true;
                }
            } while (!maxDistance && it.hasNext());
            intermediate.calculateMyOwnDistance();
            if (intermediate.getDistance() != -1) {
                String idBest = intermediate.getId();
                putInto(getCache1(), id, idBest);
            } else
                putInto(getCache1(), id, "-1");

            /*
        if(!cache.containsKey(id))
        {

        }else
            result.setServices(cache.get(id));*/
            return intermediate;
        }
    }

    private void putInto(Hashtable<String, String> cache1, String id, String s) {
        //To change body of created methods use File | Settings | File Templates.
        if (cache1.size() < 20000000) {
            if (id.split("-").length > 2)
                cache1.put(id, s);
        } else {
            System.out.println("Cache petadas ");
            cache1.clear();
        }


    }

    private ClusterWert getResult(ClusterWert c1, ClusterWert c2, String bestOrder) {
        ClusterWert result = new ClusterWert();
        result.setServices(new ArrayList<Service>(c1.getServices().size() + c2.getServices().size()));
        String[] aux = bestOrder.split("-");
        for (int j = 0; j < aux.length; j++) {
            boolean detected = false;
            for (int i = 0; i < c1.getServices().size(); i++) {
                if (aux[j].equals(c1.getServices().get(i).getId())) {
                    result.getServices().add((Service) c1.getServices().get(i).clone());
                    detected = true;
                    break;
                }
            }
            for (int i = 0; !detected && i < c2.getServices().size(); i++) {
                if (aux[j].equals(c2.getServices().get(i).getId())) {
                    result.getServices().add((Service) c2.getServices().get(i).clone());
                    break;
                }
            }
        }
        return result;

    }

    private ArrayList<Service> getMinimumDistance(ArrayList<Service> array, Service n) {
        ArrayList<Service> result = new ArrayList<Service>();
        return result;
    }

    private ArrayList<Service> getBestOrder(ArrayList<Service> array) {
        ArrayList<Service> result = new ArrayList<Service>();
        String id = Service.getOrderedID(array);
        if (getCache().contains(id)) {

        } else {

        }
        return result;
    }

    public Hashtable<String, ArrayList<Service>> getCache() {
        return cache;
    }

    public Hashtable<String, String> getCache1() {
        return cache1;
    }

    public void setCache(Hashtable<String, ArrayList<Service>> cache) {
        this.cache = cache;
    }

    public long getFallos() {
        return fallos;
    }

    public void setFallos(long fallos) {
        this.fallos = fallos;
    }

    public long getAciertos() {
        return aciertos;
    }

    public void setAciertos(long aciertos) {
        this.aciertos = aciertos;
    }

    public void setCache1(Hashtable<String, String> cache1) {
        this.cache1 = cache1;
    }
}
