package Autenticazione;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainSetupCredenziali {

    // Nome del file dove verranno salvate le credenziali
    private static final String FILE_NAME = "us.bin";

    public static void main(String[] args) {
        
        // --- PARTE 1: Creazione e Salvataggio (Serializzazione) ---
        
        // 1. Definisci qui le credenziali che vuoi salvare
        String usernameAdmin = "Gruppo13";
        String passwordAdmin = "DrDoom13!";

        // 2. Istanzia l'oggetto Bibliotecario
        Bibliotecario admin = new Bibliotecario(usernameAdmin, passwordAdmin);

        System.out.println("--- Inizio procedura di salvataggio ---");

        // 3. Scrittura su file usando ObjectOutputStream
        // Il costrutto try(...) chiude automaticamente lo stream alla fine (try-with-resources)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            
            oos.writeObject(admin);
            System.out.println("✅ Credenziali salvate correttamente nel file: " + FILE_NAME);
            System.out.println("Dati salvati -> User: " + admin.getUsername() + ", Pass: " + admin.getPassword());

        } catch (IOException e) {
            System.err.println("❌ Errore durante il salvataggio del file: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n-------------------------------------\n");

        // --- PARTE 2: Verifica della Lettura (Deserializzazione) ---
        // Questa parte serve solo per verificare che il file funzioni.
        
        System.out.println("--- Verifica lettura file ---");

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            
            // Leggiamo l'oggetto e facciamo il cast a Bibliotecario
            Bibliotecario adminLetto = (Bibliotecario) ois.readObject();
            
            System.out.println("✅ Oggetto letto correttamente dal file.");
            System.out.println("Username recuperato: " + adminLetto.getUsername());
            
            // Verifica banale per vedere se è tutto ok
            if (adminLetto.getUsername().equals(usernameAdmin)) {
                System.out.println("Test integrità: SUPERATO");
            } else {
                System.out.println("Test integrità: FALLITO");
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Errore durante la lettura del file: " + e.getMessage());
        }
    }
}