## SPRINT 3 

## Nammpande an'ilay poste 
```bash
    curl -X POST http://localhost:8080/DOdola/ctl/andrana2
```

```bash
    -X POST 
```
-> De base rehefa manao curl dia en GET le request , fa là on le force à utiliser POST. 

## Dans la class UrlMapping 
```java
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlMapping mapping2 = (UrlMapping) o;
        return Objects.equals(url, mapping2.url) && 
               Objects.equals(httpMethod, mapping2.httpMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpMethod);
    }
```
    -> Fonctionnement du hashCode()
        -> Prend les données de l'objet et le transforme en nombre entier. ce nombre sera l'étiquette dans l'armoire (hashmap) et quand on fait link.containsKey(urlRecherche) , il compare directement le nombre.

        -> une hashmap , c'est un gros dictionnaire qui contient une liste de clé valeur. 

        -> link.containsKey(urlRecherche) , link c'est le hashmap et il chèque raha misy anle urlRecherche ao @ ilay liste. 

        -> Le fonction hash mande isak'mikasika anle hashmap.

    -> Fonctionnement du equals()
        -> Compare le texte/contenu et non via adresse mémoire comme "==".
         
        -> Rehefa mifanaraka le hashcode sy le bucket dia zay vô mande le equals. 

        -> Dia ampifanarahan'zareo le fandehany.