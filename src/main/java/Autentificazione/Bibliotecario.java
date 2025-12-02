/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Autentificazione;

/**
 *
 * @author chiara
 */
public class Bibliotecario {
    private final String username;
    private final String password;
    
    public Bibliotecario(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public boolean login(String user, String pass){
        return true;
    }
}
