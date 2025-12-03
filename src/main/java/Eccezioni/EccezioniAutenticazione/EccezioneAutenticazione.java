/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniAutenticazione;

/**
 *
 * @author jackross
 */
public class EccezioneAutenticazione extends Exception {
    public EccezioneAutenticazione(){
    }
    
    public EccezioneAutenticazione(String msg){
        super(msg);
    }
}
