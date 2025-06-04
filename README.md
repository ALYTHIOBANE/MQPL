# Distributeur Automatique de Boissons

Ce projet implémente un système de gestion pour un distributeur automatique de boissons en Java. Il permet aux utilisateurs de consulter les boissons disponibles, d'acheter des boissons, et au personnel administratif de gérer le stock et les fonds.

## Structure du Projet

### Diagramme de Classes

Le diagramme de classes UML complet est disponible dans le fichier `diagramme_classes.md`.

### Classes Principales

- **Distributeur**: Classe principale qui gère l'ensemble du système.
- **Boisson**: Représente une boisson disponible dans le distributeur.
- **Transaction**: Enregistre les détails d'une transaction d'achat.
- **Portefeuille**: Gère les montants d'argent (caisse du distributeur ou portefeuille utilisateur).
- **JournalVentes**: Enregistre l'historique des transactions.
- **Utilisateur**: Représente un utilisateur du distributeur.
- **Admin**: Hérite d'Utilisateur et ajoute des fonctionnalités d'administration.

## Fonctionnalités

- Affichage des boissons disponibles
- Achat de boissons
- Gestion du stock
- Gestion des fonds
- Historique des ventes

## Tests

Le projet inclut deux types de tests :

1. **Tests Unitaires**: Testent chaque classe individuellement pour s'assurer que chaque méthode fonctionne correctement.
2. **Tests d'Acceptance**: Simulent des scénarios complets d'utilisation du système.

## Prérequis

- Java 11 ou supérieur
- Maven

## Installation et Exécution

1. Cloner le dépôt :

   ```
   git clone [URL_DU_DEPOT]
   cd distributeur-boissons
   ```

2. Compiler le projet avec Maven :

   ```
   mvn clean compile
   ```

3. Exécuter les tests :

   ```
   mvn test
   ```

4. Exécuter l'application :
   ```
   mvn exec:java -Dexec.mainClass="com.distributeur.Main"
   ```

## Utilisation

L'application propose un menu interactif avec deux modes :

1. **Mode Utilisateur** :

   - Voir les boissons disponibles
   - Acheter une boisson
   - Ajouter de l'argent au portefeuille

2. **Mode Administrateur** :
   - Voir toutes les boissons (disponibles ou non)
   - Recharger le stock d'une boisson
   - Collecter les fonds
   - Consulter les ventes

## Auteur

[Votre Nom]

## Licence

Ce projet est sous licence [Licence].
