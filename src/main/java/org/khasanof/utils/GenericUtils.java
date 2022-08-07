package org.khasanof.utils;

import org.khasanof.enums.FieldsEnum;
import org.khasanof.enums.JavaFieldEnums;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GenericUtils {

    public Object get(ResultSet resultSet, Object o) throws SQLException, IllegalAccessException {
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            String typeName = declaredField.getGenericType().getTypeName();
            String name = declaredField.getName();
            declaredField.set(o, get(resultSet, typeName, name));
        }
        return o;
    }

    private Object get(ResultSet resultSet, String fieldType, String fieldName) throws SQLException {
        if (fieldType.contains(JavaFieldEnums.STRING.getValue())) {
            return resultSet.getString(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.INTEGER.getValue()) || fieldType.equals("int")) {
            return resultSet.getInt(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.LONG.getValue()) || fieldType.equals("long")) {
            return resultSet.getLong(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.DOUBLE.getValue()) || fieldType.equals("double")) {
            return resultSet.getDouble(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.BOOLEAN.getValue()) || fieldType.equals("boolean")) {
            return resultSet.getBoolean(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.BIG_DECIMAL.getValue())) {
            return resultSet.getBigDecimal(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.SHORT.getValue()) || fieldType.equals("short")) {
            return resultSet.getShort(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.DATE.getValue()) && fieldType.substring(10).equals("Date")) {
            return resultSet.getDate(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.FLOAT.getValue()) || fieldType.equals("float")) {
            return resultSet.getFloat(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.BYTE.getValue()) || fieldType.equals("byte")) {
            return resultSet.getByte(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.TIMESTAMP.getValue())) {
            return resultSet.getTime(fieldName);
        } else if (fieldType.contains(JavaFieldEnums.LOCAL_DATE.getValue()) && (fieldType.substring(10).equals("LocalDate"))) {
            return DateTimeFormatter.dateParseLocalDate(resultSet.getDate(fieldName));
        } else if (fieldType.contains(JavaFieldEnums.LOCAL_DATE_TIME.getValue())) {
            return DateTimeFormatter.timestampParseLocalDateTime(resultSet.getTimestamp(fieldName));
        } else if (fieldType.contains(JavaFieldEnums.LOCAL_TIME.getValue())) {
            return DateTimeFormatter.timeParseLocalTime(resultSet.getTime(fieldName));
        } else if (fieldType.contains(JavaFieldEnums.OFFSET_TIME.getValue())) {
            return DateTimeFormatter.timeParseOffsetTime(resultSet.getTime(fieldName));
        } else if (fieldType.contains(JavaFieldEnums.UUID.getValue())) {
            return UUID.fromString(resultSet.getString(fieldName));
        } else if (fieldType.contains(JavaFieldEnums.TIME.getValue()) && fieldType.substring(9).equals("Time")) {
            return resultSet.getTime(fieldName);
        }
        return resultSet.getObject(fieldName);
    }

    public String dataTypeConvertToSQl(Type genericType) {
        if (genericType.getTypeName().contains(JavaFieldEnums.STRING.getValue())) {
            return FieldsEnum.VARCHAR.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.INTEGER.getValue()) || genericType.getTypeName().equals("int")) {
            return FieldsEnum.INT4.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.LONG.getValue()) || genericType.getTypeName().equals("long")) {
            return FieldsEnum.INT8.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.DOUBLE.getValue()) || genericType.getTypeName().equals("double")) {
            return FieldsEnum.FLOAT8.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.BOOLEAN.getValue()) || genericType.getTypeName().equals("boolean")) {
            return FieldsEnum.BOOLEAN.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.BIG_DECIMAL.getValue())) {
            return FieldsEnum.NUMERIC.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.SHORT.getValue()) || genericType.getTypeName().equals("short")) {
            return FieldsEnum.INT2.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.DATE.getValue()) && genericType.getTypeName().substring(10).equals("Date")) {
            return FieldsEnum.DATE.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.FLOAT.getValue()) || genericType.getTypeName().equals("float")) {
            return FieldsEnum.FLOAT4.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.TIMESTAMP.getValue())) {
            return FieldsEnum.TIMESTAMP.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.UUID.getValue())) {
            return FieldsEnum.UUID.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.LOCAL_DATE.getValue()) && (genericType.getTypeName().substring(10).equals("LocalDate"))) {
            return FieldsEnum.DATE.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.TIMESTAMP.getValue())) {
            return FieldsEnum.TIMESTAMP.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.LOCAL_TIME.getValue())) {
            return FieldsEnum.TIMETZ.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.TIME.getValue()) && genericType.getTypeName().substring(10).equals("Time")) {
            return FieldsEnum.TIME.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.OFFSET_TIME.getValue())) {
            return FieldsEnum.TIMETZ.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.LOCAL_DATE_TIME.getValue())) {
            return FieldsEnum.TIMESTAMP.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.BYTEA.getValue())) {
            return FieldsEnum.BYTEA.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.INTERVAL.getValue())) {
            return FieldsEnum.INTERVAL.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.CIRCLE.getValue())) {
            return FieldsEnum.CIRCLE.getPostgres();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.POINT.getValue())) {
            return FieldsEnum.POINT.getPostgres();
        } else {
            return FieldsEnum.UNKNOWN.getPostgres();
        }
    }


}
