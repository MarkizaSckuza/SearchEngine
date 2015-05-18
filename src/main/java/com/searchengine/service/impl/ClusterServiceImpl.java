package com.searchengine.service.impl;

import com.searchengine.clusterer.Clusterer;
import com.searchengine.clusterer.StopWord;
import com.searchengine.clusterer.TrieNode;
import com.searchengine.clusterer.WordSuffixTree;
import com.searchengine.clusterer.dataobject.Snippet;
import com.searchengine.clusterer.search.SearchEngine;
import com.searchengine.service.ClusterService;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.ext.RussianStemmer;

import javax.ejb.LocalBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Margo on 18.05.2015.
 */
@LocalBean
public class ClusterServiceImpl implements ClusterService, Serializable {



    public ArrayList<Snippet> serch(SearchEngine searchEngine, String query) {
        return searchEngine.search(query);
    }

    public void stemm(ArrayList<Snippet> snippets) {
        SnowballProgram stemmer = new EnglishStemmer();
        for (Snippet snippet : snippets) {
            ArrayList<String> tempStemmedWords = new ArrayList<>(snippets.size());
            for (int j = 0; j < snippet.getOriginalWords().size(); j++) {
                // Eliminate empty words
                if (snippet.getOriginalWords().get(j).isEmpty()) {
                    snippet.getOriginalWords().remove(j--);
                } else {
                    stemmer.setCurrent(snippet.getOriginalWords().get(j));
                    stemmer.stem();
                    tempStemmedWords.add(stemmer.getCurrent());
                }
            }
            snippet.setStemmedWords(tempStemmedWords);
        }
    }

    public void clearStopWords(ArrayList<Snippet> snippets) {
        for (Snippet snippet : snippets) {
            for (int j = 0; j < snippet.getStemmedWords().size(); j++) {
                for (int k = 0; k < StopWord.stopWords.length; k++) {
                    if (snippet.getStemmedWords().get(j).trim().equalsIgnoreCase(StopWord.stopWords[k])) {
                        snippet.getStemmedWords().remove(j);
                        snippet.getOriginalWords().remove(j);
                        j--;
                        break;
                    }
                }
            }
        }
    }

    public TrieNode buildSuffixTree(ArrayList<Snippet> snippets) {
        WordSuffixTree wordSuffixTree = new WordSuffixTree(snippets);
        TrieNode suffixRoot = wordSuffixTree.constructSuffixTree();
        return suffixRoot;
    }

    public Clusterer identifyBaseClusters(TrieNode suffixRoot, Clusterer clusterer) {
        clusterer = new Clusterer(suffixRoot);
        clusterer.identifyBaseClusters();
        return clusterer;
    }

    public Map<String, ArrayList<Snippet>> generateDataForTree(Clusterer clusterer, ArrayList<Snippet> snippets) {
        Map<String, ArrayList<Snippet>> map = new HashMap<>();

        for (int i = 0; i < clusterer.getFinalList().size(); i++) {
            ArrayList<Snippet> tempSnipList = new ArrayList<>();
            for (Integer id : clusterer.getBaseClusters().get(i).getDocList()) {
                tempSnipList.add(snippets.get(id));
            }
            map.put(clusterer.getBaseClusters().get(i).getName(), tempSnipList);
        }
        return map;
    }
}
