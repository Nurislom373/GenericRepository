package org.khasanof.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.khasanof.config.ConnectionConfig;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GenericRepository<T, ID> {
    private final Connection connection = ConnectionConfig.getConnection();
    protected Class<T> persistenceClass;
    private final GenericUtils genericUtils;
    private final ObjectMapper objectMapper;

    public GenericRepository() {
        this.persistenceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.objectMapper = new ObjectMapper();
        this.genericUtils = new GenericUtils();
    }

    public T find(ID id) {
        try {
            if (tableExist(persistenceClass.getSimpleName().toLowerCase(Locale.ROOT))) {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from " + persistenceClass.getSimpleName() + " where id = " + id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Object o = genericUtils.get(resultSet, getFields());
                    System.out.println("o = " + o);
                    return objectMapper.convertValue(o, persistenceClass);
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
                    list.add(new ObjectMapper().convertValue(genericUtils.get(resultSet, getFields()), persistenceClass));
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

    public T save(T entity) {
        try {
            if (tableExist(persistenceClass.getSimpleName().toLowerCase(Locale.ROOT))) {
                connection.createStatement().execute(insertQuery(entity));
            } else {
                createTable();
                connection.createStatement().execute(insertQuery(entity));
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Field[] getFields() {
        return persistenceClass.getDeclaredFields();
    }


    private String insertQuery(T entity) throws IllegalAccessException {
        StringBuilder query = new StringBuilder("insert into " + persistenceClass.getSimpleName() + " (");
        Field[] fields = getFields();
        int count = 0;
        for (Field field : fields) {
            count++;
            if (field.getName().equals("id")) {
                continue;
            }
            if ((Arrays.stream(fields).count() - count) == 0) {
                query.append(field.getName() + ") values (");
            } else {
                query.append(field.getName() + ", ");
            }
        }
        int secondCount = 0;
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            secondCount++;
            if (declaredField.getName().equals("id")) {
                continue;
            }
            if ((Arrays.stream(declaredFields).count() - secondCount) == 0) {
                if (declaredField.getGenericType().getTypeName().contains("Integer")) {
                    query.append(getValue(entity, declaredField) + ");");
                } else {
                    query.append("'" + getValue(entity, declaredField) + "');");
                }
            } else {
                if (declaredField.getGenericType().getTypeName().contains("Integer")) {
                    query.append(getValue(entity, declaredField) + ",");
                } else {
                    query.append("'" + getValue(entity, declaredField) + "',");
                }
            }
        }
        System.out.println(query);
        return query.toString();
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
                query.append(" " + field.getName() + " " + genericUtils.dataTypeConvertToSQl(field.getGenericType()) + " );");
            } else {
                query.append(" " + field.getName() + " " + genericUtils.dataTypeConvertToSQl(field.getGenericType()) + ",");
            }
        }
        connection.createStatement().execute(query.toString());
    }


    private Object getValue(T entity, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(entity);
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
