/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionePrestitoTest;

import Eccezioni.EccezioniLibri.LibroNotFoundException;
import Eccezioni.EccezioniPrestiti.CopieEsauriteException;
import Eccezioni.EccezioniPrestiti.ElencoPienoException;
import Eccezioni.EccezioniPrestiti.PrestitiEsauritiException;
import Eccezioni.EccezioniPrestiti.PrestitoNonTrovatoException;
import Eccezioni.EccezioniPrestiti.dataRestituzioneException;
import Eccezioni.EccezioniUtenti.UtenteNotFoundException;
import GestionePrestito.ElencoPrestiti;
import GestionePrestito.GestorePrestito;
import GestionePrestito.Prestito;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import GestioneLibro.CatalogoLibri;
import GestioneUtente.ListaUtenti;

// Assicurati che le eccezioni siano visibili (importa il package se necessario)
// import mio.progetto.eccezioni.*; 

public class ElencoPrestitiTest {

    // --- VARIABILI DELLA FIXTURE ---
    private ElencoPrestiti elenco;
    private GestoreStub gestoreStub;
    
    // Costanti
    private final String FILENAMEPRESTITI_TEST = "test_elencoPrestiti.dat";
    private final String FILENAMEUTENTI_TEST = "stub_utenti.dat";
    private final String FILENAMELIBRI_TEST = "stub_libri.dat";
    private final String ISBN_VALIDO = "9781234567897";
    private final String MATRICOLA_VALIDA = "1234567890";

    /*
     * La classe stub: 
     * Estende GestorePrestito e ci permette di controllare esattamente
     * cosa restituisce il metodo nuovoPrestito() durante i test.
    */
    class GestoreStub extends GestorePrestito {
        
        // Flag di controllo
        public boolean risultatoNuovoPrestito = true; 
        public boolean lanciaEccezioneLibro = false;  
        public boolean lanciaEccezioneUtente = false;
        public boolean lanciaEccezioneCopia = false;
        public boolean lanciaEccezionePrestito = false;
        
        public GestoreStub() throws IOException, ClassNotFoundException {
            // Passiamo file fittizi
            super(FILENAMELIBRI_TEST, FILENAMEUTENTI_TEST);
        }

        @Override
        public boolean nuovoPrestito(String isbn, String matricola) throws LibroNotFoundException, UtenteNotFoundException, CopieEsauriteException, PrestitiEsauritiException {
            if (lanciaEccezioneLibro) {
                throw new LibroNotFoundException();
            }
            if(lanciaEccezioneUtente) {
                throw new UtenteNotFoundException();
            }
            if(lanciaEccezioneCopia) {
                throw new CopieEsauriteException();
            }
            if(lanciaEccezionePrestito) {
                throw new PrestitiEsauritiException();
            }
            return risultatoNuovoPrestito;
        }
        @Override
        public void diminuisciCopiaPrestitoLibro(String isbn) {
            // Non fare nulla: stiamo simulando che sia andato tutto bene
        }

        @Override
        public void aggiungiPrestitoListaUtente(String matricola, Prestito p) {
            // Non fare nulla
        }

        @Override
        public void aggiungiCopiaPrestitoLibro(String isbn) {
            // Non fare nulla (serve per il test di eliminazione)
        }

        @Override
        public void rimuoviPrestitoListaUtente(String matricola, Prestito p) {
            // Non fare nulla (serve per il test di eliminazione)
        }
    }
   
