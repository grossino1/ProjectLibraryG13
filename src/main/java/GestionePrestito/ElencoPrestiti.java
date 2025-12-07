package GestionePrestito;

import Eccezioni.EccezioniLibri.EccezioniLibro;
import Eccezioni.EccezioniLibri.LibroNotFoundException;
import Eccezioni.EccezioniPrestiti.EccezioniPrestito;
import Eccezioni.EccezioniPrestiti.PrestitoNonTrovatoException;
import Eccezioni.EccezioniUtenti.EccezioniUtente;
import Eccezioni.EccezioniUtenti.UtenteNotFoundException;
import SalvataggioFile.SalvataggioFilePrestito.SalvataggioFilePrestito;
import java.util.TreeSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @class ElencoPrestiti
 * @brief Gestisce l'insieme di tutti i prestiti attivi.
 *
 * Questa classe agisce come contenitore principale per gli oggetti @ref Prestito.
 * Utilizza un `TreeSet` per garantire che i Prestiti siano sempre ordinati
 * (in base alla data di restituzione, come definito in Prestito.compareTo) e che non ci siano duplicati. 
 * Delega le regole di validazione (numero minimo di copie e numero massimo di prestiti per utente) 
 * alla classe @ref GestorePrestito.
 *
 * @invariant elencoPrestiti != null (La struttura dati è sempre inizializzata).
 * @invariant gestore != null (La classe deve sempre avere un riferimento valido al gestore delle regole).
 *
 * @see GestorePrestito
 * @see Prestito
 *
 * @author grossino1
 * @version 1.0
 */

public class ElencoPrestiti {

    private Set<Prestito> elencoPrestiti;
    private GestorePrestito gestore; 

    /**
     * @brief Costruttore della classe ElencoPrestiti.
     *
     * @pre gestore != null (Non è possibile creare un elenco prestiti senza un gestore logico).
     * @post elencoPrestiti.isEmpty() == true (Appena creato, l'elenco è vuoto).
     * @post this.gestore == gestore.
     *
     * @param[in] gestore L'istanza di GestorePrestito già configurata.
     */
    public ElencoPrestiti(GestorePrestito gestore) {
        this.elencoPrestiti = new TreeSet<>();
        this.gestore = gestore;
    }

    /**
     * @brief Tenta di registrare un nuovo prestito nel sistema.
     *
     * Il metodo orchestra l'operazione in due fasi:
     * 1. Delega al @ref GestorePrestito la validazione dei dati.
     * 2. Se la validazione passa, crea l'oggetto Prestito e lo aggiunge al TreeSet.      
     *
     * @pre isbn != null && !isbn.isEmpty()
     * @pre matricola != null && !matricola.isEmpty()
     * @post Se successo: elencoPrestiti.size() == old_size + 1 e l'elenco prestiti aggiornato viene salvato sul file binario.
     * @post Se fallimento/errore: elencoPrestiti.size() == old_size
     * 
     * @param[in] isbn Il codice ISBN del libro da prestare.
     * @param[in] matricola La matricola dell'utente che richiede il prestito.
     * 
     * @throws LibroNotFoundException Se il codice ISBN non corrisponde a nessun libro nel catalogo.
     * @throws UtenteNotFoundException Se la matricola non corrisponde a nessun utente registrato.
     * @throws EccezioniPrestito Se uno dei vincoli per il prestito non sono rispettati.
     */
    public void registrazionePrestito(String isbn, String matricola) throws LibroNotFoundException, UtenteNotFoundException, EccezioniPrestito{
        try {
            boolean flag = gestore.nuovoPrestito(isbn, matricola);
            
            if (flag) {
                Prestito nuovoPrestito = new Prestito(isbn, matricola);
                elencoPrestiti.add(nuovoPrestito);
                SalvataggioFilePrestito.salva(this, "Elenco Prestiti");
            }

        } catch (EccezioniLibro | EccezioniUtente | EccezioniPrestito e) {
            throw e;        
        }
    }
    
    /**
     * @brief Rimuove un prestito dall'elenco
     *
     * @pre p != null
     * @post Il prestito p non è più presente nell'elenco prestiti.
     * @post L'elenco prestiti aggiornato viene salvato sul file binario.
     * 
     * @param[in] p L'oggetto Prestito da rimuovere.
     * 
     * throws PrestitoNonTrovatoException
     */
    public void eliminazionePrestito(Prestito p) throws PrestitoNonTrovatoException{
        
        if(!elencoPrestiti.remove(p)){
            throw new PrestitoNonTrovatoException("Il prestito che vuoi eliminare non è presente all'interno del catalogo.");
        }
        
        SalvataggioFilePrestito.salva(this, "Elenco Prestiti");
    }

