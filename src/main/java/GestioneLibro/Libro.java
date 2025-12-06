package GestioneLibro;

import java.io.Serializable;

/**
 * @class Libro
 * @brief Rappresenta l'entità libro all'interno del sistema di gestione bibliotecaria.
 * 
 * Questa classe gestisce i dati del libro e mantiene lo stato attuale del numero di copie ancora disponibili.
 *
 * @invariant numeroCopie >= 0 (Il numero di copie non può mai essere negativo).
 * @invariant isbn != null (Ogni libro deve avere un codice ISBN).
 * 
 * @see java.io.Serializable
 *
 * @author grossino1
 * @version 1.1
 */

public class Libro implements Comparable<Libro>, Serializable {

    private String titolo;
    private String autori;
    private int annoPubblicazione;
    private String isbn;
    private int numeroCopie;

    /**
     * @brief Costruttore della classe Libro.
     *
     * @pre isbn != null && !isbn.isEmpty()
     * @pre numeroCopie >= 0
     * @post Viene creata una nuova istanza di Libro con i valori assegnati.
     *
     * @param[in] titolo Il titolo del libro.
     * @param[in] autori I nomi degli autori.
     * @param[in] annoPubblicazione L'anno di pubblicazione.
     * @param[in] isbn Il codice ISBN univoco.
     * @param[in] numeroCopie Il numero iniziale di copie (deve essere >= 0).
     */
    
    public Libro(String titolo, String autori, int annoPubblicazione, String isbn, int numeroCopie) {
        // corpo vuoto (scheletro)
        this.titolo = titolo;
        this.autori = autori;
        this.annoPubblicazione = annoPubblicazione;
        this.isbn = isbn;
        this.numeroCopie = numeroCopie;
    }

    // Getter e Setter
    
    /**
     * @brief Restituisce il titolo del libro.
     * 
     * @return Il titolo corrente.
     */
    public String getTitolo() { return titolo; }
    
    /**
     * @brief Imposta il titolo del libro.
     * 
     * @param[in] titolo La stringa del nuovo titolo.
     */
    public void setTitolo(String titolo) { this.titolo = titolo; }

    /**
     * @brief Restituisce gli autori che hanno scritto il libro.
     * 
     * @return Gli autori del libro.
     */
    public String getAutori() { return autori; }
    
    /**
     * @brief Imposta gli autori che hanno scritto il libro.
     * 
     * @param[in] autori La stringa degli autori.
     */
    public void setAutori(String autori) { this.autori = autori; }

    /**
     * @brief Restituisce l'anno di pubblicazione del libro.
     * 
     * @return L'anno di publicazione.
     */
    public int getAnnoPubblicazione() { return annoPubblicazione; }
    
    /**
     * @brief Imposta l'anno di pubblicazione del libro.
     * 
     * @param[in] annoPubblicazione Un intero dell'anno di pubblicazione
     */
    public void setAnnoPubblicazione(int annoPubblicazione) { this.annoPubblicazione = annoPubblicazione; }

    /**
     * @brief Restituisce il codice ISBN.
     * 
     * @return Il codice ISBN.
     */
    public String getIsbn() { return isbn; }
    
    /**
     * @brief Imposta il codice ISBN.
     * 
     * @pre isbn != null (L'ISBN non può essere nullo).
     * 
     * @param[in] isbn Il nuovo codice ISBN.
     * 
     * @see #hashCode() Se l'ISBN cambia, cambia anche l'hash.
     */
    public void setIsbn(String isbn) {
 
    }

    /**
     * @brief Restituisce il numero di copie.
     * 
     * @return Intero rappresentante le copie.
     */
    public int getNumeroCopie() { return numeroCopie; }
    
    /**
     * @brief Imposta manualmente il numero di copie.
     * 
     * @pre numeroCopie >= 0 (Non sono ammesse quantità negative).
     * 
     * @param[in] numeroCopie La nuova quantità di copie.
     */
    public void setNumeroCopie(int numeroCopie) { this.numeroCopie = numeroCopie; }

    // Metodi Logici
    
    /**
     * @brief Incrementa di una unità il numero di copie.
     * 
     * @post numeroCopie == old_numeroCopie + 1
     */
    public void incrementaCopiaLibro(){}
    
    /**
     * @brief Decrementa di una unità il numero di copie.
     * 
     * @pre numeroCopie > 0 (Deve esserci almeno una copia disponibile per decrementare).
     * @post numeroCopie == old_numeroCopie - 1
     */
    public void decrementaCopiaLibro(){}
    
    /**
     * @brief Genera l'hash code basato su ISBN.
     * 
     * @return Codice hash univoco per ISBN.
     * 
     * @see #equals(Object) Utilizzato per la coerenza con equals.
     */
    @Override
    public int hashCode() { return 0; }
    
    /**
     * @brief Confronta due libri per ISBN, evitando duplicati nel catalogo.
     * 
     * @param[in] obj L'oggetto da confrontare.
     * @return true se gli ISBN coincidono, false altrimenti.
     * 
     * @see #hashCode()
     */
    @Override
    public boolean equals(Object obj) { return false; }
    
    /**
     * @brief Ordina i libri per titolo (ordinamento naturale).
     * 
     * @pre other != null
     * 
     * @param[in] other Il libro da confrontare.
     * @return < 0 se this < other, 0 se uguali, > 0 se this > other.
     */ 
    @Override
    public int compareTo(Libro other) { return 0; }

    /**
     * @brief Restituisce una rappresentazione testuale dell'oggetto Libro.
     *
     * Fornisce una stringa contenente i dati principali del libro
     * (Titolo, Autori, ISBN, Anno e Numero copie). 
     *
     * @post Il risultato non è mai null (restituisce sempre una stringa, anche vuota).
     *
     * @return Una stringa contenente la descrizione completa del libro.
     */
    @Override
    public String toString() {
        return "ISBN: " + isbn + " | Titolo: " + titolo + " | Autori: " + autori + 
               " (" + annoPubblicazione + ") | Copie disp: " + numeroCopie;
    }
}
