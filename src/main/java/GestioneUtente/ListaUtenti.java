/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestioneUtente;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author chiara
 */
public class ListaUtenti {
    private Set<Utente> listaUtenti;
    
    public ListaUtenti(){
        listaUtenti = new TreeSet<>();
    }

     public boolean registrazioneUtente(Utente u) {
        return false; // scheletro
    }

    public boolean eliminazioneUtente(Object u) {
        return false; // scheletro
    }

    public ArrayList<Utente> cercaUtente(String u) {
        return null; // scheletro
    }

    public ArrayList<Utente> visualizzazioneListaUtenti() {
        return null; // scheletro
    }

    public ArrayList<Utente> sortListaUtenti(Comparator<Utente> comp) {
        return null; // scheletro
    }

    @Override
    public String toString() {
        return ""; // scheletro
    }
    
}
