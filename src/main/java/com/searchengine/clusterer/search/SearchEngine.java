package com.searchengine.clusterer.search;


import com.searchengine.clusterer.dataobject.Snippet;

import java.util.ArrayList;

/**
 * Created by Margo on 16.05.2015.
 */
public abstract class SearchEngine {
    private SearchEngineType type = null;
    
    public SearchEngine(SearchEngineType type){
        this.type = type;
    }

    public abstract ArrayList<Snippet> search(String queryWord);
    
    public SearchEngineType getType() {
        return type;
    }

    public void setType(SearchEngineType type) {
        this.type = type;
    }
    
}
