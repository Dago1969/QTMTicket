package com.qtm.ticket.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generatore SQL per gli OSPEDALI a partire dal file CSV fornito.
 * Produce `src/main/resources/import_hospitals.sql` con upsert per
 * `hospital_type` e `hospital` associando l'ASL tramite codice_azienda + codice_regione.
 */
public class ExcelToSqlGeneratorHOSPITAL {

    private static final Map<String, String> REGION_MAP = new HashMap<>();

    static {
        REGION_MAP.put("PIEMONTE", "01");
        REGION_MAP.put("VALLE D'AOSTA", "02");
        REGION_MAP.put("VALLEE D'AOSTE", "02");
        REGION_MAP.put("LOMBARDIA", "03");
        REGION_MAP.put("TRENTINO-ALTO ADIGE", "04");
        REGION_MAP.put("PROV. AUTON. TRENTO", "04");
        REGION_MAP.put("PROV. AUTON. BOLZANO", "04");
        REGION_MAP.put("VENETO", "05");
        REGION_MAP.put("FRIULI VENEZIA GIULIA", "06");
        REGION_MAP.put("LIGURIA", "07");
        REGION_MAP.put("EMILIA ROMAGNA", "08");
        REGION_MAP.put("TOSCANA", "09");
        REGION_MAP.put("UMBRIA", "10");
        REGION_MAP.put("MARCHE", "11");
        REGION_MAP.put("LAZIO", "12");
        REGION_MAP.put("ABRUZZO", "13");
        REGION_MAP.put("MOLISE", "14");
        REGION_MAP.put("CAMPANIA", "15");
        REGION_MAP.put("PUGLIA", "16");
        REGION_MAP.put("BASILICATA", "17");
        REGION_MAP.put("CALABRIA", "18");
        REGION_MAP.put("SICILIA", "19");
        REGION_MAP.put("SARDEGNA", "20");
    }

