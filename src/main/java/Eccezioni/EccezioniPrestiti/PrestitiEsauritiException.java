package Eccezioni.EccezioniPrestiti;

/**
 * @class PrestitiEsauritiException
 * @brief Eccezione lanciata quando un utente raggiunge il limite massimo di prestiti consentiti.
 *
 * Questa eccezione viene sollevata dal sistema di controllo (GestorePrestito) quando
 * un utente tenta di richiedere un nuovo prestito pur avendo già raggiunto la soglia
 * massima di libri in carico.
 *
 * @see EccezioniPrestito
 * @see GestionePrestito.GestorePrestito#nuovoPrestito
 *
 * @author mello
 * @version 1.0
 */

public class PrestitiEsauritiException extends EccezioniPrestito {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public PrestitiEsauritiException() {
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore.
     */
    public PrestitiEsauritiException(String msg){
        super(msg);
    }
    
}