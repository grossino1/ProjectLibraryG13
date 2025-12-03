/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniPrestiti;

/**
 *
 * @author chiara
 */
public class CondizioneUtenteException extends EccezioniPrestito{
    public CondizioneUtenteException(){
        
    }
    
    public CondizioneUtenteException(String msg){
        super(msg);
    }
}
