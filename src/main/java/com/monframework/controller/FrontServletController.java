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
            //Prends la requête de l'utilsateur  
        String urlContenu = request.getRequestURI().substring(request.getContextPath().length());
            //Condition de cette requête 
        // 

        if (touteslesClasses != null && !touteslesClasses.isEmpty()) {
            for (Class<?> class1 : touteslesClasses) {
                String className = class1.getName();
                out.println("Le nom de la classe : "+ className + "<br>");

                out.println("Les fonctions de cette classe : <br>");

                Map<Controller, Method> annotees = Utilitaire.getAnnotationsWithClasses(class1);

                for (Map.Entry<Controller, Method> entry : annotees.entrySet()) {
                    Method methode = entry.getValue();
                    Controller ctl = entry.getKey();
                    if (urlContenu.equals(ctl.value())) {
                        out.println("Nom de la fonction :"+ methode.getName() + "||" + "Url tapé" + ctl.value() + "||" + "Classe correspndante" + class1.getName() + "br");
                    } else {
                        out.println("Nous ne connaisons pas ce lien :/ <br>");
                        out.println("Voici les liens disponibles : <br>");
                        out.println(methode.getName()+"||"+ ctl.value() + "<br>");
                    }
                }
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
