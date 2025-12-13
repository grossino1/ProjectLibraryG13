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
 * le istanze della classe @ref Utente su file binari.
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
     * @brief Salva lo stato di un oggetto Utente su un file binario.
     *
     * Serializza l'oggetto passato come parametro e lo scrive nel percorso specificato.
     * Se il file esiste già, viene sovrascritto.
     *
     * @pre dati != null (L'oggetto da salvare non può essere nullo).
     * @pre filename != null && !filename.isEmpty() (Il nome del file deve essere valido).
     * @post Viene creato o aggiornato un file contenente i dati degli utenti.
     *
     * @param[in] dati: L'oggetto ListaUtenti da serializzare.
     * @param[in] filename: Il percorso o nome del file di destinazione.
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
     * @brief Carica un oggetto Utente da un file binario.
     *
     * Tenta di leggere e deserializzare un oggetto Utente dal percorso specificato.
     *
     * @pre filename != null && !filename.isEmpty()
     * @post Restituisce un oggetto Utente valido se la lettura ha successo.
     * @post Restituisce null se il file non esiste o la deserializzazione fallisce.
     *
     * @param[in] filename: Il percorso del file da leggere.
     * @return L'istanza di ListaUtenti recuperata, oppure null in caso di errore.
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
