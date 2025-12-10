package GestioneUtente;

import Eccezioni.EccezioniUtenti.MatricolaNotValidException;
import Eccezioni.EccezioniUtenti.UtentePresenteException;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Comparator;
import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;
import java.io.IOException;
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
        // Viene scelto come Collection il TreeSet per la sua capacità di ordinamento.
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
     * @param[in] matricola: La stringa univoca identificativa dell'utente.
     * @return L'oggetto Utente corrispondente se trovato, altrimenti `null`.
     */
    // METODO FONDAMENTALE PER IL PRESTITO
    public Utente getUtenteByMatricola(String matricola) {
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(matricola == null){
            throw new IllegalArgumentException("Errore: La chiave di ricerca non può essere nulla.");
        }
        
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
     * Aggiunge un utente alla collezione. Se l'utente è già presente (stesso Cognome, Nome e Matricola),
     * il Set non lo duplicherà (rispettando l'implementazione del compareTo() definito in Utente) e sarà lanciato un errore.
     * Inoltre se la matricola non è valida strutturalmente verrà lanciato un altro tipo di errore.
     * 
     * @pre u != null (Non è possibile registrare utenti nulli).
     * @post listaUtenti.contains(u) == true.
     * @post size() >= old_size().
     *
     * @param[in] u: L'oggetto Utente da registrare.
     *  * @throws MatricolaNotValidException: Se l'utente ha un formato di matricola non valido.
     *    @throws UtentePresenteException: Se l'utente passato come parametro è già presente all'interno della lista degli utenti.
     */
    public void registrazioneUtente(Utente u, String nomeFile) throws MatricolaNotValidException, UtentePresenteException, IOException {
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(u == null){
            throw new IllegalArgumentException("Errore: Impossibile aggiungere un utente nullo.");
        }
        
        // Controllo della matricola: la matricola deve avere 10 caratteri
        if (!u.getMatricola().matches("\\d{10}")){
                throw new MatricolaNotValidException ("La matricola deve esser composta da 10 cifre");
        }
        
        // Controllo dell'esistenza dell'utente: se esiste già la matricola nella lista l'utente non può essere inserito
        if(getUtenteByMatricola(u.getMatricola()) != null){
            // Esiste già un utente identico (stessa matricola)
            throw new UtentePresenteException("L'utente è già presente all'interno della lista.");
        }
        
        // Se vengono superati tutti i criteri, allora l'Utente u può essere aggiunto alla listaUtenti
        listaUtenti.add(u);
        System.out.println("Utente inserito con successo: " + u.getMatricola());
        
        // Chiamata al metodo statico salva, a cui passo l'oggetto listaUtente corrente e il nome del file
        SalvataggioFileUtente.salva(this, nomeFile);

        System.out.println("Salvataggio su file completato.");
    }

    /**
     * @brief Rimuove un utente dal sistema.
     *
     * @pre u != null
     * @post L'utente specificato non è più presente nella lista.
     * @post Se l'utente passato come parametro non è presente all'interno della lista, 
     *       essa resta invariata.
     *
     * @param[in] u: L'oggetto da rimuovere (deve essere un'istanza di Utente).
     */
    public void eliminazioneUtente(Object u) {
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(u == null){
            throw new IllegalArgumentException("Errore: Impossibile rimuovere un utente nullo.");
        }
        
        // Se l'oggetto passato non appartine alla classe Utente, allora non può essere rimosso.
        // Nota: Utilizzo "instanceof" e non "getClass()" perchè se in futuro si vorrà 
        // aggiungere una sottoclasse di Utente il metodo resta sempre valido anche per essa!
        if (!(u instanceof Utente))
            return;
        
        // Se l'utente passato come parametro fa parte della lista allora viene eliminato.
        listaUtenti.remove(u);
        
    }

    /**
     * @brief Cerca utenti in base a una Stringa generica che rappresenta il Cognome o la Matricola.
     *
     * @pre u != null (La stringa di ricerca non deve essere nulla).
     * @post La lista restituita non è mai null (può essere vuota).
     *
     * @param[in] u: La stringa di ricerca (es. "Rossi").
     * @return ArrayList<Utente> contenente gli utenti che corrispondono ai criteri.
     */
    public ArrayList<Utente> cercaUtente(String u) {
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(u == null){
            throw new IllegalArgumentException("Errore: La stringa di ricerca non può essere nulla.");
        }
        
        // Creo un ArrayList<Utente> per contenere la lista di utenti.
        ArrayList<Utente> listaRicerca = new ArrayList<>(); 
        
        // Per rendere las ricerca Case-Insensitive trasformo la stringa passata con tutte lettere minuscole.
        String utenteCercato = u.toLowerCase();
        for(Utente utente : listaUtenti){
            // Controllo se la stringa corrisponde al Cognome o alla Matricola di ogni utente appartenente a listaUtenti.
            if (utente.getCognome().toLowerCase().contains(utenteCercato) || utente.getMatricola().toLowerCase().contains(utenteCercato)){
                // Se un utente corrisponde ai criteri allora viene aggiunto all'interno dell'ArrayList
                listaRicerca.add(utente);
            }           
        }
        
        // Viene ritornato l'ArrayList contenente tutti gli utenti che rispettano i requisiti.
        return listaRicerca;
    }

    /**
     * @brief Restituisce l'elenco completo degli utenti.
     * 
     * Crea un nuovo ArrayList contenente tutti gli elementi presenti nel TreeSet.
     *
     * @post La lista è una "Shallow Copy": la collezione è nuova, ma gli oggetti contenuti sono riferimenti agli stessi del TreeSet.
     * @post La lista mantiene l'ordinamento del TreeSet.
     * @post La lista non è mai null (può essere vuota).
     *
     * @return Un ArrayList contenente tutti gli utenti iscritti.
     */
    public ArrayList<Utente> getListaUtenti() {
        // Creo un'ArrayList contenente gli stessi riferimenti del TreeSet listaUtenti e lo restituisco.
        // Nota: Poichè viene passato il TreeSet al costruttore dell'ArrayList, significa che inserirà ogni elemento in ordine.
        // Quindi la lista mantiene l'ordinamento del TreeSet.
        return new ArrayList<>(listaUtenti); 
    }

    /**
     * @brief Restituisce una vista ordinata della lista secondo un criterio personalizzato.
     *
     * Permette di ottenere una copia della lista ordinata secondo un criterio diverso dall'ordine naturale 
     * (es. per Cognome invece che per Matricola).
     *
     * @pre comp != null (Il comparatore non deve essere nullo).
     * @post La lista restituita è ordinata secondo le regole di 'comp'.
     *
     * @param[in] comp: Il comparatore da utilizzare.
     * @return ArrayList<Utente> riordinato.
     *
     * @see java.util.Comparator
     */
    public ArrayList<Utente> sortListaUtenti(Comparator<Utente> comp) {
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(comp == null){
            throw new IllegalArgumentException("Errore: Il comparatore non può essere nullo.");
        }
        
        // Creo una lista di appoggio in cui inserisco tutti gli elementi del TreeSet.
        ArrayList<Utente> listaOrdinata = new ArrayList<>(listaUtenti);
        // Ordino la lista secondo al Comparator passato come parametro, invocando il metodo sort() sulla lista e passando il comparatore.
        listaOrdinata.sort(comp);
        // Ritorno la lista ordinata.
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
        StringBuffer sb = new StringBuffer(); 
        sb.append("Lista Utenti:");
        for (Utente u : listaUtenti){
            sb.append("\n*****\n");
            sb.append(u.toString());    
        }
        return sb.toString();
    }
    
}