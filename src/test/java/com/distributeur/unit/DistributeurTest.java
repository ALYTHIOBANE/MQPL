package com.distributeur.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.distributeur.Boisson;
import com.distributeur.Distributeur;
import com.distributeur.JournalVentes;
import com.distributeur.Transaction;

/**
 * Tests unitaires pour la classe Distributeur.
 */
public class DistributeurTest {

    private Distributeur distributeur;
    private Boisson boisson1;
    private Boisson boisson2;

    @BeforeEach
    public void setUp() {
        // Initialisation du distributeur et des boissons pour les tests
        distributeur = new Distributeur();
        boisson1 = new Boisson(1, "Coca-Cola", 1.5, 10);
        boisson2 = new Boisson(2, "Eau minérale", 1.0, 0); // Rupture de stock
        
        distributeur.ajouterBoisson(boisson1);
        distributeur.ajouterBoisson(boisson2);
    }

    @Test
    public void testConstructeur() {
        Distributeur d = new Distributeur();
        assertNotNull(d, "Le distributeur ne devrait pas être null");
        assertTrue(d.getToutesBoissons().isEmpty(), "La liste des boissons devrait être vide");
        assertEquals(0.0, d.getMontantCaisse(), 0.001, "La caisse devrait être vide");
    }

    @Test
    public void testAjouterBoisson() {
        Boisson nouvelleBoisson = new Boisson(3, "Jus d'orange", 2.0, 5);
        assertTrue(distributeur.ajouterBoisson(nouvelleBoisson), "L'ajout devrait réussir");
        assertEquals(3, distributeur.getToutesBoissons().size(), "Il devrait y avoir 3 boissons");
    }

    @Test
    public void testAjouterBoissonNull() {
        assertFalse(distributeur.ajouterBoisson(null), "L'ajout devrait échouer");
        assertEquals(2, distributeur.getToutesBoissons().size(), "Le nombre de boissons ne devrait pas changer");
    }

    @Test
    public void testAjouterBoissonIdExistant() {
        Boisson boissonMemeId = new Boisson(1, "Autre Coca", 1.8, 5);
        assertFalse(distributeur.ajouterBoisson(boissonMemeId), "L'ajout devrait échouer");
        assertEquals(2, distributeur.getToutesBoissons().size(), "Le nombre de boissons ne devrait pas changer");
    }

    @Test
    public void testAfficherBoissonsDisponibles() {
        List<Boisson> disponibles = distributeur.afficherBoissonsDisponibles();
        assertEquals(1, disponibles.size(), "Il devrait y avoir 1 boisson disponible");
        assertEquals(boisson1, disponibles.get(0), "La boisson disponible devrait être boisson1");
    }

    @Test
    public void testGetToutesBoissons() {
        List<Boisson> toutes = distributeur.getToutesBoissons();
        assertEquals(2, toutes.size(), "Il devrait y avoir 2 boissons au total");
        assertTrue(toutes.contains(boisson1), "La liste devrait contenir boisson1");
        assertTrue(toutes.contains(boisson2), "La liste devrait contenir boisson2");
    }

    @Test
    public void testRechercherBoisson() {
        assertEquals(boisson1, distributeur.rechercherBoisson(1), "Devrait trouver boisson1");
        assertEquals(boisson2, distributeur.rechercherBoisson(2), "Devrait trouver boisson2");
        assertNull(distributeur.rechercherBoisson(999), "Ne devrait pas trouver de boisson");
    }

    @Test
    public void testAcheterBoissonSucces() {
        Transaction transaction = distributeur.acheterBoisson(1, 2.0);
        
        assertTrue(transaction.estReussie(), "La transaction devrait réussir");
        assertEquals(boisson1, transaction.getBoisson(), "La boisson achetée devrait être boisson1");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0");
        assertEquals(0.5, transaction.getMonnaieRendue(), 0.001, "La monnaie rendue devrait être 0.5");
        assertEquals(9, boisson1.getQuantiteStock(), "Le stock devrait être diminué à 9");
        assertEquals(1.5, distributeur.getMontantCaisse(), 0.001, "La caisse devrait contenir 1.5");
    }

