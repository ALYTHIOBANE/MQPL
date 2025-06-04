package com.distributeur;

import java.util.List;

/**
 * Classe représentant un administrateur du distributeur automatique.
 * Hérite de la classe Utilisateur et ajoute des fonctionnalités spécifiques.
 */
public class Admin extends Utilisateur {

    /**
     * Constructeur de la classe Admin.
     * 
     * @param id   Identifiant unique de l'administrateur
     * @param nom  Nom de l'administrateur
     */
    public Admin(int id, String nom) {
        super(id, nom);
    }

    /**
     * Constructeur de la classe Admin avec un solde initial.
     * 
     * @param id            Identifiant unique de l'administrateur
     * @param nom           Nom de l'administrateur
     * @param soldeInitial  Solde initial du portefeuille
     */
    public Admin(int id, String nom, double soldeInitial) {
        super(id, nom, soldeInitial);
    }

    /**
     * Recharge le stock d'une boisson dans le distributeur.
     * 
     * @param distributeur  Le distributeur à recharger
     * @param idBoisson     L'identifiant de la boisson à recharger
     * @param quantite      La quantité à ajouter au stock
     * @return true si le rechargement a réussi, false sinon
     */
    public boolean rechargerDistributeur(Distributeur distributeur, int idBoisson, int quantite) {
        if (distributeur == null || quantite <= 0) {
            return false;
        }
        return distributeur.rechargerStock(idBoisson, quantite);
    }

    /**
     * Collecte les fonds du distributeur et les ajoute au portefeuille de l'administrateur.
     * 
     * @param distributeur  Le distributeur dont on collecte les fonds
     * @return Le montant collecté
     */
    public double collecterFonds(Distributeur distributeur) {
        if (distributeur == null) {
            return 0.0;
        }
        double montant = distributeur.getMontantCaisse();
        this.ajouterFonds(montant);
        distributeur.viderCaisse();
        return montant;
    }

    /**
     * Consulte l'historique des ventes du distributeur.
     * 
     * @param distributeur  Le distributeur dont on consulte les ventes
     * @return La liste des transactions
     */
    public List<Transaction> consulterVentes(Distributeur distributeur) {
        if (distributeur == null) {
            return List.of();
        }
        return distributeur.getHistoriqueVentes();
    }

    @Override
    public String toString() {
        return "Admin " + super.toString();
    }
}