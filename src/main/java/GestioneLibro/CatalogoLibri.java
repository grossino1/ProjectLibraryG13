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
     */
    public void registrazioneLibro(Libro l) throws ISBNNotValidException, LibroPresenteException, LibroNotFoundException, CatalogoPienoException, IOException {
        // scheletro: qui andrebbe la logica di validazione prima dell'add
        if (catalogoLibri.size()>999){
            throw new CatalogoPienoException("È stato raggionto il numero massimo dei libri nel catalogo");
        }
        if (l==null)
            throw new LibroNotFoundException("Il libro non può essere nullo");
        if (!l.getIsbn().matches("\\d{13}")){
            throw new ISBNNotValidException("Il formato dell'IBSN non è valido");
        }
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
     * @post La lista restituita (può essere vuota).
     *
     * @param[in] l La stringa di ricerca.
     * @return ArrayList<Libro> contenente i risultati della ricerca.
     */
    public ArrayList<Libro> cercaLibro(String l) throws LibroNotFoundException {
        if (l==null)
            throw new LibroNotFoundException("È stata inserita una stringa vuota");
        ArrayList<Libro> trovati = new ArrayList<>();
        for (Libro lib : catalogoLibri){
            if(lib.getIsbn().compareToIgnoreCase(l) == 0 || lib.getTitolo().compareToIgnoreCase(l) == 0 || lib.getAutori().compareToIgnoreCase(l) == 0){
                trovati.add(lib);
            }
        }
        return trovati;
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
     * @brief Restituisce una vista ordinata del catalogo secondo un criterio personalizzato.
     *
     * Permette di ottenere i libri ordinati diversamente dall'ordine naturale
     * (autore o anno) utilizzando un Comparator.
     *
     * @pre comp != null (Il comparatore non deve essere nullo).
     * @post Viene restituito una nuova ArrayList ordinata secondo 'comp'.
     *
     * @param[in] comp Il comparatore che definisce il nuovo criterio di ordinamento.
     * @return Un ArrayList<Libro> riordinato.
     * 
     * @see java.util.Comparator
     */
    public ArrayList<Libro> sortCatalogoLibri(Comparator<Libro> comp) throws LibroNotFoundException {
        if (comp == null)
            throw new LibroNotFoundException("È stato inserito un comparatore nullo");
        ArrayList<Libro> listaordinata = new ArrayList<>(catalogoLibri);
        listaordinata.sort(comp);
        return listaordinata; 
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