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
    private GestorePrestito gestore; // Riferimento alla classe che fa i controlli

    // Costruttore: deve ricevere il Gestore già configurato
    public ElencoPrestiti(GestorePrestito gestore) {
        this.elencoPrestiti = new TreeSet<>();
        this.gestore = gestore;
    }

    public void registrazionePrestito(String isbn, String matricola) {
        // scheletro
        try {
            // 1. CHIAMO IL GESTORE per validare e creare il prestito
            // Gli passo 'this.listaPrestiti' così lui può contare i prestiti dell'utente
            boolean flag = gestore.nuovoPrestito(isbn, matricola);

            // 2. Se il gestore non ha lanciato eccezioni, AGGIUNGO alla lista
            if (flag) {
                Prestito nuovoPrestito = new Prestito(isbn, matricola);
                elencoPrestiti.add(nuovoPrestito);
                System.out.println("Prestito aggiunto con successo!");
            }

        } catch (Exception e) {
            // Gestisco l'errore (es. mostro a video o rilancio al controller)
            System.out.println("Errore inserimento prestito: " + e.getMessage());
        }
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

