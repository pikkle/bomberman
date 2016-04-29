import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by matthieu.villard on 09.03.2016.
 */
public class DataWrapper
{
    private String[] headers;
    private LinkedList<Record> records;

    DataWrapper(ResultSet result) throws SQLException {
        if(result == null)
            throw new SQLException("No data");

        ResultSetMetaData rsmd = result.getMetaData();
        headers = new String[rsmd.getColumnCount() + 1];

        headers[0] = "";
        for(int i = 1; i < headers.length; i++){
            headers[i] = rsmd.getColumnName(i);
        }

        records = new LinkedList<Record>();
        while(result.next()){
            records.add(new Record(result));
        }
    }

    public String[] getHeaders(){
        return headers;
    }

    public Record fetch(){
        if(records != null && records.size() > 0)
            return records.get(0);
        return null;
    }

    public LinkedList<Record> fetchAll(){
        return records;
    }

    public DataWrapper getColumns(String ...columnNames) throws SQLException{
        if(headers == null)
            throw new SQLException("No data");

        int[] indexes = new int[columnNames.length];
        for(int i = 0; i < columnNames.length; i++) {
            for (int j = 0; j < headers.length; j++) {
                if (headers[j].contentEquals(columnNames[i])) {
                    indexes[i] = j;
                    break;
                }
                indexes[i] = -1;
            }
        }

        return getColumns(indexes);
    }

    public DataWrapper getColumns(int ...columnindexes) throws SQLException{
        if(headers == null)
            throw new SQLException("No data");

        DataWrapper results = clone();
        if(results == null)
            throw new SQLException("Filter Error");

        int length = 1;
        for(int i = 0; i < columnindexes.length; i++) {
            if(columnindexes[i] >= 0 && columnindexes[i] < headers.length)
                length++;
        }

        results.headers = new String[length];
        results.headers[0] = "";

        int column = 1;
        for(int i = 0; i < columnindexes.length; i++) {
            if(columnindexes[i] >= 0 && columnindexes[i] < headers.length){
                results.headers[column] = headers[i];
                column++;
            }
        }

        if(records == null)
            return results;

        results.records = new LinkedList<Record>();
        Iterator<Record> it = records.iterator();
        while(it.hasNext()){
            results.records.add(it.next());
        }

        return results;
    }

    public DataWrapper clone(){
        DataWrapper dispatcher = null;
        try {
            dispatcher = (DataWrapper)  super.clone();
        }
        catch (CloneNotSupportedException  e) { }
        return dispatcher;
    }
}
