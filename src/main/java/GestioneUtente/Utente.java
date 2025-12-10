package GestioneUtente;
import java.util.ArrayList;
import GestionePrestito.Prestito;
import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;
import Eccezioni.EccezioniUtenti.MatricolaNotValidException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;
import java.io.IOException;


/**
 * @class Utente
 * @brief Rappresenta un utente registrato nel sistema bibliotecario.
 *
 * Questa classe gestisce i dati anagrafici dell'utente e mantiene lo stato attuale dei suoi prestiti. 
 *
 * @invariant matricola != null (Ogni utente deve essere identificabile univocamente).
 * @invariant listaPrestiti != null (La lista prestiti deve essere inizializzata, anche se vuota).
 *
 * @see GestionePrestito.Prestito
 * @see java.io.Serializable
 *
 * @author jackross
 * @version 1.0
 */
public class Utente implements Comparable<Utente>, Serializable {

    private String nome;
    private String cognome;
    private final String matricola;
    private String emailIstituzionale;
    private ArrayList<Prestito> listaPrestiti;
    
    // Attributo statico per indicare l'ID di ogni studente
    // Serve per ordinare gli utenti dal più recente al meno recente e viceversa
    // Ogni volta che si crea un Utente nuovo l'ID aumenta
    private static int cnt = 0;
    private final int idUtente;
    
    /**
     * @brief Costruttore della classe Utente.
     *
     * @pre matricola != null && !matricola.isEmpty()
     * @post Viene creata una nuova istanza di Utente.
     * @post listaPrestiti != null && listaPrestiti.isEmpty() (La lista viene inizializzata vuota).
     *
     * @param[in] nome: Il nome dell'utente.
     * @param[in] cognome: Il cognome dell'utente.
     * @param[in] matricola: Il codice identificativo univoco (es. numero di matricola universitaria).
     * @param[in] emailIstituzionale: L'indirizzo email dell'utente.
     * @param[in] listaPrestiti: La lista dei Prestiti Attivi dell'Utente.
     */
    public Utente(String nome, String cognome, String matricola, String emailIstituzionale) throws IllegalArgumentException, MatricolaNotValidException {
        this.nome = nome;
        this.cognome = cognome;
        
        //Controllo che la matricola sia corretta 
        if(matricola == null)
            throw new IllegalArgumentException("La matricola non può essere nulla!");
        if(matricola.length() != 10 || matricola.matches("\\d{10}"))
            throw new MatricolaNotValidException("La matricola deve essere di 10 cifre!");
        this.matricola = matricola;
        this.emailIstituzionale = emailIstituzionale;
        this.listaPrestiti = new ArrayList<>();
        this.idUtente = cnt++;
    }
    
    // Getter e Setter 

    /**
     * @brief Restituisce il nome dell'utente.
     * 
     * @return Il nome.
     */
    public String getNome() { 
        return nome; 
    }

    /**
     * @brief Imposta il nome dell'utente.
     * 
     * @param[in] nome: Il nuovo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @brief Restituisce il cognome dell'utente.
     * 
     * @return Il cognome.
     */
    public String getCognome() { 
        return cognome;
    }

    /**
     * @brief Imposta il cognome dell'utente.
     * 
     * @param[in] cognome: Il nuovo cognome.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * @brief Restituisce la matricola.
     * 
     * @return La matricola univoca.
     */
    public String getMatricola() { 
        return matricola;
    }


    /**
     * @brief Restituisce l'email istituzionale.
     * 
     * @return L'email.
     */
    public String getEmailIstituzionale() { 
        return emailIstituzionale;
      }

    /**
     * @brief Imposta l'email istituzionale.
     * 
     * @param[in] emailIstituzionale: La nuova email.
     */
    public void setEmailIstituzionale(String emailIstituzionale) {
        this.emailIstituzionale = emailIstituzionale;
    }

    /**
     * @brief Restituisce la lista dei prestiti dell'utente.
     *
     * @post Il risultato non è mai null (al massimo vuoto)
     * 
     * @return ArrayList contenente i prestiti.
     */
    public ArrayList<Prestito> getListaPrestiti() { 
        // Inserisco la lista dei prestiti dell'utente in un ArrayList e lo restituisco.
        return new ArrayList<Prestito>(listaPrestiti); 
    }

    public int getIdUtente() {
        return idUtente;
    }

    // METODI LOGICI
    /**
     * Genera un elenco delle date di restituzione a partire da una lista di prestiti.
     * Il metodo itera attraverso la lista fornita ed estrae la data di restituzione
     * da ciascun prestito.
     *
     * @param listaPrestiti La lista di oggetti Prestito da elaborare.
     * @return Un ArrayList contenente le date di restituzione.
     * Restituisce una lista vuota se il parametro listaPrestiti è null o vuoto.
     */
    public ArrayList<LocalDate> getListaDataRestituzione(ArrayList<Prestito> listaPrestiti){
        // Se listaPrestiti è vuota ritorna una lista vuota
        if(listaPrestiti == null)
            return new ArrayList<LocalDate>();
        // Creo un ArrayList di appoggio
        ArrayList<LocalDate> listaDataRestituzione = new ArrayList<>();
        // Scorro listaPrestiti e aggiungo l'attributo "dataRestituzione" di ogni prestito nell'ArrayList
        for(Prestito p : listaPrestiti){
            listaDataRestituzione.add(p.getDataRestituzione());
        }
        // Ritorna la lista contenente le date di restituzioni di tutti i prestiti attivi dell'Utente
        return listaDataRestituzione;
    }
    
