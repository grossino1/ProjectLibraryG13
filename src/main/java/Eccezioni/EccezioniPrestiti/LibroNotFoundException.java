package Eccezioni.EccezioniPrestiti;

/**
 * @class IDPrestitoException
 * @brief Eccezione lanciata in caso di problemi legati all'identificativo (ID) di un prestito.
 *
 * Questa eccezione segnala errori specifici riguardanti il campo ID del prestito.
 *
 * @see EccezioniPrestito
 * @see GestionePrestito.Prestito#getIDPrestito()
 *
 * @author chiara
 * @version 1.0
 */

public class LibroNotFoundException extends EccezioniPrestito {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public LibroNotFoundException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore (es. "ID Prestito non valido o non trovato").
     */
    public LibroNotFoundException(String msg){
        super(msg);
    }
}