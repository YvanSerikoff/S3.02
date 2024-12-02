# Rendu de développement efficace

## Implémentation de k-NN

### Description de l’implémentation de l’algorithme k-NN
L'algorithme k-NN est implémenté dans la classe [`Model`](src/main/java/fr/univlille/iut/model/Model.java). Cette classe utilise l'interface [`KNN`](src/main/java/fr/univlille/iut/model/KNN.java) pour définir les méthodes nécessaires à l'algorithme k-NN.
La méthode knn récupère d'abord les k plus proches voisins d'un point donné à l'aide de la distance de son choix (euclidienne ou manhattan), puis détermine la catégorie majoritaire parmi 
ces voisins pour classer le point.


### Trouver les k plus proches voisins

La méthode `nearestNeighbors` de la classe [`Model`](src/main/java/fr/univlille/iut/model/Model.java) est utilisée pour trouver les k plus proches voisins d'un point donné.
La méthode prend en paramètre un point, un ensemble de données, un k et une algorithme de distance et retourne une liste des k plus proches voisins de ce point.


### Méthodes de calcul de la distance
La méthode de calcul de la distance est implémentée dans la classe [`NormalizedEuclideanDistance`](src/main/java/fr/univlille/iut/model/NormalizedEuclideanDistance.java) et [`NormalizedManhattanDistance`](src/main/java/fr/univlille/iut/model/NormalizedManhattanDistance.java). 
Ces classe utilise la méthode `distance` pour calculer la distance euclidienne normalisée entre deux objets.

#### Calcul de la différence
Pour calculer la distance entre deux objets, nous devons d'abord trouver la différence entre les attributs de ces objets. 
Pour cela on appelle la fonction définie dans [`Model`](src/main/java/fr/univlille/iut/model/Model.java) `getDifference` qui retourne un tableau de double contenant les différences normalisées entre les attributs des objets.

#### Normalisation
Afin de normaliser les valeurs, la méthode `getDifference` utilise la méthode `normalize` de la classe [`Model`](src/main/java/fr/univlille/iut/model/Model.java).
Celle-ci récupère les max et min de chaque attribut et normalise les valeurs en utilisant la formule suivante :
a = (a - min) / (max - min)


### Trouver la catégorie majoritaire

La catégorie majoritaire est déterminée par la méthode `determineCategory` des classes [`Pokemon`](src/main/java/fr/univlille/iut/model/Pokemon.java) et [`Iris`](src/main/java/fr/univlille/iut/model/Iris.java).
Celle-ci prend en paramètre une liste de voisins et retourne la catégorie majoritaire parmi ces voisins à l'aide d'une Map associant chaque catégorie des voisins à leur nombre d'occurence.


### Méthodes évaluant la robustesse
La robustesse de l'algorithme KNN est calculé par la méthode `calculateSuccessRate` de la classe [`Model`](src/main/java/fr/univlille/iut/model/Model.java).
Elle prend en paramètre une liste d'objets (Pokemon ou iris) et la catégorie définie par knn sur chaque objet avant de comparer avec la catégorie réelle.
Elle retourne le pourcentage de réussite de l'algorithme.

## Validation croisée

### Méthode de validation croisée
La méthode de validation croisée est expliquée dans la classe [`Model`](src/main/java/fr/univlille/iut/model/Model.java). Les pourcentages sont calculés en divisant les données en plusieurs sous-ensembles (folds), en utilisant chaque sous-ensemble comme ensemble de test et les autres comme ensemble d'apprentissage. Nous avons utilisé une validation croisée à 5 folds.



## Choix du meilleur k

### Résultats pour différents k et distances
Les résultats pour différents k et les deux distances (Manhattan et Euclidienne) sont présentés pour les iris et les pokemons. Les distances sont basées sur différents attributs et/ou pondération.
Vous pouvez aussi les retrouver en lançant la classe de test [`TestKNN`](test/fr/univlille/iut/model/TestKNN.java)

| k | Distance | Iris Accuracy | Pokemon Accuracy |
|---|----------|---------------|------------------|
| 3 | Euclidean| 94.6%         | 20.2%            |
| 5 | Euclidean| 92.6%         | 16.6%            |
| 3 | Manhattan| 94.6%         | 20.2%            |
| 5 | Manhattan| 92.6%         | 19.2%            |

Au vue des résultats de robustesse, nous avons choisi k=3 et la distance euclidienne pour les iris et les pokemons



## Efficacité

### Structures de données utilisées
Pour stocker les données, nous avons choisi d'utiliser des List. Leur accès séquentiel est plus rapide que les tableaux et elles permettent de stocker des objets de manière dynamique, c'est donc un choix de taille pour
l'algorithme k-NN.