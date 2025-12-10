package Sorting.SortingUtenti;

import GestioneUtente.Utente;
import java.util.Comparator;

/**
 * @class OrdinamentoUtenti
 * @brief Classe di utilità che fornisce criteri di ordinamento per gli utenti.
 *
 * Espone costanti statiche di tipo `Comparator` per ordinare liste di utenti
 * secondo diverse logiche, che possono essere passate ai metodi di ordinamento.
 * 
 * @see GestioneUtente.Utente
 * @see java.util.Comparator
 *
 * @author grossino1
 * @version 1.0
 */

public class OrdinamentoUtenti {
    
    /**
     * @brief Comparatore per ordinare gli utenti in ordine alfabetico.
     *
     * L'ordinamento avviene controllando prima il Cognome e,
     * in caso di omonimia, il Nome.
     * Utile per la visualizzazione standard degli elenchi.
     */
    public static final Comparator<Utente> ALFABETICO = new Comparator<Utente>() {
        
        /**
         * @brief Confronta due utenti per nome/cognome.
         * 
         * @param[in] l1 Il primo utente.
         * @param[in] l2 Il secondo utente.
         * @return <0, 0, >0 in base all'ordine lessicografico.
         */
        @Override
        public int compare(Utente u1, Utente u2) {  
            int comp;
            comp = u1.getCognome().compareToIgnoreCase(u2.getCognome());
            if (comp != 0) return comp;
            comp = u1.getNome().compareToIgnoreCase(u2.getNome());
            if (comp != 0) return comp;
            return u1.getMatricola().compareToIgnoreCase(u2.getMatricola());

        } 
    };

    /**
     * @brief Comparatore per ordinare gli utenti dai più recenti ai più vecchi.
     *
     * Ordina in base all'ordine di registrazione *decrescente*.
     * Utile per vedere chi sono i nuovi iscritti alla biblioteca.
     */
    /*
    public static final Comparator<Utente> PIU_RECENTI = new Comparator<Utente>() {
        
        /**
         * @brief Confronta due utenti per data di iscrizione (Newest First).
         * 
         * @param[in] l1 Il primo utente.
         * @param[in] l2 Il secondo utente.
         * @return Un intero che privilegia gli utenti inseriti più di recente.
         
        @Override
        public int compare(Utente u1, Utente u2) {
            // ORDINE DECRESCENTE: ID MAGGIORE -> ID MINORE
            return Integer.compare(u2.getDataRegistrazioneUtente(), u1.getDataRegistrazioneUtente());
        }
    };

    /**
     * @brief Comparatore per ordinare gli utenti dai più vecchi ai più recenti.
     *
     * Ordina in base all'ordine di registrazione *decrescente*.
     * Utile per visualizzare gli utenti storici.
     
    public static final Comparator<Utente> MENO_RECENTI = new Comparator<Utente>() { 
        /**
         * @brief Confronta due utenti per data di iscrizione (Oldest First).
         * 
         * @param[in] l1 Il primo utente.
         * @param[in] l2 Il secondo utente.
         * @return Un intero che privilegia gli utenti inseriti da più tempo.
         
        @Override
        public int compare(Utente u1, Utente u2) {
            // ORDINE CRESCENTE: ID MINORE -> ID MAGGIORE
            return Integer.compare(u1.getDataRegistrazioneUtente(), u2.getDataRegistrazioneUtente()); 
        }
    };
*/
}