    public static void main(String[] args) {
        String csvPath = "src/main/resources/Dati_di_anagrafe_e_di_attività_delle_Strutture_di_Ricovero_Pubbliche_ed_equiparate.csv";
        String outputPath = "src/main/resources/import_hospitals.sql";
        try {
            generateSqlFromCsv(csvPath, outputPath);
            System.out.println("Script SQL ospedali generato in: " + outputPath);
        } catch (IOException e) {
            System.err.println("Errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void generateSqlFromCsv(String csvPath, String outputPath) throws IOException {
        File f = new File(csvPath);
        if (!f.exists()) throw new IOException("File non trovato: " + csvPath);

        try (BufferedReader br = new BufferedReader(new FileReader(f)); FileWriter fw = new FileWriter(outputPath)) {
            String header = br.readLine(); // salta intestazione
            if (header == null) return;

            String line;
            // keep track of hospital_type codes already emitted to avoid duplicates
            java.util.Set<String> emittedTypes = new java.util.HashSet<>();

            while ((line = br.readLine()) != null) {
                // split su ; rispettando il formato fornito
                String[] cols = line.split(";", -1);
                if (cols.length < 13) continue; // riga malformata (ci aspettiamo anche comune e sigla_provincia)

                // anno present in CSV but not stored in DB
                // String anno = safe(cols[0]);
                String csvCodiceRegione = safe(cols[1]);
                String regione = safe(cols[2]);
                String codiceAsl = safe(cols[3]);
                String codiceStruttura = safe(cols[5]);
                String struttura = clean(cols[6]);
                String indirizzo = clean(cols[7]);
                String comune = clean(cols[11]);
                String siglaProvincia = safe(cols[12]);
                String codiceTipoStruttura = safe(cols[8]);
                String tipoStruttura = clean(cols[10]);

                String codiceRegione = deriveRegionCode(csvCodiceRegione, regione);

                // upsert hospital_type — emit once per code to avoid repeated identical statements
                if ((!isEmpty(codiceTipoStruttura) || !isEmpty(tipoStruttura)) && !emittedTypes.contains(codiceTipoStruttura)) {
                    String sqlType = String.format(
                            "INSERT INTO hospital_type (code, description) VALUES ('%s', '%s') ON DUPLICATE KEY UPDATE description = VALUES(description);\n",
                            escape(codiceTipoStruttura), escape(tipoStruttura)
                    );
                    fw.write(sqlType);
                    emittedTypes.add(codiceTipoStruttura);
                }

                // prepare subqueries
                String hospitalTypeSub = isEmpty(codiceTipoStruttura) ? "NULL" : String.format("(SELECT id FROM hospital_type WHERE code = '%s')", escape(codiceTipoStruttura));
                String aslSub = isEmpty(codiceAsl) ? "NULL" : String.format("(SELECT id FROM asl WHERE codice_azienda = '%s' AND codice_regione = '%s')", escape(codiceAsl), escape(codiceRegione));

                // upsert hospital
                String sqlHospital2 = buildHospitalUpsert(codiceRegione, codiceAsl, codiceStruttura, struttura, indirizzo, comune, siglaProvincia, hospitalTypeSub, aslSub);
                fw.write(sqlHospital2);
            }
        }
    }

    private static String buildHospitalUpsert(String codiceRegione, String codiceAsl, String codiceStruttura, String struttura, String indirizzo, String comune, String siglaProvincia, String hospitalTypeSub, String aslSub) {
        String sStruttura = isEmpty(struttura) ? "NULL" : "'" + escapeForSql(struttura) + "'";
        String sIndirizzo = isEmpty(indirizzo) ? "NULL" : "'" + escape(indirizzo) + "'";
        String citySub = "NULL";
        if (!isEmpty(comune) && !isEmpty(siglaProvincia)) {
            String escComune = escapeForSql(comune).toLowerCase();
            String escSigla = escape(siglaProvincia).toLowerCase();
            citySub = String.format("(SELECT c.id FROM cities c JOIN provinces p ON p.id = c.province_id WHERE LOWER(TRIM(c.name)) = '%s' AND (LOWER(TRIM(p.sigla)) = '%s' OR LOWER(TRIM(p.name)) LIKE '%%%s%%') LIMIT 1)", escComune, escSigla, escSigla);
        }

        String sql = String.format(
                "INSERT INTO hospital (codice_regione, codice_asl, codice_struttura, struttura, indirizzo, hospital_type_id, asl_id, city_id) VALUES ('%s','%s','%s',%s,%s,%s,%s,%s) ON DUPLICATE KEY UPDATE codice_regione = VALUES(codice_regione), struttura = VALUES(struttura), indirizzo = VALUES(indirizzo), hospital_type_id = VALUES(hospital_type_id), asl_id = VALUES(asl_id), city_id = VALUES(city_id);\n",
                escape(codiceRegione), escape(codiceAsl), escape(codiceStruttura), sStruttura, sIndirizzo, hospitalTypeSub, aslSub, citySub
        );
        return sql;
    }

    private static String deriveRegionCode(String csvCode, String regione) {
        if (!isEmpty(csvCode)) {
            // try to normalize to two-digit code
            String cleaned = csvCode.trim();
            if (cleaned.length() == 3 && cleaned.startsWith("0")) cleaned = cleaned.substring(1);
            if (cleaned.length() == 2) return cleaned;
            // if like 010 -> take last two
            if (cleaned.length() == 3) return cleaned.substring(1);
        }
        if (regione == null) return "";
        String r = regione.trim().toUpperCase();
        // try contains matches
        for (Map.Entry<String, String> e : REGION_MAP.entrySet()) {
            if (r.contains(e.getKey())) return e.getValue();
        }
        return "";
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static String clean(String s) {
        if (s == null) return "";
        return s.trim();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("'", "''");
    }

    private static String escapeForSql(String s) {
        return escape(s);
    }
}