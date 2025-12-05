package Eccezioni.EccezioniLibri;

/**
 * @class ISBNNotValidException
 * @brief Eccezione lanciata quando un codice ISBN non rispetta il formato valido.
 *
 * Questa eccezione viene sollevata durante la validazione o la registrazione di un libro
 * se la stringa fornita come ISBN non soddisfa i requisiti strutturali standard.
 *
 * @see EccezioniLibro
 * @see GestioneLibro.CatalogoLibri#registrazioneLibro
 *
 * @author chiara
 * @version 1.0
 */

public class ISBNNotValidException extends EccezioniLibro {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public ISBNNotValidException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di dettaglio.
     *
     * @param[in] msg La descrizione specifica dell'errore.
     */
    public ISBNNotValidException(String msg){
        super(msg);
    }
}