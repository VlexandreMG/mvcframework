package com.monframework.listener;

import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletContextEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

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
            List<Class<?>> classes = Utilitaire.getClassesWithAnnotation(packageName);

            for (Class<?> class1 : classes) {
                Map<UrlMapping, Mapping> tableRoutageClasse = Utilitaire.createMapping(class1);
                mapping.putAll(tableRoutageClasse);
            }
        } catch (Exception e) {
            throw new RuntimeException("Une erreur sprint4", e);
        }

        context.setAttribute("mapping", mapping);
    }
}
