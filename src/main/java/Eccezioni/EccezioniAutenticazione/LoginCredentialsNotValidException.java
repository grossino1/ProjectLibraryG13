/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniAutenticazione;

/**
 * @class LoginCredentialsNotValidException
 * @brief Eccezione lanciata quando le credenziali non sono valide.
 *
 * Questa eccezione segnala che le stringhe fornite come credenziali (Username e Password) non sono valide.
 *
 * @see EccezioneAutenticazione
 *
 * @author mello
 * @version 1.0
 */
public class LoginCredentialsNotValidException extends EccezioneAutenticazione {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public LoginCredentialsNotValidException() {
    }
   
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione del motivo per cui le credenziali non sono valide.
     */
    public LoginCredentialsNotValidException(String msg){
        super(msg);
    }
}