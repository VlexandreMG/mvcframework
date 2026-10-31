## SPRINT 10 bis : attribuer une méthode à un rôle

### Objectif
`@Auth` savait déjà dire « route réservée aux utilisateurs connectés ».
Maintenant, on peut aussi exiger un **rôle** :

| Annotation | Condition pour accéder à la route |
|---|---|
| `@Auth` | être connecté |
| `@Auth("admin")` | être connecté **et** avoir le rôle `admin` |

On réutilise le champ `value()` qui existait déjà dans `Auth.java` (`default ""`).
Rien à changer dans l'annotation.

### Les 3 cas gérés par le framework

| Situation | Réponse |
|---|---|
| Pas connecté | redirection vers la page de login (`page-login`) |
| Connecté, mauvais rôle | **403 Forbidden** (il est identifié mais n'a pas le droit) |
| Connecté, bon rôle | la méthode du contrôleur est appelée |

Pourquoi 403 et pas le login ? L'utilisateur est **déjà connecté** : le renvoyer au login
ne servirait à rien. 403 veut dire « je sais qui tu es, mais tu n'as pas le droit ».

---

### Changement 1 : nouvel attribut (`FrontServletController.java`)
```java
String pageLogin;
// Sprint 10 bis : clé du rôle de l'utilisateur dans la session
String cleRole;
```

### Changement 2 : lecture dans `init()`
```java
cleRole = getInitParameter("session-role");
if (cleRole == null) cleRole = "role";
```
Même principe que `session-user` : le framework ne sait pas **sous quel nom** l'appli
range le rôle en session. L'appli le lui dit dans le `web.xml` (`init-param`).
Lu **une seule fois**, au moment où Tomcat crée la servlet.

### Changement 3 : vérification du rôle avant l'`invoke`
```java
if (methodController.isAnnotationPresent(Auth.class)) {
    HttpSession httpSession = request.getSession(false);
    Object user = (httpSession != null) ? httpSession.getAttribute(cleSession) : null;

    // 1. Pas connecté → login
    if (user == null) {
        response.sendRedirect(request.getContextPath() + pageLogin);
        return;
    }

    // 2. Rôle exigé ? (@Auth("admin"))
    String roleExige = methodController.getAnnotation(Auth.class).value();
    if (!roleExige.isEmpty()) {
        Object roleUser = httpSession.getAttribute(cleRole);
        if (roleUser == null || !roleExige.equals(roleUser.toString())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Accès réservé au rôle : " + roleExige);
            return;
        }
    }
}
```
- `getAnnotation(Auth.class).value()` lit ce qu'il y a entre parenthèses : `@Auth("admin")` → `"admin"`.
- `value()` vide (`@Auth` seul) → on ne vérifie que la connexion. Les anciennes routes ne changent pas.
- `return` après `sendRedirect` / `sendError` : sinon le code continue et appelle quand même la méthode.
- Ici `httpSession` ne peut pas être `null` : on y a trouvé `user` juste avant.

---

### Côté appli (à faire dans `monapp`)

**`web.xml`**, dans le `<servlet>` du FrontController :
```xml
<init-param>
    <param-name>session-role</param-name>
    <param-value>role</param-value>
</init-param>
```

**Au login**, ranger aussi le rôle (ici le `poste` de l'`Employe`) :
```java
@Controller(value = "/login", method = "POST")
public String login(Session session, Employe user) {
    session.put("user", user);
    session.put("role", user.getPoste());
    return "hello.jsp";
}
```

**Une route réservée aux admins** :
```java
@Auth("admin")
@Controller(value = "/admin")
public String admin() {
    return "hello.jsp";
}
```

---

### Test
1. `mvn clean package` → copier le jar dans `monapp/src/main/webapp/WEB-INF/lib/` → `./deployTestApplication.sh`
2. Pas connecté → `/testApplication/admin` → **redirigé vers le login**
3. Connecté avec poste = `Dev` → `/profil` **s'affiche**, `/admin` → **403**
4. Navigation privée, connecté avec poste = `admin` → `/admin` **s'affiche**

### Limites (volontaires, à voir plus tard)
- Un seul rôle par route. Pour `@Auth({"admin", "manager"})` → passer `value()` en `String[]`.
- `@Auth` seulement sur les méthodes (`@Target(METHOD)`), pas sur une classe entière.
