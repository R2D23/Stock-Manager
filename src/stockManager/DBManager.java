package stockManager;

import java.sql.*;

/**
 * The <code>class</code> that manages all necessary operations with the
 * Database. It ensures a <code>Connection</code> object.
 * <p>
 * @author R2D2 3
 */
public class DBManager {

    private static String connString = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String user = null;
    private static char[] pass = null;
    private static Connection con = null;
    private static Statement statement = null;

    /**
     * <code>connect()</code> method starts the connection by initializing the
     * <code>con</code> static variable. It may be called again for
     * reconnection. It shouldn't be called without variables <code>user</code>
     * or <code>pass</code> being kept uninitialized.
     * <p>
     * @throws SQLException
     * @throws NullPointerException - The user and password haven't been set.
     */
    public static void connect() throws SQLException, NullPointerException {
	if (pass == null || user == null)
	    throw new java.lang.NullPointerException();
	DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
	con = DriverManager.getConnection(connString, user, new String(pass));
	System.out.println("Connection Successfully.\n");
	for (char c : pass)
	    c = 0;
    }

    public static void setConnStringDirection(String s) {
	String[] aux = connString.split(":");
	aux[3] = "@" + s;
	connString = aux[0] + ":" + aux[1] + ":" + aux[2] + ":" + aux[3] + ":"
		+ aux[4] + ":" + aux[5];
    }

    public static void setUser(String s) {
	user = s;
    }

    public static void setPass(char[] s) {
	pass = s;
    }

    public static boolean isConnected() {
	return con != null;
    }

    /**
     * Provides a <code>Statement</code> to work with. The call automatically <code>
     * close()</code>s the formerly given statement to avoid opening more
     * DataBase keys. Finish using the current instance before asking for
     * another one.
     * <p>
     * @return The <code>Statement</code> to be used.
     * <p>
     * @throws SQLException
     */
    public static synchronized Statement createStatement() throws SQLException {
	if (statement != null)
	    statement.close();
	statement = con.createStatement();
	return statement;
    }

    /**
     * Provides a <code>PreparedStatement</code> to work with. The call automatically <code>
     * close()</code>s the formerly given statement to avoid opening more
     * DataBase keys. Finish using the current instance before asking for
     * another one.
     * <p>
     * @param s The Query in SQL.
     * @return The <code>PreparedStatement</code> to be used.
     * <p>
     * @throws SQLException
     */
    public static synchronized PreparedStatement prepareStatement(String s) throws SQLException {
	if (statement != null)
	    statement.close();
	statement = con.prepareStatement(s);
	return (PreparedStatement) statement;
    }
}
