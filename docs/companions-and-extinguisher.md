# Azor, Morty, Gaśnica i Alternatywka

## Użycie

- Nazwij wilka **Azor** za pomocą znacznika: złota sierść i opadające uszy przypominające Golden Retrievera.
- Nazwij wilka **Morty**: ciemna sierść z podpalanym pyskiem, brwią, piersią i łapami, jak u niemieckiego teriera myśliwskiego.
- Wielkość liter ma znaczenie. Zmiana imienia na inne przywraca zwykły wygląd wilka. Oswajanie, obroża, siadanie, potrząsanie wodą i zachowanie wilka pozostają obsługiwane przez Minecrafta.
- **Gaśnica** znajduje się w zakładce Dobroszyce Casino. Przytrzymaj PPM, aby rozpylać biały proszek/dym na odległość do 6 bloków. Gasi trafiony ogień i płonące istoty; źródła lawy zamienia w obsydian, a płynącą lawę w bruk. Strumień zatrzymuje się na przeszkodach i pierwszym bloku lawy. Nie tworzy wody, działa również w Netherze.
- Receptura gaśnicy: ` IL / IRI /  I ` (I = sztabka żelaza, L = dźwignia, R = redstone). Ma 256 jednostek trwałości i zużywa jedną co 4 ticki rozpylania; w creative się nie zużywa.
- **Alternatywka** jest przyjaznym, nieatakującym mobem z 10 HP (5 serc). Jest trwała po przyzwaniu i nie znika przez oddalenie się gracza. Nie dodaje się samoczynnie do naturalnego spawnu.
- Jajko przyzywające jest w zakładce moda. Można też użyć `/summon dobroszycecasino:alternatywka`.
- Żywy gracz **JakubJanPajdzik** w tym samym świecie, w promieniu 5 bloków, przyciąga Alternatywkę. Mob podbiega normalną ścieżką, zatrzymuje się 1,5 bloku od niego i nie teleportuje się. Widzowie są pomijani.
- Przy śmierci losuje dokładnie **5%** szansy na jedną sztukę **Fish's Feet**, bez mnożnika Looting. Standardowe `doMobLoot` i zdarzenia lootowe Forge nadal obowiązują.

## Sprawdzenie w grze

1. Nadać obu dorosłym wilkom i szczeniakom imiona, oswoić je, zmienić kolor obroży, posadzić, zmoczyć, zmienić imię ponownie. Sprawdzić również zwykłego i rozgniewanego wilka.
2. Gaśnicą trafić ogień, płonącą istotę, źródło oraz strumień lawy. Sprawdzić blok za ścianą, dystans ponad 6 bloków, ochronę spawnu, anulowanie `BlockEvent.BreakEvent`, tryb przygodowy, Nether oraz wyczerpanie trwałości.
3. Przyzwać Alternatywkę jajkiem z creative, przedmiotem z `/give` i komendą `/summon`. Sprawdzić 10 HP oraz zapis/odczyt świata.
4. Sprawdzić podbieganie przy dystansach 4,9 / 5 / 5,1 bloku, zatrzymanie blisko gracza, przeszkodę na drodze, inny nick, spectator i rozłączenie gracza.
5. Zweryfikować drop na dużej próbce; 5% jest prawdopodobieństwem na śmierć, nie gwarancją jednej sztuki na każde 20 mobów.

## Walidacja w tej zmianie

Całe `src/main/java` i `src/test/java` skompilowano bezpośrednio kompilatorem Java 8 przeciwko zmapowanemu JAR-owi Minecraft/Forge 1.12.2 oraz jego zależnościom. Testy blackjacka, ruletki i sklepu przeszły (łącznie 3 744 782 sprawdzenia). Sprawdzono modele/recepturę JSON i rozmiary atlasów tekstur.

Pełny `gradle build` zablokowało pobieranie ForgeGradle w środowisku wykonawczym. Nie przeprowadzono testu w uruchomionym kliencie ani na serwerze; powyższa lista pozostaje do weryfikacji w grze. Nie utworzono nowego produkcyjnego JAR-a.

Tekstury i bryły można odtworzyć poleceniem `python tools/generate_companion_assets.py` (Pillow). Generator używa współrzędnych UV modeli Minecrafta; nie modyfikuje starszych assetów sklepu.

## Watrouble: odporność na Poison i Wither

Noszony Watrouble blokuje nałożenie obu efektów i usuwa już aktywne efekty przed ich tickiem. Ochrona działa jako amulet Baubles albo w drugiej ręce, jeżeli Baubles nie jest zainstalowane. Nie wymaga odnowienia absorpcji. Po zdjęciu amuletu można ponownie otrzymać oba efekty. Pozostałe efekty, w tym obrażenia magiczne niezwiązane z nimi, nie są blokowane.

Do sprawdzenia w grze: trucizna/wither przed założeniem, próby nałożenia obu efektów podczas noszenia (różne poziomy), ponowne nałożenie po zdjęciu, zachowanie innych efektów oraz założenie podczas cooldownu absorpcji. Powtórzyć z Baubles i bez niego.

Propozycje nowych ikon Amnesii, Watrouble i karpia przedstawiono osobno; dotychczasowe ikony w zasobach gry nie są podmieniane w tym PR.
