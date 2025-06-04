package com.distributeur;

import java.util.List;
import java.util.Scanner;

/**
 * Classe principale pour démontrer le fonctionnement du distributeur automatique.
 */
public class Main {

    private static Distributeur distributeur;
    private static Scanner scanner;
    private static Utilisateur utilisateur;
    private static Admin admin;

    /**
     * Méthode principale.
     * 
     * @param args Arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        // Initialisation
        distributeur = new Distributeur();
        scanner = new Scanner(System.in);
        utilisateur = new Utilisateur(1, "Client", 10.0);
        admin = new Admin(2, "Administrateur");

        // Ajout de quelques boissons au distributeur
        initialiserBoissons(distributeur);

        // Menu principal
        boolean quitter = false;
        while (!quitter) {
            afficherMenuPrincipal();
            int choix = lireEntier("Votre choix: ");

            switch (choix) {
                case 1:
                    menuUtilisateur();
                    break;
                case 2:
                    menuAdmin();
                    break;
                case 3:
                    quitter = true;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }

        scanner.close();
        System.out.println("Au revoir !");
    }

    /**
     * Initialise le distributeur avec quelques boissons.
     */
    private static void initialiserBoissons(Distributeur distributeur) {
        distributeur.ajouterBoisson(new Boisson(1, "Coca-Cola", 1000, 10));
        distributeur.ajouterBoisson(new Boisson(2, "Eau minérale", 650, 15));
        distributeur.ajouterBoisson(new Boisson(3, "Jus d'orange", 1300, 8));
        distributeur.ajouterBoisson(new Boisson(4, "Café", 500, 20));
        distributeur.ajouterBoisson(new Boisson(5, "Thé", 450, 12));
    }

    /**
     * Affiche le menu principal.
     */
    private static void afficherMenuPrincipal() {
        System.out.println("\n=== DISTRIBUTEUR AUTOMATIQUE DE BOISSONS ===");
        System.out.println("1. Mode Utilisateur");
        System.out.println("2. Mode Administrateur");
        System.out.println("3. Quitter");
    }

