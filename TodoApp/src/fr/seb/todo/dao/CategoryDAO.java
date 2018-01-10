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
 * @category seb
 */
public class CategoryDAO implements DAOInterface<Category, CategoryDAO> {

    private Connection dbConnection;

    private ResultSet rs;

    private PreparedStatement pStatement;

    public CategoryDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public CategoryDAO findOneById(int id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setInt(1, id);

        this.rs = this.pStatement.executeQuery();

        return this;
    }

    @Override
    public CategoryDAO findAll() throws SQLException {
        String sql = "SELECT * FROM categories";
        Statement stm = this.dbConnection.createStatement();
        this.rs = stm.executeQuery(sql);
        return this;
    }

    @Override
    public Map getOneAsArray() throws SQLException {
        Map<String, String> result = new HashMap<>();

        if (this.rs.next()) {
            result.put("id", this.rs.getString("id"));
            result.put("category_name", this.rs.getString("category_name"));
        }

        return result;
    }

    @Override
    public Category getOne() throws SQLException {
        Category category = new Category();

        if (this.rs.next()) {
            category.setId(this.rs.getInt("id"));
            category.setName(this.rs.getString("category_name"));
        }

        return category;
    }

    public boolean isResultSetEmpty() throws SQLException {
        return (!this.rs.isBeforeFirst() && this.rs.getRow() == 0);
    }

    @Override
    public List<Category> getResults() throws SQLException {
        List<Category> categoryList = new ArrayList<>();

        while (! isResultSetEmpty()) {
            categoryList.add(this.getOne());
        }

        return categoryList;
    }

    @Override
    public List<Map<String, Object>> getResultsAsArray() throws SQLException {
        List<Map<String, Object>> categoryList = new ArrayList<>();
        Map<String, Object> category;

        while (! isResultSetEmpty()) {
            category = this.getOneAsArray();
            categoryList.add(category);
        }

        return categoryList;
    }

    public Map<String, Integer> getResultsAsMap() throws SQLException {
        Map<String, Integer> categories = new HashMap<>();

        while (this.rs.next()) {
            categories.put(this.rs.getString("category_name"), this.rs.getInt("id"));
        }

        return categories;
    }

    @Override
    public int deleteOneById(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setInt(1, id);

        return this.pStatement.executeUpdate();
    }

    public int save(Category category) throws SQLException {
        int affectedRows;
        int id = category.getId();
        if (id <= 0) {
            affectedRows = this.update(category);
        } else {
            affectedRows = this.insert(category);
        }
        return affectedRows;
    }

    private int insert(Category category) throws SQLException {
        String sql = "INSERT INTO categories (category_name) VALUES (?)";
        this.pStatement = this.dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        this.pStatement.setString(1, category.getName());

        ResultSet insertRs = this.pStatement.getGeneratedKeys();

        if (insertRs.next()) {
            category.setId(insertRs.getInt("id"));
        }

        return this.pStatement.executeUpdate();

    }

    private int update(Category category) throws SQLException {
        String sql = "UPDATE categories SET category_name = ? WHERE id= ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setString(1, category.getName());
        this.pStatement.setInt(2, category.getId());

        return this.pStatement.executeUpdate();
    }

}
