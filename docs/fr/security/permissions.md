# Autorisation

Les règles d'usage pour le contrôle des permissions sont:

 - Uniquement sur les opérations de mise à jour (PUT sur le endpoint /persistence/questionnaires/{id})
 - Refus d'accès si l'unité de l'utilisateur courant ne correspond pas à la valeur du champs owner de la ressource enregistrée correspondant à l'id de la ressource soumise.
 
Le contrôle d'accès s'effectue au moyen d'un filtre jersey définit dans le fichier ```OwnerRestrictedFilter.java```. 
Il est activé en ajoutant l'annotation ```@OwnerRestricted``` sur une ressource attendant une payload correspondant à un questionnaire.

Le fichier de test décrivant le comportement de ce filtre est le suivant:

[include](../../../src/test/java/fr/insee/pogues/jersey/TestOwnerRestrictedFilter.java)