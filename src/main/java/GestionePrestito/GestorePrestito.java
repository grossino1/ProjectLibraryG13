package GestionePrestito;

import Eccezioni.EccezioniLibri.LibroNotFoundException;
import Eccezioni.EccezioniPrestiti.CopieEsauriteException;
import Eccezioni.EccezioniPrestiti.EccezioniPrestito;
import Eccezioni.EccezioniPrestiti.PrestitiEsauritiException;
import Eccezioni.EccezioniUtenti.UtenteNotFoundException;
import GestioneLibro.CatalogoLibri;
import GestioneLibro.Libro;
import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import SalvataggioFile.SalvataggioFileLibro.SalvataggioFileLibro;
import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;

/**
 * @class GestorePrestito
 * @brief Classe che controlla se i vincoli per l'aggiunta di un prestito sono verificati.
 *
 * Questa classe funge da intermediario tra i dati (Libri, Utenti) e l'operazione di prestito.
 * Si occupa di validare se un prestito è ammissibile secondo le politiche della biblioteca.
 *
 * @invariant catalogo != null (Deve esserci un riferimento valido al catalogo libri).
 * @invariant utenti != null (Deve esserci un riferimento valido all'elenco utenti).
 *
 * @see GestioneLibro.CatalogoLibri
 * @see GestioneUtente.ListaUtenti
 *
 * @author grossino1
 * @version 1.0
 */

public class GestorePrestito {
    
    private CatalogoLibri catalogo;
    private ListaUtenti utenti;

    /**
     * @brief Costruttore del GestorePrestito.
     *
     * Associa le dipendenze necessarie per effettuare i controlli incrociati e aggiorna.
     *
     * @pre catalogo != null
     * @pre utenti != null
     * @post L'oggetto è inizializzato e pronto per validare i prestiti.
     *
     * @param[in] catalogo Il catalogo dei libri esistente.
     * @param[in] utenti La lista degli utenti esistenti.
     */
    public GestorePrestito(CatalogoLibri catalogo, ListaUtenti utenti) {
        this.catalogo = catalogo;
        this.utenti = utenti;
    }
    
    /**
     * @brief Valida e autorizza la creazione di un nuovo prestito.
     *
     * Questo metodo applica le seguenti regole:
     * 1. **Esistenza**: Il libro e l'utente devono esistere nei rispettivi archivi.
     * 2. **Disponibilità**: Il libro deve avere almeno una copia disponibile (copie > 0).
     * 3. **Affidabilità**: L'utente non deve aver superato il limite massimo di prestiti (Max 3).
     *
     * Se tutti i controlli passano, il metodo autorizza l'operazione restituendo true.
     * In caso di errore, stampa un messaggio di diagnostica sulla console e blocca l'operazione.
     *
     * @pre ISBN != null && !ISBN.isEmpty()
     * @pre matricola != null && !matricola.isEmpty()
     * @post Se restituisce true: Tutte le condizioni sono soddisfatte, old_libro.getNumeroCopie()+1 && utente.getListaPrestiti().size+1.
     * @post Se restituisce false: Almeno una condizione ha fallito (nessuna modifica ai dati).
     *
     * @param[in] ISBN Il codice del libro richiesto.
     * @param[in] matricola La matricola dell'utente richiedente.
     * @return true se il prestito è approvato, false altrimenti.
     * 
     * @throws LibroNotFoundException Se il codice ISBN non corrisponde a nessun libro nel catalogo.
     * @throws UtenteNotFoundException Se la matricola non corrisponde a nessun utente registrato.
     * @throws CopieEsauriteException Se il libro esiste ma il numero di copie disponibili è < 1.
     * @throws PrestitiEsauritiException Se l'utente ha già raggiunto il limite massimo di prestiti attivi (>= 3).
     */
    public boolean nuovoPrestito(String ISBN, String matricola) throws LibroNotFoundException, UtenteNotFoundException, EccezioniPrestito {
        
        // 1. Recupero le entità Libro e Utente tramite gli identificativi
        // Ipotizzo che questi metodi restituiscano null se non trovano l'oggetto
        Libro libro = catalogo.getLibroByISBN(ISBN);
        Utente utente = utenti.getUtenteByMatricola(matricola);

        // Controllo preliminare se esistono
        if (libro == null) {
            throw new LibroNotFoundException("Errore: Il libro cercato non esiste o è nullo.");
        }
    
        if (utente == null) {
            throw new UtenteNotFoundException("Errore: Utente non valido.");
        }

        if (libro.getNumeroCopie() < 1) {
            throw new CopieEsauriteException("Errore: Nessuna copia disponibile per il libro '" + libro.getTitolo() + "'");
        }

        if (utente.getListaPrestiti().size() >= 3) {
            throw new PrestitiEsauritiException("Errore: L'utente " + utente.getNome() + " ha raggiunto il limite di 3 prestiti.");
        }

        // 4. Se tutti i controlli passano, torno true e aggiungo il numero di copie ed il libro alla lista della matricola....
        return true;
    }
    
    /**
     * @brief Quando un prestito termina (viene eliminato dall'elenco prestiti) la copia del libro relativo viene aumentata
     *
     * @pre ISBN != null && !ISBN.isEmpty()
     * @post Il numero delle copie del libro è aumentato di 1.
     * @post Il catalogo dei libri aggiornato viene salvato sul file binario.
     * 
     * @param[in] ISBN Il codice del libro restituito.
     */
    public void aggiungiCopiaPrestitoLibro(String ISBN) {
        
        Libro libro = catalogo.getLibroByISBN(ISBN);
        libro.setNumeroCopie(libro.getNumeroCopie()+1);
        SalvataggioFileLibro.salva(catalogo, "Catalogo Libri");
    }
    
    /**
     * @brief Quando un prestito termina (viene eliminato dall'elenco prestiti) il prestito viene eliminato dalla lista dell'utente che lo ha terminato.
     *
     * @pre matricola != null && !matricola.isEmpty()
     * @pre p != null
     * @post Il numero dei prestiti dell'utente è diminuito di 1.
     * @post La lista dei prestiti aggiornato viene salvata sul file binario.
     * 
     * @param[in] matricola La matricola dell'utente cha ha restituito il libro.
     * @param[in] p Il prestito che l'utente ha appena terminato.
     * 
     */
    public void rimuoviPrestitoListaUtente(String matricola, Prestito p) {
    
        Utente utente = utenti.getUtenteByMatricola(matricola);
        utente.rimuoviPrestito(p);
        SalvataggioFileUtente.salva(utenti, "Lista Utenti");
    }
}
