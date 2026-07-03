## SPRINT 3 BIS 

## Erreur java 
```java
    "The method forName(String) in the type Class is not applicable for the arguments (Class)"
```
    -> Diso ny argument anle fonction.

## Le pourquoi de tout le bout de code 
```java
    try {
            // Chargena le class
        Class<?> testController = Class.forName(mapp.getClassName().getName());
            //Micréer instance
        Object objetTestController = testController.getConstructor().newInstance();
            //MiGet fonction rehetra 
        Method methodController = mapp.getMethode();
            //Mi_execute anle fonction 
        Object resultat = methodController.invoke(objetTestController);
                        
        if (resultat != null) {
            out.println(resultat.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
```
    -> Explique pourquoi on a procédé a tout cela pour atteindre cet objectif 
        -> Je pense qu'il fallait charger le classe dans la mémoire pour qu'on puisse attenindre la fonction et l'executer 