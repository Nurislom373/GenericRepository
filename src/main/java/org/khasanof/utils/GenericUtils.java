package org.khasanof.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.khasanof.enums.JavaFieldEnums;
import org.khasanof.enums.SqlFieldEnums;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenericUtils {

    public Object get(ResultSet resultSet, Field[] fields) throws SQLException {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        for (Field field : fields) {
            if (field.getGenericType().getTypeName().contains("String")) {
                jsonObject.addProperty(field.getName(), resultSet.getString(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Integer") || field.getGenericType().getTypeName().equals("int")) {
                jsonObject.addProperty(field.getName(), resultSet.getInt(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Long") || field.getGenericType().getTypeName().equals("long")) {
                jsonObject.addProperty(field.getName(), resultSet.getLong(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Double") || field.getGenericType().getTypeName().equals("double")) {
                jsonObject.addProperty(field.getName(), resultSet.getDouble(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Boolean") || field.getGenericType().getTypeName().equals("boolean")) {
                jsonObject.addProperty(field.getName(), resultSet.getBoolean(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("BigDecimal")) {
                jsonObject.addProperty(field.getName(), resultSet.getBigDecimal(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Short") || field.getGenericType().getTypeName().equals("short")) {
                jsonObject.addProperty(field.getName(), resultSet.getShort(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Date")) {
                jsonObject.addProperty(field.getName(), String.valueOf(resultSet.getDate(field.getName())));
            } else if (field.getGenericType().getTypeName().contains("Float") || field.getGenericType().getTypeName().equals("float")) {
                jsonObject.addProperty(field.getName(), resultSet.getFloat(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Byte") || field.getGenericType().getTypeName().equals("byte")) {
                jsonObject.addProperty(field.getName(), resultSet.getByte(field.getName()));
            } else if (field.getGenericType().getTypeName().contains("Timestamp")) {
                jsonObject.addProperty(field.getName(), String.valueOf(resultSet.getTimestamp(field.getName())));
            } else if (field.getGenericType().getTypeName().contains("Time")) {
                jsonObject.addProperty(field.getName(), String.valueOf(resultSet.getTime(field.getName())));
            }
        }
        return gson.fromJson(jsonObject.toString(), Object.class);
    }

    public String dataTypeConvertToSQl(Type genericType) {
        if (genericType.getTypeName().contains(JavaFieldEnums.STRING.getValue())) {
            return SqlFieldEnums.VARCHAR.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.INTEGER.getValue()) || genericType.getTypeName().equals("int")) {
            return SqlFieldEnums.INTEGER.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.LONG.getValue()) || genericType.getTypeName().equals("long")) {
            return SqlFieldEnums.BIGINT.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.DOUBLE.getValue()) || genericType.getTypeName().equals("double")) {
            return SqlFieldEnums.NUMERIC.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.BOOLEAN.getValue()) || genericType.getTypeName().equals("boolean")) {
            return SqlFieldEnums.BOOLEAN.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.BIG_DECIMAL.getValue())) {
            return SqlFieldEnums.NUMERIC.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.SHORT.getValue()) || genericType.getTypeName().equals("short")) {
            return SqlFieldEnums.SMALLINT.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.DATE.getValue())) {
            return SqlFieldEnums.DATE.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.FLOAT.getValue()) || genericType.getTypeName().equals("float")) {
            return SqlFieldEnums.FLOAT.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.BYTE.getValue()) || genericType.getTypeName().equals("byte")) {
            return SqlFieldEnums.SMALLINT.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.TIMESTAMP.getValue())) {
            return SqlFieldEnums.TIMESTAMP.getValue();
        } else if (genericType.getTypeName().contains(JavaFieldEnums.TIME.getValue())) {
            return SqlFieldEnums.TIME.getValue();
        } else {
            return null;
        }
    }


}
