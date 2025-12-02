/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestioneUtente;
import java.util.ArrayList;
import GestionePrestito.Prestito;
import java.util.Comparator;
/**
 *
 * @author chiara
 */
public class Utente implements Comparable<Utente>{
    private String nome;
    private String cognome;
    private String matricola;
    private String emailIstituzionale;
    private ArrayList<Prestito> listaPrestiti;
    
    public Utente(String nome, String cognome, String matricola,
                  String emailIstituzionale, ArrayList<Prestito> listaPrestiti) {
        // corpo vuoto (scheletro)
    }
    
    // getter e setter (scheletro)
    public String getNome() { return null; }
    public void setNome(String nome) {}

    public String getCognome() { return null; }
    public void setCognome(String cognome) {}

    public String getMatricola() { return null; }
    public void setMatricola(String matricola) {}

    public String getEmailIstituzionale() { return null; }
    public void setEmailIstituzionale(String emailIstituzionale) {}

    public ArrayList<Prestito> getListaPrestiti() { return null; }
    public void setListaPrestiti(ArrayList<Prestito> listaPrestiti) {}

    // Metodi
    public void addPrestito(Prestito p) {
        // scheletro
    }

    public void rimuoviPrestito(Prestito p) {
        // scheletro
    }

    // hashCode: restituisce un codice numerico basato sulla matricola
    @Override
    public int hashCode() { return 0; }

    // equals: confronta due utenti per matricola, evitando duplicati
    @Override
    public boolean equals(Object obj) { return false; }

    // compareTo: ordinamento naturale degli utenti per cognome, poi nome
    @Override
    public int compareTo(Utente other) { return 0; }
    
    
    @Override
    public String toString() {
        return "";
    }

}
