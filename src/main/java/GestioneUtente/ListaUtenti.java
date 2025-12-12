package GestioneUtente;

import Eccezioni.EccezioniUtenti.MatricolaNotValidException;
import Eccezioni.EccezioniUtenti.UtenteNotFoundException;
import Eccezioni.EccezioniUtenti.UtentePresenteException;
import Eccezioni.EccezioniUtenti.ListaUtentiPienaException;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Comparator;
import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;
import java.io.IOException;
import java.io.Serializable;
/**
 * @class ListaUtenti
 * @brief Gestisce l'insieme di tutti gli utenti registrati.
 *
 * Questa classe agisce come contenitore principale per gli oggetti @ref Utente.
 * Utilizza un `TreeSet` per garantire che gli utenti siano sempre ordinati
 * (per cognome e nome, come definito in Utente.compareTo) e che non ci siano duplicati.
 *
 * @invariant listaUtenti != null (La struttura dati interna è sempre inizializzata).
 * @invariant listaUtenti non contiene valori null.
 *
 * @author grossino
 * @version 1.0
 */
public class ListaUtenti implements Serializable{

    /**
     * Collezione ordinata degli utenti registrati.
     */
    private static final long serialVersionUID = 1L;
        
    private Set<Utente> listaUtenti;
    private String filename;
    
    /**
     * @brief Costruttore di default.
    */
    public ListaUtenti() {
        
    }
    /**
     * @brief Costruttore predefinito.
     *
     * @post listaUtenti != null && listaUtenti.isEmpty()
     */
    public ListaUtenti(boolean caricamentoFile, String filename) throws IOException, ClassNotFoundException{
        // Viene scelto come Collection il TreeSet per la sua capacità di ordinamento.
        if(caricamentoFile){
            ListaUtenti oggettoSalvato = SalvataggioFileUtente.carica(filename);
            this.listaUtenti = oggettoSalvato.listaUtenti;
        }else{
            listaUtenti = new TreeSet<>();
        }
        this.filename = filename;
    }
    
    /**
     * @brief Recupera un utente specifico tramite la matricola.
     *
     * Esegue una scansione della lista per trovare l'utente corrispondente.
     * È essenziale per le operazioni di prestito.
     *  
     * @pre matricola != null (La chiave di ricerca non può essere nulla).
     * @post Lo stato della lista rimane invariato (sola lettura).
     *
     * @param[in] matricola: La stringa univoca identificativa dell'utente.
     * @return L'oggetto Utente corrispondente se trovato, altrimenti `null`.
     * @throws IllegalArgumentException: Se la matricola inserita come parametro è nullo.
     */
    // METODO FONDAMENTALE PER IL PRESTITO
    public Utente getUtenteByMatricola(String matricola) {
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(matricola == null){
            throw new IllegalArgumentException("Errore: La chiave di ricerca non può essere nulla.");
        }
        
        for (Utente u : listaUtenti) {
            if (u.getMatricola().equals(matricola)) {
                return u;
            }
        }
        return null;
    }
    
    /**
     * @brief Registra un nuovo utente nel sistema.
     *
     * Aggiunge un utente alla collezione. Se l'utente è già presente (stesso Cognome, Nome e Matricola),
     * il Set non lo duplicherà (rispettando l'implementazione del compareTo() definito in Utente) e sarà lanciato un errore.
     * Inoltre se la matricola non è valida strutturalmente verrà lanciato un altro tipo di errore.
     * 
     * @pre u != null (Non è possibile registrare utenti nulli).
     * @post listaUtenti.contains(u) == true.
     * @post size() >= old_size().
     *
     * @param[in] u: L'oggetto Utente da registrare.
     *  * @throws IllegalArgumentException: Se l'utente inserito come parametro è nullo.
     *    @throws ListaUtentiPienaException: Se la listaUtenti è piena.
     *    @throws MatricolaNotValidException: Se l'utente ha un formato di matricola non valido.
     *    @throws UtentePresenteException: Se l'utente passato come parametro è già presente all'interno della lista degli utenti.
     *    @throws IOException Se si verifica un errore di input/output durante la scrittura sul file.
     */
    public void registrazioneUtente(Utente u)throws ListaUtentiPienaException, MatricolaNotValidException, UtentePresenteException, IOException {
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(u == null){
            throw new IllegalArgumentException("Errore: Impossibile aggiungere un utente nullo.");
        }
        
        // Controllo che listaUtenti==1000
        if(listaUtenti.size() == 1000){
            throw new ListaUtentiPienaException("La listaUtenti non può contenere più di 1000 utenti!");
        }
            
        
        // Controllo della matricola: la matricola deve avere 10 caratteri
        if (!u.getMatricola().matches("\\d{10}")){
                throw new MatricolaNotValidException ("La matricola deve esser composta da 10 cifre");
        }
        
        // Controllo dell'esistenza dell'utente: se esiste già la matricola nella lista l'utente non può essere inserito
        if(getUtenteByMatricola(u.getMatricola()) != null){
            // Esiste già un utente identico (stessa matricola)
            throw new UtentePresenteException("L'utente è già presente all'interno della lista.");
        }
        
        // Se vengono superati tutti i criteri, allora l'Utente u può essere aggiunto alla listaUtenti
        listaUtenti.add(u);
        System.out.println("Utente inserito con successo: " + u.getMatricola());
        
        // Chiamata al metodo statico salva, a cui passo l'oggetto listaUtente corrente e il nome del file
        SalvataggioFileUtente.salva(this, filename);

        System.out.println("Salvataggio su file completato.");
    }

