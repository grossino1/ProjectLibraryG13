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

    private Set<Libro> catalogo;

    public CatalogoLibri() {
        catalogo = new TreeSet<>();
    }

    public boolean registrazioneLibro(Libro l) {
        return catalogo.add(l);
    }

    public boolean eliminazioneLibro(Object l) {
        return catalogo.remove(l);
    }

    public boolean cercaLibro(Object l) {
        return catalogo.contains(l);
    }

    public ArrayList<Libro> visualizzazioneCatalogoLibri() {
        // restituisce una lista contenente tutti i libri del catalogo
        return new ArrayList<>(catalogo);
    }

    public TreeSet<Libro> sortCatalogoLibri(Comparator<Libro> comp) {
        // nuovo TreeSet ordinato secondo il comparator passato
        TreeSet<Libro> ordinato = new TreeSet<>(comp);
        ordinato.addAll(catalogo);
        return ordinato;
    }
}
