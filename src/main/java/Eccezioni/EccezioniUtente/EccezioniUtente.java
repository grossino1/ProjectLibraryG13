/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniUtente;

/**
 *
 * @author jackross
 */
public class EccezioniUtente extends Exception{
    public EccezioniUtente(){
        
    }
    
    public EccezioniUtente(String msg){
        super(msg);
    }
}
