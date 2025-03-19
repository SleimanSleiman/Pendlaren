# TraFi

TraFi är en applikation som låter användare ange två hållplatser och få en genererad spellista som varar ungefär lika länge som själva resan.

## Funktioner

- **Hitta resa:** Användaren kan söka efter resor mellan två valda hållplatser.
- **Generera spellista:** Baserat på restiden skapas en spellista med låtar som matchar resans längd.
- **Anpassad upplevelse:** Få information om band, låtnamn, och genrer från spellistan.

## Installation

Följ dessa steg för att installera och köra projektet:

Installationer:
Innan du börjar se till att följande är installerat
Java 17 (eller senare)
Maven
En textredigerare eller IDE (t.ex: Visual Studio Code, IntelliJ)
För klienten: En webbläsare

Serverinstruktioner:
Konfigurera API nyckeln
Öppna filen Server/src/main/ConfigAPI.java
Ersätt “YOUR_API_KEY” med din API nyckel från Trafiklab

Installera beroenden
Öppna terminalen i mappen Server och kör “mvn clean install”

Starta servern
Öppna projektet i en IDE (t.ex. IntelliJ, Eclipse eller VSCode)
Kör Main.java i Server/src/main/Main.java
Servern startar på localhost:7123

Klientinstruktioner:
Öppna klienten:
Öppna mappen Client i en valfri redigerare (t.ex VS Code) och navigera till index.html
Alternativt öppna Client/index.html direkt i en webbläsare genom att dubbelklicka på filen

API nyckel från Trafiklab:
Följ instruktionerna och skapa en API nyckel för ResRobot på: 
https://www.trafiklab.se/docs/getting-started/using-trafiklab/
Klistra in nyckeln i ConfigAPI.java (se Serverinstruktioner)


---

Du är nu redo att använda TraFi för att söka efter resor och generera spellistor!
