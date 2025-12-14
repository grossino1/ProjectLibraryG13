package Eccezioni.EccezioniPrestiti;

/**
 * @class CopieEsauriteException
 * @brief Eccezione lanciata quando le copie disponibili di un libro sono esaurite, 
 * ovvero un libro presenta un numero di copie <=1 e si prova a registarre un nuovo prestito con quel libro.
 *
 * @see EccezioniPrestito
 *
 * @author grossino1
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
     * @param[in] msg La descrizione dell'errore.
     */
    public CopieEsauriteException(String msg){
        super(msg);
    }
}