    /**
     * @brief Rimuove un utente dal sistema.
     *
     * @pre u != null
     * @post L'utente specificato non è più presente nella lista.
     * @post Se l'utente passato come parametro non è presente all'interno della lista, 
     *       essa resta invariata.
     *
     * @param[in] u: L'oggetto da rimuovere (deve essere un'istanza di Utente).
     * @throws IllegalArgumentException: Se l'utente inserito come parametro è nullo.
     */
    public void eliminazioneUtente(Object u) throws UtenteNotFoundException, IOException {
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(u == null)
            throw new UtenteNotFoundException("Utente non trovato!");
        if(!listaUtenti.contains(u))
            throw new UtenteNotFoundException("Utente non presente nella lista");
        
        // Se l'oggetto passato non appartine alla classe Utente, allora non può essere rimosso.
        // Nota: Utilizzo "instanceof" e non "getClass()" perchè se in futuro si vorrà 
        // aggiungere una sottoclasse di Utente il metodo resta sempre valido anche per essa!
        if (!(u instanceof Utente))
            return;
        
        // Se l'utente passato come parametro fa parte della lista allora viene eliminato.
        listaUtenti.remove(u);
        SalvataggioFileUtente.salva(this, filename);
        
    }
    
    /**
     * Modifica i dati dell'utente specificato e salva lo stato corrente su file.
     * 
     * Questo metodo aggiorna i campi dell'oggetto {@code Utente} passato come parametro
     * (reimpostando i valori correnti) e successivamente invoca il metodo statico di salvataggio
     * per persistere le modifiche nel file specificato.
     * 
     * @pre (u != null) L'utente da modificare non può essere nullo.
     *
     * @param u L'oggetto {@code Utente} da modificare. Non deve essere null.
     * @param nomeFile Il nome (o percorso) del file su cui effettuare il salvataggio dei dati.
     * @throws IllegalArgumentException Se l'oggetto {@code Utente} passato è {@code null}.
     * @throws IOException Se si verifica un errore di input/output durante la scrittura sul file.
     * 
     */
    public void modificaUtente(Utente u, String nome, String cognome, String emailIstituzionale) throws IOException{
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(u==null)
            throw new IllegalArgumentException("L'utente da modificare non può essere nullo!");
        u.setCognome(cognome);
        u.setNome(nome);
        u.setEmailIstituzionale(emailIstituzionale);
        
        // Chiamata al metodo statico salva, a cui passo l'oggetto listaUtente corrente e il nome del file
        SalvataggioFileUtente.salva(this, filename);

        System.out.println("Salvataggio su file completato.");
    }
    
    /**
     * @brief Cerca utenti in base a una Stringa generica che rappresenta il Cognome o la Matricola.
     *
     * @pre u != null (La stringa di ricerca non deve essere nulla).
     * @post La lista restituita non è mai null (può essere vuota).
     *
     * @param[in] u: La stringa di ricerca (es. "Rossi").
     * @return ArrayList<Utente> contenente gli utenti che corrispondono ai criteri.
     * @throws IllegalArgumentException: Se l'utente inserito come parametro è nullo.
     */
    public ArrayList<Utente> cercaUtente(String u) {
    // Controllo non necessario (lo deve fare il client)
    // Inserito per motivi di sicurezza del programma
    if (u == null) {
        throw new IllegalArgumentException("Errore: La stringa di ricerca non può essere nulla.");
    }
    
    // Creo un ArrayList<Utente> per contenere la lista di utenti.
    ArrayList<Utente> listaRicerca = new ArrayList<>(); 
    
    // Per rendere la ricerca Case-Insensitive trasformo la stringa passata con tutte lettere minuscole.
    String utenteCercato = u.toLowerCase();
    
    for (Utente utente : listaUtenti) {
        // Recupero i valori e li converto in minuscolo per il confronto
        String cognomeLower = utente.getCognome().toLowerCase();
        String matricolaLower = utente.getMatricola().toLowerCase();

        // Controllo se il Cognome O la Matricola INIZIANO con la stringa cercata
        if (cognomeLower.startsWith(utenteCercato) || matricolaLower.startsWith(utenteCercato)) {
            // Se un utente corrisponde ai criteri allora viene aggiunto all'interno dell'ArrayList
            listaRicerca.add(utente);
        }            
    }
    
    // Viene ritornato l'ArrayList contenente tutti gli utenti che rispettano i requisiti.
    return listaRicerca;
}
    /**
     * @brief Restituisce l'elenco completo degli utenti.
     * 
     * Crea un nuovo ArrayList contenente tutti gli elementi presenti nel TreeSet.
     *
     * @post La lista è una "Shallow Copy": la collezione è nuova, ma gli oggetti contenuti sono riferimenti agli stessi del TreeSet.
     * @post La lista mantiene l'ordinamento del TreeSet.
     * @post La lista non è mai null (può essere vuota).
     *
     * @return Un ArrayList contenente tutti gli utenti iscritti.
     */
    public ArrayList<Utente> getListaUtenti() {
        // Creo un'ArrayList contenente gli stessi riferimenti del TreeSet listaUtenti e lo restituisco.
        // Nota: Poichè viene passato il TreeSet al costruttore dell'ArrayList, significa che inserirà ogni elemento in ordine.
        // Quindi la lista mantiene l'ordinamento del TreeSet.
        return new ArrayList<>(listaUtenti); 
    }

    /**
     * @brief Restituisce una rappresentazione testuale dell'oggetto ListaUtenti.
     *
     * @post Il risultato non è mai null (restituisce sempre una stringa, anche vuota).
     *
     * @return Una stringa contenente la descrizione completa della lista utenti.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(); 
        sb.append("Lista Utenti:");
        for (Utente u : listaUtenti){
            sb.append("\n*****\n");
            sb.append(u.toString());    
        }
        return sb.toString();
    }
    
}