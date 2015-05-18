package com.searchengine.clusterer;

import com.searchengine.clusterer.dataobject.Snippet;
import com.searchengine.clusterer.search.SearchEngine;
import com.searchengine.clusterer.search.SearchEngineFactory;
import com.searchengine.clusterer.search.SearchEngineType;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.RussianStemmer;

import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Margo on 17.05.2015.
 */
public class Main {

    public static ArrayList<Snippet> snippets;

    public static void main(String[] args) throws IOException, BadLocationException {
        try {
            SearchEngine searchEngine = SearchEngineFactory.createEngine(SearchEngineType.GOOGLE);

            snippets = searchEngine.search("jaguar");
            for (int i = 0; i < snippets.size(); i++) {
                System.out.println(i + "   " + snippets.get(i).getDescription());
            }

            //stemming
            SnowballProgram stemmer = new RussianStemmer();
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

            // Clear stopwords
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

            //print stemmed words
            for (int i = 0; i < snippets.size(); i++) {
                System.out.println(i + "   " + snippets.get(i).getStemmedWords());
            }

            //build suffix tree
            WordSuffixTree wordSuffixTree = new WordSuffixTree(snippets);
            TrieNode suffixRoot = wordSuffixTree.constructSuffixTree();


            //Identify base clusters
            Clusterer clusterer = new Clusterer(suffixRoot);
            clusterer.identifyBaseClusters();

            /*
             * Connect base clusters
             */
            clusterer.connectBaseClusters();

            /*
             * Sort the final clusters, biggest sets on front
             */
            clusterer.sortFinalClusters();


//
            // Print final list
            System.out.println("SORTED CLUSTERS--------------------------");
            for (int i = 0; i < clusterer.getFinalList().size(); i++) {
                System.out.println((clusterer.getFinalList().get(i).toString()));
            }
            System.out.println("----------------------------------------");


        /*
         * Print out the urls and descriptions of search result snippets
         * in a clustered groups
         */
            System.out.println("CLUSTERS AND CONTENTS-------------------------");
            for (int i = 0; i < clusterer.getFinalList().size(); i++) {
                System.out.print(i + " the cluster keywords: ");
                for (Integer clusterId : clusterer.getFinalList().get(i)) {
                    System.out.print(clusterer.getBaseClusters().get(clusterId).getName() + ", ");
                }

                for (Integer clusterId : clusterer.getFinalList().get(i)) {
                    for (Integer resultId : clusterer.getBaseClusters().get(clusterId).getDocList()) {
                        System.out.println("\t Url: " + snippets.get(resultId).getUrl());
                        System.out.println("\t Description: " + snippets.get(resultId).getDescription());

                    }
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
