package GestionePrestito;

import Eccezioni.EccezioniPrestiti.dataRestituzioneException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final long serialVersionUID = 1L;
    private final LocalDateTime dataRegistrazione;
    private final String ISBNLibro;
    private final String matricolaUtente;
    private LocalDate dataRestituzione;

    /**
     * @brief Costruttore della classe Prestito.
     *
     * @pre ISBNLibro != null && !ISBNLibro.isEmpty()
     * @pre matricolaUtente != null && !matricolaUtente.isEmpty()
     * @post Viene creata una nuova istanza valida di Prestito.
     * @post dataRegistrazionePrestito viene generato univocamente in base alla data di aggiunta.
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
        this.dataRegistrazione = LocalDateTime.now();
        this.dataRestituzione = LocalDate.now().plusDays(30); // oggi + 30 giorni
    }
       
    // Getter
    
    /**
     * @brief Restituisce la data univoca di registrazione del prestito.
     *
     * @return La data di aggiunta identificativa del prestito.
     */
    public String getDataRegistrazione() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dataRegistrazione.format(formatter);
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
     * @brief Calcola l'hash code basato sulla coppia univoca Matricola + ISBN.
     *
     * @return Codice hash univoco per la combinazione Studente-Libro.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.matricolaUtente == null ? 0 : this.matricolaUtente.hashCode());
        result = 31 * result + (this.ISBNLibro == null ? 0 : this.ISBNLibro.hashCode());
        return result;
    }

    /**
     * @brief Confronta due prestiti basandosi esclusivamente su Matricola e ISBN.
     *
     * Due prestiti sono considerati "lo stesso prestito" se riguardano
     * lo stesso studente e lo stesso libro.
     *
     * @param[in] obj L'oggetto da confrontare.
     * @return true se Matricola e ISBN coincidono.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        Prestito other = (Prestito) obj;

        // 1. Confronto Matricola
        if (!this.matricolaUtente.equals(other.matricolaUtente)) return false;

        // 2. Confronto ISBN
        if (!this.ISBNLibro.equals(other.ISBNLibro)) return false;

        return true;
    }

    /**
     * @brief Confronta i prestiti per matricola prima e per ISBN poi, evitando duplicati in un TreeSet.
     *
     * Questa logica non permette un ordinamento del TreeSet per dataRestituzione, che quindi dovrà essere implementato implicitamente dal controller.
     * 
     * @param[in] other Il prestito con cui confrontare.
     *@return 
     * Un valore < 0 se questo prestito precede alfabeticamente per matricola/ISBN l'altro,
     * 0 se i prestiti hanno la stessa matricola e lo stesso ISBN (sono lo stesso prestito logico), 
     * un valore > 0 se questo prestito segue alfabeticamente per matricola/ISBN l'altro.
     * 
    */
    @Override
    public int compareTo(Prestito other) {
    
        int res = this.matricolaUtente.compareTo(other.matricolaUtente);
        if (res != 0) return res;
        return this.ISBNLibro.compareTo(other.ISBNLibro);
    }

    /**
     * @brief Restituisce una rappresentazione testuale del prestito.
     *
     * Fornisce una stringa contenente i dati principali del prestito
     * (Data di Registarzione, ISBN, Matricola e Data di restituzione).
     * 
     * @post Il risultato non è mai null.
     *
     * @return Una stringa contenente ID, ISBN, Matricola e Data.
       */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ISBN: "+ getISBNLibro());
        sb.append("\n");
        sb.append("Data di Restituzione: " + getDataRestituzione());
        return sb.toString();
    }
}