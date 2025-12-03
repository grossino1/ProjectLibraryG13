/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionePrestito;
import java.util.TreeSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;

import GestioneUtente.Utente;
/**
 *
 * @author chiara
 */

public class ElencoPrestiti {

    private Set<Prestito> elencoPrestiti;

    public ElencoPrestiti() {
        elencoPrestiti = new TreeSet<>();
    }

    public void registrazionePrestito(Prestito p) {
        // scheletro
    }

    public void eliminazionePrestito(Prestito p) {
        // scheletro
    }

    public ArrayList<Prestito> cercaPrestito(String chiave) {
        return null; // scheletro
    }

    public ArrayList<Prestito> getElencoPrestiti() {
        return null; // scheletro
    }

    public ArrayList<Prestito> sortListaUtenti(Comparator<Prestito> comp) {
        return null; // scheletro
    }
    
    @Override
    public String toString() {  
        return null;
    }
}

