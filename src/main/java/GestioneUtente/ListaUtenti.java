package GestioneUtente;

import Eccezioni.EccezioniUtenti.MatricolaNotValidException;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @class ListaUtenti
 * @brief Gestisce l'insieme di tutti gli utenti registrati.
 *
 * Questa classe agisce come contenitore principale per gli oggetti @ref Utente.
 * Utilizza un `TreeSet` per garantire che gli utenti siano sempre ordinati
 * (per cognome e nome, come definito in Utente.compareTo) e che non ci siano duplicati.
 *
 * @invariant listaUtenti != null (La struttura dati interna è sempre inizializzata).
 * @invariant listaUtenti non contiene valori null.
 *
 * @author grossino
 * @version 1.0
 */
public class ListaUtenti {

    /**
     * Collezione ordinata degli utenti registrati.
     */
    private Set<Utente> listaUtenti;
    
    /**
     * @brief Costruttore predefinito.
     *
     * @post listaUtenti != null && listaUtenti.isEmpty()
     */
    public ListaUtenti(){
        listaUtenti = new TreeSet<>();
    }
    
    /**
     * @brief Recupera un utente specifico tramite la matricola.
     *
     * Esegue una scansione della lista per trovare l'utente corrispondente.
     * È essenziale per le operazioni di prestito.
     * 
     * @pre matricola != null (La chiave di ricerca non può essere nulla).
     * @post Lo stato della lista rimane invariato (sola lettura).
     *
     * @param[in] matricola La stringa univoca identificativa dell'utente.
     * @return L'oggetto Utente corrispondente se trovato, altrimenti `null`.
     */
    // METODO FONDAMENTALE PER IL PRESTITO
    public Utente getUtenteByMatricola(String matricola) {
        for (Utente u : listaUtenti) {
            if (u.getMatricola().equals(matricola)) {
                return u;
            }
        }
        return null;
    }
    
    /**
     * @brief Registra un nuovo utente nel sistema.
     *
     * Aggiunge un utente alla collezione. Se l'utente è già presente (stessa Matricola),
     * il Set non lo duplicherà (rispettando l'equals di Utente), ma non verrà lanciato errore.
     * L'errore viene lanciato solo se la matricola non è valida strutturalmente.
     * 
     * @pre u != null (Non è possibile registrare utenti nulli).
     * @post listaUtenti.contains(u) == true.
     * @post size() >= old_size().
     *
     * @param[in] u L'oggetto Utente da registrare.
     *  * @throws MatricolaNotValidException Se l'utente ha un formato di matricola non valido.
     */
    public void registrazioneUtente(Utente u) throws MatricolaNotValidException {
        // scheletro
        if (u == null)
        return;
            if (!u.getMatricola().matches("\\d{10}")){
                throw new MatricolaNotValidException ("La matricola deve esser composta da 10 cifre");
        }
            listaUtenti.add(u);
    }

    /**
     * @brief Rimuove un utente dal sistema.
     *
     * @pre u != null
     * @post L'utente specificato non è più presente nella lista.
     * @post Se l'utente non c'è, la lista resta invariata.
     *
     * @param[in] u L'oggetto da rimuovere (deve essere un'istanza di Utente).
     */
    public void eliminazioneUtente(Object u) {
        // scheletro
        if (u == null || u.getClass() != Utente.class)
            return;
        listaUtenti.remove(u);
        
    }

    /**
     * @brief Cerca utenti in base a una stringa generica (es. Nome o Cognome).
     *
     * @pre u != null (La stringa di ricerca non deve essere nulla).
     * @post La lista restituita non è mai null (può essere vuota).
     *
     * @param[in] u La stringa di ricerca (es. "Rossi").
     * @return ArrayList<Utente> contenente gli utenti che corrispondono ai criteri.
     */
    public ArrayList<Utente> cercaUtente(String u) {
        ArrayList<Utente> trovato = new ArrayList<>(); // scheletro
        if(u== null)
            return trovato; //restituisce una lista vuota
        String ricerca = u.toLowerCase();
        for(Utente utente : listaUtenti){
            if (utente.getNome().toLowerCase().contains(ricerca) || utente.getCognome().toLowerCase().contains(ricerca))
                    trovato.add(utente);
        }
        return trovato;        
    }

    /**
     * @brief Restituisce l'elenco completo degli utenti.
     * 
     * Crea un nuovo ArrayList contenente tutti gli elementi presenti nel TreeSet.
     *
     * @post La lista restituita è una copia indipendente.
     * @post La lista mantiene l'ordinamento del TreeSet
     * @post La lista non è mai null (può essere vuota).
     *
     * @return Un ArrayList contenente tutti gli utenti iscritti.
     */
    public ArrayList<Utente> getListaUtenti() {
        return new ArrayList<>(listaUtenti); // scheletro
    }

    /**
     * @brief Restituisce una vista ordinata della lista secondo un criterio personalizzato.
     *
     * Permette di ottenere gli utenti ordinati diversamente dall'ordine naturale
     * (aggiunto piu recente o meno recente) utilizzando un Comparator.
     *
     * @pre comp != null (Il comparatore non deve essere nullo).
     * @post La lista restituita è ordinata secondo le regole di 'comp'.
     *
     * @param[in] comp Il comparatore da utilizzare.
     * @return ArrayList<Utente> riordinato.
     *
     * @see java.util.Comparator
     */
    public ArrayList<Utente> sortListaUtenti(Comparator<Utente> comp) {
        ArrayList<Utente> listaOrdinata = new ArrayList<>(listaUtenti);
        listaOrdinata.sort(comp);
        return listaOrdinata;
    }

   /**
     * @brief Restituisce una rappresentazione testuale dell'oggetto ListaUtenti.
     *
     * @post Il risultato non è mai null (restituisce sempre una stringa, anche vuota).
     *
     * @return Una stringa contenente la descrizione completa della lista utenti.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(); // scheletro
        for (Utente u : listaUtenti){
            sb.append(u.toString() + "\n");            
        }
        return sb.toString();
    }
    
}