package com.monframework.controller;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.monframework.annotation.Controller;

public class Utilitaire {
    public static List<Class<?>> getClassesInPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();

        try {
            // 1. Convertir le package (ex: "com.monapp") en chemin de dossier
            // ("com/monapp")
            String packagePath = packageName.replace('.', '/');

            // 2. Demander au ClassLoader de trouver l'URL de ce dossier
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL packageUrl = classLoader.getResource(packagePath);

            if (packageUrl == null) {
                System.out.println("Le package " + packageName + " est introuvable.");
                return classes;
            }

            // 3. Convertir l'URL en fichier physique (Dossier)
            File directory = new File(packageUrl.getFile());

            if (directory.exists() && directory.isDirectory()) {
                // 4. Lister tous les fichiers du dossier
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        // On ne prend que les fichiers qui se terminent par .class
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            // Enlever l'extension ".class" pour avoir juste le nom de la classe
                            String className = file.getName().substring(0, file.getName().length() - 6);

                            // Reconstituer le nom complet (ex: com.monapp.controller.EmpController)
                            String fullClassName = packageName + "." + className;

                            // Charger la classe en mémoire et l'ajouter à la liste
                            Class<?> clazz = Class.forName(fullClassName);
                            classes.add(clazz);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    public static List<Class<?>> getClassesWithAnnotation(String packageName) {
        List<Class<?>> classesWithAnnotation = new ArrayList<>();
        List<Class<?>> classesInPackage = getClassesInPackage(packageName);
        for (Class<?> cp : classesInPackage) {
            if (cp.isAnnotationPresent(Annotation.class)) {
                classesWithAnnotation.add(cp);
            }
        }

        return classesWithAnnotation;
    }

    public static Map<Controller, Method> getAnnotationsWithClasses(Class<?> className) {
        Map<Controller, Method> methodesAnnotess = new HashMap();

        // 1. On vérifie si la classe fournie n'est pas nulle pour éviter les plantages
        if (className != null) {
            // 2. Détecter toutes les méthodes publiques déclarées dans la classe
            Method[] methodesArray = className.getDeclaredMethods();

            for (Method method : methodesArray) {
                if (method.isAnnotationPresent(Controller.class)) {
                   Controller annotation = method.getAnnotation(Controller.class);
                   methodesAnnotess.put(annotation, method);
                }
            }
        } else {
            System.out.println("La classe " + className + "n'existe pas.");
        }

        return methodesAnnotess;
    }
}
