/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniUtenti;

/**
 *
 * @author chiara
 */
public class MatricolaNotValidException extends EccezioniUtente{
    public MatricolaNotValidException(){
        
    }
    
    public MatricolaNotValidException(String msg){
        super(msg);
    }
}
