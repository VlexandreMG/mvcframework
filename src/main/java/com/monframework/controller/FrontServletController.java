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
import com.monframework.model.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Parameter;

public class FrontServletController extends HttpServlet {

    List<Class<?>> touteslesClasses = new ArrayList<>();
    private HashMap<UrlMapping, Mapping> mapping;
    String annotationRest;

    @Override
    public void init() throws ServletException {
        // touteslesClasses =
        // Utilitaire.getClassesWithAnnotation("com.monapp.controller");
        this.mapping = (HashMap<UrlMapping, Mapping>) getServletContext().getAttribute("mapping");
        annotationRest = (String) getServletContext().getAttribute("annotationRest");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //
        // Prends la requête de l'utilsateur
        String urlContenu = request.getPathInfo() != null ? request.getPathInfo() : request.getServletPath();
        String typeRequete = request.getMethod();
        System.out.println("Recherche de : " + urlContenu + " en " + typeRequete);
        // Condition de cette requête
        //

        boolean trouvee = false;

        if (this.mapping != null && !this.mapping.isEmpty()) {

            // System.out.println("La classe " + class1 + "<br>");

            // Map<UrlMapping, Mapping> link = Utilitaire.createMapping(class1);

            UrlMapping urlRecherche = new UrlMapping(urlContenu, typeRequete);

            if (this.mapping.containsKey(urlRecherche)) {
                Mapping mapp = this.mapping.get(urlRecherche);
                trouvee = true; // On a trouvé la route !

                try {
                    Class<?> testController = Class.forName(mapp.getClassName().getName());
                    Object objetTestController = testController.getConstructor().newInstance();
                    Method methodController = mapp.getMethode();

                    // Lister la liste des params
                    Parameter[] listeParam = methodController.getParameters();

                    // Tableau d'objet dont la taille est listeParam 
                    Object[] arg = new Object[listeParam.length];

                    // Parcourir tous les paramètres 
                    for (int i = 0 ; i < listeParam.length ; i++) {
                            // Prends le nom du parametre
                        String nom = listeParam[i].getName();
                            // Les associe au name dans l'input 
                        String valeur = request.getParameter(nom);
                            // Prend le type du paramètre
                        Class<?> type = listeParam[i].getType();

                        // Vériefie si le type attendue est de type String pour l'instant 
                        if (type == String.class) arg[i] = valeur;
                    }


                    // On exécute la fonction (elle renvoie "page.jsp")
                    Object resultat = methodController.invoke(objetTestController, arg);

                    if (resultat != null) {
                        // Drapeau pour voir les rest 
                        boolean estRest = false;

                        if (annotationRest != null && !annotationRest.isEmpty()) {
                            // Charger la classe 
                            Class<? extends Annotation> restClass = (Class<? extends Annotation>) Class.forName(annotationRest);

                            // Verifier s'il possède l'annotation 
                            if (methodController.isAnnotationPresent(restClass)) {
                                estRest = true;
                            }
                        }

                        // JSON OU VUE 
                        if (estRest) {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                            String jsonOutput = mapper.writeValueAsString(resultat);
                            response.getWriter().write(jsonOutput);
                            return;

                        } 
                            String pageJsp = resultat.toString(); // "page.jsp"

                        // --- TON BLOC SPRINT 5 (Récupération du ModelAndView et setAttribute) ---
                        try {
                            Method getMvMethod = testController.getMethod("getMv");
                            Object modelAndViewObjet = getMvMethod.invoke(objetTestController);

                            if (modelAndViewObjet != null) {
                                Method getDataMethod = modelAndViewObjet.getClass().getMethod("getData");
                                java.util.HashMap<String, Object> données = (java.util.HashMap<String, Object>) getDataMethod
                                        .invoke(modelAndViewObjet);

                                for (java.util.Map.Entry<String, Object> entry : données.entrySet()) {
                                    request.setAttribute(entry.getKey(), entry.getValue());
                                }
                            }
                        } catch (NoSuchMethodException e) {
                            System.out.println("[SPRINT 4] Pas de ModelAndView.");
                        }    
                        
                         // "page.jsp"

                        // --- TON BLOC SPRINT 5 (Récupération du ModelAndView et setAttribute) ---
                        try {
                            Method getMvMethod = testController.getMethod("getMv");
                            Object modelAndViewObjet = getMvMethod.invoke(objetTestController);

                            if (modelAndViewObjet != null) {
                                Method getDataMethod = modelAndViewObjet.getClass().getMethod("getData");
                                java.util.HashMap<String, Object> données = (java.util.HashMap<String, Object>) getDataMethod
                                        .invoke(modelAndViewObjet);

                                for (java.util.Map.Entry<String, Object> entry : données.entrySet()) {
                                    request.setAttribute(entry.getKey(), entry.getValue());
                                }
                            }
                        } catch (NoSuchMethodException e) {
                            System.out.println("[SPRINT 4] Pas de ModelAndView.");
                        }
                        // --- FIN BLOC SPRINT 5 ---

                        // ON SUPPRIME le out.println(resultat.toString()) ET ON FAIT LE VRAI FORWARD :
                        request.getRequestDispatcher("/" + pageJsp).forward(request, response);
                    }
                } catch (Exception e) {
                    if (!response.isCommitted()) {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
                    }
                }
            }

            if (!trouvee) {
                // out.println("HSeeeee Il n'y a pas de fonction associé à cette Url. <br>");
                // for (Class<?> class1 : touteslesClasses) {
                // Map<String, Mapping> lien = Utilitaire.createMapping(class1);
                // for (Map.Entry<String,Mapping> ln : lien.entrySet()) {
                // String url = ln.getKey();
                // Mapping map = ln.getValue();
                // out.println("Nom de la fonction : "+ map.getMethode().getName() + " || " + "
                // Url correspondant : " + url + "<br>");
                // }
                // }
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Aucune route pour " + typeRequete + " " + urlContenu);
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
