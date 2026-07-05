package com.monframework.listener;

import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletContextEvent;

import java.util.HashMap;

import com.monframework.core.UrlMapping;
import com.monframework.core.Mapping;
import com.monframework.controller.Utilitaire;
import jakarta.servlet.ServletContext;

public class AppContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sc) {
        ServletContext context = sc.getServletContext();
        String packageName = context.getInitParameter("com.monapp.controller");
        HashMap<UrlMapping, Mapping> mapping= new HashMap<>();

        try {
            Utilitaire.getClassesWithAnnotation(packageName);
        } catch (Exception e) {
            throw new RuntimeException("Une erreur sprint4", e);
        }

        context.setAttribute("mapping", mapping);
    }
}
