package Eccezioni.EccezioniAutenticazione;

/**
 * @class UsernameFieldEmptyException
 * @brief Eccezione lanciata quando il campo dell'username è vuoto.
 *
 * @see EccezioneAutenticazione
 *
 * @author mello
 * @version 1.0
 */

public class UsernameFieldEmptyException extends EccezioneAutenticazione {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public UsernameFieldEmptyException() {
    }
   
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione del motivo per cui lo username è invalido.
     */
    public UsernameFieldEmptyException(String msg) {
        super(msg);
    }
}