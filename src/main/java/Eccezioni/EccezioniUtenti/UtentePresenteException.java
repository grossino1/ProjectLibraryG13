/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniUtenti;

/**
 *
 * @author chiara
 */

/**
 * @class UtentePresenteException
 * @brief Eccezione lanciata quando un utente è già presente nel sistema.
 *
 *  Questa eccezione viene lanciata quando si vuole registrare un utente già presente
 *  all'interno di listaUtenti.
 *
 * @see EccezioniUtente
 * @see GestioneUtente.ListaUtenti#registrazioneUtente(GestioneUtente.Utente) 
 *
 * @author chiara
 * @version 1.0
 */
public class UtentePresenteException extends EccezioniUtente{
    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public UtentePresenteException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore (es. "Utente già presente").
     */
    public UtentePresenteException(String msg){
        super(msg);
    }
}
