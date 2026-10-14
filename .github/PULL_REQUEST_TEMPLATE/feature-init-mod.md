# Feature: init-mod

Ez a pull request a feature/init-mod branch változtatásait hozza be a fő branchre (main). A PR célja egy egyszerű Forge 1.12.2 mod váz hozzáadása, amely egy regisztrált parancsot (/sayhi) biztosít, valamint alap build és meta fájlokat.

Változások röviden:
- README kibővítése magyar nyelvű leírással és gyors indítási útmutatóval
- build.gradle: Forge repo és processResources egyszerűsítése, Forge verzió beállítása
- settings.gradle: project név frissítve
- Új Java fájlok: MainMod.java (alap mod osztály), CommandHello.java (/sayhi parancs)
- mcmod.info frissítve a mod metaadataival

Javasolt teendők a PR elfogadása előtt:
1. Ellenőrizd a build.gradle Forge verzióját és mappings beállításokat (ha szükséges, frissítsd a kívánt Forge build-re).
2. Hozzáadás Gradle wrapper-hez és/ vagy CI (GitHub Actions) build workflow létrehozása.
3. Ellenőrizd a parancs jogosultsági szintjét és használatát különböző környezetekben (szerver, singleplayer, parancsblokk).

Ha jóváhagyod, létrehozom a Pull Requestet a feature/init-mod -> main irányba, ezzel bevezetve a vázat a fő branchbe.
