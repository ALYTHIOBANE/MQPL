package com.distributeur;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe principale représentant le distributeur automatique de boissons.
 */
public class Distributeur {
    private List<Boisson> boissons;
    private Portefeuille caisse;
    private JournalVentes journal;

    /**
     * Constructeur initialisant le distributeur avec une liste vide de boissons,
     * une caisse vide et un journal des ventes vide.
     */
    public Distributeur() {
        this.boissons = new ArrayList<>();
        this.caisse = new Portefeuille();
        this.journal = new JournalVentes();
    }

    /**
     * Ajoute une nouvelle boisson au distributeur.
     * 
     * @param boisson La boisson à ajouter
     * @return true si la boisson a été ajoutée, false si une boisson avec le même ID existe déjà
     */
    public boolean ajouterBoisson(Boisson boisson) {
        if (boisson == null) {
            return false;
        }
        
        // Vérifier si une boisson avec le même ID existe déjà
        if (boissons.stream().anyMatch(b -> b.getId() == boisson.getId())) {
            return false;
        }
        
        return boissons.add(boisson);
    }

    /**
     * Retourne la liste des boissons disponibles (quantité en stock > 0).
     * 
     * @return La liste des boissons disponibles
     */
    public List<Boisson> afficherBoissonsDisponibles() {
        return boissons.stream()
                .filter(Boisson::estDisponible)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Retourne la liste complète des boissons (disponibles ou non).
     * 
     * @return La liste complète des boissons
     */
    public List<Boisson> getToutesBoissons() {
        return new ArrayList<>(boissons);
    }

    /**
     * Recherche une boisson par son ID.
     * 
     * @param idBoisson L'ID de la boisson recherchée
     * @return La boisson si elle existe, null sinon
     */
    public Boisson rechercherBoisson(int idBoisson) {
        Optional<Boisson> optionalBoisson = boissons.stream()
                .filter(b -> b.getId() == idBoisson)
                .findFirst();
        
        return optionalBoisson.orElse(null);
    }

    /**
     * Effectue l'achat d'une boisson.
     * 
     * @param idBoisson    L'ID de la boisson à acheter
     * @param montantInsere Le montant inséré par l'utilisateur
     * @return La transaction effectuée
     */
    public Transaction acheterBoisson(int idBoisson, double montantInsere) {
        if (montantInsere < 0) {
            throw new IllegalArgumentException("Le montant inséré ne peut pas être négatif");
        }
        
        Boisson boisson = rechercherBoisson(idBoisson);
        
        // Si la boisson n'existe pas
        if (boisson == null) {
            return new Transaction(null, montantInsere, montantInsere, false);
        }
        
        // Si la boisson n'est pas disponible
        if (!boisson.estDisponible()) {
            return new Transaction(boisson, montantInsere, montantInsere, false);
        }
        
        // Si le montant inséré est insuffisant
        if (montantInsere < boisson.getPrix()) {
            return new Transaction(boisson, montantInsere, montantInsere, false);
        }
        
        // Calcul de la monnaie à rendre
        double monnaieARendre = montantInsere - boisson.getPrix();
        
        // Diminution du stock
        boisson.diminuerStock();
        
        // Ajout du prix de la boisson à la caisse
        caisse.ajouterFonds(boisson.getPrix());
        
        // Création de la transaction réussie
        Transaction transaction = new Transaction(boisson, montantInsere, monnaieARendre, true);
        
        // Enregistrement de la transaction dans le journal
        journal.ajouterTransaction(transaction);
        
        return transaction;
    }

    /**
     * Recharge le stock d'une boisson.
     * 
     * @param idBoisson L'ID de la boisson à recharger
     * @param quantite  La quantité à ajouter au stock
     * @return true si le rechargement a réussi, false sinon
     */
    public boolean rechargerStock(int idBoisson, int quantite) {
        if (quantite <= 0) {
            return false;
        }
        
        Boisson boisson = rechercherBoisson(idBoisson);
        
        if (boisson == null) {
            return false;
        }
        
        boisson.setQuantiteStock(boisson.getQuantiteStock() + quantite);
        return true;
    }

    /**
     * Retourne le montant total dans la caisse du distributeur.
     * 
     * @return Le montant dans la caisse
     */
    public double getMontantCaisse() {
        return caisse.getSolde();
    }

    /**
     * Vide la caisse du distributeur.
     * 
     * @return Le montant qui était dans la caisse
     */
    public double viderCaisse() {
        double montant = caisse.getSolde();
        caisse.reinitialiser();
        return montant;
    }

    /**
     * Retourne l'historique complet des ventes.
     * 
     * @return La liste des transactions
     */
    public List<Transaction> getHistoriqueVentes() {
        return journal.getTransactions();
    }

    /**
     * Retourne le journal des ventes.
     * 
     * @return Le journal des ventes
     */
    public JournalVentes getJournal() {
        return journal;
    }
}