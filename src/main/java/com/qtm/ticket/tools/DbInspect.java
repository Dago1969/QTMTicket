package com.qtm.ticket.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbInspect {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/qtmticket?useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String pass = "dago";

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection c = DriverManager.getConnection(url, user, pass)) {
            printCount(c, "hospital");
            printCount(c, "hospital_staging");

            System.out.println("\nTop duplicate groups in hospital_staging (codice_asl, codice_struttura):");
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT TRIM(codice_asl) AS codice_asl, TRIM(codice_struttura) AS codice_struttura, COUNT(*) AS cnt " +
                    "FROM hospital_staging " +
                    "GROUP BY TRIM(codice_asl), TRIM(codice_struttura) " +
                    "ORDER BY cnt DESC LIMIT 20")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.printf("%s | %s => %d\n", rs.getString(1), rs.getString(2), rs.getInt(3));
                    }
                }
            }

            System.out.println("\nSample hospital_staging rows (non-empty codice_asl) :");
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT codice_regione, codice_asl, codice_struttura, struttura, indirizzo FROM hospital_staging WHERE TRIM(codice_asl)<>'' LIMIT 20")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.printf("%s | %s | %s | %s | %s\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                    }
                }
            }
        }
    }

    private static void printCount(Connection c, String table) throws Exception {
        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM " + table)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) System.out.println(table + " count: " + rs.getLong(1));
            }
        }
    }
}
