/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestioneLibro;

import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Comparator;
/**
 *
 * @author chiara
 */

public class CatalogoLibri {

    private Set<Libro> catalogoLibri;

    public CatalogoLibri() {
        catalogoLibri = new TreeSet<>();
    }

    public void registrazioneLibro(Libro l) {
        if(catalogoLibri.add(l)){
            throw new LibroPresenteException("");
        }
    }

    public void eliminazioneLibro(Object l) {
        catalogoLibri.remove(l);
    }

    public ArrayList<Libro> cercaLibro(String l) {
        return new ArrayList<>(catalogoLibri);
        // Ritorna ArrayList perché ci possono essere libri con lo stesso titolo
    }

    public ArrayList<Libro> getCatalogoLibri() {
        // restituisce una lista contenente tutti i libri del catalogo
        return new ArrayList<>(catalogoLibri);
    }

    public TreeSet<Libro> sortCatalogoLibri(Comparator<Libro> comp) {
        // nuovo TreeSet ordinato secondo il comparator passato
        TreeSet<Libro> ordinato = new TreeSet<>(comp);
        ordinato.addAll(catalogoLibri);
        return ordinato;
    }
    //toString
    @Override
    public String toString(){
        return "";
    }
}

