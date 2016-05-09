package ch.heigvd.bomberman.server.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by matthieu.villard on 14.03.2016.
 */
public class ActionManager
{
    private static String[] ACTIONS = new String[]{"1. Open database", "2. Show tables", "3. Execute query", "4. Quit"};
    private static String[] METHODS_NAMES = new String[]{"openDatabase", "showTables", "executeQuery", "quit"};
    private int action;
    private  DBManager dbm;

    public ActionManager(){
        try {
            dbm = new DBManager();
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void showActions(){
        for(String action : ACTIONS)
            System.out.println(action);
    }

    public void chooseAction(){
        Scanner sin = new Scanner(System.in);
        do{
            System.out.print("Choosen action : ");
            action = sin.nextInt() - 1;
        }while(action < 0 || action >= METHODS_NAMES.length);
    }

    public boolean execute()  {
        Method m;
        try{
            m = this.getClass().getDeclaredMethod(METHODS_NAMES[action]);
        }
        catch(NoSuchMethodException ex){
            System.out.println("No public methode named " + ex.getMessage());
            return false;
        }

        try{
            System.out.println();
            boolean quit = (boolean) m.invoke(this);
            System.out.println();
            return quit;
        }
        catch(InvocationTargetException ex){
            System.out.println("Invocation error : " + ex.getMessage() + "s");
            return false;
        }
        catch (IllegalAccessException ex){
            System.out.println("No access to " + ex.getMessage());
            return false;
        }
    }

    private boolean checkDriverConnection(){
        if(dbm == null){
            System.out.println("No available driver");
            return false;
        }
        return true;
    }

    private boolean checkDatabaseConnection(){
        if(!checkDriverConnection())
            return false;
        if(!dbm.isOpened()){
            System.out.println("No available database");
            return false;
        }
        return true;
    }

    private boolean openDatabase(){
        if(!checkDriverConnection())
            return false;

        Scanner sin = new Scanner(System.in);
        String path = "";
        do{
            System.out.print("Database path : ");
            path = sin.nextLine();
            try {
                dbm.openDatabase(path);
            }
            catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }while(!checkDatabaseConnection() && !path.isEmpty());
        return false;
    }

    private boolean showTables(){
        if(!checkDatabaseConnection())
            return false;
        LinkedList<DataWrapper> dw;
        try {
            dw = dbm.getTables();
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }

        Iterator<DataWrapper> it = dw.iterator();
        while (it.hasNext()) {
            Displayer displayer = null;
            try {
                displayer = new Displayer(it.next());
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return false;
            }
            System.out.println(displayer);
        }

        return false;
    }

    private boolean executeQuery(){
        if(!checkDatabaseConnection())
            return false;

        Scanner sin = new Scanner(System.in);
        String query = "";
        do{
            System.out.print("Query : ");
            query = sin.nextLine();
            if(query.isEmpty())
                return false;
            try {
                dbm.clear();
                dbm.prepare(query);
                showResults(dbm.doQueries());
            }
            catch(SQLException ex){
                System.out.println(ex.getMessage() + "\n");
            }
        }while(true);
    }

    private boolean quit(){
        return true;
    }

    private void showResults(LinkedList<DataWrapper> dw){
        if(dw != null && !dw.isEmpty()) {
            Iterator<DataWrapper> it = dw.iterator();
            while (it.hasNext()) {
                try {
                    Displayer displayer = new Displayer(it.next());
                    System.out.println(displayer);
                }
                catch (SQLException ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

}
