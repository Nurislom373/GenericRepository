package org.khasanof;

import org.khasanof.config.ConnectionConfig;
import org.khasanof.core.SchemaCore;
import org.khasanof.enums.SchemaEnum;
import org.khasanof.exception.exceptions.EntityNotFoundException;
import org.khasanof.utils.BaseUtils;
import org.khasanof.utils.GenericUtils;
import org.khasanof.utils.QueryUtils;
import org.khasanof.utils.Sort;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * The GenericRepository Class is Difference method with Spring JpaRepository
 * The difference with JpaRepository is that GenericRepository doesn't need
 * to be a spring project in order to use it. That is, it can be easily used
 * in simple build tools like Maven or Gradle without spring. Very easy to use
 * and lightweight.
 *
 * @param <T>  Table class
 * @param <ID> Table id
 * @author Khasanov Nurislom
 * @since 1.0
 */
public class GenericRepository<T, ID> implements AsyncRepository<T, ID> {
    private Connection connection;
    protected Class<T> persistenceClass;
    private final GenericUtils genericUtils;
    private final QueryUtils queryUtils;
    private final SchemaCore schemaCore;
    private String schemaVal;

    /**
     * This is one of the main points of GenericRepository.
     * The reason is that when GenericRepository starts, many processes take place,
     * for example, it checks whether the table exists in the database, if the
     * table exists, it is checked internally, if not, then the table is created
     * in the database. after that you can use GenericRepository without any difficulty.
     */
    public GenericRepository() {
        this.persistenceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.genericUtils = new GenericUtils();
        this.queryUtils = new QueryUtils();
        this.schemaCore = new SchemaCore();
        setConnection();
        checkTable();
    }

