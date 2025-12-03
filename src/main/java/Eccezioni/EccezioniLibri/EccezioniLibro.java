/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniLibri;

/**
 *
 * @author jackross
 */

public abstract class EccezioniLibro extends Exception {
    public EccezioniLibro(){
    }
    
    public EccezioniLibro(String msg){
        super(msg);
    }
}
