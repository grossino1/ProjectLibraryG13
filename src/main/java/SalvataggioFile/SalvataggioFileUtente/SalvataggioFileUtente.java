/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SalvataggioFile.SalvataggioFileUtente;

import GestioneUtente.ListaUtenti;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 *
 * @author chiara
 */

/**
 * @class SalvataggioFileUtente
 * @brief Gestisce la persistenza su file degli oggetti di tipo Utente.
 *
 * Questa classe di utilità si occupa di serializzare (salvare) e deserializzare (caricare)
 * le istanze della classe ListaUtenti su file binari.
 *
 * @see GestioneUtente.Utente
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 *
 * @author chiara
 * @version 1.0
 */

public class SalvataggioFileUtente {
    
    /**
    * @brief Salva lo stato di un oggetto ListaUtenti su un file binario.
    *
    * Serializza l'oggetto passato come parametro e lo scrive nel percorso specificato.
    * Se il file esiste già, viene sovrascritto.
    *
    * @pre dati != null
    * @pre filename != null
    * @post Viene creato o aggiornato un file sul disco contenente la serializzazione di dati.
    *
    * @param[in] dati L'oggetto ListaUtenti da serializzare.
    * @param[in] filename Il percorso o nome del file di destinazione.
    * 
    * @throws IOException Se si verifica un errore durante la scrittura su disco (es. permessi negati).
    * @throws IllegalArgumentException Se dati o filename sono nulli.
    */
    public static void salva(ListaUtenti dati, String filename) throws IOException{
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(dati == null)
            throw new IOException("Non puoi salvare un oggetto vuoto!");
        if(filename == null)
            throw new IOException("Percorso non specificato!");
        
        // APERTURA STREAM (Try-with-resources):
        // - FileOutputStream: Apre il file in scrittura.
        // - BufferedOutputStream: Migliora le performance riducendo gli accessi al disco.
        // - ObjectOutputStream: Traduce l'oggetto in byte (Serializzazione).
        // Viene assicurata la chiusura automatica del file alla fine.
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))){
            // SCRITTURA: Scrive fisicamente l'oggetto nel file.
            out.writeObject(dati);
        }catch(IOException ex){
            // GESTIONE ERRORI
            throw new IOException(ex.getMessage());
        }
    }
    
    /**
    * @brief Carica un oggetto ListaUtenti da un file binario.
    *
    * Tenta di leggere e deserializzare un oggetto ListaUtenti dal percorso specificato.
    *
    * @pre filename != null
    * @post Restituisce un'istanza valida di ListaUtenti popolata con i dati del file.
    *
    * @param[in] filename Il percorso del file da leggere.
    * @return L'istanza di ListaUtenti recuperata.
    * 
    * @throws IOException Se il file non esiste o ci sono errori di lettura.
    * @throws ClassNotFoundException Se la struttura dell'oggetto nel file non corrisponde alla classe ListaUtenti.
    * @throws IllegalArgumentException Se filename è nullo.
    */
    public static ListaUtenti carica(String filename)  throws IOException, ClassNotFoundException{
        // Controllo non necessario (lo deve fare il client)
        // Inserito per motivi di sicurezza del programma
        if(filename == null)
            throw new IOException("Percorso non specificato!");
        
        File file = new File(filename);
        if (!file.exists())
            throw new IOException("Percorso non esistente!");
        
        // APERTURA STREAM (Try-with-resources)
        // - FileInputStream: Apre il file in lettura.
        // - BufferedInputStream: Ottimizza la lettura usando un buffer in memoria.
        // - ObjectInputStream: Ricostruisce l'oggetto dai byte letti.
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))){
            // DESERIALIZZAZIONE E CASTING
            ListaUtenti datiLetti = (ListaUtenti) in.readObject();
            return datiLetti;
        }catch(IOException ex){
            throw new IOException(ex.getMessage());
        }catch(ClassNotFoundException ex){
            throw new ClassNotFoundException(ex.getMessage());
        }
    }
}
