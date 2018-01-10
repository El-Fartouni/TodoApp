/*
 * The MIT License
 *
 * Copyright 2017 seb.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.seb.todo.dao;

import fr.seb.todo.entity.Category;
import fr.seb.todo.entity.Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @task DAMAR ALI El-Fartouni
 */
public class TaskDAO implements DAOInterface<Task, TaskDAO> {

    private Connection dbConnection;

    private ResultSet rs;

    private PreparedStatement pStatement;

    public TaskDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Récupération d'une entité en fonction de sa clef primaire
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public TaskDAO findOneById(int id) throws SQLException {
        String sql = "SELECT t.*, c.category_name FROM tasks as t INNER JOIN categories as c ON t.category_id=c.id WHERE id = ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setInt(1, id);

        this.pStatement.executeQuery();

        return this;
    }

    /**
     * Exécute une requête sélectionnant l'ensemble des lignes de la table
     *
     * @return
     * @throws SQLException
     */
    @Override
    public TaskDAO findAll() throws SQLException {
        String sql = "SELECT t.*, c.category_name FROM tasks as t INNER JOIN categories as c ON t.category_id=c.id";
        Statement stm = this.dbConnection.createStatement();
        this.rs = stm.executeQuery(sql);
        return this;
    }

    @Override
    public Map getOneAsArray() throws SQLException {
        Map<String, String> result = new HashMap<>();

        if (this.rs.next()) {
            result.put("id", this.rs.getString("id"));
            result.put("task_name", this.rs.getString("task_name"));
            result.put("category_id", this.rs.getString("category_id"));
            result.put("category_name", this.rs.getString("category_name"));
            result.put("done", this.rs.getString("done"));
        }

        return result;
    }

    /**
     * Récupération des résultats d'une requête sous la forme d'une entité
     *
     * @return
     * @throws SQLException
     */
    public Task getOne() throws SQLException {
        Task task = new Task();

        if (rs.next()) {
            Integer categoryId = this.rs.getInt("category_id");
            task.setId(this.rs.getInt("id"));
            task.setTaskName(this.rs.getString("task_name"));
            task.setDone(this.rs.getBoolean("done"));
            task.setCategoryId(this.rs.getInt("category_id"));

            if (categoryId != null) {
                CategoryDAO catDAO = new CategoryDAO(dbConnection);
                Category category = catDAO.findOneById(categoryId).getOne();
                task.setCategory(category);
            }
        }

        return task;
    }

    public boolean isResultSetEmpty() throws SQLException {
        return (!this.rs.isBeforeFirst() && this.rs.getRow() == 0);
    }

    @Override
    public List<Task> getResults() throws SQLException {
        List<Task> taskList = new ArrayList<>();

        if (!isResultSetEmpty()) {

            while (!rs.isLast()) {
                taskList.add(this.getOne());
            }
        }

        return taskList;
    }

    @Override
    public List<Map<String, Object>> getResultsAsArray() throws SQLException {
        List<Map<String, Object>> taskList = new ArrayList<>();
        Map<String, Object> task;

        while (!isResultSetEmpty() && !rs.isAfterLast()) {
            task = this.getOneAsArray();
            taskList.add(task);
        }

        return taskList;
    }

    /**
     * Suppression d'une entité en fonction de son identifiant
     *
     * @param entity
     */
    @Override
    public int deleteOneById(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setInt(1, id);

        return this.pStatement.executeUpdate();
    }

    @Override
    public int save(Task task) throws SQLException {
        int affectedRows;
        Integer id = task.getId();
        if (id != null) {
            //insertion
            affectedRows = this.update(task);
        } else {
            //mise à jour
            affectedRows = this.insert(task);
        }
        return affectedRows;
    }

    /**
     * Insertion dans la base de données
     *
     * @param entity
     * @return
     */
    private int insert(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (task_name, done, category_id) VALUES (?,?,?)";
        this.pStatement = this.dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        this.pStatement.setString(1, task.getTaskName());
        this.pStatement.setBoolean(2, task.getDone());
        this.pStatement.setInt(3, task.getCategoryId());

        // Récupération de la clef auto incrémentée
        ResultSet insertRs = this.pStatement.getGeneratedKeys();

        if (insertRs.next()) {
            task.setId(insertRs.getInt("id"));
        }

        return this.pStatement.executeUpdate();

    }

    /**
     * Insertion dans la base de données
     *
     * @param entity
     * @return
     */
    private int update(Task task) throws SQLException {
        String sql = "UPDATE tasks SET task_name = ?, done= ?, category_id=? WHERE id= ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setString(1, task.getTaskName());
        this.pStatement.setBoolean(2, task.getDone());
        this.pStatement.setInt(3, task.getCategoryId());
        this.pStatement.setInt(4, task.getId());

        return this.pStatement.executeUpdate();
    }

}
