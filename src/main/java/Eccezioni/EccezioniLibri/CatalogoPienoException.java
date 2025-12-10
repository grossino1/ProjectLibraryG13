  
package Eccezioni.EccezioniLibri;

/**
 * @class CatalogoPienoExceptoin
 * @brief Eccezione lanciata quando viene superato il limite di 1000 libri nel catalogo.
 *
 * Questa eccezione viene sollevata durante la registrazione di un libro
 * se sono già presenti 1000 libri nel catalogo.
 *
 * @see EccezioniLibro
 *
 * @author Chris7iaN4
 * @version 1.0
 */

public class CatalogoPienoException extends EccezioniLibro {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public CatalogoPienoException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di dettaglio.
     *
     * @param[in] msg La descrizione specifica dell'errore.
     */
    public CatalogoPienoException(String msg){
        super(msg);
    }
}