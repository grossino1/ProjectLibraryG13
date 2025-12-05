package GestionePrestito;

import java.time.LocalDate;

/**
 * @class Prestito
 * @brief Rappresenta l'entità prestito all'interno del sistema di gestione bibliotecaria.
 * 
 * Il presto nasce dall'associazione di un libro ed un utente per un periodo di tempo limitato. 
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

public class Prestito implements Comparable<Prestito> {

    private String IDPrestito;
    private String ISBNLibro;
    private String matricolaUtente;
    private LocalDate dataRestituzione;

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
    public Prestito(String ISBNLibro, String matricolaUtente) {
        // corpo vuoto (scheletro)
    }

    // Getter

    /**
     * @brief Restituisce l'ID univoco del prestito.
     *
     * @return La stringa identificativa del prestito.
     */
    public String getIDPrestito() { return null; }

    /**
     * @brief Restituisce l'ISBN del libro prestato.
     *
     * @return Il codice ISBN.
     */
    public String getISBNLibro() { return null; }

    /**
     * @brief Restituisce la matricola dell'utente.
     *
     * @return La matricola dell'utente.
     */
    public String getMatricolaUtente() { return null; }

    /**
     * @brief Restituisce la data prevista per la restituzione.
     *
     * @return La data di scadenza del prestito.
     */
    public LocalDate getDataRestituzione() { return null; }

    // Setter

    /**
     * @brief Imposta l'ID del prestito.
     *
     * @pre IDPrestito != null
     *
     * @param[in] IDPrestito Il nuovo ID da assegnare.
     */
    public void setIDPrestito(String IDPrestito) {}

    /**
     * @brief Imposta l'ISBN del libro.
     *
     * @pre ISBNLibro != null
     *
     * @param[in] ISBNLibro Il nuovo ISBN.
     */
    public void setISBNLibro(String ISBNLibro) {}

    /**
     * @brief Imposta la matricola dell'utente.
     *
     * @pre matricolaUtente != null
     *
     * @param[in] matricolaUtente La nuova matricola.
     */
    public void setMatricolaUtente(String matricolaUtente) {}

    /**
     * @brief Imposta la data di restituzione.
     *
     * @param[in] dataRestituzione La nuova data di restituzione.
     */
    public void setDataRestituzione(LocalDate dataRestituzione) {}

    // Metodi Logici

    /**
     * @brief Calcola l'hash code basato sull'IDPrestito.
     *
     * @return Codice hash univoco per IDPrestito.
     *
     * @see #equals(Object) Utilizzato per la coerenza con equals.
     */
    @Override
    public int hashCode() { return 0; }

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
    public boolean equals(Object obj) { return false; }

    /**
     * @brief Ordina i prestiti in base alla data di restituzione (ordinamento naturale).
     *
     * Utile per visualizzare i prestiti in ordine di scadenza (dal più urgente al meno urgente).
     *
     * @pre other != null
     *
     * @param[in] other Il prestito con cui confrontare la data.
     * @return < 0 se this scade prima, 0 se stesso giorno, > 0 se scade dopo.
     */
    @Override
    public int compareTo(Prestito other) { return 0; }

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
    public String toString() { return ""; }
}