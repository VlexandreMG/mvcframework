package com.monframework.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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

        if (touteslesClasses != null && !touteslesClasses.isEmpty()) {
            for (Class<?> class1 : touteslesClasses) {
                String className = class1.getName();
                out.println(className + "<br>");
                List<Class<?>> fonctionClasses = Utilitaire.getFunctionsInClass(class1);
                for (Class<?> class2 : fonctionClasses) {
                    out.println("Fonction :"+ class2);
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
