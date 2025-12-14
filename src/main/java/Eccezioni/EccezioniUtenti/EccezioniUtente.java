package Eccezioni.EccezioniUtenti;
/**
 *
 * @author chiara
 */

/**
 * @class EccezioniUtente
 * @brief Classe base astratta per tutte le eccezioni legate alla gestione degli utenti.
 *
 * Questa classe estende `Exception` e funge da superclasse per tutti gli errori specifici
 * del dominio Utenti.
 * Essendo astratta, non può essere istanziata direttamente, ma permette di catturare
 * tutti gli errori relativi ai libri in un unico blocco catch.
 *
 * @see java.lang.Exception
 *
 * @author chiara
 * @version 1.0
 */

public abstract class EccezioniUtente extends Exception{

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public EccezioniUtente(){   
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * Permette di specificare il motivo dell'errore tramite una stringa descrittiva, 
     * recuperabile successivamente tramite `getMessage()`.
     *
     * @param[in] msg La stringa che descrive il dettaglio dell'eccezione.
     */
    public EccezioniUtente(String msg){
        super(msg);
    }
}