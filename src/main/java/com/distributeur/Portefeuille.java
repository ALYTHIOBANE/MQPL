package com.distributeur;

/**
 * Classe représentant un portefeuille qui gère les montants d'argent.
 * Utilisée à la fois pour la caisse du distributeur et pour le portefeuille de l'utilisateur.
 */
public class Portefeuille {
    private double solde;

    /**
     * Constructeur par défaut initialisant le solde à 0.
     */
    public Portefeuille() {
        this.solde = 0.0;
    }

    /**
     * Constructeur initialisant le solde avec un montant spécifié.
     * 
     * @param soldeInitial Le solde initial du portefeuille
     */
    public Portefeuille(double soldeInitial) {
        if (soldeInitial >= 0) {
            this.solde = soldeInitial;
        } else {
            this.solde = 0.0;
        }
    }

    /**
     * Retourne le solde actuel du portefeuille.
     * 
     * @return Le solde actuel
     */
    public double getSolde() {
        return solde;
    }

    /**
     * Ajoute des fonds au portefeuille.
     * 
     * @param montant Le montant à ajouter
     * @throws IllegalArgumentException si le montant est négatif
     */
    public void ajouterFonds(double montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant à ajouter ne peut pas être négatif");
        }
        this.solde += montant;
    }

    /**
     * Retire des fonds du portefeuille si le solde est suffisant.
     * 
     * @param montant Le montant à retirer
     * @return true si le retrait a été effectué, false si le solde est insuffisant
     * @throws IllegalArgumentException si le montant est négatif
     */
    public boolean retirerFonds(double montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant à retirer ne peut pas être négatif");
        }
        if (this.solde >= montant) {
            this.solde -= montant;
            return true;
        }
        return false;
    }

    /**
     * Réinitialise le solde du portefeuille à 0.
     */
    public void reinitialiser() {
        this.solde = 0.0;
    }
}