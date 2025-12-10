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

import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.io.File;

// Assicurati di importare le tue eccezioni corrette
// import mio.package.eccezioni.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

// Assicurati che le eccezioni siano visibili (importa il package se necessario)
// import mio.progetto.eccezioni.*; 

public class ElencoPrestitiTest {

    // --- VARIABILI DELLA FIXTURE ---
    private ElencoPrestiti elenco;
    private GestoreStub gestoreStub;
    
    // Costanti
    private final String FILENAME_TEST = "test_elencoPrestiti.dat";
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
            super("stub_libri.dat", "stub_utenti.dat");
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
    }
   
    @BeforeEach
    public void setUp() {
        try {
            // 1. Creiamo lo stub
            gestoreStub = new GestoreStub();
            
            // 2. Creiamo l'elenco pulito (false = non caricare da file)
            elenco = new ElencoPrestiti(false, FILENAME_TEST, gestoreStub);
            
        } catch (Exception e) {
            fail("Setup fallito: " + e.getMessage());
        }
    }
    
    @AfterEach
    public void Pulizia() {
        // Rilasciamo i riferimenti
        elenco = null;
        gestoreStub = null;
        
        // Cancelliamo il file creato dai test per non lasciare spazzatura
        File f = new File(FILENAME_TEST);
        if (f.exists()) {
            f.delete();
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
            elenco.registrazionePrestito(ISBN_VALIDO, MATRICOLA_VALIDA);
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
        elenco.registrazionePrestito("1111111111111", "MAT01");
        elenco.registrazionePrestito("2222222222222", "MAT02");

        // Verifica ricerca per matricola
        ArrayList<Prestito> risultatiMatricola = elenco.cercaPrestito("MAT02");
        assertEquals(1, risultatiMatricola.size());
        assertEquals("2222222222222", risultatiMatricola.get(0).getISBNLibro());
        
        // Verifica ricerca per ISBN
        ArrayList<Prestito> risultatiISBN = elenco.cercaPrestito("2222222222222");
        assertEquals(1, risultatiISBN.size());
        assertEquals("MAT02", risultatiISBN.get(0).getISBNLibro());
        
        // Verifica tramite IDPrestito
        ArrayList<Prestito> risultatiID = elenco.cercaPrestito("2");
        assertEquals(1, risultatiID.size());
        assertEquals("MAT02", risultatiID.get(0).getISBNLibro());
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
}