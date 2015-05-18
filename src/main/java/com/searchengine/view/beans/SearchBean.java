package com.searchengine.view.beans;

import com.searchengine.clusterer.Clusterer;
import com.searchengine.clusterer.TrieNode;
import com.searchengine.clusterer.dataobject.Snippet;
import com.searchengine.clusterer.search.SearchEngine;
import com.searchengine.clusterer.search.SearchEngineFactory;
import com.searchengine.clusterer.search.SearchEngineType;
import com.searchengine.service.ClusterService;
import org.primefaces.component.tree.Tree;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Margo on 18.05.2015.
 */
@Named
@ViewScoped
public class SearchBean implements Serializable {

    @Inject
    ClusterService clusterService;

    private String query;
    private ArrayList<Snippet> snippets;
    private SearchEngine searchEngine;
    private  Clusterer clusterer;
    private Map<String, ArrayList<Snippet>> dataTree;
    private Map<String, ArrayList<Snippet>> currentDataTree;
    private TreeNode root;
    private boolean isTreeVisible;

    private List<Snippet> currentSnippets;

    @PostConstruct
    public void init() {
        setTreeVisible(false);
    }

    public void search() {
        searchEngine = SearchEngineFactory.createEngine(SearchEngineType.GOOGLE);
        snippets = clusterService.serch(searchEngine, query);
        currentSnippets = snippets;
        clusterService.stemm(snippets);
        clusterService.clearStopWords(snippets);
        TrieNode suffixRoot =  clusterService.buildSuffixTree(snippets);
        clusterer = clusterService.identifyBaseClusters(suffixRoot, clusterer);

        clusterer.connectBaseClusters();
        clusterer.sortFinalClusters();

        dataTree = clusterService.generateDataForTree(clusterer, snippets);
        if (currentDataTree == null || currentDataTree.isEmpty()) {
            currentDataTree = dataTree;
        }
        createTree();
        setTreeVisible(true);
        root.setExpanded(true);
    }

    public void createTree() {
        root = new DefaultTreeNode("root", null);
        TreeNode main = new DefaultTreeNode("All Results", root);

        for (Map.Entry<String, ArrayList<Snippet>> entry : currentDataTree.entrySet()) {
            main.getChildren().add(new DefaultTreeNode(entry.getKey()));
        }
    }

    public void currentData() {
        UIComponent component = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        Tree node = (Tree) component;
        currentDataTree = new HashMap<>();
        currentSnippets = dataTree.get(node.getRowNode().toString());

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Clusterer getClusterer() {
        return clusterer;
    }

    public void setClusterer(Clusterer clusterer) {
        this.clusterer = clusterer;
    }

    public ArrayList<Snippet> getSnippets() {
        return snippets;
    }

    public void setSnippets(ArrayList<Snippet> snippets) {
        this.snippets = snippets;
    }

    public boolean isTreeVisible() {
        return isTreeVisible;
    }

    public void setTreeVisible(boolean isTreeVisible) {
        this.isTreeVisible = isTreeVisible;
    }

    public Map<String, ArrayList<Snippet>> getDataTree() {
        return dataTree;
    }

    public void setDataTree(Map<String, ArrayList<Snippet>> dataTree) {
        this.dataTree = dataTree;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Map<String, ArrayList<Snippet>> getCurrentDataTree() {
        return currentDataTree;
    }

    public void setCurrentDataTree(Map<String, ArrayList<Snippet>> currentDataTree) {
        this.currentDataTree = currentDataTree;
    }

    public List<Snippet> getCurrentSnippets() {
        return currentSnippets;
    }

    public void setCurrentSnippets(List<Snippet> currentSnippets) {
        this.currentSnippets = currentSnippets;
    }
}
