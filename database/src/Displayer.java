/**
 * Created by matthieu.villard on 09.03.2016.
 */
/*
Matthieu Villard & Adriano Ruberto
 */
import java.sql.SQLException;
import java.util.LinkedList;

public class Displayer {
    private LinkedList<Record> records;
    private int[] colWidths;
    protected String headers[];
    protected char colSep = '|';
    protected char headerSep = '-';

    Displayer(DataWrapper dispatcher) throws SQLException{
        if(dispatcher == null)
            return;
        records = dispatcher.fetchAll();
        headers = dispatcher.getHeaders();

        colWidths = new int[headers.length];

        for(int i = 0; i < headers.length; i++){
            colWidths[i] = headers[i].length();
        }

        for(int i = 0; i < headers.length; i++){
            for(int j = 0; j < records.size(); j++){
                if(records.get(j).get(i) != null)
                    colWidths[i] = Math.max(colWidths[i], records.get(j).get(i).length());
            }
        }
    }

    public String toString()
    {
        String buffer = "";
        if(headers != null) {
            for (int i = 0; i < headers.length; i++) {
                buffer += headers[i] + " ";
                for (int j = 0; j < colWidths[i] - headers[i].length(); j++) {
                    buffer += " ";
                }
                buffer += colSep + " ";
            }
            buffer += "\n";
            for (int i = 0; i < headers.length; i++) {
                for (int j = 0; j < colWidths[i] + 2; j++) {
                    buffer += headerSep;
                }
                if (i + 1 < headers.length)
                    buffer += headerSep;
            }
            buffer += "\n";
        }
        if(records != null) {
            for (int i = 0; i < records.size(); i++) {
                buffer += "";
                for (int j = 0; j < colWidths.length; j++) {
                    int width;
                    if (records.get(i).get(j) != null) {
                        buffer += records.get(i).get(j) + " ";
                        width = records.get(i).get(j).length();
                    } else {
                        buffer += "null ";
                        width = 4;
                    }
                    for (int s = 0; s < colWidths[j] - width; s++) {
                        buffer += " ";
                    }
                    buffer += colSep + " ";
                }
                buffer += "\n";
            }
        }

        return buffer;
    }
}

