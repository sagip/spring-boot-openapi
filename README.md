# Related Blog Posts

* [API-First Development with Spring Boot and Swagger](https://reflectoring.io/spring-boot-openapi/)


Az alkalmazás API first koncepció alkalmazásával lett megvalósítva Spring Tool Suite környezetben openapi-generator-maven-plugin segítségével. 

Az interfész specifikáció itt található: "spring-boot-openapi\specification\src\main\resources\openapi.yml"

A spring-boot-openapi könyvtárban futtatott "mvn install" parancs:
  - Specification projekt-ben generálja az interfészeket, json objektumokat és kontrollert
  - Specification és App projektben végrehajtja a szükséges fordításokat és definiált teszteket

Alkalmazás futtatás a projekt könyvtárban:
  -  ".\spring-boot-openapi\app\target\java -jar app-0.0.1-SNAPSHOT.jar"

Production readiness feladatok
  - követelmények pontosítása:
    - várhatóan több kliens és állás attribútumot kell tárolni/validálni
	- tervezett adatmennyiség és adatkezelés (gdpr, stb.)
	- meglévő adatbázis használata h2 in memory helyett adatvesztés elkerülésére
  - esetleges problémák egyeztetése:
    - pl. a követelmény nem korlátozza ugyan azon pozíció többszöri felvételét (ezt név/lokáció alapján limitáltam)
  - teljesülési kritériumok meghatározása és megvalósítása tesztesetekkel
  - meglévő infrastruktúrába integrálás (pl. docker)
  - további tesztesetek definiálása és megvalósítása

Továbbfejlesztési lehetőségek:
  - Az alkalmazást rugalmassá kell tenni a jövőben várható ill. környezeti változásokkal szemben, ezeket fel kell mérni:
    - pl: validációs szabályok konfigurálhatóvá tétele

