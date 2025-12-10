/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniPrestiti;

/**
 * @class ElencoPienoException
 * @brief Eccezione lanciata quando all'interno dell'elenco sono presenti più 
 * di 100 prestiti
 *
 * @see EccezioniPrestito
 * 
 * @author grossino1
 * @version 1.0
 */

public class ElencoPienoException extends EccezioniPrestito{
    
    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public ElencoPienoException() {
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore (es. "Copie non disponibili per il libro richiesto").
     */
    public ElencoPienoException(String msg){
        super(msg);
    }
}
