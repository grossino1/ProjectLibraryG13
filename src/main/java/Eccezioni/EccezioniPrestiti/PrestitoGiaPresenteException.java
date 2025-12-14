package Eccezioni.EccezioniPrestiti;

/**
 * @class PrestitoGiaPresenteException
 * @brief Eccezione lanciata quando un utente ha già un libro in prestito e prova a 
 * registrare un nuovo prestito con lo stesso libro.
 *
 * @see EccezioniPrestito
 *
 * @author grossino1
 * @version 1.0
 */

public class PrestitoGiaPresenteException extends EccezioniPrestito {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public PrestitoGiaPresenteException() {
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore.
     */
    public PrestitoGiaPresenteException(String msg){
        super(msg);
    }
}