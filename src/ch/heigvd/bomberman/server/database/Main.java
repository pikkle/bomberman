package ch.heigvd.bomberman.server.database;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Created by matthieu.villard on 09.03.2016.
 */
public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ActionManager am = new ActionManager();
        do{
            System.out.println("Choose action to execute : ");
            am.showActions();
            System.out.println();
            am.chooseAction();
        }while(!am.execute());


        /*System.out.println("1. Show tables\n2. Show table");
        System.out.print("Choosen action : ");
        Scanner sin = new Scanner(System.in);
        int action = sin.nextInt();
        System.out.println();

        DBManager dbm = new DBManager();
        dbm.openDatabase("C:\\sqlite\\sqlite-tools-win32-x86-3110100\\hello.db");
        LinkedList<DataWrapper> dw = null;

        //dbm.prepare("INSERT INTO data values(3);INSERT INTO data values(4)");
        if(dw != null) {
            Iterator<DataWrapper> it = dw.iterator();
            while (it.hasNext()) {
                Displayer displayer = new Displayer(it.next());
                System.out.println(displayer);
            }
        }*/
    }
}
