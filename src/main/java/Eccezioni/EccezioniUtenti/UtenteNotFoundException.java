/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniUtenti;

/**
 *
 * @author mello
 */
public class UtenteNotFoundException extends EccezioniUtente{
    public UtenteNotFoundException(){
        
    }
    
    public UtenteNotFoundException(String msg){
        super(msg);
    }
}
