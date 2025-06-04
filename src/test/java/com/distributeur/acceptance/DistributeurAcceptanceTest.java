package com.distributeur.acceptance;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.distributeur.Admin;
import com.distributeur.Boisson;
import com.distributeur.Distributeur;
import com.distributeur.Transaction;
import com.distributeur.Utilisateur;

/**
 * Tests d'acceptance pour le distributeur automatique de boissons.
 * Ces tests simulent des scénarios complets d'utilisation du système.
 */
public class DistributeurAcceptanceTest {

    private Distributeur distributeur;
    private Utilisateur utilisateur;
    private Admin admin;

    @BeforeEach
    public void setUp() {
        // Initialisation du distributeur, de l'utilisateur et de l'admin pour les tests
        distributeur = new Distributeur();
        utilisateur = new Utilisateur(1, "Client", 10000.0); // Solde initial en FCFA
        admin = new Admin(2, "Administrateur");

        // Ajout de quelques boissons au distributeur
        distributeur.ajouterBoisson(new Boisson(1, "Coca-Cola", 1000, 10));
        distributeur.ajouterBoisson(new Boisson(2, "Eau minérale", 650, 15));
        distributeur.ajouterBoisson(new Boisson(3, "Jus d'orange", 1300, 8));
        distributeur.ajouterBoisson(new Boisson(4, "Café", 500, 0)); // Rupture de stock
        distributeur.ajouterBoisson(new Boisson(5, "Thé", 450, 12));
    }

    @Test
    @DisplayName("Scénario 1: Affichage des boissons disponibles")
    public void testAffichageBoissonsDisponibles() {
        // Contexte: Un utilisateur veut voir les boissons disponibles dans le distributeur
        // Action: L'utilisateur demande la liste des boissons disponibles
        List<Boisson> boissonsDisponibles = distributeur.afficherBoissonsDisponibles();

        // Résultat attendu: La liste contient toutes les boissons avec un stock > 0
        assertEquals(4, boissonsDisponibles.size(), "Il devrait y avoir 4 boissons disponibles");
        assertFalse(boissonsDisponibles.stream().anyMatch(b -> b.getId() == 4), 
                "Le café (en rupture de stock) ne devrait pas être dans la liste");
    }

    @Test
    @DisplayName("Scénario 2: Achat d'une boisson avec succès")
    public void testAchatBoissonSucces() {
        // Contexte: Un utilisateur veut acheter une boisson avec un montant suffisant
        double soldeInitial = utilisateur.getSolde();
        Boisson boisson = distributeur.rechercherBoisson(1); // Coca-Cola à 1000 FCFA
        int stockInitial = boisson.getQuantiteStock();

        // Action: L'utilisateur achète la boisson
        utilisateur.retirerFonds(boisson.getPrix());
        Transaction transaction = distributeur.acheterBoisson(boisson.getId(), boisson.getPrix());

        // Résultat attendu: La transaction réussit, le stock diminue, la caisse augmente
        assertTrue(transaction.estReussie(), "La transaction devrait réussir");
        assertEquals(stockInitial - 1, boisson.getQuantiteStock(), "Le stock devrait diminuer de 1");
        assertEquals(boisson.getPrix(), distributeur.getMontantCaisse(), "La caisse devrait augmenter du prix de la boisson");
        assertEquals(soldeInitial - boisson.getPrix(), utilisateur.getSolde(), "Le solde de l'utilisateur devrait diminuer");
    }

    @Test
    @DisplayName("Scénario 3: Achat d'une boisson avec montant insuffisant")
    public void testAchatBoissonMontantInsuffisant() {
        // Contexte: Un utilisateur veut acheter une boisson mais n'a pas assez d'argent
        Boisson boisson = distributeur.rechercherBoisson(3); // Jus d'orange à 1300 FCFA
        double montantInsuffisant = 650; // Moins que le prix

        // Action: L'utilisateur tente d'acheter la boisson
        Transaction transaction = distributeur.acheterBoisson(boisson.getId(), montantInsuffisant);

        // Résultat attendu: La transaction échoue, le stock reste inchangé, la monnaie est rendue
        assertFalse(transaction.estReussie(), "La transaction devrait échouer");
        assertEquals(8, boisson.getQuantiteStock(), "Le stock ne devrait pas changer");
        assertEquals(0.0, distributeur.getMontantCaisse(), "La caisse ne devrait pas changer");
        assertEquals(montantInsuffisant, transaction.getMonnaieRendue(), "Toute la monnaie devrait être rendue");
    }

