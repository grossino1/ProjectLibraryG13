/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sorting.SortingPrestiti;

import GestionePrestito.Prestito;
import java.util.Comparator;

/**
 *
 * @author jackross
 */
public class OrdinamentoPrestiti {
    
    public static final Comparator<Prestito> PER_SCADENZA = new Comparator<Prestito>() {
        @Override
        public int compare(Prestito l1, Prestito l2) {
            return 0; 
        }
    };

    public static final Comparator<Prestito> PIU_RECENTI = new Comparator<Prestito>() {
        @Override
        public int compare(Prestito l1, Prestito l2) {
            return 0;
        }
    };

    public static final Comparator<Prestito> MENO_RECENTI = new Comparator<Prestito>() {
        @Override
        public int compare(Prestito l1, Prestito l2) {
            return 0; 
        }
    };
}
