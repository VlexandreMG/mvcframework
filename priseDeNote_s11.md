## SPRINT 11 : les contrôleurs en singleton

### Objectif
Avant, **à chaque requête**, le framework créait un nouveau contrôleur :
```java
Class<?> testController = Class.forName(mapp.getClassName().getName());
Object objetTestController = testController.getConstructor().newInstance();
```
1000 requêtes → 1000 `TestController` créés puis jetés. Inutile.

Maintenant : **une seule instance par classe de contrôleur**, créée **une fois au démarrage**,
réutilisée par toutes les requêtes. C'est le comportement par défaut de Spring pour ses `@Controller`.

### Où ranger les instances ?
Dans une `Map<Class<?>, Object>` (« classe → son unique instance ») mise dans le **`ServletContext`**.
Cette fois c'est le bon endroit : c'est une donnée **globale, commune à toute l'appli**, comme le `mapping`.
(À l'inverse des données d'utilisateur, qui vont dans la `HttpSession`, cf. sprint 9.)

---

### Changement 1 : `AppContextListener.java`, créer les instances au démarrage
```java
HashMap<UrlMapping, Mapping> mapping = new HashMap<>();
// Sprint 11 : classe du contrôleur → son unique instance (singleton)
HashMap<Class<?>, Object> controleurs = new HashMap<>();

for (Class<?> class1 : classes) {
    // Sprint 11 : une seule instance par contrôleur
    controleurs.put(class1, class1.getConstructor().newInstance());

    Map<UrlMapping, Mapping> tableRoutageClasse = Utilitaire.createMapping(class1);
    ...
}
...
context.setAttribute("mapping", mapping);
context.setAttribute("controleurs", controleurs);
```
Bonus : un contrôleur sans constructeur vide plante **au démarrage**, pas au premier clic d'un utilisateur.

### Changement 2 : `FrontServletController.java`, récupérer la Map dans `init()`
```java
// Sprint 11 : instances uniques des contrôleurs (créées par AppContextListener)
private HashMap<Class<?>, Object> controleurs;
...
this.controleurs = (HashMap<Class<?>, Object>) getServletContext().getAttribute("controleurs");
```

### Changement 3 : `FrontServletController.java`, réutiliser au lieu de recréer
```java
Class<?> testController = mapp.getClassName();
// Sprint 11 : on réutilise l'instance unique au lieu d'en créer une
Object objetTestController = this.controleurs.get(testController);
```
- Plus de `newInstance()` par requête.
- Plus de `Class.forName(...)` : `mapp.getClassName()` est **déjà** un objet `Class`.
- Le reste (`methodController.invoke(objetTestController, arg)`, bloc `getMv`...) ne change pas.

### Changement 4 : appli `monapp`, preuve dans `TestController.java`
```java
// Sprint 11 : doit s'afficher une seule fois (singleton)
public TestController() {
    System.out.println("[SPRINT 11] TestController créé : " + this.hashCode());
}
```

---

### ⚠️ La règle du singleton
L'instance est **partagée par tous les utilisateurs en même temps**.
Un **attribut** de contrôleur devient donc commun à tout le monde :
```java
public class TestController {
    private Employe dernier;                 // ❌ partagé par TOUS les utilisateurs
    public String login(Session s, Employe e) {
        dernier = e;                         // Bob écrase Alice
    }
}
```
**Pas de données d'utilisateur dans les attributs d'un contrôleur.**
Elles vont dans : les paramètres de la méthode, la `Session`, ou des variables locales.

Attention au mécanisme `getMv()` du sprint 5 (`ModelAndView` stocké en attribut du contrôleur) :
avec un singleton, il serait partagé entre utilisateurs. Le `TestController` actuel n'a aucun attribut → aucun risque aujourd'hui.

---

### Test (fait)
1. `mvn clean package` → copier le jar dans `monapp/src/main/webapp/WEB-INF/lib/` → `./deployTestApplication.sh`
2. `catalina.out` après redéploiement : **11 requêtes** traitées, **une seule** ligne
   `[SPRINT 11] TestController créé : 2021750141` (au démarrage).
3. Non-régression sprint 10 (avec `curl`) :

| Cas | Résultat |
|---|---|
| Non connecté → `/admin` | 302 → `/testApplication/login` |
| Connecté `Dev` → `/profil` | 200 |
| Connecté `Dev` → `/admin` | 403 |
| Connecté `admin` → `/admin` | 200 |
