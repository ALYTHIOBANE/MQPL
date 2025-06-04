# Diagramme de Classes - Distributeur Automatique de Boissons

```mermaid
classDiagram
    class Distributeur {
        -List~Boisson~ boissons
        -Portefeuille caisse
        -JournalVentes journal
        +List~Boisson~ afficherBoissonsDisponibles()
        +Transaction acheterBoisson(int idBoisson, double montant)
        +boolean rechargerStock(int idBoisson, int quantite)
        +double getMontantCaisse()
        +List~Transaction~ getHistoriqueVentes()
    }

    class Boisson {
        -int id
        -String nom
        -double prix
        -int quantiteStock
        +int getId()
        +String getNom()
        +double getPrix()
        +int getQuantiteStock()
        +void setQuantiteStock(int quantite)
        +boolean estDisponible()
        +boolean diminuerStock()
    }

    class Transaction {
        -int id
        -Boisson boisson
        -double montantInsere
        -double monnaieRendue
        -LocalDateTime dateHeure
        -boolean reussie
        +int getId()
        +Boisson getBoisson()
        +double getMontantInsere()
        +double getMonnaieRendue()
        +LocalDateTime getDateHeure()
        +boolean estReussie()
    }

    class Portefeuille {
        -double solde
        +double getSolde()
        +void ajouterFonds(double montant)
        +boolean retirerFonds(double montant)
        +void reinitialiser()
    }

    class JournalVentes {
        -List~Transaction~ transactions
        +void ajouterTransaction(Transaction transaction)
        +List~Transaction~ getTransactions()
        +List~Transaction~ getTransactionsParDate(LocalDate date)
        +double getChiffreAffaires()
    }

    class Utilisateur {
        -int id
        -String nom
        -Portefeuille portefeuille
        +int getId()
        +String getNom()
        +Portefeuille getPortefeuille()
        +double getSolde()
        +void ajouterFonds(double montant)
    }

    class Admin {
        +boolean rechargerDistributeur(Distributeur distributeur, int idBoisson, int quantite)
        +double collecterFonds(Distributeur distributeur)
        +List~Transaction~ consulterVentes(Distributeur distributeur)
    }

    Distributeur "1" *-- "*" Boisson : contient
    Distributeur "1" *-- "1" Portefeuille : possède
    Distributeur "1" *-- "1" JournalVentes : enregistre
    JournalVentes "1" *-- "*" Transaction : stocke
    Transaction "*" -- "1" Boisson : concerne
    Utilisateur "1" *-- "1" Portefeuille : possède
    Admin --|> Utilisateur : est un
```

## Description des classes

### Distributeur

Cette classe représente le distributeur automatique de boissons. Elle gère la liste des boissons disponibles, les transactions d'achat et le rechargement du stock.

### Boisson

Cette classe représente une boisson disponible dans le distributeur. Elle contient les informations sur le nom, le prix et la quantité en stock de la boisson.

### Transaction

Cette classe représente une transaction d'achat. Elle enregistre les détails de la transaction, comme la boisson achetée, le montant inséré, la monnaie rendue et la date/heure de la transaction.

### Portefeuille

Cette classe gère les montants d'argent. Elle est utilisée à la fois pour la caisse du distributeur et pour le portefeuille de l'utilisateur.

### JournalVentes

Cette classe enregistre toutes les transactions effectuées par le distributeur. Elle permet de consulter l'historique des ventes et de calculer le chiffre d'affaires.

### Utilisateur

Cette classe représente un utilisateur du distributeur. Elle contient les informations sur l'utilisateur et son portefeuille.

### Admin

Cette classe représente un administrateur du système. Elle hérite de la classe Utilisateur et ajoute des fonctionnalités spécifiques comme le rechargement du distributeur et la collecte des fonds.
