package Eccezioni.EccezioniPrestiti;

/**
 * @class dataRestituzioneException
 * @brief Eccezione lanciata quando la data di restituzione di un libro è > di 30 giorni.
 *
 * @see EccezioniPrestito
 *
 * @author grossino1
 * @version 1.0
 */

public class dataRestituzioneException extends EccezioniPrestito{
    
    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public dataRestituzioneException() {
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore.
     */
    public dataRestituzioneException(String msg){
        super(msg);
    }
}
