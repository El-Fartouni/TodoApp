/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.seb.todo.entity;

/**
 *
 * @author DAMAR ALI El-Fartouni
 */
public class Category {
    private Integer id;
    private String name;

    //Constructeur vide
    public Category() {
    }

    //Getter et les Setter
    public Category(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
