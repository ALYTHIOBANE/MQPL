package com.distributeur;

import java.time.LocalDateTime;

/**
 * Classe représentant une transaction d'achat dans le distributeur automatique.
 */
public class Transaction {
    private static int compteurId = 0;
    
    private int id;
    private Boisson boisson;
    private double montantInsere;
    private double monnaieRendue;
    private LocalDateTime dateHeure;
    private boolean reussie;

    /**
     * Constructeur de la classe Transaction.
     * 
     * @param boisson        La boisson concernée par la transaction
     * @param montantInsere  Le montant inséré par l'utilisateur
     * @param monnaieRendue  La monnaie rendue à l'utilisateur
     * @param reussie        Indique si la transaction a réussi
     */
    public Transaction(Boisson boisson, double montantInsere, double monnaieRendue, boolean reussie) {
        this.id = ++compteurId;
        this.boisson = boisson;
        this.montantInsere = montantInsere;
        this.monnaieRendue = monnaieRendue;
        this.dateHeure = LocalDateTime.now();
        this.reussie = reussie;
    }

    /**
     * Retourne l'identifiant de la transaction.
     * 
     * @return L'identifiant de la transaction
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne la boisson concernée par la transaction.
     * 
     * @return La boisson concernée
     */
    public Boisson getBoisson() {
        return boisson;
    }

    /**
     * Retourne le montant inséré par l'utilisateur.
     * 
     * @return Le montant inséré
     */
    public double getMontantInsere() {
        return montantInsere;
    }

    /**
     * Retourne la monnaie rendue à l'utilisateur.
     * 
     * @return La monnaie rendue
     */
    public double getMonnaieRendue() {
        return monnaieRendue;
    }

    /**
     * Retourne la date et l'heure de la transaction.
     * 
     * @return La date et l'heure
     */
    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    /**
     * Indique si la transaction a réussi.
     * 
     * @return true si la transaction a réussi, false sinon
     */
    public boolean estReussie() {
        return reussie;
    }

    @Override
    public String toString() {
        // Formater les montants sans décimales si ce sont des nombres entiers
        String montantInsereFormate = (montantInsere == (int) montantInsere) ? 
                String.valueOf((int) montantInsere) : String.valueOf(montantInsere);
        String monnaieRendueFormatee = (monnaieRendue == (int) monnaieRendue) ? 
                String.valueOf((int) monnaieRendue) : String.valueOf(monnaieRendue);
        
        return "Transaction #" + id + " - Boisson: " + 
                (boisson != null ? boisson.getNom() : "N/A") + 
                " - Montant: " + montantInsereFormate + 
                " FCFA - Monnaie: " + monnaieRendueFormatee +
                " FCFA - Date: " + dateHeure +
                " - Réussie: " + (reussie ? "Oui" : "Non");
    }
}