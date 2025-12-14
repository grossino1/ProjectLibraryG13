package Eccezioni.EccezioniPrestiti;

/**
 * @class PrestitiEsauritiException
 * @brief Eccezione lanciata quando un utente raggiunge il limite massimo di prestiti consentiti 
 * ovvero 3 e prova a reggistrare un nuovo prestito.
 *
 * @see EccezioniPrestito
 * 
 * @author grossino1
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