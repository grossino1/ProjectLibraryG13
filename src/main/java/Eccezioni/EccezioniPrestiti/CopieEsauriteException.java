/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni.EccezioniPrestiti;

/**
 *
 * @author mello
 */
public class CopieEsauriteException extends EccezioniPrestito{
    public CopieEsauriteException() {
    }
    
    public CopieEsauriteException(String msg){
        super(msg);
    }
}