    /**
     * Gère le menu utilisateur.
     */
    private static void menuUtilisateur() {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== MODE UTILISATEUR ===");
            System.out.println("Solde actuel: " + utilisateur.getSolde() + " FCFA");
            System.out.println("1. Voir les boissons disponibles");
            System.out.println("2. Acheter une boisson");
            System.out.println("3. Ajouter de l'argent");
            System.out.println("4. Retour au menu principal");

            int choix = lireEntier("Votre choix: ");

            switch (choix) {
                case 1:
                    afficherBoissonsDisponibles();
                    break;
                case 2:
                    acheterBoisson();
                    break;
                case 3:
                    ajouterArgent();
                    break;
                case 4:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    /**
     * Gère le menu administrateur.
     */
    private static void menuAdmin() {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== MODE ADMINISTRATEUR ===");
            System.out.println("1. Voir toutes les boissons");
            System.out.println("2. Recharger le stock d'une boisson");
            System.out.println("3. Collecter les fonds");
            System.out.println("4. Consulter les ventes");
            System.out.println("5. Retour au menu principal");

            int choix = lireEntier("Votre choix: ");

            switch (choix) {
                case 1:
                    afficherToutesBoissons();
                    break;
                case 2:
                    rechargerStock();
                    break;
                case 3:
                    collecterFonds();
                    break;
                case 4:
                    consulterVentes();
                    break;
                case 5:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    /**
     * Affiche les boissons disponibles.
     */
    private static void afficherBoissonsDisponibles() {
        System.out.println("\n=== BOISSONS DISPONIBLES ===");
        List<Boisson> boissonsDisponibles = distributeur.afficherBoissonsDisponibles();

        if (boissonsDisponibles.isEmpty()) {
            System.out.println("Aucune boisson disponible actuellement.");
        } else {
            for (Boisson boisson : boissonsDisponibles) {
                System.out.println(boisson);
            }
        }
    }

    /**
     * Affiche toutes les boissons (disponibles ou non).
     */
    private static void afficherToutesBoissons() {
        System.out.println("\n=== TOUTES LES BOISSONS ===");
        List<Boisson> toutesBoissons = distributeur.getToutesBoissons();

        if (toutesBoissons.isEmpty()) {
            System.out.println("Aucune boisson dans le distributeur.");
        } else {
            for (Boisson boisson : toutesBoissons) {
                System.out.println(boisson);
            }
        }
    }

    /**
     * Gère l'achat d'une boisson.
     */
    private static void acheterBoisson() {
        afficherBoissonsDisponibles();

        int idBoisson = lireEntier("\nEntrez l'ID de la boisson que vous souhaitez acheter (0 pour annuler): ");
        if (idBoisson == 0) {
            return;
        }

        Boisson boisson = distributeur.rechercherBoisson(idBoisson);
        if (boisson == null) {
            System.out.println("Boisson non trouvée.");
            return;
        }

        if (!boisson.estDisponible()) {
            System.out.println("Cette boisson n'est pas disponible (rupture de stock).");
            return;
        }

        System.out.println("Prix de la boisson: " + boisson.getPrix() + " FCFA");
        System.out.println("Votre solde: " + utilisateur.getSolde() + " FCFA");

        if (utilisateur.getSolde() < boisson.getPrix()) {
            System.out.println("Solde insuffisant. Veuillez ajouter de l'argent.");
            return;
        }

        // Effectuer l'achat
        utilisateur.retirerFonds(boisson.getPrix());
        Transaction transaction = distributeur.acheterBoisson(idBoisson, boisson.getPrix());

        if (transaction.estReussie()) {
            System.out.println("\nAchat réussi ! Vous avez acheté: " + boisson.getNom());
            System.out.println("Nouveau solde: " + utilisateur.getSolde() + " FCFA");
        } else {
            System.out.println("\nErreur lors de l'achat.");
            utilisateur.ajouterFonds(boisson.getPrix()); // Remboursement
        }
    }

    /**
     * Gère l'ajout d'argent au portefeuille de l'utilisateur.
     */
    private static void ajouterArgent() {
        double montant = lireDouble("\nEntrez le montant à ajouter (FCFA): ");

        try {
            utilisateur.ajouterFonds(montant);
            System.out.println("Montant ajouté avec succès.");
            System.out.println("Nouveau solde: " + utilisateur.getSolde() + " FCFA");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    /**
     * Gère le rechargement du stock d'une boisson.
     */
    private static void rechargerStock() {
        afficherToutesBoissons();

        int idBoisson = lireEntier("\nEntrez l'ID de la boisson à recharger (0 pour annuler): ");
        if (idBoisson == 0) {
            return;
        }

        Boisson boisson = distributeur.rechercherBoisson(idBoisson);
        if (boisson == null) {
            System.out.println("Boisson non trouvée.");
            return;
        }

        int quantite = lireEntier("Entrez la quantité à ajouter: ");

        if (admin.rechargerDistributeur(distributeur, idBoisson, quantite)) {
            System.out.println("Stock rechargé avec succès.");
            System.out.println("Nouveau stock pour " + boisson.getNom() + ": " + boisson.getQuantiteStock());
        } else {
            System.out.println("Erreur lors du rechargement du stock.");
        }
    }

    /**
     * Gère la collecte des fonds du distributeur.
     */
    private static void collecterFonds() {
        double montantCaisse = distributeur.getMontantCaisse();
        System.out.println("\nMontant dans la caisse: " + montantCaisse + " FCFA");

        if (montantCaisse > 0) {
            double montantCollecte = admin.collecterFonds(distributeur);
            System.out.println("Fonds collectés: " + montantCollecte + " FCFA");
            System.out.println("Solde de l'administrateur: " + admin.getSolde() + " FCFA");
        } else {
            System.out.println("La caisse est vide.");
        }
    }

    /**
     * Gère la consultation des ventes.
     */
    private static void consulterVentes() {
        System.out.println("\n=== HISTORIQUE DES VENTES ===");
        List<Transaction> transactions = admin.consulterVentes(distributeur);

        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction enregistrée.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
            System.out.println("\nNombre total de transactions: " + transactions.size());
            System.out.println("Chiffre d'affaires: " + distributeur.getJournal().getChiffreAffaires() + " FCFA");
        }
    }

    /**
     * Lit un entier depuis l'entrée standard.
     * 
     * @param message Le message à afficher
     * @return L'entier lu
     */
    private static int lireEntier(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Veuillez entrer un nombre entier.");
            scanner.next();
            System.out.print(message);
        }
        int valeur = scanner.nextInt();
        scanner.nextLine(); // Consommer la fin de ligne
        return valeur;
    }

    /**
     * Lit un nombre à virgule depuis l'entrée standard.
     * 
     * @param message Le message à afficher
     * @return Le nombre à virgule lu
     */
    private static double lireDouble(String message) {
        System.out.print(message);
        while (!scanner.hasNextDouble()) {
            System.out.println("Veuillez entrer un nombre.");
            scanner.next();
            System.out.print(message);
        }
        double valeur = scanner.nextDouble();
        scanner.nextLine(); // Consommer la fin de ligne
        return valeur;
    }
}