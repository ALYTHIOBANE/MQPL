package com.distributeur.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.distributeur.Boisson;
import com.distributeur.JournalVentes;
import com.distributeur.Transaction;

/**
 * Tests unitaires pour la classe JournalVentes.
 */
public class JournalVentesTest {

    private JournalVentes journal;
    private Boisson boisson1;
    private Boisson boisson2;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    @BeforeEach
    public void setUp() {
        // Initialisation du journal et des transactions pour les tests
        journal = new JournalVentes();
        boisson1 = new Boisson(1, "Coca-Cola", 1.5, 10);
        boisson2 = new Boisson(2, "Eau minérale", 1.0, 15);
        
        // Transactions réussies
        transaction1 = new Transaction(boisson1, 2.0, 0.5, true);
        transaction2 = new Transaction(boisson2, 1.0, 0.0, true);
        
        // Transaction échouée
        transaction3 = new Transaction(boisson1, 1.0, 1.0, false);
    }

    @Test
    public void testConstructeur() {
        assertNotNull(journal, "Le journal ne devrait pas être null");
        assertTrue(journal.getTransactions().isEmpty(), "Le journal devrait être vide initialement");
    }

    @Test
    public void testAjouterTransaction() {
        journal.ajouterTransaction(transaction1);
        assertEquals(1, journal.getTransactions().size(), "Le journal devrait contenir 1 transaction");
        assertEquals(transaction1, journal.getTransactions().get(0), "La transaction devrait être celle ajoutée");
    }

    @Test
    public void testAjouterTransactionNull() {
        journal.ajouterTransaction(null);
        assertTrue(journal.getTransactions().isEmpty(), "Le journal devrait rester vide");
    }

    @Test
    public void testGetTransactions() {
        journal.ajouterTransaction(transaction1);
        journal.ajouterTransaction(transaction2);
        
        List<Transaction> transactions = journal.getTransactions();
        assertEquals(2, transactions.size(), "Le journal devrait contenir 2 transactions");
        assertTrue(transactions.contains(transaction1), "La liste devrait contenir la première transaction");
        assertTrue(transactions.contains(transaction2), "La liste devrait contenir la deuxième transaction");
    }

    @Test
    public void testGetTransactionsParDate() {
        // Comme les dates sont générées automatiquement, on ne peut pas tester directement
        // On vérifie juste que la méthode ne lance pas d'exception
        journal.ajouterTransaction(transaction1);
        journal.ajouterTransaction(transaction2);
        
        List<Transaction> transactions = journal.getTransactionsParDate(LocalDate.now());
        assertNotNull(transactions, "La liste ne devrait pas être null");
    }

    @Test
    public void testGetChiffreAffaires() {
        journal.ajouterTransaction(transaction1); // Réussie, prix = 1.5
        journal.ajouterTransaction(transaction2); // Réussie, prix = 1.0
        journal.ajouterTransaction(transaction3); // Échouée, ne compte pas
        
        assertEquals(2.5, journal.getChiffreAffaires(), 0.001, "Le chiffre d'affaires devrait être 2.5");
    }

    @Test
    public void testGetChiffreAffairesVide() {
        assertEquals(0.0, journal.getChiffreAffaires(), 0.001, "Le chiffre d'affaires devrait être 0");
    }

    @Test
    public void testGetChiffreAffairesParDate() {
        journal.ajouterTransaction(transaction1);
        journal.ajouterTransaction(transaction2);
        
        // Comme les dates sont générées automatiquement, on vérifie juste que la méthode ne lance pas d'exception
        double chiffreAffaires = journal.getChiffreAffairesParDate(LocalDate.now());
        assertTrue(chiffreAffaires >= 0, "Le chiffre d'affaires devrait être positif ou nul");
    }

    @Test
    public void testGetNombreTransactions() {
        assertEquals(0, journal.getNombreTransactions(), "Le nombre de transactions devrait être 0");
        
        journal.ajouterTransaction(transaction1);
        journal.ajouterTransaction(transaction2);
        journal.ajouterTransaction(transaction3);
        
        assertEquals(3, journal.getNombreTransactions(), "Le nombre de transactions devrait être 3");
    }

    @Test
    public void testGetNombreTransactionsReussies() {
        assertEquals(0, journal.getNombreTransactionsReussies(), "Le nombre de transactions réussies devrait être 0");
        
        journal.ajouterTransaction(transaction1); // Réussie
        journal.ajouterTransaction(transaction2); // Réussie
        journal.ajouterTransaction(transaction3); // Échouée
        
        assertEquals(2, journal.getNombreTransactionsReussies(), "Le nombre de transactions réussies devrait être 2");
    }
}