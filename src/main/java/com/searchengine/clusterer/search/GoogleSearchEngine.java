package com.searchengine.clusterer.search;


import com.searchengine.clusterer.dataobject.Snippet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Margo on 16.05.2015.
 */
public class GoogleSearchEngine extends SearchEngine{

    public GoogleSearchEngine(){
        super(SearchEngineType.GOOGLE);
    }
    
    @Override
    public ArrayList<Snippet> search(String query) {
        String request = "https://www.google.com/search?q=" + query + "&num=150&hl=en&gl=en";
        ArrayList<Snippet> snippets = new ArrayList<>();
        try {
            Document doc = Jsoup
                    .connect(request)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .timeout(5000).get();

            Elements searchResult = doc.select("div.rc");
            for (Element searchElement : searchResult) {
                String link = searchElement.select("h3.r > a[href]").attr("href");
                String description = searchElement.select("div.s > div > span.st").text();
                String[] splittedDescription = description.toLowerCase().replaceAll("[^A-Za-z ]", "").split(" ");
                ArrayList<String> originalWords = new ArrayList<>(Arrays.asList(splittedDescription));
                Snippet snippet = new Snippet(originalWords, link, description);
                snippets.add(snippet);
            }

        } catch (IOException e) {
            Logger.getLogger(GoogleSearchEngine.class.getName()).log(Level.SEVERE, null, e);
        }
    
        return snippets;
    }

    
}
