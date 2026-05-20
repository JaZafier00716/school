# Poznámky: datové formáty a vybraná webová API

## Datové formáty

### JSON

**JSON** (*JavaScript Object Notation*) je textový formát pro ukládání a přenos dat.  
Je podobný XML, ale bývá jednodušší, rychlejší a úspornější z hlediska velikosti dat.

Data jsou zapisována pomocí:

- objektů,
- polí,
- základních datových typů,
- dvojic **klíč–hodnota** (*key-value pairs*).

Syntaxe JSONu je podobná syntaxi jazyka JavaScript.

Výhody oproti XML:

- neobsahuje ukončovací tagy,
- má kratší zápis,
- rychleji se serializuje a deserializuje,
- obvykle zabírá méně paměti.

Příklad JSONu:

```json
{
  "name": "Jan Novák",
  "email": "jan.novak@example.com",
  "phones": ["+420123456789", "+420987654321"]
}
```

---

### XML

**XML** (*Extensible Markup Language*) je značkovací formát, ve kterém jsou data uložena pomocí vlastních tagů.

Je vhodný hlavně pro:

- hierarchická data,
- dobře popsaná data,
- dokumenty, kde je důležitá čitelnost a struktura.

Nevýhody oproti JSONu:

- delší zápis,
- větší velikost souborů,
- obvykle pomalejší zpracování.

Příklad XML:

```xml
<contact>
  <name>Jan Novák</name>
  <email>jan.novak@example.com</email>
</contact>
```

---

### CSV

**CSV** (*Comma-Separated Values*) je jednoduchý textový tabulkový formát.  
Hodnoty jsou oddělené oddělovačem, například:

- čárkou,
- středníkem,
- tabulátorem,
- jiným znakem.

CSV se hodí pro jednoduchá tabulková data, například seznam kontaktů.

Nevýhody CSV:

- nehodí se pro složitější datové struktury,
- neumí přirozeně reprezentovat hierarchii,
- více hodnot u jedné položky, například více telefonů u jednoho kontaktu, se zapisuje hůře.

CSV **není obecně založené na dvojicích klíč–hodnota**. Klíče bývají obvykle reprezentovány názvy sloupců v prvním řádku.

Příklad CSV:

```csv
name,email,phone
Jan Novák,jan.novak@example.com,+420123456789
Petra Svobodová,petra.svobodova@example.com,+420987654321
```

---

## Přehled vybraných HTML5 a webových API

| API / technologie | Popis |
|---|---|
| **HTML5 Fetch API** | Slouží pro síťové požadavky, typicky pro načítání dat ze serveru. |
| **HTML5 Canvas** | 2D rastrová grafika vykreslovaná pomocí JavaScriptu. |
| **HTML5 Inline SVG** | 2D vektorová grafika vložená přímo do HTML. |
| **WebGL** | 3D grafika vykreslovaná v canvasu pomocí GPU. |
| **HTML5 Geolocation** | Získání polohy zařízení. Vyžaduje oprávnění uživatele a často připojení k internetu. Nepracuje přímo se senzory jako orientační senzor nebo kompas. |
| **HTML5 Orientation** | Práce s orientací zařízení pomocí hodnot `x`, `y`, `z` nebo `alpha`, `beta`, `gamma`. |
| **HTML5 Multimedia** | Práce s audiem a videem. Neexistuje jeden audio/video formát podporovaný úplně všemi prohlížeči. |
| **HTML5 Media Capture** | Přístup ke kameře a mikrofonu. |
| **Vibration API** | Ovládání vibrací zařízení. |
| **Sensor API** | Přístup k senzorům, například gyroskopu, akcelerometru, magnetometru nebo senzoru světla. |
| **Battery API** | Informace o stavu baterie. |
| **HTML5 Widgets** | Uživatelské prvky a komponenty webových aplikací. |
| **Indexed Database API** | Klientská databáze v prohlížeči pro ukládání strukturovaných dat. |
| **File API / File System Access API** | Práce se soubory na straně klienta. File System Access API obvykle vyžaduje HTTPS. |
| **Web Audio API** | Pokročilé zpracování a generování zvuku v prohlížeči. |
| **Service Worker** | Skript běžící na pozadí, například pro offline režim, cache nebo push notifikace. |
| **WebSocket API** | Obousměrná komunikace mezi klientem a serverem v reálném čase. |
| **Web Storage API** | Ukládání dat v prohlížeči pomocí `localStorage` a `sessionStorage`. |
| **Web Animations API** | Tvorba animací pomocí JavaScriptu. |
| **Pointer Events API** | Sjednocené zpracování vstupu z myši, dotyku a stylusu. |
| **Push API** | Příjem push zpráv ze serveru. |
| **Notifications API** | Zobrazování systémových notifikací. |
| **Web Authentication API / WebAuthn** | Bezpečné přihlašování pomocí veřejného klíče, například pomocí biometrie nebo bezpečnostních klíčů. |

---

## Canvas API

Canvas slouží pro kreslení 2D rastrové grafiky pomocí JavaScriptu.

### Vlastnosti kontextu

```js
context.lineWidth = value;
```

Tloušťka čáry. Hodnota je v rozsahu `[0..]`.

```js
context.textAlign = "start"; 
```

Zarovnání textu.

