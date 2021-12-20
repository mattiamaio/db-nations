package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
	private final static String URL = "jdbc:mysql://localhost:3306/nations";
	private final static String USER = "root";
	private final static String PW = "!Jgrs827a1";
	
	public static void main(String[] args) {
		try(Connection con = DriverManager.getConnection(URL, USER, PW)){
			String sql = "SELECT c.name as country, c.country_id, r.name as region, co.name as continent \n"
					+ "FROM countries c\n"
					+ "JOIN regions r \n"
					+ "ON r.region_id = c.region_id\n"
					+ "JOIN continents co \n"
					+ "ON co.continent_id = r.continent_id\n"
					+ "ORDER BY c.name ASC;\n"
					+ "";
			PreparedStatement ps = con.prepareStatement(sql);
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()){
					System.out.print(rs.getString(1) + " ");
					System.out.print(rs.getInt(2) + " ");
					System.out.print(rs.getString(3) + " ");
					System.out.println(rs.getString(4));
				}
			}
				
			}
					
					
		catch (SQLException e) {
		System.out.println("Errore");
		System.out.println(e.getMessage());
		}
		
		
	}
}
