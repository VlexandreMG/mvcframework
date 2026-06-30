package com.monframework.core;

import java.util.Objects;

public class UrlMapping {
    private String url;
    private String httpMethod; // "GET" ou "POST"

    public UrlMapping() {}

    public UrlMapping(String url, String httpMethod) {
        this.url = url;
        // On force en majuscules pour éviter les erreurs de saisie (get -> GET)
        this.httpMethod = httpMethod.toUpperCase(); 
    }

    // Getters
    public String getUrl() { return url; }
    public String getHttpMethod() { return httpMethod; }

    // Très important pour la HashMap : comparer les valeurs brutes et non les adresses mémoires
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlMapping mapping2 = (UrlMapping) o;
        return Objects.equals(url, mapping2.url) && 
               Objects.equals(httpMethod, mapping2.httpMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpMethod);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}