package GestionePrestito;

import Eccezioni.EccezioniPrestiti.dataRestituzioneException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @class Prestito
 * @brief Rappresenta l'entità prestito all'interno del sistema di gestione bibliotecaria.
 * 
 * Il prestito nasce dall'associazione di un libro ed un utente per un periodo di tempo limitato. 
 *
 * @see Libro
 * @see Utente
 * 
 * @invariant IDPrestito != null (Il prestito deve avere un identificativo univoco).
 * @invariant ISBNLibro != null (Un prestito deve riferirsi a un libro esistente).
 * @invariant matricolaUtente != null (Un prestito deve essere associato a un utente).
 *
 * @author grossino1
 * @version 1.0
 */

public class Prestito implements Comparable<Prestito>, Serializable {

    private final String IDPrestito;
    private final String ISBNLibro;
    private final String matricolaUtente;
    private LocalDate dataRestituzione;
    private static int contatoreID;

    /**
     * @brief Costruttore della classe Prestito.
     *
     * @pre ISBNLibro != null && !ISBNLibro.isEmpty()
     * @pre matricolaUtente != null && !matricolaUtente.isEmpty()
     * @post Viene creata una nuova istanza valida di Prestito.
     * @post IDPrestito viene generato univocamente (logica interna).
     * @post dataRestituzione viene inizializzata (Data odierna + 30gg).
     *
     * @param[in] ISBNLibro Il codice ISBN del libro da prestare.
     * @param[in] matricolaUtente La matricola dell'utente richiedente.
     */
    public Prestito(String ISBNLibro, String matricolaUtente) throws IllegalArgumentException{
    
        if (ISBNLibro == null || ISBNLibro.isEmpty() || !ISBNLibro.matches("\\d{13}"))
            throw new IllegalArgumentException("Formato ISBN non valido");
        if (matricolaUtente == null || matricolaUtente.isEmpty() || !matricolaUtente.matches("\\d{10}"))
            throw new IllegalArgumentException("Formato matricola non valido");
        
        this.ISBNLibro = ISBNLibro;
        this.matricolaUtente = matricolaUtente;
        this.contatoreID++;
        this.IDPrestito = Integer.toString(contatoreID);
        this.dataRestituzione = LocalDate.now().plusDays(30); // oggi + 30 giorni
    }
       
    // Getter

    /**
     * @brief Restituisce l'ID univoco del prestito.
     *
     * @return La stringa identificativa del prestito.
     */
    public String getIDPrestito() {
     
        return IDPrestito;
    }

    /**
     * @brief Restituisce l'ISBN del libro prestato.
     *
     * @return Il codice ISBN.
     */
    public String getISBNLibro() {
        //Controllo sull'ISBN
        return ISBNLibro;
    }

    /**
     * @brief Restituisce la matricola dell'utente.
     *
     * @return La matricola dell'utente.
     */
    public String getMatricolaUtente() {
        //Controllo sulla matricola
        return matricolaUtente;
    }

    /**
     * @brief Restituisce la data prevista per la restituzione.
     *
     * @return La data di scadenza del prestito.
     */
    public LocalDate getDataRestituzione() {
        
        return dataRestituzione;
    }

    // Setter

    /**
     * @brief Imposta la data di restituzione, che non può essere maggiore di 30 giorni dal giorno corrente 
     * e non può essere un valore negatio o nullo.
     *
     * @pre dataRestituzione < 30 giorni dal giorno corrente.
     * 
     * @param[in] dataRestituzione La nuova data di restituzione.
     * @throws dataRestituzioneException
     */
    public void setDataRestituzione(LocalDate dataRestituzione) throws dataRestituzioneException{
        
        // Controllo 1: La data non deve essere null
        if (dataRestituzione == null) {
            throw new dataRestituzioneException("La data di restituzione non può essere nulla.");
        }

        // Controllo 2: La data non deve superare i 30 giorni da oggi
        long giorniDiDifferenza = ChronoUnit.DAYS.between(LocalDate.now(), dataRestituzione);

        if (giorniDiDifferenza > 30) {
            throw new dataRestituzioneException("La data di restituzione non può superare i 30 giorni da oggi.");
        } 
        
        // Controllo opzionale consigliato: La data non deve essere nel passato!
        if (giorniDiDifferenza < 0) {
             throw new dataRestituzioneException("La data di restituzione non può essere nel passato.");
        }
        
        this.dataRestituzione = dataRestituzione;
    }

    // Metodi Logici

    /**
     * @brief Calcola l'hash code basato sull'IDPrestito.
     *
     * @return Codice hash univoco per IDPrestito.
     *
     * @see #equals(Object) Utilizzato per la coerenza con equals.
     */
    @Override
    public int hashCode() {
        
        int result = 17;
        result = 31* result + this.IDPrestito.hashCode();
        return result;
    }

    /**
     * @brief Confronta due prestiti per per IDPrestito, evitando duplicati nell'elenco.
     *
     * Due prestiti sono uguali se hanno lo stesso ID, indipendentemente
     * dalla data o dagli attori coinvolti.
     *
     * @param[in] obj L'oggetto da confrontare.
     * @return true se gli IDPrestito coincidono, false altrimenti.
     *
     * @see #hashCode()
     */             
    @Override
    public boolean equals(Object obj) {
        
        if(obj == null) return false;
        if(this == obj) return true;
        if(this.getClass() != obj.getClass()) return false;
        
        Prestito other = (Prestito) obj;
        return this.IDPrestito.equals(other.IDPrestito);               
    }

    /**
     * @brief Ordina i prestiti in base alla data di restituzione (ordinamento naturale)
     * nel caso due prestiti dovessero avere la stessa data, si considera l'ID del Prestito.
     *
     * Utile per visualizzare i prestiti in ordine di scadenza (dal più urgente al meno urgente).
     *
     * @pre other != null
     *
     * @param[in] other Il prestito con cui confrontare la data.
     * @return < 0 se this scade prima, 0 se stesso giorno, > 0 se scade dopo.
     */
    @Override
    public int compareTo(Prestito other) { 
        
        int result = this.dataRestituzione.compareTo(other.dataRestituzione);
        if(result != 0) return result;
        
        return this.IDPrestito.compareTo(other.IDPrestito);
    }

    /**
     * @brief Restituisce una rappresentazione testuale del prestito.
     *
     * Fornisce una stringa contenente i dati principali del prestito
     * (IDPrestito, ISBN, Matricola e Data di restituzione).
     * 
     * @post Il risultato non è mai null.
     *
     * @return Una stringa contenente ID, ISBN, Matricola e Data.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ID prestito: "+ getIDPrestito());
        sb.append("\n");
        sb.append("ISBN: "+ getISBNLibro());
        sb.append("\n");
        sb.append(" Matricola: " + getMatricolaUtente());
        sb.append("\n");
        sb.append("Data di Restituzione: " + getDataRestituzione());
        return sb.toString();
    }
}