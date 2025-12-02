/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionePrestito;
import java.time.LocalDate;

/**
 *
 * @author chiara
 */

public class Prestito implements Comparable<Prestito> {

    private String IDPrestito;
    private String ISBNLibro;
    private String matricolaUtente;
    private LocalDate dataRestituzione;

    public Prestito(String IDPrestito, String ISBNLibro, String matricolaUtente,
                    LocalDate dataRestituzione) {
        // corpo vuoto (scheletro)
    }

    // Getter
    public String getIDPrestito() { return null; }
    public String getISBNLibro() { return null; }
    public String getMatricolaUtente() { return null; }
    public LocalDate getDataRestituzione() { return null; }

    // Setter
    public void setIDPrestito(String IDPrestito) {}
    public void setISBNLibro(String ISBNLibro) {}
    public void setMatricolaUtente(String matricolaUtente) {}
    public void setDataRestituzione(LocalDate dataRestituzione) {}

    // hashCode (basato su IDPrestito)
    @Override
    public int hashCode() { return 0; }

    // equals (basato su IDPrestito)
    @Override
    public boolean equals(Object obj) { return false; }

    // compareTo (ordinamento per dataRestituzione)
    @Override
    public int compareTo(Prestito other) { return 0; }

    @Override
    public String toString() { return ""; }
}
