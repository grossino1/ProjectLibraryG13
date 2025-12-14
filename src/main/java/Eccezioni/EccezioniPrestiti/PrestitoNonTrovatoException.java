package Eccezioni.EccezioniPrestiti;

/**
 * @class PrestitoNonTrovatoException
 * @brief Eccezione lanciata quando un prestito non viene trovato nell'elenco.
 *
 * @see EccezioniPrestito
 *
 * @author grossino1
 * @version 1.0
 */
public class PrestitoNonTrovatoException extends EccezioniPrestito {
    
    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public PrestitoNonTrovatoException() {
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore.
     */
    public PrestitoNonTrovatoException(String msg){
        super(msg);
    }
    
}
