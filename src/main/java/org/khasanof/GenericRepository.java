package org.khasanof;

import org.khasanof.config.ConnectionConfig;
import org.khasanof.utils.BaseUtils;
import org.khasanof.utils.GenericUtils;
import org.khasanof.utils.QueryUtils;
import org.khasanof.utils.Sort;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;

/**
 * The GenericRepository Class is Difference method with Spring JpaRepository
 * The difference with JpaRepository is that GenericRepository doesn't need
 * to be a spring project in order to use it. That is, it can be easily used
 * in simple build tools like Maven or Gradle without spring. Very easy to use
 * and lightweight.
 *
 * @param <T> Table class
 * @param <ID> Table id
 * @author Khasanov Nurislom
 * @since 1.0
 */
public class GenericRepository<T, ID> implements AsyncRepository<T, ID> {
    private final Connection connection = ConnectionConfig.getHikariConnection();
    protected Class<T> persistenceClass;
    private final GenericUtils genericUtils;
    private final QueryUtils queryUtils;

    /**
     *
     * This is one of the main points of GenericRepository.
     * The reason is that when GenericRepository starts, many processes take place,
     * for example, it checks whether the table exists in the database, if the
     * table exists, it is checked internally, if not, then the table is created
     * in the database. after that you can use GenericRepository without any difficulty.
     *
     */
    public GenericRepository() {
        this.persistenceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.genericUtils = new GenericUtils();
        this.queryUtils = new QueryUtils();
        checkTable();
    }

    public T getById(ID id) {
        BaseUtils.checkNotNullId(id);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(queryUtils.getByIdQuery(id, persistenceClass.getSimpleName()));
            ResultSet resultSet = preparedStatement.executeQuery();
            Class<?> aClass = Class.forName(persistenceClass.getName());
            while (resultSet.next()) {
                return (T) genericUtils.get(resultSet, aClass.newInstance());
            }
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<T> findById(ID id) {
        BaseUtils.checkNotNullId(id);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(queryUtils.findByIdQuery(id, persistenceClass.getSimpleName()));
            ResultSet resultSet = preparedStatement.executeQuery();
            Class<?> aClass = Class.forName(persistenceClass.getName());
            while (resultSet.next()) {
                return Optional.of((T) genericUtils.get(resultSet, aClass.newInstance()));
            }
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<T> findAll() {
        try {
            List<T> list = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(queryUtils.findAllQuery(persistenceClass.getSimpleName()));
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

    public List<T> findAll(Sort sort) {
        try {
            List<T> list = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(queryUtils.findAllSortQuery(sort, persistenceClass.getSimpleName()));
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
            PreparedStatement preparedStatement = connection.prepareStatement(queryUtils.findAllDirectionQuery(request, persistenceClass.getSimpleName()));
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
            connection.createStatement().execute(queryUtils.insertQuery(entity, persistenceClass.getSimpleName()));
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
            connection.createStatement().execute(queryUtils.deleteQuery(entity, persistenceClass.getSimpleName()));
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(ID id) {
        BaseUtils.checkNotNullId(id);
        try {
            connection.createStatement().execute(queryUtils.deleteByIdQuery(id, persistenceClass.getSimpleName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            connection.createStatement().execute(queryUtils.deleteAllQuery(persistenceClass.getSimpleName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existById(ID id) {
        BaseUtils.checkNotNullId(id);
        return findById(id).isPresent();
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
                connection.createStatement().execute(queryUtils.deleteQuery(entity, persistenceClass.getSimpleName()));
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public long count() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(queryUtils.countQuery(persistenceClass.getSimpleName()));
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

        if (Objects.isNull(id)) {
            return queryUtils.insertQuery(entity, persistenceClass.getSimpleName());
        } else {
            if (objIsPresent(id, type)) {
                return queryUtils.updateQuery(entity, persistenceClass.getSimpleName());
            } else {
                return queryUtils.insertQuery(entity, persistenceClass.getSimpleName());
            }
        }
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
            if (!tableExistQuery(persistenceClass.getSimpleName().toLowerCase(Locale.ROOT))) {
                connection.createStatement().execute(queryUtils.createTableQuery(getFields(), persistenceClass.getSimpleName()));
            } else {
                fieldsCheckTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fieldsCheckTable() throws SQLException {
        Map<String, String> mapNameType = new HashMap<>();
        List<Field> newFields = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(queryUtils.selectTableColumns());
        preparedStatement.setString(1, persistenceClass.getSimpleName().toLowerCase(Locale.ROOT));
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
                    String alterQuery = getAlterQuery(newField, persistenceClass.getSimpleName());
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
                    String alterQuery = getAlterQuery(stringStringEntry.getKey(), persistenceClass.getSimpleName());
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
}
