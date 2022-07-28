package org.khasanof.utils;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;

public class QueryUtils {

    private final GenericUtils genericUtils;

    public QueryUtils() {
        this.genericUtils = new GenericUtils();
    }

    public String findAllQuery(String className) {
        return "select * from " + className;
    }

    public <ID> String getByIdQuery(ID id, String className) {
        return "select * from " + className + " where id = " + id;
    }

    public <ID> String findByIdQuery(ID id, String className) {
        return "select * from " + className + " where id = " + id;
    }

    public String deleteAllQuery(String className) {
        return "truncate table " + className + ";";
    }

    public String countQuery(String className) {
        return "select count(*) from " + className + ";";
    }

    public <ID> String deleteByIdQuery(ID id, String className) {
        StringBuilder query = new StringBuilder("delete from " + className + " where id = ");
        if (BaseUtils.isNumber(id.getClass().getName())) {
            query.append(id);
        } else {
            query.append("'").append(id).append("'");
        }
        return String.valueOf(query);
    }

    public  <T> String deleteQuery(T entity, String className) throws IllegalAccessException {
        StringBuilder query = new StringBuilder("delete from " + className + " where ");
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
        return String.valueOf(query);
    }

    public  <T> String insertQuery(T entity, String className) throws IllegalAccessException {
        StringBuilder query = new StringBuilder("insert into " + className + " (");
        Field[] fields = entity.getClass().getDeclaredFields();
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
        return query.toString();
    }

    public String createTableQuery(Field[] fields, String className) throws SQLException {
        long fieldsCount = Arrays.stream(fields).count();
        int count = 0;
        StringBuilder query = new StringBuilder("create table " + className + " ( ");
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
        return String.valueOf(query);
    }

    private <T> Object getValue(T entity, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(entity);
    }

}