    @Test
    public void testAcheterBoissonMontantInsuffisant() {
        Transaction transaction = distributeur.acheterBoisson(1, 1.0);
        
        assertFalse(transaction.estReussie(), "La transaction devrait échouer");
        assertEquals(boisson1, transaction.getBoisson(), "La boisson concernée devrait être boisson1");
        assertEquals(1.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 1.0");
        assertEquals(1.0, transaction.getMonnaieRendue(), 0.001, "La monnaie rendue devrait être 1.0");
        assertEquals(10, boisson1.getQuantiteStock(), "Le stock ne devrait pas changer");
        assertEquals(0.0, distributeur.getMontantCaisse(), 0.001, "La caisse devrait rester vide");
    }

    @Test
    public void testAcheterBoissonRuptureStock() {
        Transaction transaction = distributeur.acheterBoisson(2, 2.0);
        
        assertFalse(transaction.estReussie(), "La transaction devrait échouer");
        assertEquals(boisson2, transaction.getBoisson(), "La boisson concernée devrait être boisson2");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0");
        assertEquals(2.0, transaction.getMonnaieRendue(), 0.001, "La monnaie rendue devrait être 2.0");
        assertEquals(0, boisson2.getQuantiteStock(), "Le stock ne devrait pas changer");
        assertEquals(0.0, distributeur.getMontantCaisse(), 0.001, "La caisse devrait rester vide");
    }

    @Test
    public void testAcheterBoissonInexistante() {
        Transaction transaction = distributeur.acheterBoisson(999, 2.0);
        
        assertFalse(transaction.estReussie(), "La transaction devrait échouer");
        assertNull(transaction.getBoisson(), "La boisson devrait être null");
        assertEquals(2.0, transaction.getMontantInsere(), 0.001, "Le montant inséré devrait être 2.0");
        assertEquals(2.0, transaction.getMonnaieRendue(), 0.001, "La monnaie rendue devrait être 2.0");
        assertEquals(0.0, distributeur.getMontantCaisse(), 0.001, "La caisse devrait rester vide");
    }

    @Test
    public void testAcheterBoissonMontantNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            distributeur.acheterBoisson(1, -1.0);
        }, "Devrait lancer une exception pour un montant négatif");
    }

    @Test
    public void testRechargerStock() {
        assertTrue(distributeur.rechargerStock(2, 5), "Le rechargement devrait réussir");
        assertEquals(5, boisson2.getQuantiteStock(), "Le stock devrait être 5");
    }

    @Test
    public void testRechargerStockBoissonInexistante() {
        assertFalse(distributeur.rechargerStock(999, 5), "Le rechargement devrait échouer");
    }

    @Test
    public void testRechargerStockQuantiteNegative() {
        assertFalse(distributeur.rechargerStock(1, -5), "Le rechargement devrait échouer");
        assertEquals(10, boisson1.getQuantiteStock(), "Le stock ne devrait pas changer");
    }

    @Test
    public void testGetMontantCaisse() {
        assertEquals(0.0, distributeur.getMontantCaisse(), 0.001, "La caisse devrait être vide initialement");
        
        distributeur.acheterBoisson(1, 2.0); // Prix = 1.5, donc 1.5 dans la caisse
        assertEquals(1.5, distributeur.getMontantCaisse(), 0.001, "La caisse devrait contenir 1.5");
    }

    @Test
    public void testViderCaisse() {
        distributeur.acheterBoisson(1, 2.0); // Prix = 1.5, donc 1.5 dans la caisse
        assertEquals(1.5, distributeur.viderCaisse(), 0.001, "Le montant vidé devrait être 1.5");
        assertEquals(0.0, distributeur.getMontantCaisse(), 0.001, "La caisse devrait être vide");
    }

    @Test
    public void testGetHistoriqueVentes() {
        assertTrue(distributeur.getHistoriqueVentes().isEmpty(), "L'historique devrait être vide initialement");
        
        distributeur.acheterBoisson(1, 2.0);
        assertEquals(1, distributeur.getHistoriqueVentes().size(), "L'historique devrait contenir 1 transaction");
    }

    @Test
    public void testGetJournal() {
        JournalVentes journal = distributeur.getJournal();
        assertNotNull(journal, "Le journal ne devrait pas être null");
        assertTrue(journal.getTransactions().isEmpty(), "Le journal devrait être vide initialement");
    }
}