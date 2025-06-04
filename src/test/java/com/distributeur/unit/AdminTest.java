package com.distributeur.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.distributeur.Admin;
import com.distributeur.Boisson;
import com.distributeur.Distributeur;
import com.distributeur.Transaction;

/**
 * Tests unitaires pour la classe Admin.
 */
public class AdminTest {

    private Admin admin;
    private Distributeur distributeur;
    private Boisson boisson;

    @BeforeEach
    public void setUp() {
        // Initialisation de l'admin et du distributeur pour les tests
        admin = new Admin(1, "Admin");
        distributeur = new Distributeur();
        boisson = new Boisson(1, "Coca-Cola", 1.5, 10);
        distributeur.ajouterBoisson(boisson);
    }

    @Test
    public void testConstructeur() {
        assertEquals(1, admin.getId(), "L'ID devrait être 1");
        assertEquals("Admin", admin.getNom(), "Le nom devrait être 'Admin'");
        assertEquals(0.0, admin.getSolde(), 0.001, "Le solde initial devrait être 0");
    }

    @Test
    public void testConstructeurAvecSolde() {
        Admin adminAvecSolde = new Admin(2, "Super Admin", 100.0);
        assertEquals(2, adminAvecSolde.getId(), "L'ID devrait être 2");
        assertEquals("Super Admin", adminAvecSolde.getNom(), "Le nom devrait être 'Super Admin'");
        assertEquals(100.0, adminAvecSolde.getSolde(), 0.001, "Le solde initial devrait être 100");
    }

    @Test
    public void testRechargerDistributeur() {
        assertTrue(admin.rechargerDistributeur(distributeur, 1, 5), "Le rechargement devrait réussir");
        assertEquals(15, boisson.getQuantiteStock(), "La quantité en stock devrait être 15");
    }

    @Test
    public void testRechargerDistributeurBoissonInexistante() {
        assertFalse(admin.rechargerDistributeur(distributeur, 999, 5), "Le rechargement devrait échouer");
    }

    @Test
    public void testRechargerDistributeurQuantiteNegative() {
        assertFalse(admin.rechargerDistributeur(distributeur, 1, -5), "Le rechargement devrait échouer");
        assertEquals(10, boisson.getQuantiteStock(), "La quantité en stock ne devrait pas changer");
    }

    @Test
    public void testRechargerDistributeurNull() {
        assertFalse(admin.rechargerDistributeur(null, 1, 5), "Le rechargement devrait échouer");
    }

    @Test
    public void testCollecterFonds() {
        // Simuler des ventes pour avoir des fonds dans la caisse
        distributeur.acheterBoisson(1, 2.0); // Prix = 1.5, donc 1.5 dans la caisse
        
        assertEquals(1.5, admin.collecterFonds(distributeur), 0.001, "Le montant collecté devrait être 1.5");
        assertEquals(1.5, admin.getSolde(), 0.001, "Le solde de l'admin devrait être 1.5");
        assertEquals(0.0, distributeur.getMontantCaisse(), 0.001, "La caisse du distributeur devrait être vide");
    }

    @Test
    public void testCollecterFondsCaisseVide() {
        assertEquals(0.0, admin.collecterFonds(distributeur), 0.001, "Le montant collecté devrait être 0");
        assertEquals(0.0, admin.getSolde(), 0.001, "Le solde de l'admin ne devrait pas changer");
    }

    @Test
    public void testCollecterFondsDistributeurNull() {
        assertEquals(0.0, admin.collecterFonds(null), 0.001, "Le montant collecté devrait être 0");
        assertEquals(0.0, admin.getSolde(), 0.001, "Le solde de l'admin ne devrait pas changer");
    }

    @Test
    public void testConsulterVentes() {
        // Simuler des ventes
        distributeur.acheterBoisson(1, 2.0);
        distributeur.acheterBoisson(1, 2.0);
        
        List<Transaction> ventes = admin.consulterVentes(distributeur);
        assertEquals(2, ventes.size(), "Il devrait y avoir 2 transactions");
    }

    @Test
    public void testConsulterVentesAucuneVente() {
        List<Transaction> ventes = admin.consulterVentes(distributeur);
        assertTrue(ventes.isEmpty(), "Il ne devrait pas y avoir de transactions");
    }

    @Test
    public void testConsulterVentesDistributeurNull() {
        List<Transaction> ventes = admin.consulterVentes(null);
        assertTrue(ventes.isEmpty(), "Il ne devrait pas y avoir de transactions");
    }

    @Test
    public void testToString() {
        String expected = "Admin Utilisateur #1 - Admin (Solde: 0.0 FCFA)";
        assertEquals(expected, admin.toString(), "La représentation en chaîne devrait être correcte");
    }
}