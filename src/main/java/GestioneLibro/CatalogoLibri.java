/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestioneLibro;
import java.util.Set;
import java.util.TreeSet;
/**
 *
 * @author chiara
 */
public class CatalogoLibri {
    private Set<Libro> catalogox;
    
    public CatalogoLibri(){
        catalogo = new TreeSet<>();
    }
    
    public boolean registrazioneLibro(Libro l){
        return catalogo.add(l);
    }
}
