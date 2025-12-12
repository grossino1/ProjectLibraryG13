package Eccezioni.EccezioniUtenti;
/**
 *
 * @author chiara
 */

/**
 * @class UtenteNotFoundException
 * @brief Eccezione lanciata quando un utente richiesto non è presente nel sistema.
 *
 * Questa eccezione segnala che l'identificativo fornito non corrisponde
 * a nessun oggetto @ref Utente presente nel sistema.  
 *
 * @see EccezioniUtente
 * @see GestioneUtente.ListaUtenti#getUtenteByMatricola
 *
 * @author mello
 * @version 1.0
 */

public class UtenteNotFoundException extends EccezioniUtente {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public UtenteNotFoundException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore (es. "Utente con matricola X non trovato").
     */
    public UtenteNotFoundException(String msg){
        super(msg);
    }
}