    /**
     * Return a reference to the entity with the given id.
     * If the entity is not found throw and {@link org.khasanof.exception.exceptions.EntityNotFoundException}
     *
     * @param id must be not null
     * @return Return a reference to the entity with the given id.
     * @since 1.0
     */
    public T getById(ID id) {
        BaseUtils.checkNotNullId(id);
        try {
            PreparedStatement preparedStatement;
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                preparedStatement = connection.prepareStatement(queryUtils.getByIdQuery(id, persistenceClass.getSimpleName()));
            } else {
                preparedStatement = connection.prepareStatement(queryUtils.getByIdQuery(id, schemaCore.getNameValue()));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            Class<?> aClass = Class.forName(persistenceClass.getName());
            while (resultSet.next()) {
                return (T) genericUtils.get(resultSet, aClass.newInstance());
            }
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new EntityNotFoundException("Entity not found");
    }

    /**
     * Return reference to the entity with given id. In a wrapped Optional.
     * If the entity is not found throw and {@link org.khasanof.exception.exceptions.EntityNotFoundException}
     *
     *
     * @param id must be not null.
     * @return Return reference to the entity with given id. In a wrapped Optional
     * @since 1.0
     */
    public Optional<T> findById(ID id) {
        BaseUtils.checkNotNullId(id);
        try {
            PreparedStatement preparedStatement;
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                preparedStatement = connection.prepareStatement(queryUtils.findByIdQuery(id, persistenceClass.getSimpleName()));
            } else {
                preparedStatement = connection.prepareStatement(queryUtils.findByIdQuery(id, schemaCore.getNameValue()));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            Class<?> aClass = Class.forName(persistenceClass.getName());
            while (resultSet.next()) {
                return Optional.of((T) genericUtils.get(resultSet, aClass.newInstance()));
            }
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new EntityNotFoundException("Entity not found");
    }

    /**
     * Return a reference entity list.
     * If the entity list will be null return empty list.
     *
     * @return entities list.
     * @since 1.0
     */
    public List<T> findAll() {
        try {
            List<T> list = new ArrayList<>();
            PreparedStatement preparedStatement;
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                preparedStatement = connection.prepareStatement(queryUtils.findAllQuery(persistenceClass.getSimpleName()));
            } else {
                preparedStatement = connection.prepareStatement(queryUtils.findAllQuery(schemaCore.getNameValue()));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            Class<?> aClass = Class.forName(persistenceClass.getName());
            while (resultSet.next()) {
                list.add((T) genericUtils.get(resultSet, aClass.newInstance()));
            }
            return list;
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Returns all entities sorted by given options.
     * If the entities will be null return empty list.
     *
     * @param sort must be not null
     * @return all entities sorted by given options
     * @since 1.0
     */
    public List<T> findAll(Sort sort) {
        try {
            List<T> list = new ArrayList<>();
            PreparedStatement preparedStatement;
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                preparedStatement = connection.prepareStatement(queryUtils.findAllSortQuery(sort, persistenceClass.getSimpleName()));
            } else {
                preparedStatement = connection.prepareStatement(queryUtils.findAllSortQuery(sort, schemaCore.getNameValue()));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            Class<?> aClass = Class.forName(persistenceClass.getName());
            while (resultSet.next()) {
                list.add((T) genericUtils.get(resultSet, aClass.newInstance()));
            }
            return list;
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> findAll(DirectionRequest request) {
        try {
            List<T> list = new ArrayList<>();
            PreparedStatement preparedStatement;
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                preparedStatement = connection.prepareStatement(queryUtils.findAllDirectionQuery(request, persistenceClass.getSimpleName()));
            } else {
                preparedStatement = connection.prepareStatement(queryUtils.findAllDirectionQuery(request, schemaCore.getNameValue()));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            Class<?> aClass = Class.forName(persistenceClass.getName());
            while (resultSet.next()) {
                list.add((T) genericUtils.get(resultSet, aClass.newInstance()));
            }
            return list;
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> findAllById(Iterable<ID> ids) {
        BaseUtils.checkNotNullEntity(ids);
        List<T> list = new ArrayList<>();
        for (ID id : ids) {
            list.add(getById(id));
        }
        return list;
    }

    public void save(T entity) {
        BaseUtils.checkNotNullEntity(entity);
        try {
            connection.createStatement().execute(saveOrUpdate(entity));
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void insert(T entity) {
        BaseUtils.checkNotNullEntity(entity);
        try {
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                connection.createStatement().execute(queryUtils.insertQuery(entity, persistenceClass.getSimpleName()));
            } else {
                connection.createStatement().execute(queryUtils.insertQuery(entity, schemaCore.getNameValue()));
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void saveAll(List<T> tList) {
        BaseUtils.checkNotNullList(tList);
        try {
            for (T entity : tList) {
                connection.createStatement().execute(saveOrUpdate(entity));
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void insertAll(Iterable<T> entities) {
        BaseUtils.checkNotNullEntity(entities);
        try {
            for (T next : entities) {
                insert(next);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(T entity) {
        BaseUtils.checkNotNullEntity(entity);
        try {
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                connection.createStatement().execute(queryUtils.deleteQuery(entity, persistenceClass.getSimpleName()));
            } else {
                connection.createStatement().execute(queryUtils.deleteQuery(entity, schemaCore.getNameValue()));
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(ID id) {
        BaseUtils.checkNotNullId(id);
        try {
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                connection.createStatement().execute(queryUtils.deleteByIdQuery(id, persistenceClass.getSimpleName()));
            } else {
                connection.createStatement().execute(queryUtils.deleteByIdQuery(id, schemaCore.getNameValue()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                connection.createStatement().execute(queryUtils.deleteAllQuery(persistenceClass.getSimpleName()));
            } else {
                connection.createStatement().execute(queryUtils.deleteAllQuery(schemaCore.getNameValue()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existById(ID id) {
        BaseUtils.checkNotNullId(id);
        return findById(id).isPresent();
    }

    public boolean contain(String key, String value) {
        try {
            BaseUtils.notNull(key, "key required is not null!");
            BaseUtils.notNull(value, "value required is not null!");
            ResultSet resultSet = containCore(key, value, String.class);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean contain(String key, Integer value) {
        try {
            BaseUtils.notNull(key, "key required is not null!");
            BaseUtils.notNull(value, "value required is not null!");
            ResultSet resultSet = containCore(key, value, Integer.class);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean contain(String key, Boolean value) {
        try {
            BaseUtils.notNull(key, "key required is not null!");
            BaseUtils.notNull(value, "value required is not null!");
            ResultSet resultSet = containCore(key, value, Boolean.class);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ResultSet containCore(String key, Object value, Class<?> aClass) throws SQLException {
        if (contains(key)) {
            PreparedStatement preparedStatement;
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                String formatted = queryUtils.containQuery()
                        .formatted(persistenceClass.getSimpleName(), key);
                System.out.println("formatted = " + formatted);
                preparedStatement = connection.prepareStatement(formatted);
            } else {
                String formatted = queryUtils.containQuery()
                        .formatted(schemaCore.getNameValue(), key);
                preparedStatement = connection.prepareStatement(formatted);
                System.out.println("formatted = " + formatted);
            }
            return genericUtils.setValueType(preparedStatement, aClass, value).executeQuery();
        } else {
            throw new IllegalArgumentException("key column not found!");
        }
    }


    public void deleteAll(Iterable<? extends ID> ids) {
        BaseUtils.checkNotNullId(ids);
        try {
            for (ID id : ids) {
                deleteById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll(List<T> tList) {
        BaseUtils.checkNotNullList(tList);
        try {
            for (T entity : tList) {
                if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                    connection.createStatement().execute(queryUtils.deleteQuery(entity, persistenceClass.getSimpleName()));
                } else {
                    connection.createStatement().execute(queryUtils.deleteQuery(entity, schemaCore.getNameValue()));
                }
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public long count() {
        try {
            PreparedStatement preparedStatement;
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                preparedStatement = connection.prepareStatement(queryUtils.countQuery(persistenceClass.getSimpleName()));
            } else {
                preparedStatement = connection.prepareStatement(queryUtils.countQuery(schemaCore.getNameValue()));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private String saveOrUpdate(T entity) throws IllegalAccessException {
        Field[] fields = entity.getClass().getDeclaredFields();
        Object id = null;
        String type = null;

        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("Id")) {
                id = queryUtils.getValue(entity, field);
                type = field.getGenericType().getTypeName();
            }
        }

        if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
            if (Objects.isNull(id)) {
                return queryUtils.insertQuery(entity, persistenceClass.getSimpleName());
            } else {
                if (objIsPresent(id, type)) {
                    return queryUtils.updateQuery(entity, persistenceClass.getSimpleName());
                } else {
                    return queryUtils.insertQuery(entity, persistenceClass.getSimpleName());
                }
            }
        } else {
            if (Objects.isNull(id)) {
                return queryUtils.insertQuery(entity, schemaCore.getNameValue());
            } else {
                if (objIsPresent(id, type)) {
                    return queryUtils.updateQuery(entity, schemaCore.getNameValue());
                } else {
                    return queryUtils.insertQuery(entity, schemaCore.getNameValue());
                }
            }
        }
    }

    private boolean contains(String key) throws SQLException {
        PreparedStatement preparedStatement;

        if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
            preparedStatement = connection.prepareStatement(queryUtils.selectTableColumns());
            preparedStatement.setString(1, persistenceClass.getSimpleName());
        } else {
            preparedStatement = connection.prepareStatement(queryUtils.selectTableColumns());
            preparedStatement.setString(1, schemaCore.getNameValue());
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, String> columnsAndTypes = genericUtils.getColumnsAndTypes(resultSet);
        return columnsAndTypes.containsKey(key);
    }

    private Field[] getFields() {
        return persistenceClass.getDeclaredFields();
    }

    private boolean objIsPresent(Object id, String type) {
        try {
            ResultSet resultSet = connection.prepareStatement(queryUtils.objIsPresentQuery(id, type, persistenceClass.getSimpleName())).executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void checkTable() {
        try {
            if (schemaVal.equals(SchemaEnum.NO_ANNOTATION.getValue()) || schemaVal.equals(SchemaEnum.ONLY_SCHEMA.getValue())) {
                if (!tableExistQuery(persistenceClass.getSimpleName().toLowerCase(Locale.ROOT))) {
                    connection.createStatement().execute(queryUtils.createTableQuery(getFields(), persistenceClass.getSimpleName()));
                } else {
                    fieldsCheckTable(persistenceClass.getSimpleName());
                }
            } else {
                if (!tableExistQuery(schemaCore.getNameValue())) {
                    connection.createStatement().execute(queryUtils.createTableQuery(getFields(), schemaCore.getNameValue()));
                } else {
                    fieldsCheckTable(schemaCore.getNameValue());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fieldsCheckTable(String className) throws SQLException {
        Map<String, String> mapNameType = new HashMap<>();
        List<Field> newFields = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(queryUtils.selectTableColumns());
        preparedStatement.setString(1, className.toLowerCase(Locale.ROOT));
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            mapNameType.put(resultSet.getString(1), resultSet.getString(2));
        }

        Field[] declaredFields = persistenceClass.getDeclaredFields();
        long count = Arrays.stream(declaredFields).count();

        if ((int) count > mapNameType.size()) {
            for (Field declaredField : declaredFields) {
                if (!mapNameType.containsKey(declaredField.getName())) {
                    newFields.add(declaredField);
                }
            }

            if (!newFields.isEmpty()) {
                for (Field newField : newFields) {
                    String alterQuery = getAlterQuery(newField, className);
                    connection.createStatement().execute(alterQuery);
                }
            }
        } else if ((int) count < mapNameType.size()) {
            for (Field declaredField : declaredFields) {
                for (Map.Entry<String, String> stringStringEntry : mapNameType.entrySet()) {
                    if (declaredField.getName().equals(stringStringEntry.getKey())) {
                        mapNameType.remove(stringStringEntry.getKey());
                    }
                }
            }

            if (!mapNameType.isEmpty()) {
                for (Map.Entry<String, String> stringStringEntry : mapNameType.entrySet()) {
                    String alterQuery = getAlterQuery(stringStringEntry.getKey(), className);
                    connection.createStatement().execute(alterQuery);
                }
            }
        }
    }

    private String getAlterQuery(String name, String className) {
        String s = queryUtils.alterTableDropQuery();
        return s.formatted(className, name);
    }

    private String getAlterQuery(Field field, String className) {
        String s = queryUtils.alterTableAddQuery();
        return s.formatted(className, field.getName(), genericUtils.dataTypeConvertToSQl(field.getGenericType()));
    }

    private boolean tableExistQuery(String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(queryUtils.tableExistQuery());
        preparedStatement.setString(1, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) != 0;
    }

    private void setConnection() {
        if (schemaCore.annotationPresent(persistenceClass)) {
            schemaCore.annotationValueInitialize(persistenceClass);
            if (BaseUtils.checkTwoStringValue(schemaCore.getNameValue(), schemaCore.getSchemaValue())) {
                setSchemaVal(SchemaEnum.TWO_VALUE.getValue());
                this.connection = ConnectionConfig.getConnection(schemaCore.getSchemaValue());
            } else {
                if (BaseUtils.nonNullString(schemaCore.getSchemaValue())) {
                    setSchemaVal(SchemaEnum.ONLY_SCHEMA.getValue());
                    this.connection = ConnectionConfig.getConnection(schemaCore.getSchemaValue());
                } else if (BaseUtils.nonNullString(schemaCore.getNameValue())) {
                    setSchemaVal(SchemaEnum.ONLY_NAME.getValue());
                    this.connection = ConnectionConfig.getHikariConnection();
                } else {
                    setSchemaVal(SchemaEnum.NO_ANNOTATION.getValue());
                    this.connection = ConnectionConfig.getHikariConnection();
                }
            }
        } else {
            setSchemaVal(SchemaEnum.NO_ANNOTATION.getValue());
            this.connection = ConnectionConfig.getHikariConnection();
        }
    }

    private void setSchemaVal(String schemaVal) {
        this.schemaVal = schemaVal;
    }

    private String getSchemaVal() {
        return schemaVal;
    }
}
