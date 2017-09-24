package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result = null;
//		BufferedReader br = null;
//		InputStreamReader isr = null;
		
		Connection conn = null;
		PreparedStatement stmt = null;
//		Statement stmt = null;
		
		try {
			conn = this.getConnection();
			String sql = "SELECT keyword, response FROM lab3 where keyword=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,text);
			ResultSet rs = stmt.executeQuery();
			
			
//			stmt = conn.createStatement();
//			String sql = "SELECT keywork, response FROM lab3";
//			ResultSet rs = stmt.executeQuery(sql);
			
			while(result==null && rs.next()) {
				String x=null;
				String y=null;
				x=rs.getString("keyword");
				y=rs.getString("response");
				if(text.toLowerCase().equals(x.toLowerCase())) {
					result = y;
					break;
				}
			}
		
			rs.close();
			stmt.close();
			conn.close();
			
		} catch(SQLException e)
		{System.out.println(e);}
//		finally {
//			try {
//				if(stmt!=null)
//					stmt.close();
//				if(conn!=null)
//					conn.close();
//				
//			} catch(SQLException ex) {
//				System.out.println(ex);
//			}
//		}
		if(result!=null)
			return result;
		throw new Exception("NOT FOUND");
	}

		
		
//		try {
//			isr = new InputStreamReader(
//                    this.getClass().getResourceAsStream(FILENAME));
//			br = new BufferedReader(isr);
//			String sCurrentLine;
//			
//			while (result == null && (sCurrentLine = br.readLine()) != null) {
//				String[] parts = sCurrentLine.split(":");
//				if (text.toLowerCase().equals(parts[0].toLowerCase())) {
//					result = parts[1];
//				}
//			}
//		} catch (IOException e) {
//			log.info("IOException while reading file: {}", e.toString());
//		} finally {
//			try {
//				if (br != null)
//					br.close();
//				if (isr != null)
//					isr.close();
//			} catch (IOException ex) {
//				log.info("IOException while closing file: {}", ex.toString());
//			}
//		}
//		if (result != null)
//			return result;
//		throw new Exception("NOT FOUND");
//	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
