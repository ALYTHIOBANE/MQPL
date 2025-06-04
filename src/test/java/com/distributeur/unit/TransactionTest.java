package com.distributeur.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.distributeur.Boisson;
import com.distributeur.Transaction;

/**
 * Tests unitaires pour la classe Transaction.
 */
public class TransactionTest {

    private Boisson boisson;
    private Transaction transactionReussie;
    private Transaction transactionEchouee;

    @BeforeEach
    public void setUp() {
        // Initialisation d'une boisson et de transactions pour les tests
        boisson = new Boisson(1, "Coca-Cola", 1000, 10);
        transactionReussie = new Transaction(boisson, 1500, 500, true);
        transactionEchouee = new Transaction(boisson, 650, 650, false);
    }

    @Test
    public void testConstructeur() {
        assertNotNull(transactionReussie, "La transaction ne devrait pas être null");
        assertEquals(boisson, transactionReussie.getBoisson(), "La boisson devrait être correcte");
        assertEquals(1500, transactionReussie.getMontantInsere(), 0.001, "Le montant inséré devrait être 1500");
        assertEquals(500, transactionReussie.getMonnaieRendue(), 0.001, "La monnaie rendue devrait être 500");
        assertTrue(transactionReussie.estReussie(), "La transaction devrait être réussie");
    }

    @Test
    public void testGetId() {
        // Les IDs sont auto-incrémentés, donc on vérifie juste qu'ils sont différents
        assertNotEquals(transactionReussie.getId(), transactionEchouee.getId(), "Les IDs devraient être différents");
    }

    @Test
    public void testGetBoisson() {
        assertEquals(boisson, transactionReussie.getBoisson(), "La boisson devrait être correcte");
    }

    @Test
    public void testGetMontantInsere() {
        assertEquals(1500, transactionReussie.getMontantInsere(), 0.001, "Le montant inséré devrait être 1500");
        assertEquals(650, transactionEchouee.getMontantInsere(), 0.001, "Le montant inséré devrait être 650");
    }

    @Test
    public void testGetMonnaieRendue() {
        assertEquals(500, transactionReussie.getMonnaieRendue(), 0.001, "La monnaie rendue devrait être 500");
        assertEquals(650, transactionEchouee.getMonnaieRendue(), 0.001, "La monnaie rendue devrait être 650");
    }

    @Test
    public void testGetDateHeure() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime transactionDate = transactionReussie.getDateHeure();
        
        // Vérifier que la date de la transaction est proche de maintenant (à quelques secondes près)
        assertTrue(transactionDate.isAfter(now.minusSeconds(10)) && 
                   transactionDate.isBefore(now.plusSeconds(10)),
                   "La date de la transaction devrait être proche de maintenant");
    }

    @Test
    public void testEstReussie() {
        assertTrue(transactionReussie.estReussie(), "La transaction devrait être réussie");
        assertFalse(transactionEchouee.estReussie(), "La transaction ne devrait pas être réussie");
    }

    @Test
    public void testToString() {
        String result = transactionReussie.toString();
        
        // Vérifier que la chaîne contient les informations importantes
        assertTrue(result.contains("Transaction #"), "La chaîne devrait contenir l'ID");
        assertTrue(result.contains("Coca-Cola"), "La chaîne devrait contenir le nom de la boisson");
        assertTrue(result.contains("Montant: 1500"), "La chaîne devrait contenir le montant inséré");
        assertTrue(result.contains("Monnaie: 500"), "La chaîne devrait contenir la monnaie rendue");
        assertTrue(result.contains("Réussie: Oui"), "La chaîne devrait indiquer que la transaction est réussie");
    }

    @Test
    public void testTransactionSansBoisson() {
        Transaction transaction = new Transaction(null, 650, 650, false);
        assertNull(transaction.getBoisson(), "La boisson devrait être null");
        assertFalse(transaction.estReussie(), "La transaction ne devrait pas être réussie");
    }
}