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
 * @class ListaUtentiPienaException
 * @brief Eccezione lanciata quando la listaUtenti ha raggiunto il limite di utenti (1000 utenti).
 *
 * Questa eccezione viene sollevata durante l'aggiunta e registrazione di un nuovo utente.
 *
 * @see EccezioniUtente
 * @see GestioneUtente.ListaUtenti#registrazioneUtente(GestioneUtente.Utente) 
 *
 * @author chiara
 * @version 1.0
 */
public class ListaUtentiPienaException extends EccezioniUtente{
    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public ListaUtentiPienaException(){
        
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione dell'errore (es. "Utente con matricola X non trovato").
     */
    public ListaUtentiPienaException(String msg){
        super(msg);
    }
}
