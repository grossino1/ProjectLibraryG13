package GestioneUtente;
import java.util.ArrayList;
import GestionePrestito.Prestito;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

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
    private String matricola;
    private String emailIstituzionale;
    private ArrayList<Prestito> listaPrestiti;
    
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
    public Utente(String nome, String cognome, String matricola, String emailIstituzionale) {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.emailIstituzionale = emailIstituzionale;
        this.listaPrestiti = new ArrayList<>();
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
     * @brief Imposta la matricola dell'utente.
     *
     * @pre matricola != null
     *
     * @param[in] matricola: La nuova matricola.
     */
    public void setMatricola(String matricola) {
        this.matricola = matricola;
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

    // METODI LOGICI

    /**
     * Aggiunge un prestito alla lista dei prestiti attivi dell'utente.
     *
     * @pre  p != null
     * @post listaPrestiti.size() == old_size + 1
     * @post listaPrestiti.contains(p) == true
     *
     * @param p Il prestito da aggiungere.
     */
    public void addPrestito(Prestito p) {
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
     */
   public void rimuoviPrestito(Prestito p) {
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
        res = 31 * res + getMatricola().hashCode();
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
     *
     * @param other L'utente da confrontare con quello corrente.
     * @return un numero negativo se questo utente precede {@code other},
     *         zero se sono equivalenti per ordinamento,
     *         un numero positivo se questo utente segue {@code other}.
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
        StringBuffer sb = new StringBuffer();
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