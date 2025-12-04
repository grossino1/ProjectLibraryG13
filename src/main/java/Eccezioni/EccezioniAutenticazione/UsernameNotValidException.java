/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniAutenticazione;

/**
 *
 * @author mello
 */
public class UsernameNotValidException extends EccezioneAutenticazione{
    public UsernameNotValidException() {
    }
   
    public UsernameNotValidException(String msg) {
        super(msg);
    }
}
