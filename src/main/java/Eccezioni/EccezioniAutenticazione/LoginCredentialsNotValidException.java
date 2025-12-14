package Eccezioni.EccezioniAutenticazione;

/**
 * @class LoginCredentialsNotValidException
 * @brief Eccezione lanciata quando l'usarname o la passward sono errati.
 *
 * @see EccezioneAutenticazione
 *
 * @author mello
 * @version 1.0
 */

public class LoginCredentialsNotValidException extends EccezioneAutenticazione {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public LoginCredentialsNotValidException() {
    }
   
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione del motivo per cui la password non è valida.
     */
    public LoginCredentialsNotValidException(String msg) {
        super(msg);
    
    }
}