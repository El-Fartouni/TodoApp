/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.seb.todo;

import fr.seb.todo.frame.TodoFrame;

/**
 *
 * @author seb
 */
public class TodoApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TodoFrame frame = new TodoFrame();
        
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    
}