    @Test
    @DisplayName("Scénario 4: Achat d'une boisson en rupture de stock")
    public void testAchatBoissonRuptureStock() {
        // Contexte: Un utilisateur veut acheter une boisson en rupture de stock
        Boisson boisson = distributeur.rechercherBoisson(4); // Café à 500 FCFA, stock = 0
        double montant = 650; // Plus que le prix

        // Action: L'utilisateur tente d'acheter la boisson
        Transaction transaction = distributeur.acheterBoisson(boisson.getId(), montant);

        // Résultat attendu: La transaction échoue, la monnaie est rendue
        assertFalse(transaction.estReussie(), "La transaction devrait échouer");
        assertEquals(0, boisson.getQuantiteStock(), "Le stock devrait rester à 0");
        assertEquals(0.0, distributeur.getMontantCaisse(), "La caisse ne devrait pas changer");
        assertEquals(montant, transaction.getMonnaieRendue(), "Toute la monnaie devrait être rendue");
    }

    @Test
    @DisplayName("Scénario 5: Achat d'une boisson inexistante")
    public void testAchatBoissonInexistante() {
        // Contexte: Un utilisateur veut acheter une boisson qui n'existe pas
        int idInexistant = 999;
        double montant = 2.0;

        // Action: L'utilisateur tente d'acheter la boisson
        Transaction transaction = distributeur.acheterBoisson(idInexistant, montant);

        // Résultat attendu: La transaction échoue, la monnaie est rendue
        assertFalse(transaction.estReussie(), "La transaction devrait échouer");
        assertNull(transaction.getBoisson(), "La boisson devrait être null");
        assertEquals(0.0, distributeur.getMontantCaisse(), "La caisse ne devrait pas changer");
        assertEquals(montant, transaction.getMonnaieRendue(), "Toute la monnaie devrait être rendue");
    }

    @Test
    @DisplayName("Scénario 6: Recharge du stock d'une boisson")
    public void testRechargeStock() {
        // Contexte: Un administrateur veut recharger le stock d'une boisson
        Boisson boisson = distributeur.rechercherBoisson(4); // Café à 0.8€, stock = 0
        int quantiteAjout = 5;

        // Action: L'administrateur recharge le stock
        boolean resultat = admin.rechargerDistributeur(distributeur, boisson.getId(), quantiteAjout);

        // Résultat attendu: Le stock est rechargé
        assertTrue(resultat, "Le rechargement devrait réussir");
        assertEquals(quantiteAjout, boisson.getQuantiteStock(), "Le stock devrait être augmenté");
        assertTrue(boisson.estDisponible(), "La boisson devrait maintenant être disponible");
    }

    @Test
    @DisplayName("Scénario 7: Collecte des fonds par l'administrateur")
    public void testCollecteFonds() {
        // Contexte: Des ventes ont été effectuées et l'administrateur veut collecter les fonds
        // Simuler quelques ventes
        distributeur.acheterBoisson(1, 1000); // Coca-Cola à 1000 FCFA
        distributeur.acheterBoisson(2, 650); // Eau minérale à 650 FCFA
        distributeur.acheterBoisson(3, 1950); // Jus d'orange à 1300 FCFA
        
        // Récupérer le montant dans la caisse avant la collecte
        double montantCaisse = distributeur.getMontantCaisse(); // 2950 FCFA

        // Action: L'administrateur collecte les fonds
        double montantCollecte = admin.collecterFonds(distributeur);

        // Résultat attendu: Les fonds sont collectés, la caisse est vidée
        assertEquals(montantCaisse, montantCollecte, "Le montant collecté devrait être égal au montant dans la caisse");
        assertEquals(montantCaisse, admin.getSolde(), "Le solde de l'admin devrait augmenter du montant collecté");
        assertEquals(0.0, distributeur.getMontantCaisse(), "La caisse devrait être vide");
    }

