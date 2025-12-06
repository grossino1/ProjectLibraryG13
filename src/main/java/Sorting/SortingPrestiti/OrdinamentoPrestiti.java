package Sorting.SortingPrestiti;

import GestionePrestito.Prestito;
import java.util.Comparator;

/**
 * @class OrdinamentoPrestiti
 * @brief Classe di utilità che fornisce criteri di ordinamento per i prestiti.
 *
 * Espone costanti statiche di tipo `Comparator` per ordinare collezioni di prestiti
 * secondo diverse logiche, che possono essere passate ai metodi di ordinamento.
 *
 * @see GestionePrestito.Prestito
 * @see java.util.Comparator
 *
 * @author grossino1
 * @version 1.0
 */

public class OrdinamentoPrestiti {
    
    /**
     * @brief Comparatore per ordinare i prestiti per data di scadenza (Urgenza).
     *
     * Ordina i prestiti in ordine cronologico crescente rispetto alla `dataRestituzione`.
     * I prestiti con scadenza più vicina (o già passata) appariranno per primi.
     * Utile per individuare i libri da restituire o i ritardi.
     */
    public static final Comparator<Prestito> PER_SCADENZA = new Comparator<Prestito>() {
        
        /**
         * @brief Confronta due prestiti per data di restituzione.
         * 
         * @param[in] l1 Il primo prestito.
         * @param[in] l2 Il secondo prestito.
         * @return <0 se l1 scade prima, 0 se stesso giorno, >0 se l1 scade dopo.
         */
        @Override
        public int compare(Prestito l1, Prestito l2) {
            return 0; // l1.getDataRestituzione().compareTo(l2.getDataRestituzione());
        }
    };

    /**
     * @brief Comparatore per ordinare i prestiti dai più recenti ai più vecchi.
     *
     * Ordina in ordine cronologico *decrescente*.
     * Utile per visualizzare le ultime operazioni effettuate.
     */
    public static final Comparator<Prestito> PIU_RECENTI = new Comparator<Prestito>() {
        
        /**
         * @brief Confronta due prestiti per aggiunta.
         * 
         * @param[in] l1 Il primo prestito.
         * @param[in] l2 Il secondo prestito.
         * @return Un intero che posiziona le date più recenti all'inizio della lista.
         */
        @Override
        public int compare(Prestito l1, Prestito l2) {
            return 0; // l2.getData...().compareTo(l1.getData...()); (Inverso)
        }
    };

    /**
     * @brief Comparatore per ordinare i prestiti dai più vecchi ai più recenti.
     *
     * Ordina in ordine cronologico *crescente*.
     * Utile per visualizzare lo storico delle operazioni partendo dall'inizio.
     */
    public static final Comparator<Prestito> MENO_RECENTI = new Comparator<Prestito>() {
        
        /**
         * @brief Confronta due prestiti per anzianità.
         * 
         * @param[in] l1 Il primo prestito.
         * @param[in] l2 Il secondo prestito.
         * @return Un intero che posiziona le date più vecchie all'inizio della lista.
         */
        @Override
        public int compare(Prestito l1, Prestito l2) {
            return 0; // l1.getData...().compareTo(l2.getData...());
        }
    };
}