    /**
     * @brief Cerca Prestiti in base a una stringa generica (IDPrestito, ISBN o Matricola).
     * 
     * Restituisce una lista di prestiti che corrispondono alla ricerca.
     * Nota: Restituisce un ArrayList invece di un Set per gestire potenziali
     * omonimie.
     *
     * @pre chiave != null.
     * @post La lista restituita.
     * 
     * @param[in] chaive la stringa di ricerca.
     * @return ArrayList<Prestito> contenente i prestiti che soddisfano il criterio.
     * 
     * @throws PrestitoNonTrovatoException
     */
    public ArrayList<Prestito> cercaPrestito(String chiave) throws PrestitoNonTrovatoException {
        
        ArrayList<Prestito> listaRicerca = new ArrayList<>();
        
        for(Prestito p: elencoPrestiti) {
            
            if(p.getIDPrestito().equals(chiave)) {
                listaRicerca.add(p);
            }
            else if(p.getMatricolaUtente().equals(chiave)) {
                listaRicerca.add(p);
            }
            else if(p.getISBNLibro().equals(chiave)) {
                listaRicerca.add(p);
            }         
        }
        
        if(listaRicerca == null) throw new PrestitoNonTrovatoException("Nessun prestito presente");
        else return listaRicerca;
    }

    /**
     * @brief Restituisce l'elenco completo dei prestiti attivi secondo l'ordinamento naturale (data di restituzione).
     *
     * Crea un nuovo ArrayList contenente tutti gli elementi presenti nel TreeSet.
     *
     * @post La lista restituita è una copia indipendente.
     * @post La lista mantiene lo stesso ordinamento del TreeSet.
     * @post La lista non è mai null (può essere vuota).
     *
     * @return Un ArrayList<Prestito> ordinato contenente tutti i libri presenti.
     */
    public ArrayList<Prestito> getElencoPrestiti() {
        
        ArrayList<Prestito> elencoCompleto = new ArrayList<>(elencoPrestiti);
        return elencoCompleto;
    }

    /**
     * @brief Restituisce una vista ordinata dell'elenco secondo un criterio personalizzato.
     *
     * Permette di ottenere i libri ordinati diversamente dall'ordine naturale
     * (scadenza, più recente o meno recente) utilizzando un Comparator.
     *
     * @pre comp != null (Il comparatore non deve essere nullo).
     * @post Viene restituito una nuova ArrayList ordinata secondo 'comp'.
     *
     * @param[in] comp Il comparatore che definisce il nuovo criterio di ordinamento.
     * @return Un ArrayList<Prestito> contenente gli stessi libri, ma riordinati.
     * 
     * @see java.util.Comparator
     */
    public ArrayList<Prestito> sortListaUtenti(Comparator<Prestito> comp) {
        
        ArrayList<Prestito> lista = this.getElencoPrestiti();
    
        lista.sort(comp);
        return lista;
    }
    
    /**
     * @brief Restituisce una rappresentazione testuale dell'oggetto ElencoPrestiti.
     *
     * @post Il risultato non è mai null (restituisce sempre una stringa, anche vuota).
     *
     * @return Una stringa contenente la descrizione completa dell'elenco prestiti.
     */
    @Override
    public String toString() {  
        StringBuffer sb = new StringBuffer();
        sb.append("Prestiti all'interno della lista:\n" );
        
        for(Prestito p: elencoPrestiti) {
            
            sb.append(p.toString() + "\n");
        }
        
        return sb.toString();
    }
    
    /**
     * @brief Restituisce una rappresentazione testuale della lista dei prestiti ordinata 
     * precedentemente tramite un'ordinamento scelto.
     *
     * @post Il risultato non è mai null (restituisce sempre una stringa, anche vuota).
     * 
     * @param[in] ArrayList<Prestito> listaOrdinata L'insieme dei prestiti ordinati.
     * @return Una stringa contenente la descrizione completa dell'elenco prestiti ordinato.
     */
    public String toStringListaOrdinata(ArrayList<Prestito> listaOrdinata) {
        
        StringBuffer sb = new StringBuffer();
        sb.append("Prestiti all'interno della lista:\n" );
        
        for(Prestito p: listaOrdinata) {
            
            sb.append(p.toString() + "\n");
        }
        
        return sb.toString();
    }
}