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
    private CachedDistance(){
        setCache(new Hashtable<String, ArrayList<Service>>());
    }

    public static CachedDistance getInstance() {
        if (instance == null) {
            synchronized (CachedDistance.class){
                if (instance == null) {
                    instance = new CachedDistance ();
                }
            }
        }
        return instance;
    }

    public ClusterWert getMinimumDistance(ClusterWert c1, ClusterWert c2)
    {

        ArrayList<Service> srvC1 = c1.getServices();
        ArrayList<Service> srvC2 = c2.getServices();
        ClusterWert intermediate = new ClusterWert();
        //Copy all services.
        for(Service s: srvC1)
            intermediate.getServices().add((Service) s.clone());

        //Insert iteratively each service into the intermediate array,
        // if one of the inserted service get max distance then distance(C1,C2) = maxDistance
        boolean maxDistance = false;
        Iterator<Service> it = srvC2.iterator();
        do{
            Service next = it.next();
            if(intermediate.peekService((Service)next.clone())==-1){
                maxDistance=true;
                /*while (it.hasNext()){
                    intermediate.getServices().add(it.next());
                    String key = Service.getOrderedID(intermediate.getServices());
                    cache.put(key,intermediate.getServices());
                }    */

            }

        }while(!maxDistance && it.hasNext());



        double aux = intermediate.calculateMyOwnDistance();

                                                /*
        if(!cache.containsKey(id))
        {

        }else
            result.setServices(cache.get(id));*/
        return intermediate;
    }

    private ArrayList<Service> getMinimumDistance(ArrayList<Service> array, Service n)
    {
        ArrayList<Service> result = new ArrayList<Service>();
        return result;
    }

    private ArrayList<Service> getBestOrder (ArrayList<Service> array)
    {
        ArrayList<Service> result = new ArrayList<Service>();
        String id = Service.getOrderedID(array);
        if(getCache().contains(id))
        {

        }else
        {

        }
        return result;
    }

    public Hashtable<String, ArrayList<Service>> getCache() {
        return cache;
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
}
