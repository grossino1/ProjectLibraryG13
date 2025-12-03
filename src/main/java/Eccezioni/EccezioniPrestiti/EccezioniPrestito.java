/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniPrestiti;

/**
 *
 * @author jackross
 */
public class EccezioniPrestito extends Exception {
    public EccezioniPrestito(){
        
    }
    
    public EccezioniPrestito(String msg){
        super(msg);
    }
}
