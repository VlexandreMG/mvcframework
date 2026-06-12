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