package chatbot;

import java.sql.*;
import molecs.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class ChatBot here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ChatBot
{
    private List<String> GOVERNORS, DIVISIONS, REGIONS, LANGUAGES;
    private Connection conn;

    private boolean hasRegion(String region){
        if (REGIONS.contains(region.toUpperCase())){
            return true;
        } else {
            return false;
        }
    }

    public ChatBot(){
        Statement stmt = null;
        ResultSet rs = null;
        conn = MySQLConnection.getConnection(); 
        
        GOVERNORS = new ArrayList();
        DIVISIONS = new ArrayList();
        REGIONS = new ArrayList();
        LANGUAGES = new ArrayList();

        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT Governor_name FROM governors");

            while(rs.next()){
                GOVERNORS.add(rs.getString("Governor_name").toUpperCase());
            }

            rs = stmt.executeQuery("SELECT language FROM language");

            while(rs.next()){
                LANGUAGES.add(rs.getString("language").toUpperCase());
            }

            rs = stmt.executeQuery("SELECT division FROM division");

            while(rs.next()){
                DIVISIONS.add(rs.getString("division").toUpperCase());
            }

            rs = stmt.executeQuery("SELECT reg_name FROM region");

            while(rs.next()){
                REGIONS.add(rs.getString("reg_name").toUpperCase());
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore

                stmt = null;
            }
        }
    }

    public static void main(String [] args) throws SQLException, InterruptedException {

        ChatBot bot = new ChatBot();

        ChatBot.slowPrint("Please wait", 30);
        for (int i = 0; i < 30; i++) {
            ChatBot.slowPrint(".", 15);
        }
        ChatBot.slowPrintln(" 100%\n", 30);

        ChatBot.slowPrintln("Oh hey, I am Tundroid!", 5);
        ChatBot.slowPrintln("What should I call you? ", 5);
        String name = IO.getLine(); 
        ChatBot.slowPrintln("Nice to meet you, " + name + " \ud83d\ude00.", 5);
        IO.println();

        ChatBot.slowPrintln(name + ", now that we know ourselves, what can I do for you? ", 5);
        String request = "";
        while(true){
            request = IO.getLine().trim().toLowerCase();
            if (request.equals("")){
                ChatBot.slowPrintln("Sorry, I didn't catch that, can you rephrase? ", 5);
                continue;
            } else if (request.equals("nothing")) {
                break;
            } else {
                if (request.contains("who")){
                    if(request.contains("governor")){
                        //conn.createStatement().executeQuery("SELECC)

                        int start = request.indexOf("governor of the ") + "governor of the ".length();
                        int end = request.indexOf(" region");
                        String region = request.substring(start, end).trim();

                        if (bot.hasRegion(region)){
                            ResultSet rs = bot.conn.createStatement().executeQuery("SELECT Governor_name FROM governors JOIN region ON region.governor = governors.id WHERE reg_name ='" + region + "'");

                            while(rs.next()){
                                IO.println(rs.getString("Governor_name") + " is the governor of " + region + ".");
                            }

                            IO.print("What else can I do for you? ");                            
                        } else {
                            IO.println("Oops! sorry, " + region + " is not a region in Cameroon.");
                            IO.print("Please, try again: ");
                        }
                    }
                } else {
                    ChatBot.slowPrintln("Sorry, I didn't catch that, can you rephrase? ", 5);
                    continue;
                }
            }
        }

    }
    public static void slowPrint(String message, int delay) throws InterruptedException {
        for (char character : message.toCharArray()) {
            Thread.sleep(delay);
            IO.print(character);
        }
    }

    public static void slowPrintln(String message, int delay) throws InterruptedException {
        for (char character : message.toCharArray()) {
            Thread.sleep(100, delay);
            IO.print(character);
        }
        IO.println();
    }

}
