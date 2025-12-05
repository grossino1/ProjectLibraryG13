package Eccezioni.EccezioniPrestiti;

/**
 * @class EccezioniPrestito
 * @brief Classe base astratta per tutte le eccezioni legate alla gestione dei prestiti.
 *
 * Questa classe estende `Exception` e funge da superclasse per tutti gli errori specifici
 * del dominio Prestiti.
 * Essendo astratta, non può essere istanziata direttamente, ma permette di catturare
 * tutti gli errori relativi ai libri in un unico blocco catch.
 *
 * @see java.lang.Exception
 *
 * @author grossino1
 * @version 1.0
 */

public abstract class EccezioniPrestito extends Exception {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public EccezioniPrestito(){
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * Permette di passare una descrizione testuale della causa dell'errore,
     * recuperabile successivamente tramite `getMessage()`.
     *
     * @param[in] msg La stringa che descrive il dettaglio dell'eccezione.
     */
    public EccezioniPrestito(String msg){
        super(msg);
    }
}