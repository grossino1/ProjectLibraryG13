package GestionePrestito;

import Eccezioni.EccezioniLibri.EccezioniLibro;
import Eccezioni.EccezioniLibri.LibroNotFoundException;
import Eccezioni.EccezioniPrestiti.EccezioniPrestito;
import Eccezioni.EccezioniPrestiti.ElencoPienoException;
import Eccezioni.EccezioniPrestiti.PrestitoNonTrovatoException;
import Eccezioni.EccezioniPrestiti.dataRestituzioneException;
import Eccezioni.EccezioniUtenti.EccezioniUtente;
import Eccezioni.EccezioniUtenti.UtenteNotFoundException;
import SalvataggioFile.SalvataggioFilePrestito.SalvataggioFilePrestito;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.TreeSet;
import java.util.Set;
import java.util.ArrayList;

/**
 * @class ElencoPrestiti
 * @brief Gestisce l'insieme di tutti i prestiti attivi.
 *
 * Questa classe agisce come contenitore principale per gli oggetti @ref Prestito.
 * Utilizza un `TreeSet` per garantire che non ci siano duplicati. 
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

public class ElencoPrestiti implements Serializable{

    private static final long serialVersionUID = 1L;
    private Set<Prestito> elencoPrestiti;
    private static final int MAX_PRESTITI = 500;
    private transient GestorePrestito gestore; 
    private final String filename;

    /**
     * @brief Costruttore della classe ElencoPrestiti.
     *
     * Controlla una flag che indica se deve caricare i dati da un file. 
     * Se la flag è true li carica.
     * Se la flag è false inizializza la struttura senza dati (senza prestiti).
     * 
     * @pre gestore != null (Non è possibile creare un elenco prestiti senza un gestore logico).
     * @post Se caricamentoFile è false allora elencoPrestiti.isEmpty() == true.
     * @post Se caricamentoFile è true allora elencoPrestiti.isEmpty() == false.
     * @post this.gestore == gestore.
     * @post this.filename = filename.
     *
     * @param[in] caricamentoFile Flag indica se prendere i dati da un file.
     * @param[in] filename Nome del file da cui caricare o su cui salvare.
     * @param[in] gestore É un'istanza del gestore delle regore.;
     * 
     * @see SalvataggioFile.SalvataggioFilePrestito.SalvataggioFilePrestito#carica(java.lang.String) 
     * 
     * @throws IOException Se il caricamento dal file fallisce.
     * @throws ClassNotFoundException Se il casting dell'oggetto dal file fallisce.
     */
    public ElencoPrestiti(boolean caricamentoFile, String filename, GestorePrestito gestore) throws IOException, ClassNotFoundException {
       
        this.filename = filename;
        this.gestore = gestore;
        if (caricamentoFile) {
            ElencoPrestiti oggettoSalvato = SalvataggioFilePrestito.carica(filename);
            this.elencoPrestiti = oggettoSalvato.elencoPrestiti;
        }
        else {
            this.elencoPrestiti = new TreeSet<>();
        }
    }

    /**
     * @brief Tenta di registrare un nuovo prestito nel sistema.
     *
     * Il metodo divide l'operazione in quattro fasi:
     * 1. Delega al @ref GestorePrestito la validazione dei dati.
     * 2. Se la validazione passa, crea l'oggetto Prestito e lo aggiunge al TreeSet.   
     * 3. Delega al @ref GestorePrestito l'aggiornamento dei dati.
     * 4. Salva l'elenco aggiornato sul file.
     *
     * @pre isbn != null && !isbn.isEmpty()
     * @pre matricola != null && !matricola.isEmpty()
     * @post Se successo: elencoPrestiti.size() == old_size + 1 e l'elenco prestiti aggiornato viene salvato sul file binario.
     * @post Se fallimento/errore: elencoPrestiti.size() == old_size
     * 
     * @param[in] isbn Il codice ISBN del libro da prestare.
     * @param[in] matricola La matricola dell'utente che richiede il prestito.
     * 
     * @see GestorePrestito#nuovoPrestito(java.lang.String, java.lang.String) 
     * @see GestorePrestito#diminuisciCopiaPrestitoLibro(java.lang.String) 
     * @see GestorePrestito#aggiungiPrestitoListaUtente(java.lang.String, GestionePrestito.Prestito) 
     * @see SalvataggioFile.SalvataggioFilePrestito.SalvataggioFilePrestito#salva(GestionePrestito.ElencoPrestiti, java.lang.String) 
     * 
     * @throws LibroNotFoundException Se il codice ISBN non corrisponde a nessun libro nel catalogo.
     * @throws UtenteNotFoundException Se la matricola non corrisponde a nessun utente registrato.
     * @throws EccezioniPrestito Se uno dei vincoli per il prestito non è rispettato.
     * @throws IOException Se il salvataggio sul file fallisce.
     * @throws ClassNotFoundException Se il casting nella carica dal file (di Gestore.nuovoPrestito()) fallisce.
     */
    public void registrazionePrestito(String isbn, String matricola) throws LibroNotFoundException, UtenteNotFoundException, EccezioniPrestito, IOException, ClassNotFoundException{
        try {
            if(elencoPrestiti.size() >= MAX_PRESTITI) {
                throw new ElencoPienoException("ERRORE: Numero massimo di prestiti inseriti!");
            }
            boolean flag = gestore.nuovoPrestito(isbn, matricola);
            
            if (flag) {
                Prestito nuovoPrestito = new Prestito(isbn, matricola);
                elencoPrestiti.add(nuovoPrestito);
                gestore.diminuisciCopiaPrestitoLibro(isbn);
                gestore.aggiungiPrestitoListaUtente(matricola, nuovoPrestito);
                SalvataggioFilePrestito.salva(this, filename);
            }

        } catch (EccezioniLibro | EccezioniUtente | EccezioniPrestito e) {
            throw e;        
        }
    }
    
    /**
     * @brief Rimuove un prestito dall'elenco.
     *
     * @pre p != null
     * @post Se il  p è presente, p non è più presente nell'elenco prestiti.
     * @post I dati sul libro e l'utente vengono aggiornati.
     * @post L'elenco prestiti aggiornato viene salvato sul file binario.
     * 
     * @param[in] p L'oggetto Prestito da rimuovere.
     * 
     * @see GestorePrestito#aggiungiCopiaPrestitoLibro(java.lang.String) 
     * @see GestorePrestito#rimuoviPrestitoListaUtente(java.lang.String, GestionePrestito.Prestito) 
     * @see SalvataggioFile.SalvataggioFilePrestito.SalvataggioFilePrestito#salva(GestionePrestito.ElencoPrestiti, java.lang.String) 
     * 
     * @throws IOException Se il salvataggio sul file fallisce.
     * @throws PrestitoNonTrovatoException Se il prestito indicato non viene trovato nell'elenco.
     */
    public void eliminazionePrestito(Prestito p) throws PrestitoNonTrovatoException, IOException{
        
        if(!elencoPrestiti.remove(p)){
            throw new PrestitoNonTrovatoException("ERRORE: Il prestito che vuoi eliminare non è presente all'interno del catalogo.");
        }
        
        gestore.aggiungiCopiaPrestitoLibro(p.getISBNLibro());
        gestore.rimuoviPrestitoListaUtente(p.getMatricolaUtente(), p);
        SalvataggioFilePrestito.salva(this, filename);
    }

    /**
     * @brief Cerca Prestiti in base a una stringa generica (ISBN o Matricola).
     * 
     * Restituisce una lista di prestiti che corrispondono alla ricerca.
     * Nota: Restituisce un ArrayList invece di un Set per gestire potenziali
     * omonimie.
     *
     * @pre chiave != null.
     * @post La lista restituita.
     * 
     * @param[in] chiave La stringa di ricerca.
     * @return ArrayList<Prestito> contenente i prestiti che soddisfano il criterio.
     * 
     * @throws PrestitoNonTrovatoException Se un prestito non viene trovato.
     */
    public ArrayList<Prestito> cercaPrestito(String chiave) throws PrestitoNonTrovatoException {
    
        ArrayList<Prestito> listaRicerca = new ArrayList<>();
    
        for(Prestito p : elencoPrestiti) {
               
            if(p.getMatricolaUtente().startsWith(chiave) || p.getISBNLibro().startsWith(chiave)) {
                listaRicerca.add(p);
            }
        }
    
        if(listaRicerca.isEmpty()) {
            throw new PrestitoNonTrovatoException("ERRORE: Nessun prestito presente");
        } else {
            return listaRicerca;
    }
}
    
    /**
     * @brief Modifica le informazioni del prestito.
     * 
     * Nello specifico modifica solo la data di restituzione, visto che gli altri 
     * attributi non sono modificabili.
     * 
     * @post La data di restituzione del prestito viene modificata.
     * 
     * @param[inout] p Il Prestito da modificare.
     * @param[in] dataNuova La nuova data del prestito.
     * 
     * @see Prestito#setDataRestituzione(java.time.LocalDate) 
     * @see SalvataggioFile.SalvataggioFilePrestito.SalvataggioFilePrestito#salva(GestionePrestito.ElencoPrestiti, java.lang.String) 
     * 
     * @throws dataRestituzioneException Se uno dei vincoli sulla data di restituzione non viene rispetatto.
     * @throws IOException Se il salvataggio sul file fallisce.
     */ 
    public void modificaPrestito(Prestito p, LocalDate dataNuova) throws dataRestituzioneException, IOException {
        p.setDataRestituzione(dataNuova);    
        SalvataggioFilePrestito.salva(this, filename);
    }

    /**
     * @brief Restituisce l'elenco completo dei prestiti attivi.
     *
     * Crea un nuovo ArrayList contenente tutti gli elementi presenti nel TreeSet.
     *
     * @post La lista restituita è una copia indipendente.
     * @post La lista mantiene lo stesso ordinamento del TreeSet.
     * @post La lista non è mai null (può essere vuota).
     *
     * @return Un ArrayList<Prestito> contenente tutti i libri presenti.
     */
    public ArrayList<Prestito> getElencoPrestiti() {
        
        ArrayList<Prestito> elencoCompleto = new ArrayList<>(elencoPrestiti);
        return elencoCompleto;
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
}