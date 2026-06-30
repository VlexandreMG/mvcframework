package com.monframework.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.lang.reflect.Method;
import com.monframework.core.Mapping;
import com.monframework.core.UrlMapping;

public class FrontServletController extends HttpServlet {

    List<Class<?>> touteslesClasses = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        touteslesClasses = Utilitaire.getClassesWithAnnotation("com.monapp.controller");
        
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //
        // Prends la requête de l'utilsateur
        String urlContenu = request.getPathInfo();
        String typeRequete = request.getMethod();
        System.out.println("Recherche de : " + urlContenu + " en " + typeRequete);
        // Condition de cette requête
        //
        
        boolean trouvee = false;

        if (touteslesClasses != null && !touteslesClasses.isEmpty()) {
            for (Class<?> class1 : touteslesClasses) {

                System.out.println("La classe " + class1 + "<br>");
                
                Map<UrlMapping, Mapping> link = Utilitaire.createMapping(class1);

                // On affiche les clés trouvées dans CETTE classe spécifique
                System.out.println("   -> Clés trouvées dans " + class1.getSimpleName() + " : " + link.keySet());
                
                UrlMapping urlRecherche = new UrlMapping(urlContenu , typeRequete);

                if (link.containsKey(urlRecherche)) {
                    
                    Mapping mapp = link.get(urlRecherche);
                    System.out.println("Clés disponibles dans la Map : " + link.keySet());

                    out.println("Nom de la fonction : "+ mapp.getMethode().getName() + " || " + " Nom de la classe : " + mapp.getClassName().getName() + " || " + " Lien tapé : " + urlContenu + " || " + " Méthode de ce lien : " + typeRequete + "<br>");
                    trouvee = true;
                    break;

                    try {
                        // Chargena le class
                        Class<?> testController = Class.forName(mapp.getClassName().getName());
                        //Micréer instance
                        Object objetTestController = testController.getConstructor().newInstance();
                        //MiGet fonction rehetra 
                        Method methodController = testController.getDeclaredMethods.mapp.getMethode();
                        //Mi_execute anle fonction 
                        Object resultat = methodController.invoke(objetTestController);
                        
                        if (resultat != null) {
                            out.println(resultat.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!trouvee) {
                out.println("HSeeeee Il n'y a pas de fonction associé à cette Url. <br>");
                // for (Class<?> class1 : touteslesClasses) {
                //     Map<String, Mapping> lien = Utilitaire.createMapping(class1);
                //     for (Map.Entry<String,Mapping> ln : lien.entrySet()) {
                //         String url = ln.getKey();
                //         Mapping map = ln.getValue();
                //         out.println("Nom de la fonction : "+ map.getMethode().getName() + " || " + " Url correspondant : " + url + "<br>");
                //     }
                // }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("⚠️ Le framework est bien là, mais aucune classe n'a été trouvée.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
