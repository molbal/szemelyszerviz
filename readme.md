# Személy mikroszerviz

## Projekt leírás
Ez a projekt egy példafeladat megoldása.

A projektnél adott volt az *SzemelyDTO* objektum, amiknek mezőit kell validálja ez az alkalmazás. 


## Projekt indítása
A projekt Maven függőségkezelőt használ, így a `mvn install` parancs kiadása letölti a szükséges függőségeket.

### Konfiguráció
A mikroszerviz következő paramétereit lehet konfigurációból állítani:

- `kodszotar.filenev` A megadott kódszótár JSON fájl elérési útvonala 
- `okmanyszerviz.wsroot` Okmányszerviz webservice root
- `nev.maxhossz` A nevek maximum hossza
- `nev.karakterek` A nevek ABC-jét tartalmazó reguláris kifejezés (TODO: A regex jelenleg nem tartalmazza az aposztróf karaktereket)

## Webszerviz leírás

### `szemely/ellenoriz`
Ez a végpont egy *SzemelyDTO* objektumot vár és ezt is ad vissza. Hiba esetén egy listát ad vissza
 
 
## Tesztelés
A program függ az Okmányszerviztől (https://github.com/molbal/okmanyszerviz). A tesztelés előtt azt is el kell indítani.
A tesztelés egyszerűsítépséhez Swagger UI használható, ami program indítása után a `http://localhost:8002/swagger-ui` útvonalon érhető el.

A feladat unit, integrációs és átvételi tesztek készítését nem igényelte, ezért ezek elkészítésére nem került sor.