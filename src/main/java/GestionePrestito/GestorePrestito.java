/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionePrestito;

import GestioneLibro.CatalogoLibri;
import GestioneLibro.Libro;
import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import java.util.ArrayList;

/**
 *
 * @author jackross
 */
public class GestorePrestito {
    
    private CatalogoLibri catalogo;
    private ListaUtenti utenti;

    // Costruttore: riceve i riferimenti agli altri package
    public GestorePrestito(CatalogoLibri catalogo, ListaUtenti utenti) {
        this.catalogo = catalogo;
        this.utenti = utenti;
    }
    
    public boolean nuovoPrestito(String ISBN, String matricola){
        
        // 1. Recupero le entità Libro e Utente tramite gli identificativi
        // Ipotizzo che questi metodi restituiscano null se non trovano l'oggetto
        Libro libro = catalogo.getLibroByISBN(ISBN);
        Utente utente = utenti.getUtenteByMatricola(matricola);

        // Controllo preliminare se esistono
        if (libro == null || utente == null) {
            System.err.println("Errore: Libro o Utente non trovato.");
            return false;
        }

        // 2. Controllo disponibilità copie del Libro
        // Ipotizzo un metodo in Libro o GestoreLibri che mi dica le copie
        if (libro.getNumeroCopie()< 1) {
            System.err.println("Errore: Nessuna copia disponibile per il libro " + libro.getTitolo());
            return false;
        }

        // 3. Controllo numero di prestiti dell'Utente
    
        if (utente.getListaPrestiti().size() >= 3) {
            System.err.println("Errore: L'utente " + utente.getNome() + " ha già 3 prestiti attivi.");
            return false;
        }

        // 4. Se tutti i controlli passano, torno true e aggiungo il numero di copie ed il libro alla lista della matricola....
        return true;
    }
}
