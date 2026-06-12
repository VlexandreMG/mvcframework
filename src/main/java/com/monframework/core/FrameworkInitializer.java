package com.monframework.core;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import java.util.Set;

public class FrameworkInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        System.out.println("[Framework] Initialisation en cours...");

        // On instancie dynamiquement notre FrontServlet
        FrontServlet frontServlet = new FrontServlet();
        
        // On l'enregistre auprès du conteneur de servlets (Tomcat)
        ServletRegistration.Dynamic registration = ctx.addServlet("FrontServlet", frontServlet);
        
        // On lui demande d'intercepter ABSOLUMENT TOUT ("/")
        registration.addMapping("/");
        
        // On force le chargement immédiat au démarrage du serveur
        registration.setLoadOnStartup(1);

        System.out.println("[Framework] FrontServlet enregistré avec succès sur '/' !");
    }
}