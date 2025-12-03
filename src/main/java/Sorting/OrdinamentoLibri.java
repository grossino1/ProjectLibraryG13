/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sorting;

import java.util.Comparator;
import GestioneLibro.Libro;

/**
 *
 * @author jackross
 */
public class OrdinamentoLibri {
    
    // SONO ATTRIBUTI E NON METODI
    public static final Comparator<Libro> PER_TITOLO = new Comparator<Libro>() {
        @Override
        public int compare(Libro l1, Libro l2) {
            return 0; 
        }
    };

    public static final Comparator<Libro> PER_AUTORE = new Comparator<Libro>() {
        @Override
        public int compare(Libro l1, Libro l2) {
            return 0;
        }
    };

    public static final Comparator<Libro> PER_ANNO = new Comparator<Libro>() {
        @Override
        public int compare(Libro l1, Libro l2) {
            return 0; 
        }
    };
}
