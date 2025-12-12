package Eccezioni.EccezioniUtenti;
/**
 *
 * @author chiara
 */

/**
 * @class MatricolaNotValidException
 * @brief Eccezione lanciata quando il formato di una matricola non è valido.
 *
 * Questa eccezione viene sollevata durante la creazione o la modifica di un utente
 * se la stringa fornita come matricola non rispetta i requisiti formali richiesti.
 *
 * @see EccezioniUtente
 * @see GestioneUtente.Utente#setMatricola
 *
 * @author chiara
 * @version 1.0
 */

public class MatricolaNotValidException extends EccezioniUtente {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public MatricolaNotValidException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione specifica dell'errore di validazione (es. "Matricola non può essere vuota").
     */
    public MatricolaNotValidException(String msg){
        super(msg);
    }
}