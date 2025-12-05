package Eccezioni.EccezioniLibri;

/**
 * @class LibroNotFoundException
 * @brief Eccezione lanciata quando un libro richiesto non viene trovato nel catalogo.
 *
 * Questa eccezione segnala che l'identificativo fornito non corrisponde
 * a nessun oggetto @ref Libro presente nel sistema. 
 * 
 * @see EccezioniLibro
 * @see GestionePrestito.GestorePrestito#nuovoPrestito
 *
 * @author mello
 * @version 1.0
 */

public class LibroNotFoundException extends EccezioniLibro {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio.
     */
    public LibroNotFoundException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore.
     */
    public LibroNotFoundException(String msg){
        super(msg);
    }
}
