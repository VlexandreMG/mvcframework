package com.monframework.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.monframework.annotation.Controller;
import com.monframework.core.Mapping;

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
        // Condition de cette requête
        //
        
        boolean trouvee = false;

        if (touteslesClasses != null && !touteslesClasses.isEmpty()) {
            for (Class<?> class1 : touteslesClasses) {
                
                Map<String, Mapping> link = Utilitaire.createMapping(class1);
                
                if (link.containsKey(urlContenu)) {
                    
                    Mapping mapp = link.get(urlContenu);

                    out.println("Nom de la fonction : "+ mapp.getMethode().getName() + " || " + " Nom de la classe : " + mapp.getClassName().getName() + " || " + " Lien tapé : " + urlContenu + "<br>");
                    trouvee = true;
                    break;
                }
            }
            if (!trouvee) {
                out.println("Il n'y a pas de fonction associé à cette Url. <br>");
                // for (Class<?> class1 : touteslesClasses) {
                //     Map<String, Mapping> lien = createMapping(class1);
                //     for (Map.Entry<K,V> class2 : touteslesClasses) {
                        
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
