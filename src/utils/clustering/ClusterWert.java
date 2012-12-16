package utils.clustering;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 16/12/12
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class ClusterWert implements Comparable
{

    ArrayList<Service> services;

    static int contador =0;

    ArrayList<String> permutationsOf(String s) {
        ArrayList<String> result = new ArrayList<String>();

        if (s.length() == 1) { // base case
            // return a new ArrayList containing just s
            result.add(s);
            return result;
        }
        else {
            // separate the first character from the rest
            char first = s.charAt(0);
            String rest = s.substring(1);
            // get all permutationsOf the rest of the characters
            ArrayList<String> simpler = permutationsOf(rest);  // recursive step
            // for each permutation,
            for (String permutation : simpler) { // extra work            // add the first character in all possible positions, and
                ArrayList additions = insertAtAllPositions(first, permutation);            // put each result into a new ArrayList
                result.addAll(additions);
            }
            return result;
        }
    }

    private ArrayList insertAtAllPositions(char ch, String s) {

        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i <= s.length(); i++) {
            String inserted = s.substring(0, i) + ch + s.substring(i);
            result.add(inserted);
        }
        return result;
    }

    static ArrayList<ArrayList<Service>> permutationsOf(ArrayList<Service> s) {
        ArrayList<ArrayList<Service>> result = new ArrayList<ArrayList<Service>>();

        if (s.size() == 1) { // base case
            // return a new ArrayList containing just s
            result.add(s);
            return result;
        }
        else {
            // separate the first character from the rest
            Service first = s.get(0);
            ArrayList<Service> rest = new ArrayList<Service>(s.subList(1,s.size()));
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
            ArrayList<Service>  inserted = new ArrayList<Service>();
            ArrayList<Service> first = new ArrayList<Service>(s.subList(0,i));
            inserted.addAll(first);
            inserted.add(ch);
            ArrayList<Service> rest = new ArrayList<Service>(s.subList(i,s.size()));
            inserted.addAll(rest);
            result.add(inserted);
        }
        return result;
    }



    @Override
    public int compareTo(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main (String args[])
    {
        ArrayList<Service> services1 = new ArrayList<Service>();
        for(int i = 0;i<3;i++){
            Service s = new Service(String.valueOf(i)) ;
            services1.add(s);
        }
        ArrayList<ArrayList<Service>> permutation = permutationsOf(services1);

        for(int i=0;i<permutation.size();i++){
            String id = "";
            for (int j = 0;j<permutation.get(i).size();j++)
            {
                 id+= permutation.get(i).get(j).getId() +"-";
            }
            System.out.println(id);
        }

    }

    public static class Service{
        private String id;
        double x;
        double y;
        double duration;
        Date ini;
        Date fin;
        public Service(String d )
        {
            setId(d);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

