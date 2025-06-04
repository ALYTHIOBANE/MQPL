package com.distributeur;

/**
 * Classe représentant un utilisateur du distributeur automatique.
 */
public class Utilisateur {
    private int id;
    private String nom;
    private Portefeuille portefeuille;

    /**
     * Constructeur de la classe Utilisateur.
     * 
     * @param id   Identifiant unique de l'utilisateur
     * @param nom  Nom de l'utilisateur
     */
    public Utilisateur(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.portefeuille = new Portefeuille();
    }

    /**
     * Constructeur de la classe Utilisateur avec un solde initial.
     * 
     * @param id            Identifiant unique de l'utilisateur
     * @param nom           Nom de l'utilisateur
     * @param soldeInitial  Solde initial du portefeuille
     */
    public Utilisateur(int id, String nom, double soldeInitial) {
        this.id = id;
        this.nom = nom;
        this.portefeuille = new Portefeuille(soldeInitial);
    }

    /**
     * Retourne l'identifiant de l'utilisateur.
     * 
     * @return L'identifiant de l'utilisateur
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom de l'utilisateur.
     * 
     * @return Le nom de l'utilisateur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le portefeuille de l'utilisateur.
     * 
     * @return Le portefeuille
     */
    public Portefeuille getPortefeuille() {
        return portefeuille;
    }

    /**
     * Retourne le solde du portefeuille de l'utilisateur.
     * 
     * @return Le solde du portefeuille
     */
    public double getSolde() {
        return portefeuille.getSolde();
    }

    /**
     * Ajoute des fonds au portefeuille de l'utilisateur.
     * 
     * @param montant Le montant à ajouter
     * @throws IllegalArgumentException si le montant est négatif
     */
    public void ajouterFonds(double montant) {
        portefeuille.ajouterFonds(montant);
    }

    /**
     * Retire des fonds du portefeuille de l'utilisateur si le solde est suffisant.
     * 
     * @param montant Le montant à retirer
     * @return true si le retrait a été effectué, false si le solde est insuffisant
     * @throws IllegalArgumentException si le montant est négatif
     */
    public boolean retirerFonds(double montant) {
        return portefeuille.retirerFonds(montant);
    }

    @Override
    public String toString() {
        return "Utilisateur #" + id + " - " + nom + " (Solde: " + portefeuille.getSolde() + " FCFA)";
    }
}