    @Test
    @DisplayName("Scénario 8: Consultation de l'historique des ventes")
    public void testConsultationHistoriqueVentes() {
        // Contexte: Des ventes ont été effectuées et l'administrateur veut consulter l'historique
        // Simuler quelques ventes
        distributeur.acheterBoisson(1, 1000); // Coca-Cola à 1000 FCFA
        distributeur.acheterBoisson(2, 650); // Eau minérale à 650 FCFA
        distributeur.acheterBoisson(3, 1950); // Jus d'orange à 1300 FCFA

        // Action: L'administrateur consulte l'historique des ventes
        List<Transaction> historique = admin.consulterVentes(distributeur);

        // Résultat attendu: L'historique contient toutes les transactions
        assertEquals(3, historique.size(), "L'historique devrait contenir 3 transactions");
        assertEquals(2950, distributeur.getJournal().getChiffreAffaires(), 0.001, 
                "Le chiffre d'affaires devrait être la somme des prix des boissons vendues");
    }

    @Test
    @DisplayName("Scénario 9: Ajout de fonds au portefeuille de l'utilisateur")
    public void testAjoutFondsUtilisateur() {
        // Contexte: Un utilisateur veut ajouter des fonds à son portefeuille
        double soldeInitial = utilisateur.getSolde();
        double montantAjout = 3000;

        // Action: L'utilisateur ajoute des fonds
        utilisateur.ajouterFonds(montantAjout);

        // Résultat attendu: Le solde de l'utilisateur augmente
        assertEquals(soldeInitial + montantAjout, utilisateur.getSolde(), 0.001, 
                "Le solde devrait augmenter du montant ajouté");
    }

    @Test
    @DisplayName("Scénario 10: Tentative d'ajout de fonds négatifs")
    public void testAjoutFondsNegatifsUtilisateur() {
        // Contexte: Un utilisateur tente d'ajouter un montant négatif à son portefeuille
        double soldeInitial = utilisateur.getSolde();

        // Action et résultat attendu: Une exception est levée, le solde reste inchangé
        assertThrows(IllegalArgumentException.class, () -> {
            utilisateur.ajouterFonds(-5.0);
        }, "Devrait lancer une exception pour un montant négatif");
        assertEquals(soldeInitial, utilisateur.getSolde(), 0.001, "Le solde ne devrait pas changer");
    }

    @Test
    @DisplayName("Scénario 11: Achat de plusieurs boissons successives")
    public void testAchatPlusieursBoissonsSuccessives() {
        // Contexte: Un utilisateur veut acheter plusieurs boissons successivement
        double soldeInitial = utilisateur.getSolde(); // 10000 FCFA
        Boisson boisson1 = distributeur.rechercherBoisson(5); // Thé à 400 FCFA
        Boisson boisson2 = distributeur.rechercherBoisson(2); // Eau minérale à 700 FCFA

        // Action: L'utilisateur achète les boissons successivement
        utilisateur.retirerFonds(boisson1.getPrix());
        Transaction transaction1 = distributeur.acheterBoisson(boisson1.getId(), boisson1.getPrix());

        utilisateur.retirerFonds(boisson2.getPrix());
        Transaction transaction2 = distributeur.acheterBoisson(boisson2.getId(), boisson2.getPrix());

        // Résultat attendu: Les deux transactions réussissent, le solde diminue correctement
        assertTrue(transaction1.estReussie(), "La première transaction devrait réussir");
        assertTrue(transaction2.estReussie(), "La deuxième transaction devrait réussir");
        assertEquals(soldeInitial - boisson1.getPrix() - boisson2.getPrix(), utilisateur.getSolde(), 0.001, 
                "Le solde devrait diminuer du prix total des boissons");
        assertEquals(boisson1.getPrix() + boisson2.getPrix(), distributeur.getMontantCaisse(), 0.001, 
                "La caisse devrait contenir la somme des prix des boissons");
    }

