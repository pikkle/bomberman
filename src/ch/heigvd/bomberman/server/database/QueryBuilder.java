package ch.heigvd.bomberman.server.database;

import java.util.LinkedList;

/**
 * Created by matthieu.villard on 09.03.2016.
 */
public class QueryBuilder
{
    private LinkedList<String> queries;
    public QueryBuilder(){
        queries = new LinkedList<String>();
    }

    public LinkedList<String> getQueries(){
        return queries;
    }

    public void parseSql(String sql){
        boolean openedStringType1 = false;
        boolean openedStringType2 = false;
        int lastSeparation = 0;

        for(int i = 0; i < sql.length(); i++){
            if(!openedStringType2 && sql.charAt(i) == '\''){
                if(!openedStringType1)
                    openedStringType1 = true;
                else
                    openedStringType1 = false;
            }
            else if(!openedStringType1 && sql.charAt(i) == '"') {
                if(!openedStringType2)
                    openedStringType2 = true;
                else
                    openedStringType2 = false;
            }
            else if(!openedStringType1 && !openedStringType2 && sql.charAt(i) == ';'){
                String query = sql.substring(lastSeparation, i).trim();
                if(!query.isEmpty())
                    queries.add(query);
                lastSeparation = i + 1;
            }
        }
        if(!sql.isEmpty() && !sql.endsWith(";")){
            String query = sql.substring(lastSeparation, sql.length()).trim();
            if(!query.isEmpty())
                queries.add(query);
        }
    }
}
