package org.khasanof.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.khasanof.enums.FieldsEnum;
import org.khasanof.enums.JavaFieldEnums;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class GenericUtils {

    @Deprecated
    public Object get(ResultSet resultSet, Field[] fields) throws SQLException {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        for (Field field : fields) {
            if (field.getGenericType().getTypeName().contains(JavaFieldEnums.STRING.getValue())) {
                jsonObject.addProperty(field.getName(), resultSet.getString(field.getName()));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.INTEGER.getValue()) || field.getGenericType().getTypeName().equals("int")) {
                jsonObject.addProperty(field.getName(), resultSet.getInt(field.getName()));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.LONG.getValue()) || field.getGenericType().getTypeName().equals("long")) {
                jsonObject.addProperty(field.getName(), resultSet.getLong(field.getName()));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.DOUBLE.getValue()) || field.getGenericType().getTypeName().equals("double")) {
                jsonObject.addProperty(field.getName(), resultSet.getDouble(field.getName()));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.BOOLEAN.getValue()) || field.getGenericType().getTypeName().equals("boolean")) {
                jsonObject.addProperty(field.getName(), resultSet.getBoolean(field.getName()));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.BIG_DECIMAL.getValue())) {
                jsonObject.addProperty(field.getName(), resultSet.getBigDecimal(field.getName()));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.SHORT.getValue()) || field.getGenericType().getTypeName().equals("short")) {
                jsonObject.addProperty(field.getName(), resultSet.getShort(field.getName()));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.DATE.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(resultSet.getDate(field.getName())));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.FLOAT.getValue()) || field.getGenericType().getTypeName().equals("float")) {
                jsonObject.addProperty(field.getName(), resultSet.getFloat(field.getName()));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.BYTE.getValue()) || field.getGenericType().getTypeName().equals("byte")) {
                jsonObject.addProperty(field.getName(), resultSet.getByte(field.getName()));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.TIMESTAMP.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(resultSet.getTimestamp(field.getName())));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.SHORT.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(resultSet.getShort(field.getName())));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.LOCAL_DATE.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(DateTimeFormatter.dateParseLocalDate(resultSet.getDate(field.getName()))));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.LOCAL_DATE_TIME.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(DateTimeFormatter.timestampParseLocalDateTime(resultSet.getTimestamp(field.getName()))));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.LOCAL_TIME.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(DateTimeFormatter.timeParseLocalTime(resultSet.getTime(field.getName()))));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.OFFSET_TIME.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(DateTimeFormatter.timeParseOffsetTime(resultSet.getTime(field.getName()))));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.UUID.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(UUID.fromString(resultSet.getString(field.getName()))));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.CHARACTER.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(resultSet.getString(field.getName())));
            } else if (field.getGenericType().getTypeName().contains(JavaFieldEnums.TIME.getValue())) {
                jsonObject.addProperty(field.getName(), String.valueOf(resultSet.getTime(field.getName())));
            }
        }
        return gson.fromJson(jsonObject.toString(), Object.class);
    }

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
