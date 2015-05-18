package com.searchengine.clusterer.search;

/**
 * Created by Margo on 16.05.2015.
 */
public class SearchEngineFactory {
    public static SearchEngine createEngine(SearchEngineType type){
        SearchEngine engine = null;
        switch(type){
            case GOOGLE:
                engine = new GoogleSearchEngine();
                break;
            default:
                break;
        }
        return engine;
    }
}
