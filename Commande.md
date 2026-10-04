mvn clean compile -e (Recommandé) : Nettoie le dossier target/ des anciennes compilations avant de recompiler tout le code source à neuf.

mvn package : Compile le code et génère le fichier livrable (un fichier .jar ou .war selon ton projet) dans le dossier target/.

mvn clean package : Nettoie, recompile et génère le fichier .jar / .war propre.

mvn test : Compile le code et exécute les tests unitaires.

mvn clean install : Compile, teste, empaquette et installe le résultat (.jar) dans ton dépôt local ~/.m2/repository pour qu'il soit utilisable par d'autres projets sur ta machine.