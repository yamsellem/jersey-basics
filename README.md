Jersey Basics
=============

Présentation du framework Jersey — sans slide — par paire en 2 heures.

### Introduction

Parallèlement à l'essor de la mobilité et des clients JavaScript, le style d'architecture REST a vu sa popularité s'affirmer. Nombreux sont les ouvrages consacrés au sujet mais peu traitent de son implémentation. Ce slot est l'occasion — pour tout public — de mettre en oeuvre Jersey, l'implémentation JAX-RS de référence, et de revenir sur les fondamentaux de REST. Constitué en grande partie de programmation en TDD par paire, c'est l'occasion d'identifier les ressources d'un système, de définir les représentations échangées entre client et serveur, de naviguer entre ressources et d'aborder les nombreuses problématiques relatives à l'architecture du web (interface unifiée HTTP, application sans état, cache, sécurité, concurrence, etc).

### 0. Code source et déploiement du serveur

Cloner ce repo git puis l'importer dans un IDE. L'annotation @ClassRule en tête des tests déploye un servlet `Jersey` sur un serveur `Grizzly` ou `Jetty` afin de lancer des tests junit via http sur la ressource `ProductResource`. Cette dernière définit les méthodes suivantes, `GET`, `POST` -> `/product` et `GET`, `DELETE` -> `/product/{id}`. Elle produit et consomme la représentation suivante, `{id:long, name:string, price:int}`.

Le `Server` déployé par défaut est `Grizzly`, sa configuration s'effectue dans sa classe. `Jetty`  peut également être utilisé, sa configuration s'effectue via le web.xml.

### 1. ProductResourceXmlTest : 3 minutes

1. Lancer les tests, ils échouent : la console affiche les échanges http
2. Afin d'activer le marshalling JAXB sur l'objet Product, l'annoter avec `@XmlRootElement` et `@XmlAccessorType`
3. ProductResource est déjà annotée, regarder rapidement l'utilisation des annotation `@Path`, `@GET` et `@PathParam`
4. Lancer les tests de nouveau, ils passent

--> Regarder la représentation retournéee par le serveur lors du POST, il lui manque des attributs (nous y reviendrons)

### 2. ProductResourceJsonTest : 2 minutes

1. Lancer les tests, ils passent

--> Jersey utilise JAXB pour le marshalling XML et JSON par défaut

### 3. ProductResourceJaxbTest : 31 minutes

1. Lancer les tests, ils échouent
2. Modifier les accès de JAXB sur `Product` en configurant l'annotation `@XmlAccessorType`
3. Lancer les tests, `List` pose problème
4. Utiliser `GenericType` côté client et `GenericEntity` côté serveur afin de résoudre le problème
5. Lancer les tests, `List` fonctionne, seule la suppression pose problème
6. Modifier la méthode `delete({/id})` avec une réponse construite à l'aide du builder de `Response`
7. Lancer les tests, seule la suppression d'un id inexistant ne fonction pas
8. Modifier la méthode `delete({/id})` afin qu'elle retourne un status 404, `Not Found`, lors de la demande de suppression d'un id inexistant
9. Supprimer le code du point 6 et gérer l'erreur via une `NotFoundException`. Et, afin que Jersey soit en mesure de la capturer, créer la classe `NotFoundExceptionMapper` implémentant `ExceptionMapper`. L'annoter `@Provider`.

--> La gestion d'exceptions de Jersey à l'aide de `Providers` suit l'arbre d'héritage à partir des feuilles 

### 4. ProductResourceJacksonTest : 43 minutes

1. Lancer les tests, le premier passe et pas le second pourtant l'assertion est correcte
2. Configurer le serveur Jersey afin qu'il utilise Jackson pour le marshalling Json au lieu de Jaxb (dans `Grizzly` et/ou `Jetty`)
3. Configurer le client Jersey afin qu'il fasse de même à l'aide d'une `ClientConfig`
4. Attention à bien utiliser un client standard `Client.createClient()` pour le premier test et un client avec `ClientConfig` pour les autres
5. Corriger l'assertion du premier test, Jaxb est en mesure de marshaller du JSON standard (même s'il n'en produit pas par défaut)
6. Lancer les tests, ils passent
7. Supprimer les annotations de la représentation `Product`
8. Lancer les tests, ils échouent. Cel signifie que Jackson utilisait les annotations JAXB
9. Annoter `JacksonMapperProvider` avec `@Provider`
10. Lancer les tests, le POST fonctionne, mais pas le GET : le client n'arrive pas à accèder aux propriétés du produit
11. Ajouter le `JacksonMapperProvider` au `ClientConfig` du client
12. Lancer les tests, ils passent

--> Identifier à quel moment le mapper Jackson custom est instancié

--> Rechercher comment customiser le mapper Jaxb de la même manière

### 5. ProductResourceHeaderTest : 8 minutes

1. Lancer les tests, ils échouent
2. Ajouter un header `Cache-Control` sur les méthodes `get({id})` et `post()`
3. Lancer les tests, ils passent
4. Supprimer le code du point 2 et utiliser une instance de `FilterFactory` dans le mapper Jackson custom
5. Lancer les tests, ils passent

--> Identifier pourquoi `CacheControlFilter` doit implèmenter deux interfaces

--> Rechercher comment cette technique peut produire du JSONP sans modifier la ressource

### ∞. And beyond

+ Jersey configuration correspondance web.xml <-> code : http://blogs.oracle.com/PavelBucek/entry/jersey_server_and_client_side
+ Jaxb plus en détails : http://blog.xebia.fr/2011/03/17/jaxb-le-parsing-xml-objet
+ Jackson documentation : http://wiki.fasterxml.com/JacksonDocumentation
+ JavaScript/Jersey du xke de novembre : https://github.com/yamsellem/Backbone-Jersey