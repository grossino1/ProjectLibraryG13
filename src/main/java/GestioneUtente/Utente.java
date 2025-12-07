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
    private List<Prestito> listaPrestiti;
    
    /**
     * @brief Costruttore della classe Utente.
     *
     * @pre matricola != null && !matricola.isEmpty()
     * @post Viene creata una nuova istanza di Utente.
     * @post listaPrestiti != null && listaPrestiti.isEmpty() (La lista viene inizializzata vuota).
     *
     * @param[in] nome Il nome dell'utente.
     * @param[in] cognome Il cognome dell'utente.
     * @param[in] matricola Il codice identificativo univoco (es. numero di matricola universitaria).
     * @param[in] emailIstituzionale L'indirizzo email dell'utente.
     */
    public Utente(String nome, String cognome, String matricola, String emailIstituzionale) {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.emailIstituzionale = emailIstituzionale;
        this.listaPrestiti = new ArrayList<>();
    }
    
    // --- Getter e Setter ---

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
     * @param[in] nome Il nuovo nome.
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
     * @param[in] cognome Il nuovo cognome.
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
     * @param[in] matricola La nuova matricola.
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
     * @param[in] emailIstituzionale La nuova email.
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
    public List<Prestito> getListaPrestiti() { 
        return listaPrestiti; 
    }

    // Metodi Logici

    /**
     * @brief Aggiunge un prestito alla lista dell'utente.
     *
     * @pre p != null
     * @post listaPrestiti.size() == old_size + 1
     * @post listaPrestiti.contains(p) == true
     *
     * @param[in] p L'oggetto Prestito da aggiungere.
     */
    public void addPrestito(Prestito p) {
        // scheletro
        if (p!= null){
            listaPrestiti.add(p);
        }
    }

    /**
     * @brief Rimuove un prestito dalla lista dell'utente (es. alla restituzione).
     *
     * @pre p != null
     * @post listaPrestiti.contains(p) == false
     *
     * @param[in] p L'oggetto Prestito da rimuovere.
     */
    public void rimuoviPrestito(Prestito p) {
        // scheletro
        if (p!=null){
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
        if (matricola == null)
            return 0;
        else return matricola.hashCode();
    }

    /**
     * @brief Confronta due utenti per matricola, evitando duplicati nella lista.
     *
     * @param[in] obj L'oggetto da confrontare.
     * @return true se le matricole coincidono, false altrimenti.
     * 
     * @see #hashCode()
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Utente other = (Utente) obj;
        if (matricola != null && matricola.equals(other.matricola))
            return true;
        else return false;            
    }

    /**
     * @brief Ordina gli utenti per Cognome e poi per Nome (ordinamento naturale).
     *
     * @pre other != null
     *
     * @param[in] other L'utente da confrontare.
     * @return < 0 se this < other, 0 se uguali, > 0 se this > other.
     */
    @Override
    public int compareTo(Utente other) {
        int compare = this.cognome.compareToIgnoreCase(other.cognome);
        if (compare != 0)
            return compare;
        else return this.nome.compareToIgnoreCase(other.nome);
    }
    
    /**
     * @brief Restituisce una rappresentazione testuale dell'utente.
     *
     * Fornisce una stringa contenente i dati principali dell'utente
     * (Nome, Cognome, Matricola, Email e Lista libri in prestito). 
     * 
     * @post Il risultato non è mai null (restituisce sempre una stringa, anche vuota).
     * 
     * @return Stringa con Nome, Cognome, Matricola ed Email.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Utente: ");
        sb.append(getNome());
        sb.append(" " + getCognome()+ "\n");
        sb.append("Matricola: " + getMatricola() + "\n");
        sb.append("Email: " + getEmailIstituzionale() + "\n");
        sb.append("Prestiti: " + listaPrestiti.size()+ "\n");
        return sb.toString();
    }

}