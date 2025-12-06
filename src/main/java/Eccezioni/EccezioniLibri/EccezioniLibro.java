package Eccezioni.EccezioniLibri;

/**
 * @class EccezioniLibro
 * @brief Classe base astratta per tutte le eccezioni legate alla gestione dei libri.
 *
 * Questa classe estende `Exception` e funge da superclasse per tutti gli errori specifici
 * del dominio Libro.
 * Essendo astratta, non può essere istanziata direttamente, ma permette di catturare
 * tutti gli errori relativi ai libri in un unico blocco catch.
 *
 * @see java.lang.Exception
 *
 * @author grossino1
 * @version 1.0
 */

public abstract class EccezioniLibro extends Exception {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public EccezioniLibro(){
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * Permette di passare una stringa descrittiva che spiega la causa dell'errore.
     * Il messaggio può essere recuperato successivamente tramite `getMessage()`.
     *
     * @param[in] msg La stringa che descrive il dettaglio dell'eccezione.
     */
    public EccezioniLibro(String msg){
        super(msg);
    }
}