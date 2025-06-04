package com.distributeur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe représentant le journal des ventes du distributeur automatique.
 * Elle enregistre toutes les transactions effectuées.
 */
public class JournalVentes {
    private List<Transaction> transactions;

    /**
     * Constructeur initialisant une liste vide de transactions.
     */
    public JournalVentes() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Ajoute une transaction au journal des ventes.
     * 
     * @param transaction La transaction à ajouter
     */
    public void ajouterTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    /**
     * Retourne la liste complète des transactions.
     * 
     * @return La liste des transactions
     */
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Retourne la liste des transactions effectuées à une date spécifique.
     * 
     * @param date La date pour laquelle on veut les transactions
     * @return La liste des transactions à cette date
     */
    public List<Transaction> getTransactionsParDate(LocalDate date) {
        return transactions.stream()
                .filter(t -> t.getDateHeure().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Calcule le chiffre d'affaires total (somme des montants des transactions réussies).
     * 
     * @return Le chiffre d'affaires total
     */
    public double getChiffreAffaires() {
        return transactions.stream()
                .filter(Transaction::estReussie)
                .mapToDouble(t -> t.getBoisson().getPrix())
                .sum();
    }

    /**
     * Calcule le chiffre d'affaires pour une date spécifique.
     * 
     * @param date La date pour laquelle on veut le chiffre d'affaires
     * @return Le chiffre d'affaires pour cette date
     */
    public double getChiffreAffairesParDate(LocalDate date) {
        return transactions.stream()
                .filter(t -> t.estReussie() && t.getDateHeure().toLocalDate().equals(date))
                .mapToDouble(t -> t.getBoisson().getPrix())
                .sum();
    }

    /**
     * Retourne le nombre total de transactions.
     * 
     * @return Le nombre de transactions
     */
    public int getNombreTransactions() {
        return transactions.size();
    }

    /**
     * Retourne le nombre de transactions réussies.
     * 
     * @return Le nombre de transactions réussies
     */
    public int getNombreTransactionsReussies() {
        return (int) transactions.stream()
                .filter(Transaction::estReussie)
                .count();
    }
}