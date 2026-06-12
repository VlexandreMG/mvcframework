package com.monframework.core;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;

public class FrontServlet extends HttpServlet {

    // Notre table de routage : Clé = URL, Valeur = Classe + Méthode à appeler
    private HashMap<String, Mapping> mappingUrls = new HashMap<>();

    @Override
    public void init() throws ServletException {
        // Pour le MVP, on enregistre manuellement une route fictive qui sera dans l'App Web plus tard
        // URL: "/employe-liste" -> correspondra à la classe EmpController et sa méthode "getListe"
        mappingUrls.put("monapp//employe-liste", new Mapping("com.monapp.controller.EmpController", "getListe"));
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 1. On récupère l'URL tapée
        String urlContenu = request.getRequestURI().substring(request.getContextPath().length());

        // 2. On vérifie si notre framework connaît cette URL
        if (mappingUrls.containsKey(urlContenu)) {
            Mapping mapping = mappingUrls.get(urlContenu);

            try {
                // --- INSTANTIATION DYNAMIQUE (Java Reflection) ---
                // On charge la classe de l'application web par son nom
                Class<?> clazz = Class.forName(mapping.getClassName());
                Object targetObject = clazz.getDeclaredConstructor().newInstance();

                // On cherche la méthode à exécuter
                Method method = clazz.getDeclaredMethod(mapping.getMethod());

                // On exécute la méthode. Supposons qu'elle retourne une String (du HTML ou du texte)
                Object result = method.invoke(targetObject);

                // On affiche le résultat renvoyé par l'application web
                out.println(result.toString());

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("<h1>Erreur Framework</h1>");
                out.println("<p>Impossible d'exécuter la méthode : " + e.getMessage() + "</p>");
                e.printStackTrace(out);
            }
        } else {
            // 3. Si l'URL n'existe pas dans le framework -> Erreur 404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("<h1>Erreur 404</h1>");
            out.println("<p>L'URL <strong>" + urlContenu + "</strong> n'est pas reconnue par le framework.</p>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}