package com.distributeur.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.distributeur.Boisson;

/**
 * Tests unitaires pour la classe Boisson.
 */
public class BoissonTest {

    private Boisson boisson;

    @BeforeEach
    public void setUp() {
        // Initialisation d'une boisson pour les tests
        boisson = new Boisson(1, "Coca-Cola", 1000, 10);
    }

    @Test
    public void testConstructeur() {
        // Vérification que le constructeur initialise correctement les attributs
        assertEquals(1, boisson.getId(), "L'ID devrait être 1");
        assertEquals("Coca-Cola", boisson.getNom(), "Le nom devrait être 'Coca-Cola'");
        assertEquals(1000, boisson.getPrix(), 0.001, "Le prix devrait être 1000");
        assertEquals(10, boisson.getQuantiteStock(), "La quantité en stock devrait être 10");
    }

    @Test
    public void testGetId() {
        assertEquals(1, boisson.getId(), "L'ID devrait être 1");
    }

    @Test
    public void testGetNom() {
        assertEquals("Coca-Cola", boisson.getNom(), "Le nom devrait être 'Coca-Cola'");
    }

    @Test
    public void testGetPrix() {
        assertEquals(1000, boisson.getPrix(), 0.001, "Le prix devrait être 1000");
    }

    @Test
    public void testGetQuantiteStock() {
        assertEquals(10, boisson.getQuantiteStock(), "La quantité en stock devrait être 10");
    }

    @Test
    public void testSetQuantiteStock() {
        boisson.setQuantiteStock(5);
        assertEquals(5, boisson.getQuantiteStock(), "La quantité en stock devrait être 5");
    }

    @Test
    public void testSetQuantiteStockNegative() {
        // La quantité ne devrait pas changer si on essaie de mettre une valeur négative
        boisson.setQuantiteStock(-5);
        assertEquals(10, boisson.getQuantiteStock(), "La quantité en stock ne devrait pas changer");
    }

    @Test
    public void testEstDisponible() {
        assertTrue(boisson.estDisponible(), "La boisson devrait être disponible");

        // Mettre le stock à 0
        boisson.setQuantiteStock(0);
        assertFalse(boisson.estDisponible(), "La boisson ne devrait pas être disponible");
    }

    @Test
    public void testDiminuerStock() {
        assertTrue(boisson.diminuerStock(), "Le stock devrait être diminué");
        assertEquals(9, boisson.getQuantiteStock(), "La quantité en stock devrait être 9");
    }

    @Test
    public void testDiminuerStockVide() {
        // Mettre le stock à 0
        boisson.setQuantiteStock(0);
        assertFalse(boisson.diminuerStock(), "Le stock ne devrait pas être diminué");
        assertEquals(0, boisson.getQuantiteStock(), "La quantité en stock devrait rester 0");
    }

    @Test
    public void testToString() {
        String expected = "1 - Coca-Cola - 1000 FCFA (Stock: 10)";
        assertEquals(expected, boisson.toString(), "La représentation en chaîne devrait être correcte");
    }
}