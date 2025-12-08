package Sorting.SortingLibri;

import java.util.Comparator;
import GestioneLibro.Libro;

/**
 * @class OrdinamentoLibri
 * @brief Classe di utilità che fornisce criteri di ordinamento per i libri.
 *
 * Espone costanti statiche di tipo `Comparator` per ordinare collezioni di prestiti
 * secondo diverse logiche, che possono essere passate ai metodi di ordinamento.
 *
 * @see GestioneLibro.Libro
 * @see java.util.Comparator
 *
 * @author grossino1
 * @version 1.0
 */

public class OrdinamentoLibri {
    
    // SONO ATTRIBUTI E NON METODI

    /**
     * @brief Comparatore per ordinare i libri per Titolo.
     *
     * Definisce un ordinamento alfabetico basato sul campo 'titolo' dell'oggetto Libro.
     * Utile per visualizzare il catalogo in ordine alfabetico (A-Z).
     */
    public static final Comparator<Libro> PER_TITOLO = new Comparator<Libro>() {
        
        /**
         * @brief Confronta due libri per titolo.
         * 
         * @param[in] l1 Il primo libro.
         * @param[in] l2 Il secondo libro.
         * @return <0, 0, >0 in base all'ordine lessicografico dei titoli.
         */
        @Override
        public int compare(Libro l1, Libro l2) {
            return l1.getTitolo().compareToIgnoreCase(l2.getTitolo()); // Implementazione: l1.getTitolo().compareToIgnoreCase(l2.getTitolo());
        }
    };

    /**
     * @brief Comparatore per ordinare i libri per Autore.
     *
     * Definisce un ordinamento alfabetico basato sul campo 'autori'.
     * Utile per raggruppare i libri dello stesso autore.
     */
    public static final Comparator<Libro> PER_AUTORE = new Comparator<Libro>() {
        
        /**
         * @brief Confronta due libri per autore.
         * 
         * @param[in] l1 Il primo libro.
         * @param[in] l2 Il secondo libro.
         * @return <0, 0, >0 in base all'ordine lessicografico degli autori.
         */
        @Override
        public int compare(Libro l1, Libro l2) {
            return l1.getAutori().compareToIgnoreCase(l2.getAutori()); // Implementazione: l1.getAutori().compareToIgnoreCase(l2.getAutori());
        }
    };

    /**
     * @brief Comparatore per ordinare i libri per Anno di Pubblicazione.
     *
     * Definisce un ordinamento cronologico (numerico) basato sull'anno.
     * Utile per vedere le pubblicazioni dalla più vecchia alla più recente (o viceversa).
     */
    public static final Comparator<Libro> PER_ANNO = new Comparator<Libro>() {
        
        /**
         * @brief Confronta due libri per anno.
         * 
         * @param[in] l1 Il primo libro.
         * @param[in] l2 Il secondo libro.
         * @return Differenza numerica tra gli anni (l1.anno - l2.anno).
         */
        @Override
        public int compare(Libro l1, Libro l2) {
            return Integer.compare(l1.getAnnoPubblicazione(), l2.getAnnoPubblicazione()); // Implementazione: Integer.compare(l1.getAnno(), l2.getAnno());
        }
    };
}