Možné hodnoty:

- `"start"`
- `"end"`
- `"left"`
- `"right"`
- `"center"`

```js
context.textBaseline = "alphabetic";
```

Základní linka textu.

Možné hodnoty:

- `"top"`
- `"middle"`
- `"bottom"`
- `"hanging"`
- `"alphabetic"`

```js
context.font = "italic 600 12px Droid Sans, sans-serif";
```

Nastavení fontu.

---

### Transformace

```js
context.scale(scaleX, scaleY);
context.translate(dx, dy);
context.rotate(angle);
```

| Metoda | Popis |
|---|---|
| `scale(scaleX, scaleY)` | Změní měřítko vykreslování. |
| `translate(dx, dy)` | Posune souřadnicový systém. |
| `rotate(angle)` | Otočí souřadnicový systém o zadaný úhel. |

---

### Gradienty

```js
context.createLinearGradient(x0, y0, x1, y1);
context.createRadialGradient(x0, y0, r0, x1, y1, r1);
```

| Metoda | Popis |
|---|---|
| `createLinearGradient(x0, y0, x1, y1)` | Vytvoří lineární gradient. |
| `createRadialGradient(x0, y0, r0, x1, y1, r1)` | Vytvoří radiální gradient. |

---

### Text

```js
context.fillText(text, x, y, maxWidth);
context.strokeText(text, x, y, maxWidth);
```

| Metoda | Popis |
|---|---|
| `fillText(text, x, y, maxWidth)` | Vykreslí vyplněný text. |
| `strokeText(text, x, y, maxWidth)` | Vykreslí obrys textu. |

Parametr `maxWidth` je volitelný.

---

### Obdélníky

```js
context.fillRect(x, y, width, height);
context.strokeRect(x, y, width, height);
```

| Metoda | Popis |
|---|---|
| `fillRect(x, y, width, height)` | Vykreslí vyplněný obdélník. |
| `strokeRect(x, y, width, height)` | Vykreslí obrys obdélníku. |

---

### Cesty

```js
context.beginPath();
context.moveTo(x, y);
context.lineTo(x, y);
context.rect(x, y, width, height);
context.arc(x, y, radius, startAngle, endAngle, anticlockwise);
context.closePath();
context.fill();
context.stroke();
```

| Metoda | Popis |
|---|---|
| `beginPath()` | Začne novou cestu. |
| `moveTo(x, y)` | Přesune kurzor na zadanou pozici bez kreslení. |
| `lineTo(x, y)` | Nakreslí čáru do zadaného bodu. |
| `rect(x, y, width, height)` | Přidá do cesty obdélník. |
| `arc(x, y, radius, startAngle, endAngle, anticlockwise)` | Přidá oblouk nebo kružnici. |
| `closePath()` | Spojí začátek a konec cesty. |
| `fill()` | Vyplní aktuální cestu. |
| `stroke()` | Vykreslí obrys aktuální cesty. |

Parametr `anticlockwise` je volitelný.

---

## Časovače v JavaScriptu

### Jednorázová úloha

```js
const id = window.setTimeout(functionName, timeoutMs);
```

Příklad:

```js
setTimeout(onetime, 8000); // spuštění funkce po 8 sekundách
```

Zrušení časovače:

```js
window.clearTimeout(id);
```

---

### Opakovaná úloha s pevnou periodou

```js
const id = window.setInterval(functionName, intervalMs);
```

Příklad:

```js
setInterval(repaint, 2000); // spuštění funkce repaint každé 2 sekundy
```

Zastavení časovače:

```js
window.clearInterval(id);
```

---

## Geolocation API

Ukázka získání aktuální polohy:

```js
function getLocation() {
  if (navigator.geolocation !== undefined) {
    navigator.geolocation.getCurrentPosition(showPosition, failedPosition);
  }
}

function showPosition(pos) {
  x.value =
    pos.coords.latitude +
    " / " +
    pos.coords.longitude +
    " @ " +
    pos.coords.altitude +
    " m (+/- " +
    pos.coords.accuracy +
    " m)";
}

function failedPosition(err) {
  x.value = err.message + " (code: " + err.code + ")";
}
```

---

## IndexedDB API

### Kontrola podpory IndexedDB

```js
let idbSupported = false;

if ("indexedDB" in window) {
  idbSupported = true;
}
```

---

### Vytvoření databáze a objektového úložiště

```js
let db;

if (idbSupported) {
  const request = indexedDB.open("example", 1);

  request.onupgradeneeded = function (event) {
    const myDB = event.target.result;

    if (!myDB.objectStoreNames.contains("students")) {
      const objectStore = myDB.createObjectStore("students", {
        keyPath: "login",
      });

      objectStore.createIndex("name", "name");
      objectStore.createIndex("email", "email", { unique: true });
    }
  };

  request.onsuccess = function (event) {
    db = event.target.result;

    const transaction = db.transaction(["students"], "readwrite");
    const students = transaction.objectStore("students");

    students.add({
      name: "Jan Novák",
      email: "xxx9999@vsb.cz",
      login: "xxx999",
      gender: "M",
    });
  };

  request.onerror = function (event) {
    console.error("IndexedDB error:", event.target.error);
  };
}
```

Poznámka: V původním zápisu bylo `keypath`, správně má být `keyPath`.
