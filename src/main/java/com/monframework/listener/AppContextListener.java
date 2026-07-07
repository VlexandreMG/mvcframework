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
        String packageName = context.getInitParameter("package-a-scanner");
        HashMap<UrlMapping, Mapping> mapping= new HashMap<>();

        try {
            List<Class<?>> classes = Utilitaire.getClassesWithAnnotation(packageName);

            for (Class<?> class1 : classes) {
                Map<UrlMapping, Mapping> tableRoutageClasse = Utilitaire.createMapping(class1);
                for (Map.Entry<UrlMapping, Mapping> entry : tableRoutageClasse.entrySet()) {
                    UrlMapping urlMapping = entry.getKey();
                    Mapping mapping2 = entry.getValue();

                    if (mapping.containsKey(urlMapping)) {
                        System.out.println("Cette url est déja dispo.");
                    } else {
                        mapping.put(urlMapping,mapping2);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Une erreur sprint4", e);
        }

        context.setAttribute("mapping", mapping);
    }
}
