package com.monframework.core;

import java.util.Set;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

public class FrameworkInitializer implements ServletContainerInitializer {
    
    @Override
    public void onStartup(Set<Class<?>> c , ServletContext ctx) throws ServletException {
        System.out.println("[Framework] Initialisation de la framework....");

            // Instancitation de FrontServlet 
        FrontServlet frontServlet = new FrontServlet();

            //Ataoa anaty tomcat 
        ServletRegistration.Dynamic registration = ctx.addServlet("FrontServlet", frontServlet);

            //Intercepter tout 
        registration.addMapping("/");

            //Tsy miandry ny user fa tonga dia atao 
        registration.setLoadOnStartup(1);

        System.out.println("[Framework] FrontServlet enregistré avec succès sur '/'");
    }
}
