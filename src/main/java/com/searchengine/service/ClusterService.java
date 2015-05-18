package com.searchengine.service;

import com.searchengine.clusterer.Clusterer;
import com.searchengine.clusterer.TrieNode;
import com.searchengine.clusterer.dataobject.Snippet;
import com.searchengine.clusterer.search.SearchEngine;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Margo on 18.05.2015.
 */
public interface ClusterService {

    ArrayList<Snippet> serch(SearchEngine searchEngine, String query);

    void stemm(ArrayList<Snippet> snippets);

    public void clearStopWords(ArrayList<Snippet> snippets);

    TrieNode buildSuffixTree(ArrayList<Snippet> snippets);

    Clusterer identifyBaseClusters(TrieNode suffixRoot, Clusterer clusterer);

    Map<String, ArrayList<Snippet>> generateDataForTree(Clusterer clusterer, ArrayList<Snippet> snippets);
}
