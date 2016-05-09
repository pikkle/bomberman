package ch.heigvd.bomberman.server.database;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Created by matthieu.villard on 09.03.2016.
 */

public class DBManager
{
    private final int TIMEOUT;
    private String dbName;
    private String path;
    private Connection mConnection;
    private LinkedList<PreparedStatement> preparedStatements;
    private LinkedList<DataWrapper> results;

    public DBManager() throws SQLException {
        TIMEOUT = 30;
        // register the driver
        try {
            String sDriverName = "org.sqlite.JDBC";
            Class.forName(sDriverName);
        }
        catch (ClassNotFoundException ex){
            throw new SQLException("Sqlite driver not found");
        }
    }

    public void openDatabase(String path) throws SQLException {
        close();

        Path p = Paths.get(path);
        if(!p.toFile().exists())
            throw new SQLException("Invalid database name");
        this.path = path;
        dbName = p.getFileName().toString();
        int pos = dbName.lastIndexOf(".");
        if (pos > 0) {
            dbName = dbName.substring(0, pos);
        }
        // Connexion à la base de données
        String url = "jdbc:sqlite:" + path;
        mConnection = DriverManager.getConnection(url);

        preparedStatements = new LinkedList<PreparedStatement>();
    }

    public void close(){
        mConnection = null;
        dbName = null;
        path = null;
        preparedStatements = null;
        results = null;
    }

    public boolean isOpened(){
        return mConnection != null;
    }

    public void prepare(String query) throws SQLException{
        if(mConnection == null)
            throw new SQLException("No database available");

        QueryBuilder qb = new QueryBuilder();
        qb.parseSql(query);

        Iterator<String> it = qb.getQueries().iterator();
        while (it.hasNext()) {
            preparedStatements.add(mConnection.prepareStatement(it.next()));
        }
    }

    public LinkedList<DataWrapper> doQueries() throws SQLException {
        if(preparedStatements.size() == 0)
            throw new SQLException("No query");

        results = new LinkedList<DataWrapper>();

        Iterator<PreparedStatement> it = preparedStatements.iterator();
        while(it.hasNext()){
            PreparedStatement preparedStatement = it.next();
            preparedStatement.setQueryTimeout(TIMEOUT);
            if(preparedStatement.execute()){
                ResultSet rs = preparedStatement.getResultSet();
                results.add(new DataWrapper(rs));
            }
        }

        preparedStatements = new LinkedList<PreparedStatement>();

        return results;
    }

    public void clear(){
        preparedStatements = new LinkedList<PreparedStatement>();
        results = null;
    }

    public LinkedList<DataWrapper> getResults(){
        return results;
    }

    public LinkedList<DataWrapper> getTables() throws SQLException{
        preparedStatements = new LinkedList<PreparedStatement>();
        String query = "SELECT name AS 'Tables_in_" +  dbName + "' FROM sqlite_master WHERE type = 'table'";
        prepare(query);
        return doQueries();
    }
}

