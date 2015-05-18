package com.searchengine.clusterer.dataobject;

import java.util.ArrayList;

/**
 * Created by Margo on 16.05.2015.
 */
public class Snippet {

    private ArrayList<String> originalWords;
    private ArrayList<String> stemmedWords;
    private String url;
    private String description;
    private Language lang;

    public Snippet(ArrayList<String> originalWords, String url, String description) {
        this.originalWords = originalWords;
        this.url = url;
        this.description = description;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getOriginalWords() {
        return originalWords;
    }

    public ArrayList<String> getStemmedWords() {
        return stemmedWords;
    }

    public void setOriginalWords(ArrayList<String> originalWords) {
        this.originalWords = originalWords;
    }

    public void setStemmedWords(ArrayList<String> stemmedWords) {
        this.stemmedWords = stemmedWords;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Snippet snippet = (Snippet) o;

        if (description != null ? !description.equals(snippet.description) : snippet.description != null) return false;
        if (originalWords != null ? !originalWords.equals(snippet.originalWords) : snippet.originalWords != null) return false;
        if (stemmedWords != null ? !stemmedWords.equals(snippet.stemmedWords) : snippet.stemmedWords != null)
            return false;
        if (url != null ? !url.equals(snippet.url) : snippet.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = originalWords != null ? originalWords.hashCode() : 0;
        result = 31 * result + (stemmedWords != null ? stemmedWords.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Snippet{");
        sb.append("originalWords=").append(originalWords);
        sb.append(", stemmedWords=").append(stemmedWords);
        sb.append(", url='").append(url).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", lang=").append(lang);
        sb.append('}');
        return sb.toString();
    }
}
