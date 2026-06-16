### SPRINT 1 

## Utilitaire.getClassesInPackage() 
```java
    public static List<Class<?>> getClassesInPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        
        try {
            // 1. Convertir le package (ex: "com.monapp") en chemin de dossier ("com/monapp")
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
``` 
    - Pourquoi il faut remplacer le chemin du repertoire 
        -> Le class loader a besoin d'un chemin standard pour pouvoir lire les fichiers. 
    - C'est quoi le ClassLoader
        ->  Une classe qui permet de charger les classes Java en mémoire. 
    - Le lien avec le thread 
        ->  Parce que la thread permet de charger les classes à n'importe quel moment d'execution. 
    - Thread c'est le flux si je m'en souviens bien 
        -> Oui 
    - Pourquoi convertir l'url en fichier physique 
        -> classLoader.getResource() retourne un objet du genre 
            file:/home/balou/.../WEB-INF/classes/com/monapp/controller 
            alors il faut le convertir en file pour pouvoir voir tous les fichiers
            dedans. 

    - pourquoi je dois les charger en mémoire , je l'entend tous les temps mais c'est quoi son rôle en programmation 
        -> 