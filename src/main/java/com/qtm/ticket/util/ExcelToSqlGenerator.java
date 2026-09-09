package com.qtm.ticket.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class ExcelToSqlGenerator {

    public static void main(String[] args) {
        // Configura i percorsi dei file
        String excelFilePath = "src/main/resources/ASL_2010-2026.xlsx";
        String outputSqlPath = "src/main/resources/import_asl.sql";

        try {
            generateSqlFromFile(excelFilePath, outputSqlPath);
            System.out.println("Script SQL generato con successo in: " + outputSqlPath);
        } catch (IOException e) {
            System.err.println("Errore durante la lettura o scrittura dei file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void generateSqlFromFile(String excelPath, String outputPath) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File(excelPath));
             Workbook workbook = new XSSFWorkbook(fis);
             FileWriter fw = new FileWriter(outputPath)) {

            // Prende il primo foglio dell'Excel
            Sheet sheet = workbook.getSheet("foglio1");
            
            // Ciclo sulle righe saltando l'intestazione (riga 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Estrazione dei dati (Adatta gli indici 0, 1, 2... alla struttura del tuo Excel)
                String region = getCellValueAsString(row.getCell(0));
                String codiceAzienda = getCellValueAsLong(row.getCell(1)) != null ? String.valueOf(getCellValueAsLong(row.getCell(1))) : "";
                String denominazioneAzienda = cleanSqlString(getCellValueAsString(row.getCell(2)));
                String indirizzo = cleanSqlString(getCellValueAsString(row.getCell(3)));
                String cap = getCellValueAsString(row.getCell(4));
                Long cityId = getCellValueAsLong(row.getCell(5)); // Se presente numerico
               Long regionId = 0L; // Fondamentale per la constraint
                String siglaprovincia = cleanSqlString(getCellValueAsString(row.getCell(6)));
                
                String telefono = getCellValueAsString(row.getCell(7));
                String fax = getCellValueAsString(row.getCell(8));
                String email = cleanSqlString(getCellValueAsString(row.getCell(9)));
                String sitoWeb = cleanSqlString(getCellValueAsString(row.getCell(10)));
                String partitaIva = getCellValueAsString(row.getCell(11));

                // Salta la riga se i dati fondamentali del vincolo sono nulli
                if (region.isEmpty() ) {
                    continue; 
                }

                // Generazione della query SQL (Sintassi PostgreSQL: ON CONFLICT)
                // Se usi MySQL, usa: ON DUPLICATE KEY UPDATE denominazione_azienda = VALUES(denominazione_azienda), ...
                String sql = String.format(
                    "INSERT INTO asl (codice_azienda, denominazione_azienda, city_id, region_id, indirizzo, cap, telefono, fax, email, sito_web, partita_iva) " +
                    "VALUES ('%s', '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s) " +
                    "ON CONFLICT (region_id, codice_azienda) DO UPDATE SET " +
                    "denominazione_azienda = EXCLUDED.denominazione_azienda, " +
                    "city_id = EXCLUDED.city_id, " +
                    "indirizzo = EXCLUDED.indirizzo, " +
                    "cap = EXCLUDED.cap, " +
                    "telefono = EXCLUDED.telefono, " +
                    "fax = EXCLUDED.fax, " +
                    "email = EXCLUDED.email, " +
                    "sito_web = EXCLUDED.sito_web, " +
                    "partita_iva = EXCLUDED.partita_iva;\n",
                    
                    codiceAzienda, denominazioneAzienda, 
                    cityId, regionId,
                    formatNullableString(indirizzo), formatNullableString(cap),
                    formatNullableString(telefono), formatNullableString(fax),
                    formatNullableString(email), formatNullableString(sitoWeb),
                    formatNullableString(partitaIva)
                );

                fw.write(sql);
            }
        }
    }

    // Helper per estrarre il testo da qualsiasi tipo di cella
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: 
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                // Rimuove i decimali superflui se è un intero letto come double
                double num = cell.getNumericCellValue();
                if (num == (long) num) return String.valueOf((long) num);
                return String.valueOf(num);
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    // Helper per estrarre ID numerici (Fk)
    private static Long getCellValueAsLong(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
        return (long) cell.getNumericCellValue();
    }

    // Gestisce i valori nulli nell'inserimento SQL
    private static String formatNullableString(String val) {
        return (val == null || val.isEmpty()) ? "NULL" : "'" + val + "'";
    }

    // Evita SQL Injection rudimentali raddoppiando l'apice singolo (es: L'Aquila -> L''Aquila)
    private static String cleanSqlString(String str) {
        if (str == null) return null;
        return str.replace("'", "''");
    }
}