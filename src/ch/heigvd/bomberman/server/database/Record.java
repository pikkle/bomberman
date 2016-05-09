package ch.heigvd.bomberman.server.database;

/**
 * Created by matthieu.villard on 09.03.2016.
 */
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Record implements Cloneable
{
    private String[] headers;
    private String[] data;
    private int id;

    public Record(ResultSet result) throws SQLException {
        if(result == null)
            throw new SQLException("No data");

        ResultSetMetaData rsmd = result.getMetaData();
        headers = new String[rsmd.getColumnCount() + 1];

        headers[0] = "";
        for(int i = 1; i < headers.length; i++){
            headers[i] = rsmd.getColumnName(i);
        }

        data = new String[headers.length];
        data[0] = String.valueOf(result.getRow());
        for(int i = 1; i < data.length; i++){
            data[i] = result.getString(i);
        }

        id = result.getRow();
    }

    public String get(String label){
        int index = -1;
        for(int i = 0; i < headers.length; i++){
            if(headers[i].contentEquals(label)) {
                index = i;
                break;
            }
        }
        return get(index);
    }

    public Record get(String ...labels){
        int[] indexes = new int[labels.length];
        for(int i = 0; i < labels.length; i++) {
            for (int j = 0; j < headers.length; j++) {
                if (headers[j].contentEquals(labels[i])) {
                    indexes[i] = j;
                    break;
                }
                indexes[i] = -1;
            }
        }

        return get(indexes);
    }

    public String get(int index){
        if(data == null || index < 0 || index >= data.length)
            return null;
        return data[index];
    }

    public Record get(int ...indexes){
        Record row = clone();
        if(row == null)
            return null;

        int length = 1;
        for(int i = 0; i < indexes.length; i++) {
            if(get(indexes[i]) != null)
                length++;
        }

        if(data != null)
            row.data = new String[length];
        row.headers = new String[length];

        row.data[0] = String.valueOf(id);
        row.headers[0] = "";

        int column = 1;
        for(int i = 0; i < indexes.length; i++) {
            if(get(indexes[i]) != null){
                row.headers[column] = headers[indexes[i]];
                if(data != null)
                    row.data[column] = data[indexes[i]];
                column++;
            }
        }

        return row;
    }

    public Record clone(){
        Record record = null;
        try {
            record = (Record)  super.clone();
        }
        catch (CloneNotSupportedException  e) { }
        return record;
    }
}
