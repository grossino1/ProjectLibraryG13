package GestioneLibro;

import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Comparator;
import Eccezioni.EccezioniLibri.ISBNNotValidException;
import Eccezioni.EccezioniLibri.LibroPresenteException;
import Eccezioni.EccezioniLibri.LibroNotFoundException;
import Eccezioni.EccezioniLibri.CatalogoPienoException;
import SalvataggioFile.SalvataggioFileLibro.SalvataggioFileLibro;
import java.io.IOException;
import java.io.Serializable;


/**
 * @class CatalogoLibri
 * @brief Gestisce l'insieme dei libri presenti nel sistema bibliotecario.
 *
 * Questa classe funge da container principale per gli oggetti di tipo @ref Libro.
 * Utilizza internamente un `TreeSet` per mantenere i libri ordinati secondo il
 * loro ordinamento naturale (definito in Libro.compareTo) ed evitare duplicati.
 *
 * @invariant catalogoLibri != null (Il riferimento alla collezione interna non è mai nullo).
 * @invariant catalogoLibri non contiene elementi null (TreeSet non accetta null).
 *
 * @author grossino1
 * @version 1.0
 */

public class CatalogoLibri implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String filename;

    private Set<Libro> catalogoLibri;

    /**
     * @brief Costruttore di default.
    */
    public CatalogoLibri() {
        
    }
    
    public CatalogoLibri(boolean caricamentoFile, String filename) throws IOException, ClassNotFoundException {
        if (caricamentoFile) {
            CatalogoLibri oggettoSalvato = SalvataggioFileLibro.carica(filename);
            this.catalogoLibri = oggettoSalvato.catalogoLibri;
        }
        else {
            this.catalogoLibri = new TreeSet<>();
        } 
        this.filename = filename;
    

    }

    /**
     * @brief Cerca un libro specifico tramite il suo codice ISBN.
     *
     * Esegue una scansione del catalogo per trovare il libro corrispondente.
     * È essenziale per le operazioni di prestito.
     *
     * @pre isbn != null (Il codice di ricerca non può essere nullo).
     * @post Se trovato, restituisce l'istanza del libro; altrimenti null.
     * @post Lo stato del catalogo rimane invariato (sola lettura).
     *
     * @param[in] isbn La stringa rappresentante il codice ISBN da cercare.
     * @return L'oggetto Libro associato all'ISBN, oppure `null` se non presente.
     */
    public Libro getLibroByISBN(String isbn) {
        for (Libro l : catalogoLibri) {
            if (l.getIsbn().equals(isbn)) {
                return l;
            }
        }
        return null; 
    }

    /**
     * @brief Registra un nuovo libro nel catalogo.
     *
     * Aggiunge un libro alla collezione. Se il libro è già presente (stesso ISBN),
     * il Set non lo duplicherà (rispettando l'equals di Libro), ma non verrà lanciato errore.
     * L'errore viene lanciato solo se l'ISBN non è valido strutturalmente.
     *
     * @pre l != null (Non si possono inserire libri nulli).
     * @post catalogoLibri.contains(l) == true.
     * @post size() >= old_size().
     *
     * @param[in] l: L'oggetto Libro da aggiungere.
     * @throws ISBNNotValidException Se il libro ha un formato ISBN non valido.
     * @throws LibroNotFoundException Se il libro non è presente nel catalogo.
     * @throws CatalogoPienoException Se il catalogo ha raggiunto la capienza massima.
     * @throws IOException Se il salvataggio fallisce.
     * @throws LibroPresenteException Se il libro è già presente nel catalogo.
     */
    public void registrazioneLibro(Libro l) throws ISBNNotValidException, LibroPresenteException, LibroNotFoundException, CatalogoPienoException, IOException {
        // scheletro: qui andrebbe la logica di validazione prima dell'add
        if (catalogoLibri.size()>999){
            throw new CatalogoPienoException("È stato raggionto il numero massimo dei libri nel catalogo");
        }
        if (l==null)
            throw new LibroNotFoundException("Il libro non può essere nullo");
        
        if (!catalogoLibri.add(l))
            throw new LibroPresenteException("Il libro scelto è già presente nel catalogo libri");
        
        
        SalvataggioFileLibro.salva(this, filename);
        
    }

    /**
     * @brief Rimuove un libro dal catalogo.
     *
     * @pre l != null.
     * @post Il libro specificato non è più presente nel catalogo.
     * @post Se il libro non c'è, il catalogo resta invariato.
     *
     * @param[in] l: L'oggetto Libro da rimuovere.
     * @throws LibroNotFoundException il libro non è presente nel catalogo
     * @throws IOException se il salvataggio fallisce
     */
    public void eliminazioneLibro(Libro l) throws LibroNotFoundException, IOException{
        if (l==null)
            throw new LibroNotFoundException("Il libro non può essere nullo");
        if (!catalogoLibri.contains(l))
            throw new LibroNotFoundException("Il libro non è presente nel catalogo");
        catalogoLibri.remove(l); 
        SalvataggioFileLibro.salva(this, filename);
    }

    /**
     * @brief Cerca libri in base a una stringa generica (Titolo, Autore o ISBN).
     * 
     * Restituisce una lista di libri che corrispondono alla ricerca.
     * Nota: Restituisce un ArrayList invece di un Set per gestire potenziali
     * omonimie.
     *
     * @pre l != null
     * @post La lista restituita.
     *
     * @param[in] l La stringa di ricerca.
     * @return ArrayList<Libro> contenente i risultati della ricerca.
     * @throws LibroNotFoundException se un libro non viene trovato
     */
    public ArrayList<Libro> cercaLibro(String l) throws LibroNotFoundException {
        
        ArrayList<Libro> trovati = new ArrayList<>();
        String query = l.toLowerCase(); // trasforma la stringa di ricerca in minuscolo
        for (Libro lib : catalogoLibri){

            if (lib.getIsbn().toLowerCase().startsWith(query) || lib.getTitolo().toLowerCase().startsWith(query) || lib.getAutori().toLowerCase().startsWith(query)) {
                trovati.add(lib);
            }
        }
        if (trovati.isEmpty())
            throw new LibroNotFoundException ("Il libro ricercato non è presente nel catalogo");
        else return trovati;
    }

 /**
     * @brief Restituisce una copia dell'intero catalogo come lista.
     *
     * Crea un nuovo ArrayList contenente tutti gli elementi presenti nel TreeSet.
     *
     * @post La lista restituita è una copia indipendente.
     * @post La lista mantiene lo stesso ordinamento del TreeSet.
     * @post La lista non è mai null (può essere vuota).
     *
     * @return Un ArrayList<Libro> ordinato contenente tutti i libri presenti.
     */
    public ArrayList<Libro> getCatalogoLibri() {
        return new ArrayList<>(catalogoLibri);
    }

    
     /**
     * @brief Modifica le informazioni del libro.
     * 
     * La modifica avviene su tutti gli attrbuti del libro eccetto l'ISBN che non è modificabile
     * 
     * @post L'elemento Libro viene modificato.
     * 
     * @param[in] l il libro da modificare.
     * @param[i]  titolo, autori, annoPubblicazione, numeroCopie
     * @throws LibroNotFoundException il libro non è presente nel catalogo
     * @throws IOException se il salvataggio fallisce
     * 
     * 
     */ 
    public void modificaLibro(Libro l, String titolo, String autori, int annoPubblicazione, int numeroCopie) throws IOException, LibroNotFoundException {
        if(l == null)
            throw new LibroNotFoundException("Non hai selezionato un libro!");
        l.setTitolo(titolo); 
        l.setAutori(autori);
        l.setAnnoPubblicazione(annoPubblicazione);
        l.setNumeroCopie(numeroCopie);
        SalvataggioFileLibro.salva(this, filename);
    }

    /**
     * @brief Restituisce una rappresentazione testuale dell'oggetto CatalogoLibri.
     *
     * @post Il risultato non è mai null (restituisce sempre una stringa, anche vuota).
     *
     * @return Una stringa contenente la descrizione completa del catalogo.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); 
        sb.append("=== Catalogo Libri ===\n");

        if (catalogoLibri.isEmpty()) {
            sb.append("Il catalogo è attualmente vuoto.\n");
        } else {
            for (Libro l : catalogoLibri) {
                sb.append(l.toString()).append("\n");
                sb.append("----------------------\n");
            }
        }
        
        return sb.toString(); 
    }
}