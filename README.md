# Minecraft.exe - Forge 1.12.2 mod

Ez a repository egy egyszerű Forge 1.12.2 mod vázat tartalmaz, amit a `feature/init-mod` branch-re helyeztem.

Mi van benne:
- Gradle build fájl (ForgeGradle) 1.12.2-hez
- Alap mod osztály (MainMod) mely regisztrál egy parancsot a szerver indításakor
- Egy egyszerű parancs: `/sayhi [név]` amely választ küld vissza (kompatibilis parancsblokk használattal)

Gyors indítás:
1. Clone a repo és válts a branch-re:
   git checkout feature/init-mod

2. Ha szeretnéd buildelni a modot, használd a Gradle wrapper-t vagy a telepített Gradle-t. (A Forge MDK futtatásához lehet, hogy szükséges a ForgeGradle további beállítása és wrapper telepítése.)

3. A parancs a szerver indulásakor kerül regisztrálásra, így a szerveren vagy parancsblokkban használható.

Ha szeretnéd, hozzáadok Gradle wrapper fájlokat és további példa tartalmat (blokkok, itemek, konfig), illetve beállítom a fejlesztői környezethez (IDE) szükséges feladatokat.