    /**
     * Aggiunge un prestito alla lista dei prestiti attivi dell'utente.
     *
     * @pre  p != null
     * @post listaPrestiti.size() == old_size + 1
     * @post listaPrestiti.contains(p) == true
     *
     * @param p Il prestito da aggiungere.
     * @throws IllegalArgumentException: Se il prestito inserito come parametro è nullo.
     */
    public void addPrestito(Prestito p) {
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(p == null){
            throw new IllegalArgumentException("Errore: Impossibile aggiungere un prestito nullo.");
        }
        
       // La precondizione garantisce che p non sia null; quindi è sufficiente aggiungerlo alla lista.
       listaPrestiti.add(p);
    }

    /**
     * Rimuove un prestito dalla lista dei prestiti attivi dell'utente
     * (ad esempio in caso di restituzione del materiale).
     *
     * @pre  p != null
     * @post listaPrestiti.contains(p) == false
     *
     * @param p Il prestito da rimuovere.
     * @throws IllegalArgumentException: Se il prestito inserito come parametro è nullo.
     */
    public void rimuoviPrestito(Prestito p) {
       // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(p == null){
            throw new IllegalArgumentException("Errore: Impossibile rimuovere un prestito nullo.");
        }
        
       // Se p è presente nella lista, viene rimosso (p != null è garantito dalla precondizione.)
       if (listaPrestiti.contains(p)) {
           listaPrestiti.remove(p);
       }
    }

    
    
    /**
     * @brief Genera l'hash code basato sulla matricola.
     *
     * @return Codice hash univoco per matricola.
     * 
     * @see #equals(Object) Utilizzato per la coerenza con equals.
     */
    @Override
    public int hashCode() {
        int res = 7;
        res = 31 * res + getMatricola().toLowerCase().hashCode();
        return res;
    }

    /**
     * @brief Confronta due utenti per matricola, determinando se sono uguali oppure no in modo da evitare duplicati.
     *        Due utenti sono considerati uguali se possiedono la stessa matricola.
     * 
     * @param[in] obj: L'oggetto da confrontare.
     * @return    true se le matricole coincidono, false altrimenti.
     * 
     * @see #hashCode()
     */
    public boolean equals(Object other) {
        // Se l'oggetto dato come parametro è nullo, allora di sicuro sono diversi.
        if(other == null) return false;
        // Se i due oggetti hanno la stessa reference, allora sono uguali.
        if(this == other) return true;
        // Se i due oggetti appartengono a classi diverse allora sono diversi.
        if(getClass() != other.getClass()) return false;
        // L'oggetto other di sicuro appartiene alla classe Utente, quindi posso fare un cast con sicurezza.
        Utente u = (Utente) other;
        // Due utenti sono uguali se hanno la stessa matricola (confronto Case-Insensitive).
        return this.matricola.equalsIgnoreCase(u.getMatricola());
    }

    /**
     * Confronta questo utente con un altro in base all'ordinamento alfabetico.
     * L'ordinamento avviene prima per cognome e, in caso di parità, per nome.
     * Nel caso in cui due utenti hanno lo stesso cognome e nome, allora si ordina per matricola.
     * 
     * @param other: L'utente da confrontare con quello corrente.
     * @return un numero negativo se questo utente precede other {@code other},
     *         zero se sono equivalenti per ordinamento,
     *         un numero positivo se questo utente segue other {@code other}.
     */
    @Override
    public int compareTo(Utente other) {
        // Confronto i due oggetti per cognome.
        int cmp = this.cognome.compareToIgnoreCase(other.cognome);
        // Se compareTo() restituisce un numero diverso da 0, allora i due cognomi sono diversi e restituisco questo ordinamento.
        if (cmp != 0) {
            return cmp;
        }
        // Se i due utenti hanno lo stesso cognome l'ordinamento è sul nome.
        int compare = this.nome.compareToIgnoreCase(other.nome);
        if (compare != 0){
            return compare;
        }
        
        // Se due utenti hanno lo stesso cognome e nome, allora ordino per matricola,
        // la quale è per definizione univoca. 
        // Questo confronto permette di inserire due utenti con stesso cognome e nome ma con matricola diversa.
        return this.matricola.compareToIgnoreCase(other.matricola);
    }

    
    /**
     * @brief Restituisce una rappresentazione testuale dell'utente.
     *
     * Fornisce una stringa contenente i dati principali dell'utente
     * (Nome, Cognome, Matricola, Email e Lista libri in prestito). 
     * 
     * @post Il risultato non è mai null (restituisce sempre una stringa, anche vuota).
     * 
     * @return Stringa con Nome, Cognome, Matricola, Email Istituzionale e Lista dei Prestiti.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Utente: \n");
        sb.append("Nome: ");
        sb.append(getNome());
        sb.append("Cognome: ");
        sb.append(getCognome());
        sb.append("Matricola: ");
        sb.append(getMatricola());
        sb.append("E-Mail Istituzionale: ");
        sb.append(getEmailIstituzionale());
        sb.append("Elenco dei Prestiti Attivi: ");
        sb.append(getListaPrestiti());
        
        return sb.toString();
    }

}