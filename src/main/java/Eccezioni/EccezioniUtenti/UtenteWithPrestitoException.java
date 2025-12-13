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
 * @class UtenteWithPrestitoException
 * @brief Eccezione lanciata quando si vuole eliminare un utente con un prestito attivo.
 *
 *
 * @see EccezioniUtente
 * @see GestioneUtente.ListaUtenti#eliminazioneUtente(GestioneUtente.Utente) 
 *
 * @author chiara
 * @version 1.0
 */
public class UtenteWithPrestitoException extends EccezioniUtente{
    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public UtenteWithPrestitoException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore
     */
    public UtenteWithPrestitoException(String msg){
        super(msg);
    }
}
