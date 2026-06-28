from pathlib import Path
import csv
path = Path('src/main/resources/Elenco-comuni-italiani.csv')
out = Path('src/main/resources/db/changelog/geodata-cleaned.csv')
with path.open('r', encoding='latin-1', newline='') as f:
    reader = csv.reader(f, delimiter=';', quotechar='"')
    rows = list(reader)
print('ROWS', len(rows))
print('HEADER', rows[0])
print('FIRST', rows[1][:10])
with out.open('w', encoding='utf-8', newline='') as f:
    writer = csv.writer(f, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
    writer.writerow(['codice_regione','codice_unita_territoriale','codice_provincia_storico','progressivo_comune','codice_comune_alfanumerico','denominazione','denominazione_italiana','denominazione_altra_lingua','codice_ripartizione_geografica','ripartizione_geografica','denominazione_regione','denominazione_unita_territoriale','tipologia_unita_territoriale','flag_comune_capoluogo','sigla','codice_comune_numerico','codice_comune_numerico_110','codice_comune_numerico_107','codice_comune_numerico_103','codice_catastale_comune','codice_nuts1_2021','codice_nuts2_2021','codice_nuts3_2021','codice_nuts1_2024','codice_nuts2_2024','codice_nuts3_2024'])
    for row in rows[1:]:
        writer.writerow(row)
print('WRITTEN', out)