    @BeforeEach
    public void setUp() {
        try {
            
            // 2. Creamo il file fittizio 
            File fp = new File(FILENAMEPRESTITI_TEST);
            fp.createNewFile(); 
            
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fp))) {
                out.writeObject(new ArrayList<>()); // Scrive una lista vuota nel file
            }
            
            File fl = new File(FILENAMELIBRI_TEST);
            fl.createNewFile(); 
            
            try (ObjectOutputStream outLibri = new ObjectOutputStream(new FileOutputStream(fl))) {
                outLibri.writeObject(new CatalogoLibri());
            }
            
            File fu = new File(FILENAMEUTENTI_TEST);
            fu.createNewFile(); 
            try (ObjectOutputStream outUtenti = new ObjectOutputStream(new FileOutputStream(fu))) {
                outUtenti.writeObject(new ListaUtenti()); 
            }
            
            // 3. Creiamo l'elenco pulito (false = non caricare da file)
            gestoreStub = new GestoreStub();
            elenco = new ElencoPrestiti(false, FILENAMEPRESTITI_TEST, gestoreStub);
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Setup fallito: " + e.getMessage());
        }
    }
    
    @AfterEach
    public void Pulizia() {
        // Rilasciamo i riferimenti
        elenco = null;
        gestoreStub = null;
        
        // Cancelliamo il file creato dai test per non lasciare spazzatura
        File fp = new File(FILENAMEPRESTITI_TEST);
        if (fp.exists()) {
            fp.delete();
        }
        
        File fl = new File(FILENAMELIBRI_TEST);
        if (fl.exists()) {
            fl.delete();
        }
        
        File fu = new File(FILENAMEUTENTI_TEST);
        if (fu.exists()) {
            fu.delete();
        }
    }
    
    @Test
    public void testCostruttoreNuovo() {
        assertNotNull(elenco.getElencoPrestiti());
        assertTrue(elenco.getElencoPrestiti().isEmpty());
    }

    @Test
    public void testRegistrazione_GestoreRestituisceTrue() throws Exception {
        // SCENARIO: Il gestore approva il prestito (ritorna true)
        gestoreStub.risultatoNuovoPrestito = true; 

        // Azione
        elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);

        // Verifica: L'elemento DEVE essere stato aggiunto
        assertEquals(1, elenco.getElencoPrestiti().size());
        
        // Verifica contenuto
        assertEquals(ISBN_VALIDO, elenco.getElencoPrestiti().get(0).getISBNLibro());
        assertEquals(MATRICOLA_VALIDA, elenco.getElencoPrestiti().get(0).getMatricolaUtente());
    }

    @Test
    public void testRegistrazione_EccezioneDalGestore() {
       
        assertThrows(LibroNotFoundException.class, () -> {
            gestoreStub.lanciaEccezioneLibro = true;
            elenco.registrazionePrestito("ISBN_INESISTENTE", MATRICOLA_VALIDA);
        });
        
        assertThrows(LibroNotFoundException.class, () -> {
            gestoreStub.lanciaEccezioneUtente = true;
            elenco.registrazionePrestito(ISBN_VALIDO, "Matricola non valida");
        });
        
        assertThrows(LibroNotFoundException.class, () -> {
            gestoreStub.lanciaEccezioneCopia = true;
            elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);
        });

        assertThrows(LibroNotFoundException.class, () -> {
            gestoreStub.lanciaEccezionePrestito = true;
            elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);
        });
        
        // Verifica Stato: La lista deve essere vuota
        assertTrue(elenco.getElencoPrestiti().isEmpty());
    }

    // Test del Boundary Value (> 99)
    @Test
    public void testRegistrazione_ElencoPieno() throws Exception {
         
        // Riempiamo l'elenco fino a 100 elementi (limite massimo consentito)
        // Assicuriamo che il gestore dica sempre TRUE
        gestoreStub.risultatoNuovoPrestito = true;
        
        for(int i = 0; i < 100; i++) {
            // Genera: "978-0000000000", "978-0000000001", etc.
            // %03d significa "numero con 3 cifre, riempiendo con zeri se necessario"
            String isbnFinto = String.format("9780000000%03d", i); 
            String matricolaFinta = String.format("051210%04d", i);
            elenco.registrazionePrestito(isbnFinto, matricolaFinta);
        }
        
        assertEquals(100, elenco.getElencoPrestiti().size());

        // Proviamo ad inserire il 101esimo elemento
        // Deve scattare l'eccezione ElencoPienoException
        assertThrows(ElencoPienoException.class, () -> {
            elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);
        });
    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void testCercaPrestitoSuccesso() throws Exception {
        
        // Aggiungiamo i libri
        gestoreStub.risultatoNuovoPrestito = true;
        elenco.registrazionePrestito("1111111111111", "0612708792");
        elenco.registrazionePrestito("2222222222222", "0612709999");

        // Verifica ricerca per matricola parziale
        ArrayList<Prestito> risultatiMatricola = elenco.cercaPrestito("061");
        assertEquals(2, risultatiMatricola.size());
        assertEquals("1111111111111", risultatiMatricola.get(0).getISBNLibro());
        assertEquals("2222222222222", risultatiMatricola.get(1).getISBNLibro());
        
        // Verifica ricerca per parziale
        ArrayList<Prestito> risultatiISBN = elenco.cercaPrestito("2222");
        assertEquals(1, risultatiISBN.size());
        assertEquals("0612709999", risultatiISBN.get(0).getMatricolaUtente());
    }
    
    @Test
    public void testCercaPrestitoEccezione() throws Exception {
        
        // Aggiungiamo i libri
        gestoreStub.risultatoNuovoPrestito = true;
        elenco.registrazionePrestito("1111111111111", "0612708792");
        elenco.registrazionePrestito("2222222222222", "0612709999");

        // Verifica ricerca per matricola parziale
        assertThrows(PrestitoNonTrovatoException.class, () -> {
            ArrayList<Prestito> risultatiMatricola = elenco.cercaPrestito("777");
        });
    }

    @Test
    public void testEliminazionePrestito() throws Exception {
      
        // Aggiungiamo i libri
        elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);
        Prestito p = elenco.getElencoPrestiti().get(0);

        // Azione
        elenco.eliminazionePrestito(p);

        // Verifica
        assertTrue(elenco.getElencoPrestiti().isEmpty());
    }

    @Test
    public void testEliminazionePrestitoNonTrovato() {
        
        // Ipotizzo sia il prestito passato da eliminare dall'elenco
        Prestito p = new Prestito("9999999999999", "0000000000");
        
        assertThrows(PrestitoNonTrovatoException.class, () -> {
            elenco.eliminazionePrestito(p);
        });
    }

    @Test
    public void testModificaPrestito_Successo() throws Exception {
        
        // Aggiungiamo i libri
        elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);
        Prestito p = elenco.getElencoPrestiti().get(0);
        
        LocalDate nuovaData = LocalDate.now().plusDays(10);
        elenco.modificaPrestito(p, nuovaData);
       
        assertEquals(nuovaData, p.getDataRestituzione());
    }
    
    @Test
    public void testModificaPrestito_Eccezione() throws dataRestituzioneException, Exception {
        
        // Aggiungiamo i libri
        elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);
        Prestito p = elenco.getElencoPrestiti().get(0);
        
        // La data di restituzione è > 30
        LocalDate nuovaData = LocalDate.now().plusDays(31);
        
        assertThrows(dataRestituzioneException.class, () -> {
            elenco.modificaPrestito(p, nuovaData);
        });     
    }
    
    @Test
    public void testGetElencoPrestiti() throws Exception {
        
        elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);

        ArrayList<Prestito> listaEsterna = elenco.getElencoPrestiti();

        // La lista non deve essere null e deve contenere l'elemento
        assertNotNull(listaEsterna);
        assertEquals(1, listaEsterna.size());

        // Se modifico la lista restituita, l'elenco originale non deve cambiare.
        listaEsterna.clear();
        assertTrue(listaEsterna.isEmpty());
        // La lista interna deve avere ancora il prestito
        assertFalse(elenco.getElencoPrestiti().isEmpty(), 
            "Il metodo getElencoPrestiti deve restituire una copia, non il riferimento diretto.");
    }

    @Test
    public void testToString() throws Exception {
      
        String outputVuoto = elenco.toString();
        
        assertNotNull(outputVuoto);
        assertTrue(outputVuoto.contains("Prestiti all'interno della lista"), 
            "Il toString deve contenere l'intestazione corretta anche se vuoto");

        // Aggiungo un libro
        gestoreStub.risultatoNuovoPrestito = true;
        elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);

        String outputPieno = elenco.toString();

        // Verifichiamo che la stringa contenga i dati del prestito inserito
        assertTrue(outputPieno.contains(ISBN_VALIDO), "Il toString deve stampare l'ISBN");
        assertTrue(outputPieno.contains(MATRICOLA_VALIDA), "Il toString deve stampare la Matricola");
    }
}