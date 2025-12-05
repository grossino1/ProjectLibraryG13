package Eccezioni.EccezioniPrestiti;

/**
 * @class CopieEsauriteException
 * @brief Eccezione lanciata quando le copie disponibili di un libro sono esaurite.
 *
 * Questa eccezione viene sollevata dal gestore dei prestiti quando un utente
 * richiede un libro, ma il contatore delle copie disponibili è inferiore a 1.
 * Segnala che il libro esiste nel catalogo ma non è fisicamente disponibile per il prestito.
 *
 * @see EccezioniPrestito
 * @see GestioneLibro.Libro#getNumeroCopie()
 *
 * @author mello
 * @version 1.0
 */

public class CopieEsauriteException extends EccezioniPrestito {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public CopieEsauriteException() {
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore (es. "Copie non disponibili per il libro richiesto").
     */
    public CopieEsauriteException(String msg){
        super(msg);
    }
}