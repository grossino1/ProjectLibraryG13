package SalvataggioFile.SalvataggioFileAutenticazione;

import Autenticazione.Bibliotecario;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @class SalvataggioFileBibliotecario
 * @brief Gestisce la persistenza su file degli oggetti di tipo Bibliotecario.
 *
 * Questa classe si occupa di serializzare (salvare) e deserializzare (caricare)
 * le istanze della classe @ref Bibliotecario su file binari.
 *
 * @see Autenticazione.Bibliotecario
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 *
 * @author grossino1
 * @version 1.0
 */

public class SalvataggioFileBibliotecario {
    
    /**
     * @brief Carica un oggetto Bibliotecario da un file binario.
     *
     * Tenta di leggere e deserializzare un oggetto Bibliotecario dal percorso specificato.
     *
     * @pre filename != null && !filename.isEmpty()
     * @post Restituisce un oggetto Bibliotecario valido se la lettura ha successo.
     * @post Restituisce null se il file non esiste o la deserializzazione fallisce.
     *
     * @return L'istanza di Bibliotecario recuperata, oppure null in caso di errore.
     */
    
    public static Bibliotecario carica(String filename) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Bibliotecario) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Errore: File credenziali " + filename + " non trovato.");
        } catch (ClassNotFoundException e) {
            System.err.println("Errore durante la lettura del file credenziali: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * @brief Salva lo stato di un oggetto Bibliotecario su un file binario.
     *
     * Serializza l'oggetto passato come parametro e lo scrive nel percorso specificato.
     * Se il file esiste già, viene sovrascritto.
     *
     * @pre dati != null (L'oggetto da salvare non può essere nullo).
     * @post Viene creato o aggiornato un file contenente i dati dei libri.
     *
     * @param[in] dati: L'oggetto Bibliotecario da serializzare.
     */
    public static void salva(Bibliotecario dati, String filename) throws IOException {
    if (dati == null) {
        throw new IOException("Non puoi salvare un oggetto null!");
    }
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
        oos.writeObject(dati);
        System.out.println("Credenziali salvate correttamente in " + filename);
    }
}

}
