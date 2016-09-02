package connector;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
	
	/////////////////////////////////////////////////////////////////////
	// ATRIBUTOS ////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////

	/**Atributo de Coneccion a la base de Datos*/
	private static Connection connection;

	
	/////////////////////////////////////////////////////////////////////
	// METODOS //////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////

	
	public Connection connect(){
		
		String dbUrl = "jdbc:sqlserver://localhost:1433;databasename=tbMatriculas";
		String user = "userdb";
		String password = "password2016";
		
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(dbUrl,user,password);
		}catch(java.sql.SQLException e){
			e.printStackTrace();
			connection = null;
		}catch(Exception e){
			connection = null;
		}
		return connection;
	}
	
	}