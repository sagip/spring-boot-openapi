### Összegzés

Az alkalmazás API first koncepció alkalmazásával lett megvalósítva Spring Tool Suite környezetben openapi-generator-maven-plugin segítségével. Az interfész specifikáció itt található: "spring-boot-openapi\specification\src\main\resources\openapi.yml"

### Telepítés
A spring-boot-openapi könyvtárban futtatott "mvn install" parancs:
  - Specification projekt-ben generálja az interfészeket, json objektumokat és kontrollert
  - Specification és App projektben végrehajtja a szükséges fordításokat és definiált teszteket

### Alkalmazás futtatása (Windows)
  -  ".\spring-boot-openapi\app\target\java -jar app-0.0.1-SNAPSHOT.jar"
  - adott pozíció lekérdezése: curl localhost:8080/position/2
  - keresés kulcsszó és lokáció alapján: curl -X GET "http://localhost:8080/position/search?keyword=info&location=s&uuid=c58ea483-658e-4f74-8175-6c7057992a03"
  - új kliens létrehozása: curl -X POST http://localhost:8080/client -H "Content-Type: application/json" -d "{\"name\":\"Kiss Pista\",\"email\":\"pista.kiss@asdf.com\"}"
  - új pozíció létrehozása: curl -X POST http://localhost:8080/position -H "Content-Type: application/json" -d "{\"name\":\"Kamionos\",\"location\":\"Baja\", \"uuid\":\"c58ea483-658e-4f74-8175-6c7057992a03\"}"

### Production readiness feladatok
  - követelmények pontosítása:
    - várhatóan több kliens és állás attribútumot kell tárolni/validálni
	- tervezett adatmennyiség és adatkezelés (gdpr, stb.)
	- meglévő adatbázis használata h2 in memory helyett adatvesztés elkerülésére
  - esetleges problémák egyeztetése:
    - pl. a követelmény nem korlátozza ugyan azon pozíció többszöri felvételét (ezt név/lokáció alapján limitáltam)
  - teljesülési kritériumok meghatározása és megvalósítása tesztesetekkel
  - meglévő infrastruktúrába integrálás (pl. docker)
  - további tesztesetek definiálása és megvalósítása

### Továbbfejlesztési lehetőségek
  - Az alkalmazást rugalmassá kell tenni a jövőben várható ill. környezeti változásokkal szemben, ezeket fel kell mérni:
    - pl: validációs szabályok konfigurálhatóvá tétele

