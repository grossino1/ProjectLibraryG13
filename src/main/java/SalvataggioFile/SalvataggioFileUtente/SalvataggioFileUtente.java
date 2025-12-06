/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SalvataggioFile.SalvataggioFileUtente;

import GestioneUtente.ListaUtenti;

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
 * @author grossino1
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
     public void salva(ListaUtenti dati, String filename){
               
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
    public static ListaUtenti carica(String filename){
        return null;
    }
}
