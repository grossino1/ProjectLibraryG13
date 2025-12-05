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
 * @class ElencoPrestiti
 * @brief Gestisce il registro dei prestiti attivi.
 *
 * Mantiene una collezione ordinata di prestiti e delega le regole di validazione
 * (numero minimo di copie e numero massimo di prestiti per utente) alla classe @ref GestorePrestito.
 *
 * @invariant elencoPrestiti != null (La struttura dati è sempre inizializzata).
 * @invariant gestore != null (La classe deve sempre avere un riferimento valido al gestore delle regole).
 *
 * @see GestorePrestito
 * @see Prestito
 *
 * @author jackross
 * @version 1.0
 */

public class ElencoPrestiti {

    /**
     * Struttura dati principale (TreeSet) per memorizzare i prestiti in ordine naturale.
     */
    private Set<Prestito> elencoPrestiti;

    /**
     * Riferimento al componente che esegue la logica di controllo (validazione).
     */
    private GestorePrestito gestore; 

    /**
     * @brief Costruttore della classe ElencoPrestiti.
     *
     * Inizializza la collezione utilizzando un TreeSet per garantire
     * l'ordinamento automatico e l'unicità degli elementi. 
     * E associa il gestore delle regole.
     *
     * @pre gestore != null (Non è possibile creare un elenco prestiti senza un gestore logico).
     * @post elencoPrestiti.isEmpty() == true (Appena creato, l'elenco è vuoto).
     * @post this.gestore == gestore.
     *
     * @param[in] gestore L'istanza di GestorePrestito già configurata.
     */
    public ElencoPrestiti(GestorePrestito gestore) {
        this.elencoPrestiti = new TreeSet<>();
        this.gestore = gestore;
    }

    /**
     * @brief Tenta di registrare un nuovo prestito nel sistema.
     *
     * Il metodo orchestra l'operazione in due fasi:
     * 1. Delega al @ref GestorePrestito la validazione dei dati.
     * 2. Se la validazione passa, crea l'oggetto Prestito e lo aggiunge al Set.      
     *
     * @pre isbn != null && !isbn.isEmpty()
     * @pre matricola != null && !matricola.isEmpty()
     * @post Se successo: elencoPrestiti.size() == old_size + 1
     * @post Se fallimento/errore: elencoPrestiti.size() == old_size
     *
     * @param[in] isbn Il codice ISBN del libro da prestare.
     * @param[in] matricola La matricola dell'utente che richiede il prestito.
     */
    public void registrazionePrestito(String isbn, String matricola) {
        // scheletro
        try {
            // 1. CHIAMO IL GESTORE per validare e creare il prestito
            // Gli passo 'this.listaPrestiti' così lui può contare i prestiti dell'utente
            // NOTA: Qui presuppongo che GestorePrestito.nuovoPrestito accetti parametri diversi 
            // o abbia accesso a questa lista in altro modo, dato che qui passo solo stringhe.
            boolean flag = gestore.nuovoPrestito(isbn, matricola);

            // 2. Se il gestore non ha lanciato eccezioni e restituisce true
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
    
    /**
     * @brief Rimuove un prestito dall'elenco (es. alla restituzione del libro).
     *
     * @pre p != null
     * @post Il prestito p non è più presente nella collezione.
     * * @param[in] p L'oggetto Prestito da rimuovere.
     */
    public void eliminazionePrestito(Prestito p) {
        // scheletro
    }

    /**
     * @brief Cerca prestiti in base a una chiave di ricerca.
     *
     * @param[in] chiave La stringa da cercare (es. matricola utente o ISBN).
     * @return ArrayList<Prestito> contenente i prestiti che soddisfano il criterio.
     * @post La lista restituita non è mai null.
     */
    public ArrayList<Prestito> cercaPrestito(String chiave) {
        return null; // scheletro
    }

    /**
     * @brief Restituisce l'elenco completo dei prestiti attivi.
     *
     * @post La lista restituita è una copia indipendente (modificarla non altera i dati interni).
     * @post La lista mantiene l'ordinamento naturale definito in Prestito.compareTo().
     *
     * @return Un ArrayList contenente tutti i prestiti.
     */
    public ArrayList<Prestito> getElencoPrestiti() {
        return null; // scheletro
    }

    /**
     * @brief Restituisce una lista di prestiti ordinata secondo un criterio personalizzato.
     * * Nota: Il nome del metodo `sortListaUtenti` suggerisce un ordinamento di utenti, 
     * ma la firma indica un ordinamento di Prestiti.
     *
     * @pre comp != null (Il comparatore non deve essere nullo).
     * @post La lista restituita è ordinata secondo 'comp'.
     *
     * @param[in] comp Il comparatore per definire l'ordine dei prestiti.
     * @return ArrayList<Prestito> ordinata.
     */
    public ArrayList<Prestito> sortListaUtenti(Comparator<Prestito> comp) {
        return null; // scheletro
    }
    
    /**
     * @brief Rappresentazione testuale dell'elenco prestiti.
     * @return Stringa descrittiva dello stato corrente.
     */
    @Override
    public String toString() {  
        return null;
    }
}