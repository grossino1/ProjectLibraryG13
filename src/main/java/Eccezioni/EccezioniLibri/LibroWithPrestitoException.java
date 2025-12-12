/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniLibri;

import Eccezioni.EccezioniUtenti.*;

/**
 *
 * @author chiara
 */

/**
 * @class LibroWithPrestitoException
 * @brief Eccezione lanciata quando si vuole eliminare un libro con un prestito attivo.
 *
 *
 * @see EccezioniUtente
 * @see CatalogoLibri.Libro#eliminazioneLibro(CatalogoLibri.Libro) 
 *
 * @author mello
 * @version 1.0
 */
public class LibroWithPrestitoException extends EccezioniUtente{
    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public LibroWithPrestitoException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore
     */
    public LibroWithPrestitoException(String msg){
        super(msg);
    }
}
