### SPRINT 0

## FrameWorkInitializer -> Mampandefa an'ilay framework 
    - ServletContainerInitializer
        -> Interface pour casser la dépendance du web.xml 

    - Le contenu du paramètre
        -> Set<Class<?>> c
            ->
        -> ServletContext ctx
            -> Contrôle totale anle application web 
    - Un petit contenu du à propos de ce servlet s'impose 
    pour que je puisse le rédiger à la main sur tous ce que j'ai besoin 

```java
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
```

## Url -> Transformer l'url taper par l'user en texte 
```java
package com.monframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Url {
    String value();
}
```
    -> Retention 
        -> Duree de vie anle post-it
            -> SOURCE - detruite après compilation 
               CLASS - reste dans le fichier.class mais invisible lorsque l'app tourne 
               RUNTIME - tsy maty tant que mande le app 
    -> Target
        -> @ inona no tokony apetraka le post-it 
    -> le terme annotation 
        -> Otrn hoe post-it le code 
    -> value()
        -> lasa texte le url 

## Mapping -> ty le class dia ty le methode mifanaraka aminy 
```java
package com.monframework.core;

public class Mapping {
    private String className;  // Ex: "com.monapp.controller.EmpController"
    private String method;     // Ex: "listeEmployes"

    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

    // Getters et Setters
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}
```

## FrontServlet -> Mamoaka anle ataonle fonction 
```java 
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
        mappingUrls.put("/employe-liste", new Mapping("com.monapp.controller.EmpController", "getListe"));
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
```
    -> Pourquoi dans la hashmap , c'est un String avec le Mapping 
        -> Eft String c'est le lien 
    -> Explication sur la fonction init()
        -> Hérité de HttpServlet , izy no manomana anle terrain 
    -> Workflow (corrige moi): 
        - Prend l'url tapée (Pourquoi en substring)
            -> getRequestURI() : /monapp/employe-liste 
               getContextPath() : /monapp
            Zany hoe , alaina le lien dia départ avy eo @ nom anle app maka anle ambiny. 
        - Condition : si l'url d'en haut ressemble à ce qu'il y a dans la mapping 
            - NON : Mamoaka erreur 404 
            - OUI : alain'mappingURLs le notapena 
                    Je ne comprends pas le "on charge" 
                        -> le forname sert à chercher la classe correspondante , et à charger ses instructions en mémoire , puis c'est grâce à ce process que je peux l'executer en bas 
                    On cherche la méthode à executer 
                    On execute la méthode 
                    On affiche les résultats 
    -> Explique le doGet et le doPost 

## Ma question c'est où dans le code transforme le framework en .jar  
    - C'est pas java , c'est Maven qui transforme le framework en jar dans le pom.xml (après compilation)
```xml
<groupId>com.monframework</groupId>
<artifactId>mvcframework</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>jar</packaging>  <-- C'EST CETTE LIGNE ICI ! 
```