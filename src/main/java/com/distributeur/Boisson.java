package com.distributeur;

/**
 * Classe représentant une boisson disponible dans le distributeur automatique.
 */
public class Boisson {
    private int id;
    private String nom;
    private double prix;
    private int quantiteStock;

    /**
     * Constructeur de la classe Boisson.
     * 
     * @param id            Identifiant unique de la boisson
     * @param nom           Nom de la boisson
     * @param prix          Prix de la boisson
     * @param quantiteStock Quantité initiale en stock
     */
    public Boisson(int id, String nom, double prix, int quantiteStock) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
    }

    /**
     * Retourne l'identifiant de la boisson.
     * 
     * @return L'identifiant de la boisson
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom de la boisson.
     * 
     * @return Le nom de la boisson
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le prix de la boisson.
     * 
     * @return Le prix de la boisson
     */
    public double getPrix() {
        return prix;
    }

    /**
     * Retourne la quantité en stock de la boisson.
     * 
     * @return La quantité en stock
     */
    public int getQuantiteStock() {
        return quantiteStock;
    }

    /**
     * Modifie la quantité en stock de la boisson.
     * 
     * @param quantite La nouvelle quantité en stock
     */
    public void setQuantiteStock(int quantite) {
        if (quantite >= 0) {
            this.quantiteStock = quantite;
        }
    }

    /**
     * Vérifie si la boisson est disponible en stock.
     * 
     * @return true si la boisson est disponible, false sinon
     */
    public boolean estDisponible() {
        return quantiteStock > 0;
    }

    /**
     * Diminue le stock de la boisson de 1 unité si elle est disponible.
     * 
     * @return true si le stock a été diminué, false si la boisson n'est pas disponible
     */
    public boolean diminuerStock() {
        if (estDisponible()) {
            quantiteStock--;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        // Formater le prix sans décimales si c'est un nombre entier
        String prixFormate = (prix == (int) prix) ? String.valueOf((int) prix) : String.valueOf(prix);
        return id + " - " + nom + " - " + prixFormate + " FCFA (Stock: " + quantiteStock + ")";
    }
}