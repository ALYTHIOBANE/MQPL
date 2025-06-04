package com.distributeur.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.distributeur.Portefeuille;

/**
 * Tests unitaires pour la classe Portefeuille.
 */
public class PortefeuilleTest {

    private Portefeuille portefeuille;

    @BeforeEach
    public void setUp() {
        // Initialisation d'un portefeuille pour les tests
        portefeuille = new Portefeuille(100.0);
    }

    @Test
    public void testConstructeurParDefaut() {
        Portefeuille p = new Portefeuille();
        assertEquals(0.0, p.getSolde(), 0.001, "Le solde initial devrait être 0");
    }

    @Test
    public void testConstructeurAvecSoldeInitial() {
        assertEquals(100.0, portefeuille.getSolde(), 0.001, "Le solde initial devrait être 100");
    }

    @Test
    public void testConstructeurAvecSoldeInitialNegatif() {
        Portefeuille p = new Portefeuille(-50.0);
        assertEquals(0.0, p.getSolde(), 0.001, "Le solde initial devrait être 0 si la valeur est négative");
    }

    @Test
    public void testGetSolde() {
        assertEquals(100.0, portefeuille.getSolde(), 0.001, "Le solde devrait être 100");
    }

    @Test
    public void testAjouterFonds() {
        portefeuille.ajouterFonds(50.0);
        assertEquals(150.0, portefeuille.getSolde(), 0.001, "Le solde devrait être 150");
    }

    @Test
    public void testAjouterFondsNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.ajouterFonds(-50.0);
        }, "Devrait lancer une exception pour un montant négatif");
    }

    @Test
    public void testRetirerFonds() {
        assertTrue(portefeuille.retirerFonds(50.0), "Le retrait devrait réussir");
        assertEquals(50.0, portefeuille.getSolde(), 0.001, "Le solde devrait être 50");
    }

    @Test
    public void testRetirerFondsMontantEgalAuSolde() {
        assertTrue(portefeuille.retirerFonds(100.0), "Le retrait devrait réussir");
        assertEquals(0.0, portefeuille.getSolde(), 0.001, "Le solde devrait être 0");
    }

    @Test
    public void testRetirerFondsMontantSuperieurAuSolde() {
        assertFalse(portefeuille.retirerFonds(150.0), "Le retrait devrait échouer");
        assertEquals(100.0, portefeuille.getSolde(), 0.001, "Le solde ne devrait pas changer");
    }

    @Test
    public void testRetirerFondsNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.retirerFonds(-50.0);
        }, "Devrait lancer une exception pour un montant négatif");
    }

    @Test
    public void testReinitialiser() {
        portefeuille.reinitialiser();
        assertEquals(0.0, portefeuille.getSolde(), 0.001, "Le solde devrait être réinitialisé à 0");
    }
}