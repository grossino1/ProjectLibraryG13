package Eccezioni.EccezioniAutenticazione;

/**
 * @class PasswordFieldEmptyException
 * @brief Eccezione lanciata quando il campo della passward è vuoto.
 *
 * @see EccezioneAutenticazione
 *
 * @author mello
 * @version 1.0
 */

public class PasswordFieldEmptyException extends EccezioneAutenticazione {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public PasswordFieldEmptyException() {
    }
   
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione del motivo per cui la password non è valida (es. "Lunghezza insufficiente").
     */
    public PasswordFieldEmptyException(String msg) {
        super(msg);
    
    }
}