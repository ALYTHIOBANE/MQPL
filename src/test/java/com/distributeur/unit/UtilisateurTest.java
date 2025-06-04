package com.distributeur.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.distributeur.Portefeuille;
import com.distributeur.Utilisateur;

/**
 * Tests unitaires pour la classe Utilisateur.
 */
public class UtilisateurTest {

    private Utilisateur utilisateur;
    private Utilisateur utilisateurAvecSolde;

    @BeforeEach
    public void setUp() {
        // Initialisation des utilisateurs pour les tests
        utilisateur = new Utilisateur(1, "John Doe");
        utilisateurAvecSolde = new Utilisateur(2, "Jane Doe", 30000);
    }

    @Test
    public void testConstructeur() {
        assertEquals(1, utilisateur.getId(), "L'ID devrait être 1");
        assertEquals("John Doe", utilisateur.getNom(), "Le nom devrait être 'John Doe'");
        assertEquals(0.0, utilisateur.getSolde(), 0.001, "Le solde initial devrait être 0");
    }

    @Test
    public void testConstructeurAvecSolde() {
        assertEquals(2, utilisateurAvecSolde.getId(), "L'ID devrait être 2");
        assertEquals("Jane Doe", utilisateurAvecSolde.getNom(), "Le nom devrait être 'Jane Doe'");
        assertEquals(30000, utilisateurAvecSolde.getSolde(), 0.001, "Le solde initial devrait être 30000");
    }

    @Test
    public void testGetId() {
        assertEquals(1, utilisateur.getId(), "L'ID devrait être 1");
    }

    @Test
    public void testGetNom() {
        assertEquals("John Doe", utilisateur.getNom(), "Le nom devrait être 'John Doe'");
    }

    @Test
    public void testGetPortefeuille() {
        Portefeuille portefeuille = utilisateur.getPortefeuille();
        assertNotNull(portefeuille, "Le portefeuille ne devrait pas être null");
        assertEquals(0.0, portefeuille.getSolde(), 0.001, "Le solde du portefeuille devrait être 0");
    }

    @Test
    public void testGetSolde() {
        assertEquals(0.0, utilisateur.getSolde(), 0.001, "Le solde devrait être 0");
        assertEquals(30000, utilisateurAvecSolde.getSolde(), 0.001, "Le solde devrait être 30000");
    }

    @Test
    public void testAjouterFonds() {
        utilisateur.ajouterFonds(15000);
        assertEquals(15000, utilisateur.getSolde(), 0.001, "Le solde devrait être 15000");
        
        utilisateurAvecSolde.ajouterFonds(20000);
        assertEquals(50000, utilisateurAvecSolde.getSolde(), 0.001, "Le solde devrait être 50000");
    }

    @Test
    public void testAjouterFondsNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            utilisateur.ajouterFonds(-20.0);
        }, "Devrait lancer une exception pour un montant négatif");
    }

    @Test
    public void testRetirerFonds() {
        // L'utilisateur n'a pas assez de fonds
        assertFalse(utilisateur.retirerFonds(15000), "Le retrait devrait échouer");
        assertEquals(0.0, utilisateur.getSolde(), 0.001, "Le solde ne devrait pas changer");
        
        // L'utilisateur avec solde a assez de fonds
        assertTrue(utilisateurAvecSolde.retirerFonds(15000), "Le retrait devrait réussir");
        assertEquals(15000, utilisateurAvecSolde.getSolde(), 0.001, "Le solde devrait être 15000");
    }

    @Test
    public void testRetirerFondsNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            utilisateur.retirerFonds(-20.0);
        }, "Devrait lancer une exception pour un montant négatif");
    }

    @Test
    public void testToString() {
        String expected = "Utilisateur #1 - John Doe (Solde: 0.0 FCFA)";
        assertEquals(expected, utilisateur.toString(), "La représentation en chaîne devrait être correcte");
        
        String expectedWithBalance = "Utilisateur #2 - Jane Doe (Solde: 30000.0 FCFA)";
        assertEquals(expectedWithBalance, utilisateurAvecSolde.toString(), "La représentation en chaîne devrait être correcte");
    }
}