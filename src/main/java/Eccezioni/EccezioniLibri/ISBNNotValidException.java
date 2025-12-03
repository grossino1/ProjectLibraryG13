/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniLibri;

/**
 *
 * @author chiara
 */
public class ISBNNotValidException extends EccezioniLibro{
    public ISBNNotValidException(){
        
    }
    
    public ISBNNotValidException(String msg){
        super(msg);
    }
}
