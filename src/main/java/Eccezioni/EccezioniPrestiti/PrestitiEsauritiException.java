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
public class PrestitiEsauritiException extends EccezioniPrestito{
    public PrestitiEsauritiException() {
    }
    
    public PrestitiEsauritiException(String msg){
        super(msg);
    }
    
}
