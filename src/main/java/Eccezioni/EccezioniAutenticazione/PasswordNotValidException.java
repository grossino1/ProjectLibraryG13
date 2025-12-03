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
public class PasswordNotValidException extends EccezioneAutenticazione{
    public PasswordNotValidException() {
    }
   
    public PasswordNotValidException(String msg) {
        super(msg);
    }
}
