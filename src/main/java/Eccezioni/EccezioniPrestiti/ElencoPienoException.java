package Eccezioni.EccezioniPrestiti;

/**
 * @class ElencoPienoException
 * @brief Eccezione lanciata quando all'interno dell'elenco dei prestiti sono già presenti 
 * MAX_PRESTITI e si cerca di regiostrarne uno.
 *
 * @see EccezioniPrestito
 * @see GestionePrestito.ElencoPrestiti#MAX_PRESTITI
 * 
 * @author grossino1
 * @version 1.0
 */

public class ElencoPienoException extends EccezioniPrestito{
    
    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public ElencoPienoException() {
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore.
     */
    public ElencoPienoException(String msg){
        super(msg);
    }
}
