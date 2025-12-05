package Eccezioni.EccezioniAutenticazione;

/**
 * @class UsernameNotValidException
 * @brief Eccezione lanciata quando il formato dello username non è valido.
 *
 * Questa eccezione segnala che la stringa fornita come username non rispetta i requisiti
 * sintattici del sistema. 
 *
 * @see EccezioneAutenticazione
 *
 * @author mello
 * @version 1.0
 */

public class UsernameNotValidException extends EccezioneAutenticazione {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public UsernameNotValidException() {
    }
   
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione del motivo per cui lo username è invalido (es. "Username non può essere vuoto").
     */
    public UsernameNotValidException(String msg) {
        super(msg);
    }
}