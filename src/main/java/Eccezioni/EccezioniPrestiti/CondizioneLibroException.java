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
public class CondizioneLibroException extends EccezioniPrestito{
    public CondizioneLibroException(){
        
    }
    
    public CondizioneLibroException(String msg){
        super(msg);
    }
}
