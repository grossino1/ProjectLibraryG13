/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniLibri;

/**
 *
 * @author mello
 */

public class LibroPresenteException extends EccezioniLibro{
    public LibroPresenteException() {
    }
    
    public LibroPresenteException(String msg){
        super(msg);
    }
    
}
