### SPRINT 2

## La fonction qui liste tous ce que je fais 
```java
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
```
-> Niveau performance , ça coûte beaucoup de le 
mettre en static ? 
    -> Non , performant kokoa satria tsy mila manao instance tsony.
-> Critère de vérification que j'ai oublié 
    -> Annotation vérifiena hoe public . 
        -> Explique le Modifier 
            -> Modifier , c'est des modificateurs (public , private , protected) , du coup , le getModifiers , c'est getteo ny modificateur anle methode.

```java
    for (Map.Entry<Controller, Method> entry : annotees.entrySet()) {
                    Method methode = entry.getValue();
                    Controller ctl = entry.getKey();
                    out.println(methode.getName()+"||"+ ctl.value() + "<br>");
                }
```
-> Explique le Entry 
    -> Interface , représente une seul ligne (clé/valeur)
        -> Pourquoi avoir mis le entry ? 
            -> Misy blabla ana mémoire be fa , lasa otrn hoe contenu ao anaty boîte le clé/valeur dia mora kokoa manipuler anazy. 
-> Explique le entrySet 
    -> On peut pas parcourir une hashmap , du coup , 
    entrySet dit à la hashmap de se transformer en Set contenant tous les élements de Entry. 
-> Pourquoi getValue() et getKey()
    -> getKey() : Donne accès à la clé de cette ligne , @ ty izy @Controller.
    -> getValue() : Même chose que getKey() mais le objet method no retourneny.  

## Explication an_dalana 
```java
    for (Class<?> class1 : touteslesClasses) {
                
                Map<String, Mapping> link = Utilitaire.createMapping(class1);
                
                if (link.containsKey(urlContenu)) {
                    
                    Mapping mapp = link.get(urlContenu);

                    out.println("Nom de la fonction : "+ mapp.getMethode().getName() + " || " + " Nom de la classe : " + mapp.getClassName().getName() + " || " + " Lien tapé : " + urlContenu + "<br>");
                    trouvee = true;
                    break;
                }
            }
```
    -> Explique le containsKey 
        -> Mi_contenir anle key v , mireturn false na true 
    -> Explicque le get de la map 
        -> Recupère la valeur associé à une key specifique. 

## [ERROR] /home/balou/S4/WebDy/Projet/mvcframework/src/main/java/com/monframework/controller/FrontServletController.java:[63,129] value() is not public in java.lang.String; cannot be accessed from outside package
-> ay ny string kay tsy mila value() tsony 

## Mandefa anle jar any lib ny app 
```xml
    <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.3.1</version>
                <executions>
                    <execution>
                        <id>copy-jar-to-app</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>/home/balou/S4/WebDy/Projet/monapp/WEB-INF/lib</outputDirectory>
                            <resources>
                                <resource>
                                    <directory>${project.build.directory}</directory>
                                    <includes>
                                        <include>${project.build.finalName}.jar</include>
                                    </includes>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```
- Aza adino ny mireload anle pom.xml 