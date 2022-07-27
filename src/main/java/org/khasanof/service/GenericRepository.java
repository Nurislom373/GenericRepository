package org.khasanof.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.khasanof.config.ConnectionConfig;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GenericRepository<T, ID> {
    private final Connection connection = ConnectionConfig.getConnection();
    protected Class<T> persistenceClass;
    private ObjectMapper objectMapper;

    public GenericRepository() {
        this.persistenceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.objectMapper = new ObjectMapper();
    }

    public T find(ID id) {
        try {
            if (tableExist(persistenceClass.getSimpleName().toLowerCase(Locale.ROOT))) {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from " + persistenceClass.getSimpleName() + " where id = " + id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Object o = get(resultSet);
                    System.out.println("o = " + o);
                    return new ObjectMapper().convertValue(o, persistenceClass);
                }
            } else {
                createTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> findAll() {
        try {
            if (tableExist(persistenceClass.getSimpleName().toLowerCase(Locale.ROOT))) {
                List<T> list = new ArrayList<>();
                PreparedStatement preparedStatement = connection.prepareStatement("select * from " + persistenceClass.getSimpleName());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    list.add(new ObjectMapper().convertValue(get(resultSet), persistenceClass));
                }
                return list;
            } else {
                createTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    private Object get(ResultSet resultSet) throws SQLException {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.getGenericType().getTypeName().contains("String")) {
                jsonObject.addProperty(field.getName(), resultSet.getString(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Integer")) {
                jsonObject.addProperty(field.getName(), resultSet.getInt(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Long")) {
                jsonObject.addProperty(field.getName(), resultSet.getLong(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Double")) {
                jsonObject.addProperty(field.getName(), resultSet.getDouble(field.getName()));
            }
        }
        return gson.fromJson(jsonObject.toString(), Object.class);
    }

    private Field[] getFields() {
        return persistenceClass.getDeclaredFields();
    }

    private void createTable() throws SQLException {
        Field[] fields = getFields();
        long fieldsCount = Arrays.stream(fields).count();
        int count = 0;
        StringBuilder query = new StringBuilder("create table " + persistenceClass.getSimpleName() + " ( ");
        for (Field field : fields) {
            count++;
            if (field.getName().equals("id")) {
                query.append("id serial primary key,");
            } else if ((fieldsCount - count) == 0) {
                query.append(" " + field.getName() + " " + getTypeSQL(field.getGenericType()) + " );");
            } else {
                query.append(" " + field.getName() + " " + getTypeSQL(field.getGenericType()) + ",");
            }
        }
        connection.createStatement().execute(query.toString());
    }

    private String getTypeSQL(Type genericType) {
        if (genericType.getTypeName().contains("Integer")) {
            return "integer";
        } else if (genericType.getTypeName().contains("Long")) {
            return "bigint";
        } else if (genericType.getTypeName().contains("Double")) {
            return "float";
        } else {
            return "varchar";
        }
    }


    private boolean tableExist(String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) "
                + "FROM information_schema.tables "
                + "WHERE table_name = ?"
                + "LIMIT 1;");
        preparedStatement.setString(1, tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) != 0;
    }
}
