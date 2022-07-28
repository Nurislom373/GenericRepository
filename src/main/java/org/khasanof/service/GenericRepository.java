package org.khasanof.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.khasanof.config.ConnectionConfig;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GenericRepository<T, ID> {
    private final Connection connection = ConnectionConfig.getConnection();
    protected Class<T> persistenceClass;
    private final GenericUtils genericUtils;
    private final ObjectMapper objectMapper;

    public GenericRepository() {
        this.persistenceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.objectMapper = new ObjectMapper();
        this.genericUtils = new GenericUtils();
        checkTable();
    }

    public T getById(ID id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getByIdQuery(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return objectMapper.convertValue(genericUtils.get(resultSet, getFields()), persistenceClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<T> findById(ID id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(findByIdQuery(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return Optional.of(objectMapper.convertValue(genericUtils.get(resultSet, getFields()), persistenceClass));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<T> findAll() {
        try {
            List<T> list = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new ObjectMapper().convertValue(genericUtils.get(resultSet, getFields()), persistenceClass));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(T entity) {
        try {
            connection.createStatement().execute(insertQuery(entity));
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void save(List<T> tList) {
        try {
            for (T entity : tList) {
                connection.createStatement().execute(insertQuery(entity));
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void delete(T entity) {
        try {
            connection.createStatement().execute(deleteQuery(entity));
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(ID id) {
        try {
            connection.createStatement().execute(deleteByIdQuery(id));
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            connection.createStatement().execute(deleteAllQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll(List<T> tList) {
        try {
            for (T entity : tList) {
                connection.createStatement().execute(deleteQuery(entity));
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public long count() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(countQuery());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private Field[] getFields() {
        return persistenceClass.getDeclaredFields();
    }

    private String findAllQuery() {
        return "select * from " + persistenceClass.getSimpleName();
    }

    private String getByIdQuery(ID id) {
        return "select * from " + persistenceClass.getSimpleName() + " where id = " + id;
    }

    private String findByIdQuery(ID id) {
        return "select * from " + persistenceClass.getSimpleName() + " where id = " + id;
    }

    private String deleteByIdQuery(ID id) throws IllegalAccessException {
        StringBuilder query = new StringBuilder("delete from " + persistenceClass.getSimpleName() + " where id = ");
        if (BaseUtils.isNumber(id.getClass().getName())) {
            query.append(id);
        } else {
            query.append("'").append(id).append("'");
        }
        System.out.println(query);
        return String.valueOf(query);
    }

    private String deleteAllQuery() {
        return "truncate table " + persistenceClass.getSimpleName() + ";";
    }

    private String countQuery() {
        return "select count(*) from " + persistenceClass.getSimpleName() + ";";
    }

    private String deleteQuery(T entity) throws IllegalAccessException {
        StringBuilder query = new StringBuilder("delete from " + persistenceClass.getSimpleName() + " where ");
        Field[] fields = entity.getClass().getDeclaredFields();
        int count = 0;
        for (Field field : fields) {
            count++;
            if ((Arrays.stream(fields).count() - count) == 0) {
                if (BaseUtils.isNumber(field.getGenericType().getTypeName())) {
                    query.append(field.getName()).append(" = ").append(getValue(entity, field)).append(";");
                } else {
                    query.append(field.getName()).append(" = '").append(getValue(entity, field)).append("';");
                }
            } else {
                if (BaseUtils.isNumber(field.getGenericType().getTypeName())) {
                    query.append(field.getName()).append(" = ").append(getValue(entity, field)).append(" and ");
                } else {
                    query.append(field.getName()).append(" = '").append(getValue(entity, field)).append("' and ");
                }
            }
        }
        System.out.println(query);
        return String.valueOf(query);
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
                query.append(field.getName()).append(") values (");
            } else {
                query.append(field.getName()).append(", ");
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
                if (declaredField.getGenericType().getTypeName().contains("Integer") || declaredField.getGenericType().getTypeName().equals("int")) {
                    query.append(getValue(entity, declaredField)).append(");");
                } else {
                    query.append("'").append(getValue(entity, declaredField)).append("');");
                }
            } else {
                if (declaredField.getGenericType().getTypeName().contains("Integer")) {
                    query.append(getValue(entity, declaredField)).append(",");
                } else {
                    query.append("'").append(getValue(entity, declaredField)).append("',");
                }
            }
        }
        System.out.println(query);
        return query.toString();
    }

    private Object getValue(T entity, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(entity);
    }


    private void checkTable() {
        try {
            if (!tableExist(persistenceClass.getSimpleName().toLowerCase(Locale.ROOT))) {
                createTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                query.append(" ").append(field.getName()).append(" ").append(genericUtils.dataTypeConvertToSQl(field.getGenericType())).append(" );");
            } else {
                query.append(" ").append(field.getName()).append(" ").append(genericUtils.dataTypeConvertToSQl(field.getGenericType())).append(",");
            }
        }
        connection.createStatement().execute(query.toString());
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
