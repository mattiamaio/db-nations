package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	private final static String URL = "jdbc:mysql://localhost:3306/nations";
	private final static String USER = "root";
	private final static String PW = "!Jgrs827a1";

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Insert research parameter: ");
		String filter = scan.nextLine();

		try (Connection con = DriverManager.getConnection(URL, USER, PW)) {
			String sql = "SELECT c.name as country, c.country_id, r.name as region, co.name as continent \n"
					+ "FROM countries c\n" + "JOIN regions r \n" + "ON r.region_id = c.region_id\n"
					+ "JOIN continents co \n" + "ON co.continent_id = r.continent_id\n" + "WHERE c.name LIKE ? "
					+ "ORDER BY c.name ASC;\n" + "";

			filter = "%" + filter + "%";

			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, filter);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.print(rs.getInt(2) + " - ");
						System.out.print(rs.getString(1) + " - ");
						System.out.print(rs.getString(3) + " - ");
						System.out.println(rs.getString(4));
					}
				}

			}
			System.out.print("Select country id: ");
			int countryId = scan.nextInt();

			String query = "SELECT c.name\n" + "FROM countries c \n" + "WHERE c.country_id = ?; \n" + "";

			try (PreparedStatement ps = con.prepareStatement(query)) {
				ps.setInt(1, countryId);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.println("Details for country: " + rs.getString(1));
					}
				}
			}

			String query2 = "SELECT c.name , l.`language`\n" + "FROM languages l\n" + "JOIN country_languages cl\n"
					+ "ON cl.language_id = l.language_id\n" + "JOIN countries c\n" + "ON cl.country_id = c.country_id\n"
					+ "WHERE c.country_id = ?;";

			try (PreparedStatement ps = con.prepareStatement(query2)) {
				ps.setInt(1, countryId);

				try (ResultSet rs = ps.executeQuery()) {
					System.out.print("Languages: ");
					while (rs.next()) {
						System.out.print(rs.getString(2) + " ");
					}
				}
			}

			String query3 = "SELECT cs.`year`, cs.population, cs.gdp \n" + "FROM country_stats cs \n"
					+ "JOIN countries c \n" + "ON c.country_id = cs.country_id \n" + "WHERE c.country_id = ? \n"
					+ "ORDER BY `year` DESC\n" + "LIMIT 1;";

			try (PreparedStatement ps = con.prepareStatement(query3)) {
				ps.setInt(1, countryId);
				try (ResultSet rs = ps.executeQuery()) {
					System.out.println("\nMost recent stats");
					while (rs.next()) {
						System.out.println("Year: " + rs.getString(1));
						System.out.println("Population: " + rs.getString(2));
						System.out.println("GDP: " + rs.getString(3));
					}
				}
			}

		} catch (SQLException e) {
			System.out.println("Error");
			System.out.println(e.getMessage());
		}

		scan.close();

	}
}
