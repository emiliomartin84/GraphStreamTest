package utils.clustering;

import org.graphstream.graph.Node;
import utils.Distance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 19/12/12
 * Time: 21:21
 * To change this template use File | Settings | File Templates.
 */
public class Service implements Cloneable, Comparable<Service> {
    private static double SPEED = 3.0;
    private String id;
    private double x;
    private double y;
    private double duration;
    private Date h;
    private Date ini;
    private Date fin;

    public String toString() {
        String result = "";
        result += "Service: " + getId() + "\n" +
                "\tx: " + getX() + "\n" +
                "\ty: " + getY() + "\n" +
                "\th: " + getH().toString() + "\n" +
                "\tini: " + getIni().toString() + "\n" +
                "\tfin: " + getFin().toString() + "\n" +
                "\tduration: " + getDuration() + "\n";


        return result;
    }

    public Service(String d) {
        setId(d);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }

    @Override
    public int compareTo(Service o1) {
        return this.getId().compareTo(o1.getId());
    }


    public static String getOrderedID(ArrayList<Service> ser) {
        String result = "";
        /*
        ArrayList<Service> a = new ArrayList<Service>();
        for(int i=0;i<ser.size();i++)
        {
            a.add((Service)ser.get(i).clone());
        } */

        Collections.sort(ser);

        for (Service aSer : ser) {
            result += aSer.getId();
        }
        return result;

    }

    public static String getOrderedID1(ArrayList<Service> ser) {
        String result = "";

        ArrayList<String> a = new ArrayList<String>();
        for (int i = 0; i < ser.size(); i++) {
            a.add(ser.get(i).getId());
        }

        Collections.sort(a);

        for (Service aSer : ser) {
            result += aSer.getId();
        }
        return result;

    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }


    public Date getIni() {
        return ini;
    }

    public void setIni(Date ini) {
        this.ini = ini;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public double[] calculateDistanceFromToMe(Service previous) {
        double[] result = new double[2];
        long myH = calculateH(previous);

        if (!getH().after(getFin())) {
            double distance = Distance.distance(previous.getX(), previous.getY(), getX(), getY(), 'K') / SPEED; //Distance in hours
            distance = distance * 60 * 60 * 1000;//Distance in milliseconds
            result[1] = distance;
            distance += getDuration() * 60 * 1000;
            result[0] = ((getIni().getTime() - myH) + Math.abs((getIni().getTime() - myH))) / 2.0;
            result[0] += distance;

        } else
            result[0] = -1;
        return result;
    }

    private long calculateH(Service previous) {

        Date prevH = previous.getH();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(prevH);
        calendar.add(Calendar.MINUTE, (int) previous.getDuration());


        double aux = (previous.getIni().getTime() - prevH.getTime());
        aux += Math.abs((previous.getIni().getTime() - prevH.getTime()));
        aux /= 2.0;

        calendar.add(Calendar.MILLISECOND, (int) aux);
        //
        //    previous.getDuration() +
        double distance = Distance.distance(previous.getX(), previous.getY(), getX(), getY(), 'K') / SPEED;
        distance *= 60;
        calendar.add(Calendar.MINUTE, (int) distance);

        this.setH(calendar.getTime());

        return getH().getTime();
    }

    public Date getH() {
        return h;
    }

    public void setH(Date h) {
        this.h = h;
    }

    public boolean fillIn(Node next) {
        boolean result = true;
        /*
        g.getNode(id).setAttribute("xyz",punto.y,punto.x,0);
        g.getNode(id).setAttribute("ini",datos[3]+":"+datos[4]);
        g.getNode(id).setAttribute("end",datos[5]+":"+datos[6]);
        g.getNode(id).setAttribute("duration"+datos[7]);*/
        Object[] array = next.getArray("xyz");
        //setX((Double)array[0]);
        //setY((Double)array[1]);
        setX((Double) next.getAttribute("latitude"));

        setY((Double) next.getAttribute("longitude"));

        String[] init = next.getAttribute("ini").toString().split(":");
        String[] end = next.getAttribute("end").toString().split(":");

        Calendar calendar = Calendar.getInstance();

        Date date = new Date();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(init[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(init[1]));
        date = calendar.getTime();
        setIni(date);

        Date dateEnd = new Date();
        calendar.setTime(dateEnd);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(end[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(end[1]));
        dateEnd = calendar.getTime();
        setFin(dateEnd);

        setDuration(Double.valueOf(next.getAttribute("duration").toString()));

        setH((Date) this.getIni().clone());

        if (date.after(dateEnd))
            result = false;
        return result;
    }
}
