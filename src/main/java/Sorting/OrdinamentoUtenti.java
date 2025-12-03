/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sorting;

import GestioneUtente.Utente;
import java.util.Comparator;

/**
 *
 * @author jackross
 */
public class OrdinamentoUtenti {
    
    public static final Comparator<Utente> ALFABETICO = new Comparator<Utente>() {
        @Override
        public int compare(Utente l1, Utente l2) {
            return 0; 
        }
    };

    public static final Comparator<Utente> PIU_RECENTI = new Comparator<Utente>() {
        @Override
        public int compare(Utente l1, Utente l2) {
            return 0;
        }
    };

    public static final Comparator<Utente> MENO_RECENTI = new Comparator<Utente>() {
        @Override
        public int compare(Utente l1, Utente l2) {
            return 0; 
        }
    };
}
