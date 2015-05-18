package com.searchengine.clusterer;

import java.util.ArrayList;

/**
 * Created by Margo on 16.05.2015.
 */
public class Cluster {
    private String name;
    private ArrayList<Integer> docList;

    public Cluster(String name, ArrayList<Integer> docList) {
        this.name = name;
        this.docList = docList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getDocList() {
        return docList;
    }

    public void setDocList(ArrayList<Integer> docList) {
        this.docList = docList;
    }
    
}
