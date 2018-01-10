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
public class Task {
    
    private String taskName;
    
    private Boolean done;
    
    private Integer id;
    
    private Category category;
    
    private Integer categoryId;

    //Constructeur vide
    public Task() {
    }

    //Constructeur plein
    public Task(String taskName, Boolean done, Category category) {
        this.taskName = taskName;
        this.done = done;
        this.category = category;
    }
     
    //Getter et les Setter
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    
    
    
}
