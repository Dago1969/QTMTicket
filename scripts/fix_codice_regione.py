import shutil
import unicodedata
from pathlib import Path


def normalize(s: str) -> str:
    if s is None:
        return ''
    s = s.strip().upper()
    s = ''.join(c for c in unicodedata.normalize('NFD', s) if unicodedata.category(c) != 'Mn')
    s = s.replace("'", "").replace('`', '').replace('\n', ' ').replace('\r', ' ')
    s = s.replace('–', '-').replace('—', '-')
    return s


csv_path = Path(__file__).resolve().parents[0] / '..' / 'src' / 'main' / 'resources' / 'Dati_di_anagrafe_e_di_attività_delle_Strutture_di_Ricovero_Pubbliche_ed_equiparate.csv'
csv_path = csv_path.resolve()
backup_path = csv_path.with_suffix('.csv.bak')

print('CSV path:', csv_path)
print('Backup path:', backup_path)

# Read file trying common encodings
text = None
used_enc = None
for enc in ('utf-8', 'cp1252', 'latin-1'):
    try:
        text = csv_path.read_text(encoding=enc)
        used_enc = enc
        break
    except Exception:
        continue

if text is None:
    print('Impossibile leggere il file con le codifiche provate')
    raise SystemExit(1)

shutil.copy2(csv_path, backup_path)
print('Backup creato')

lines = text.splitlines()
if not lines:
    print('File vuoto')
    raise SystemExit(1)

mapping = {
    'PIEMONTE': '01',
    "VALLE DAOSTA": '02',
    "VALLE D AOSTA": '02',
    "VALLE D'AOSTA": '02',
    'LOMBARDIA': '03',
    'TRENTINO-ALTO ADIGE': '04',
    'TRENTINO ALTO ADIGE': '04',
    'PROV AUTON BOLZANO': '04',
    'PROV AUTON TRENTO': '04',
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

header = lines[0]
new_lines = [header]
changed = 0
changes_sample = []
unmapped = {}

for idx, line in enumerate(lines[1:], start=2):
    parts = line.split(';')
    if len(parts) < 3:
        new_lines.append(line)
        continue
    orig_code = parts[1].strip()
    regione = parts[2]
    key = normalize(regione)
    new_code = None
    if key in mapping:
        new_code = mapping[key]
    else:
        # try flexible matches
        k2 = key.replace('.', '').replace('  ', ' ').replace('PROV ', '').strip()
        if k2 in mapping:
            new_code = mapping[k2]
        else:
            # fallback: coerce numeric original code
            if orig_code.isdigit():
                try:
                    new_code = f"{int(orig_code):02d}"
                except Exception:
                    new_code = orig_code
            else:
                new_code = orig_code
            unmapped.setdefault(key, 0)
            unmapped[key] += 1

    if new_code != orig_code:
        changed += 1
        if len(changes_sample) < 50:
            changes_sample.append((idx, regione, orig_code, new_code))
    parts[1] = new_code
    new_lines.append(';'.join(parts))

csv_path.write_text('\n'.join(new_lines) + '\n', encoding=used_enc or 'utf-8')

print(f'Aggiornate {changed} righe (campo codice_regione normalizzato).')
print('\nPrime modifiche (fino a 50 esempi):')
for c in changes_sample[:50]:
    print(c[0], '-', c[1], '-', c[2], '->', c[3])

if unmapped:
    print('\nRegioni non mappate automaticamente (esempi e conteggi):')
    for k, v in list(unmapped.items())[:50]:
        print('-', k, ':', v)

print('\nBackup salvato in:', backup_path)
print('File aggiornato con codifica:', used_enc)
