package Eccezioni.EccezioniLibri;

/**
 * @class LibroPresenteException
 * @brief Eccezione lanciata quando si tenta di inserire un libro già esistente.
 *
 * Questa eccezione segnala che l'operazione di registrazione non può essere completata
 * perché un libro con lo stesso identificativo univoco (ISBN) è già presente nel catalogo.
 * Serve a prevenire duplicati involontari.
 *
 * @see EccezioniLibro
 * @see GestioneLibro.CatalogoLibri
 *
 * @author mello
 * @version 1.0
 */

public class LibroPresenteException extends EccezioniLibro {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public LibroPresenteException() {
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore (es. "Il libro con ISBN X è già presente").
     */
    public LibroPresenteException(String msg){
        super(msg);
    }
    
}
