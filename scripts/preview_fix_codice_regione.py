import unicodedata
from pathlib import Path

csv_path = Path(__file__).resolve().parents[0] / '..' / 'src' / 'main' / 'resources' / 'Dati_di_anagrafe_e_di_attività_delle_Strutture_di_Ricovero_Pubbliche_ed_equiparate.csv'
csv_path = csv_path.resolve()

def normalize(s):
    if s is None:
        return ''
    s = s.strip().upper()
    s = ''.join(c for c in unicodedata.normalize('NFD', s) if unicodedata.category(c) != 'Mn')
    s = s.replace("'", "").replace('`','').replace('\n',' ').replace('\r',' ')
    s = s.replace('–','-').replace('—','-')
    return s

mapping = {
    'PIEMONTE': '01',
    'VALLE D\'AOSTA': '02',
    'VALLE D AOSTA': '02',
    'VALLE D\'AOSTA/VALL%C3%89E D\'AOSTE': '02',
    'VALLE D AOSTA/VALLÉE D AOSTE': '02',
    'LOMBARDIA': '03',
    'TRENTINO-ALTO ADIGE': '04',
    'TRENTINO ALTO ADIGE': '04',
    'VENETO': '05',
    'FRIULI-VENEZIA GIULIA': '06',
    'FRIULI VENEZIA GIULIA': '06',
    'LIGURIA': '07',
    'EMILIA-ROMAGNA': '08',
    'EMILIA ROMAGNA': '08',
    'TOSCANA': '09',
    'UMBRIA': '10',
    'MARCHE': '11',
    'LAZIO': '12',
    'ABRUZZO': '13',
    'MOLISE': '14',
    'CAMPANIA': '15',
    'PUGLIA': '16',
    'BASILICATA': '17',
    'CALABRIA': '18',
    'SICILIA': '19',
    'SARDEGNA': '20'
}

text = None
for enc in ('utf-8', 'cp1252', 'latin-1'):
    try:
        text = csv_path.read_text(encoding=enc)
        print(f'File letto con codifica: {enc}')
        break
    except Exception as e:
        # proviamo con la successiva codifica
        pass
if text is None:
    print('Impossibile leggere il file con le codifiche provate:', csv_path)
    raise SystemExit(1)

lines = text.splitlines()
if not lines:
    print('File vuoto o non trovato:', csv_path)
    raise SystemExit(1)

header = lines[0]
changes = []
unmapped = {}
for idx, line in enumerate(lines[1:], start=2):
    parts = line.split(';')
    if len(parts) < 3:
        continue
    orig_code = parts[1].strip()
    regione = parts[2]
    key = normalize(regione)
    new_code = None
    if key in mapping:
        new_code = mapping[key]
    else:
        # fallback: try to coerce the original codice_regione to 2-digit ISTAT
        if orig_code.isdigit():
            try:
                new_code = f"{int(orig_code):02d}"
            except:
                new_code = orig_code
        else:
            new_code = orig_code
        if key:
            unmapped.setdefault(key, 0)
            unmapped[key] += 1
    if new_code != orig_code:
        changes.append((idx, regione, orig_code, new_code))

print('CSV:', csv_path)
print('Header:', header)
print('\nTotale modifiche proposte:', len(changes))
print('\nPrime 20 modifiche proposte (linea, regione, codice_orig -> codice_nuovo):')
for c in changes[:20]:
    print(c[0], '-', c[1], '-', c[2], '->', c[3])

if unmapped:
    print('\nRegioni non mappate automaticamente (esempi e conteggi):')
    for k, v in list(unmapped.items())[:30]:
        print('-', k, ':', v)

print('\nN.B. Questo è solo un anteprima; nessuna modifica è stata salvata.')
print("Se approvi, posso applicare le modifiche e creare un backup del file CSV.")
