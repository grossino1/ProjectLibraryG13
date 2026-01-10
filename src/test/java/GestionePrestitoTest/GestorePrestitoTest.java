package GestionePrestitoTest;

import Eccezioni.EccezioniLibri.CatalogoPienoException;
import Eccezioni.EccezioniLibri.ISBNNotValidException;
import Eccezioni.EccezioniLibri.LibroNotFoundException;
import Eccezioni.EccezioniLibri.LibroPresenteException;
import Eccezioni.EccezioniPrestiti.CopieEsauriteException;
import Eccezioni.EccezioniPrestiti.PrestitiEsauritiException;
import Eccezioni.EccezioniPrestiti.PrestitoGiaPresenteException;
import Eccezioni.EccezioniUtenti.ListaUtentiPienaException;
import Eccezioni.EccezioniUtenti.MatricolaNotValidException;
import Eccezioni.EccezioniUtenti.UtenteNotFoundException;
import Eccezioni.EccezioniUtenti.UtentePresenteException;
import GestioneLibro.CatalogoLibri;
import GestioneLibro.Libro;
import GestionePrestito.GestorePrestito;
import GestionePrestito.Prestito;
import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import SalvataggioFile.SalvataggioFileLibro.SalvataggioFileLibro;
import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class GestorePrestitoTest {

    private GestorePrestito gestore;
    
    // Nomi dei file temporanei per il test
    private final String FILE_LIBRI_TEST = "test_catalogo_gestore.dat";
    private final String FILE_UTENTI_TEST = "test_utenti_gestore.dat";

    // Dati di prova costanti
    private final String ISBN_DISPONIBILE = "9788800000001";
    private final String ISBN_ESAURITO = "9788800000002"; // Ha solo 1 copia disponibile
    private final String ISBN_INESISTENTE = "0000000000000";
    
    private final String MATR_OK = "1000000001";
    private final String MATR_PIENA = "1000000002"; // Ha già 3 prestiti
    private final String MATR_INESISTENTE = "9999999999";

    @BeforeEach
    public void setUp() throws IOException, ClassNotFoundException, ISBNNotValidException, LibroPresenteException, IllegalArgumentException, MatricolaNotValidException, PrestitiEsauritiException, ListaUtentiPienaException, LibroNotFoundException, CatalogoPienoException, UtentePresenteException {
        
        creaFileLibriTest();
        creaFileUtentiTest();
        // 2. Inizializziamo il gestore puntando ai file di test appena creati
        gestore = new GestorePrestito(FILE_LIBRI_TEST, FILE_UTENTI_TEST);
    }

    @AfterEach
    public void Pulizia() {
      
        gestore = null;
        
        new File(FILE_LIBRI_TEST).delete();
        new File(FILE_UTENTI_TEST).delete();
    }

    // --- TEST ---

    @Test
    public void testNuovoPrestito_Successo() throws Exception {
        // Caso ideale: Libro c'è, copie > 0, Utente c'è, prestiti < 3
        boolean risultato = gestore.nuovoPrestito(ISBN_DISPONIBILE, MATR_OK);
        assertTrue(risultato, "Il prestito dovrebbe essere autorizzato");
    }

    @Test
    public void testNuovoPrestito_Eccezioni() {
        
        assertThrows(LibroNotFoundException.class, () -> {
            gestore.nuovoPrestito(ISBN_INESISTENTE, MATR_OK);
        });

        assertThrows(UtenteNotFoundException.class, () -> {
            gestore.nuovoPrestito(ISBN_DISPONIBILE, MATR_INESISTENTE);
        });
  
        // Il libro esiste ma ha 0 copie
        assertThrows(CopieEsauriteException.class, () -> {
            gestore.nuovoPrestito(ISBN_ESAURITO, MATR_OK);
        });
   
        // L'utente esiste ma ha già 3 prestiti
        assertThrows(PrestitiEsauritiException.class, () -> {
            gestore.nuovoPrestito(ISBN_DISPONIBILE, MATR_PIENA);
        });
        
        // L'utente ha già quel libro in prestito
        assertThrows(PrestitoGiaPresenteException.class, () -> {
            gestore.nuovoPrestito(ISBN_DISPONIBILE, MATR_OK);
            gestore.aggiungiPrestitoListaUtente(MATR_OK, new Prestito(ISBN_DISPONIBILE, MATR_OK));
            gestore.nuovoPrestito(ISBN_DISPONIBILE, MATR_OK);
        });
    }
    
    /*
     * Poiché la classe GestorePrestito salva su file ma non ha getter 
     * per ispezionare lo stato interno del catalogo libri della lista utenti), per verificare 
     * se la modifica è avvenuta dobbiamo ricaricare i dati del catalogo e della lista dai file.
     * NON è uno Unit Test puro, ma un Integration Test, perché coinvolge il File System.
     */
    @Test
    public void testDiminuisciCopiaPrestitoLibro() throws Exception {
        
        gestore.diminuisciCopiaPrestitoLibro(ISBN_DISPONIBILE);

        // Verifica su file
        CatalogoLibri catAggiornato = SalvataggioFileLibro.carica(FILE_LIBRI_TEST);
        Libro l = catAggiornato.getLibroByISBN(ISBN_DISPONIBILE);
        
        // Inizialmente erano 5, ora dovrebbero essere 4
        assertEquals(4, l.getNumeroCopie());
        assertEquals(1, l.getNPrestitiAttivi());
    }

    @Test
    public void testAggiungiCopiaPrestitoLibro() throws Exception {
        
        // Incrementiamo una copia del libro esaurito (che era a 1)
        gestore.aggiungiCopiaPrestitoLibro(ISBN_ESAURITO);

        // Verifica su file 
        CatalogoLibri catAggiornato = SalvataggioFileLibro.carica(FILE_LIBRI_TEST);
        Libro l = catAggiornato.getLibroByISBN(ISBN_ESAURITO);
        
        assertEquals(2, l.getNumeroCopie());
        assertEquals(-1, l.getNPrestitiAttivi());
    }

    @Test
    public void testAggiungiPrestitoListaUtente() throws Exception {
        Prestito nuovoP = new Prestito(ISBN_DISPONIBILE, MATR_OK);
        
        // Aggiungiamo prestito all'utente (che ne aveva 0)
        gestore.aggiungiPrestitoListaUtente(MATR_OK, nuovoP);

        // Verifica su file
        ListaUtenti utentiAggiornati = SalvataggioFileUtente.carica(FILE_UTENTI_TEST);
        Utente u = utentiAggiornati.getUtenteByMatricola(MATR_OK);
        
        assertEquals(1, u.getListaPrestiti().size());
    }

    @Test
    public void testRimuoviPrestitoListaUtente() throws Exception {
        

        // L'utente MATR_PIENA ha 3 prestiti. Ne creiamo uno fittizio da rimuovere.
        // Recuperiamo un prestito dall'utente pieno
        ListaUtenti listaTemp = SalvataggioFileUtente.carica(FILE_UTENTI_TEST);
        Utente uTemp = listaTemp.getUtenteByMatricola(MATR_PIENA);
        Prestito pDaRimuovere = uTemp.getListaPrestiti().get(0);
        
        gestore.rimuoviPrestitoListaUtente(MATR_PIENA, pDaRimuovere);

        // Verifica su file
        ListaUtenti utentiAggiornati = SalvataggioFileUtente.carica(FILE_UTENTI_TEST);
        Utente uFinale = utentiAggiornati.getUtenteByMatricola(MATR_PIENA);
        
        // Aveva 3 prestiti, ora 2
        assertEquals(2, uFinale.getListaPrestiti().size());
    }


    // --- Classe stub ---
    // Simulano ciò che fanno le classi SalvataggioFile.

    private void creaFileLibriTest() throws IOException, ISBNNotValidException, LibroPresenteException, LibroNotFoundException, CatalogoPienoException, ClassNotFoundException {
        CatalogoLibri catalogo = new CatalogoLibri(false, FILE_LIBRI_TEST);
        
        // Libro disponibile (5 copie)
        Libro l1 = new Libro("Titolo1", "Autore1", 2020, ISBN_DISPONIBILE, 5);
        catalogo.registrazioneLibro(l1);
        
        // Libro esaurito (1 copie)
        Libro l2 = new Libro("Titolo2", "Autore2", 2021, ISBN_ESAURITO, 1);
        catalogo.registrazioneLibro(l2);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_LIBRI_TEST))) {
            out.writeObject(catalogo);
        }
    }

    private void creaFileUtentiTest() throws IOException, IllegalArgumentException, MatricolaNotValidException, PrestitiEsauritiException, ListaUtentiPienaException, UtentePresenteException, ClassNotFoundException {
        ListaUtenti listaUtenti = new ListaUtenti(false, FILE_UTENTI_TEST);
        
        // Utente OK (0 prestiti)
        Utente u1 = new Utente("Nome1", "Cognome1", MATR_OK, "Email@1");;
        listaUtenti.registrazioneUtente(u1);
        
        // Utente Pieno (3 prestiti finti)
        Utente u2 = new Utente("Nome2", "Cognome2", MATR_PIENA, "Email@2");
        // Aggiungiamo 3 prestiti fittizi per riempire la lista
        u2.addPrestito(new Prestito("1111111111111", MATR_PIENA));
        u2.addPrestito(new Prestito("2222222222222", MATR_PIENA));
        u2.addPrestito(new Prestito("3333333333333", MATR_PIENA));
        listaUtenti.registrazioneUtente(u2);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_UTENTI_TEST))) {
            out.writeObject(listaUtenti);
        }
    }
}