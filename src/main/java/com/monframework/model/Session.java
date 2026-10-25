package com.monframework.model;

import jakarta.servlet.http.HttpSession;

public class Session {
    // La vraie session de Tomcat (une par utilisateur)
    private HttpSession httpSession;

    public Session(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Object get(String cle) {
        return this.httpSession.getAttribute(cle);
    }

    public void put(String cle, Object valeur) {
        this.httpSession.setAttribute(cle, valeur);
    }

    public void remove(String cle) {
        this.httpSession.removeAttribute(cle);
    }

    public boolean contains(String cle) {
        return get(cle) != null;
    }

    // Déconnexion
    public void invalidate() {
        this.httpSession.invalidate();
    }
}