    @Test
    @DisplayName("Scénario 14: Cycle complet d'utilisation du distributeur")
    public void testCycleCompletUtilisation() {
        // Contexte: Simulation d'un cycle complet d'utilisation du distributeur

        // 1. L'administrateur vérifie l'état initial
        List<Boisson> toutesBoissons = distributeur.getToutesBoissons();
        assertEquals(5, toutesBoissons.size(), "Il devrait y avoir 5 boissons au total");
        assertEquals(0.0, distributeur.getMontantCaisse(), 0.001, "La caisse devrait être vide initialement");

        // 2. L'utilisateur consulte les boissons disponibles
        List<Boisson> boissonsDisponibles = distributeur.afficherBoissonsDisponibles();
        assertEquals(4, boissonsDisponibles.size(), "Il devrait y avoir 4 boissons disponibles");

        // 3. L'utilisateur achète une boisson
        Boisson boisson = distributeur.rechercherBoisson(1); // Coca-Cola à 1000 FCFA
        double soldeInitial = utilisateur.getSolde();
        utilisateur.retirerFonds(boisson.getPrix());
        Transaction transaction = distributeur.acheterBoisson(boisson.getId(), boisson.getPrix());

        assertTrue(transaction.estReussie(), "La transaction devrait réussir");
        assertEquals(soldeInitial - boisson.getPrix(), utilisateur.getSolde(), 0.001, 
                "Le solde de l'utilisateur devrait diminuer");
        assertEquals(boisson.getPrix(), distributeur.getMontantCaisse(), 0.001, 
                "La caisse devrait contenir le prix de la boisson");

        // 4. L'administrateur recharge une boisson en rupture de stock
        Boisson boissonRupture = distributeur.rechercherBoisson(4); // Café à 500 FCFA, stock = 0
        admin.rechargerDistributeur(distributeur, boissonRupture.getId(), 10);
        assertEquals(10, boissonRupture.getQuantiteStock(), "Le stock devrait être rechargé");

        // 5. L'administrateur collecte les fonds
        double montantCollecte = admin.collecterFonds(distributeur);
        assertEquals(boisson.getPrix(), montantCollecte, 0.001, "Le montant collecté devrait être égal au prix de la boisson");
        assertEquals(0.0, distributeur.getMontantCaisse(), 0.001, "La caisse devrait être vide après la collecte");
        
        // 6. L'administrateur consulte l'historique des ventes
        List<Transaction> historique = admin.consulterVentes(distributeur);
        assertEquals(1, historique.size(), "L'historique devrait contenir 1 transaction");
    }
    
    @Test
    @DisplayName("Scénario 12: Achat jusqu'à épuisement du stock")
    public void testAchatJusquaEpuisementStock() {
        // Contexte: Un utilisateur veut acheter une boisson jusqu'à épuisement du stock
        Boisson boisson = distributeur.rechercherBoisson(3); // Jus d'orange à 1300 FCFA, stock = 8
        int stockInitial = boisson.getQuantiteStock();

        // Action: L'utilisateur achète la boisson jusqu'à épuisement du stock
        for (int i = 0; i < stockInitial; i++) {
            utilisateur.retirerFonds(boisson.getPrix());
            Transaction transaction = distributeur.acheterBoisson(boisson.getId(), boisson.getPrix());
            assertTrue(transaction.estReussie(), "La transaction " + (i+1) + " devrait réussir");
        }

        // Une tentative d'achat supplémentaire devrait échouer
        Transaction transactionEchec = distributeur.acheterBoisson(boisson.getId(), boisson.getPrix());

        // Résultat attendu: Le stock est épuisé, la dernière transaction échoue
        assertEquals(0, boisson.getQuantiteStock(), "Le stock devrait être épuisé");
        assertFalse(transactionEchec.estReussie(), "La transaction supplémentaire devrait échouer");
        assertEquals(stockInitial * boisson.getPrix(), distributeur.getMontantCaisse(), 0.001, 
                "La caisse devrait contenir le prix total des boissons vendues");
    }

    @Test
    @DisplayName("Scénario 13: Recharge d'une boisson inexistante")
    public void testRechargeStockBoissonInexistante() {
        // Contexte: Un administrateur tente de recharger une boisson qui n'existe pas
        int idInexistant = 999;

        // Action: L'administrateur tente de recharger la boisson
        boolean resultat = admin.rechargerDistributeur(distributeur, idInexistant, 5);

        // Résultat attendu: Le rechargement échoue
        assertFalse(resultat, "Le rechargement devrait échouer");
    }

    @Test
    @DisplayName("Scénario 15: Tentative de recharge avec quantité négative")
    public void testRechargeStockQuantiteNegative() {
        // Contexte: Un administrateur tente de recharger une boisson avec une quantité négative
        Boisson boisson = distributeur.rechercherBoisson(1); // Coca-Cola
        int stockInitial = boisson.getQuantiteStock();

        // Action: L'administrateur tente de recharger la boisson avec une quantité négative
        boolean resultat = admin.rechargerDistributeur(distributeur, boisson.getId(), -5);

        // Résultat attendu: Le rechargement échoue, le stock reste inchangé
        assertFalse(resultat, "Le rechargement devrait échouer");
        assertEquals(stockInitial, boisson.getQuantiteStock(), "Le stock ne devrait pas changer");
    }
}