# Dobroszyce Casino 1.2.0

Minecraft Java 1.12.2 • Forge 14.23.5.2860 • Java 8 (64-bit)

W paczce: gotowy JAR, projekt Gradle ze źródłami, instrukcja i opis testów.

## Instalacja
1. Zamknij Minecrafta.
2. CurseForge > profil RLCraft > trzy kropki > Open Folder > mods.
3. Skopiuj dobroszyce-casino-1.12.2-1.2.0.jar do mods.
4. Na serwerze umieść TEN SAM plik w mods serwera i u każdego gracza.
5. Uruchom profil Minecraft 1.12.2 / Forge 14.23.5.2860.

Przy aktualizacji zastąp poprzednią wersję JAR kasyna. Zachowaj swój mod z jedzeniem.
Zgodność z całą paczką RLCraft nie została przetestowana.

## Szybki test
W świecie z komendami lub jako operator serwera:

/give @p dobroszycecasino:blackjack_table 1
/casinochips TWOJ_NICK 1000

Postaw stół na płaskim podłożu. Zostaw co najmniej dwa bloki wysokości i wolne
miejsce wokół stołu. Krupier pojawi się automatycznie za stołem (maksymalnie 5 s).
To własna postać o modelu villagera, z ciemnym strojem i czerwoną muszką.
Krupier nie wędruje i nie otwiera zwykłego handlu villagerów.

Kliknij prawym przyciskiem stół lub krupiera. Każdy z maksymalnie 4 graczy
powinien otworzyć TEN SAM stół. Każdy zajmuje osobne miejsce w interfejsie.
Nie ma animacji siadania na krześle; gracz stoi obok bloku i korzysta z GUI.

## Żetony i wymiana
Nominały: 5 (niebieski), 25 (zielony), 50 (czerwony), 100 (ciemny), 200 (pomarańczowy), 500 (fioletowy).
Przy stole: Kup 100 pobiera 1 emerald; Sprzedaj 100 oddaje 1 emerald.
Przyciski wymiany działają podczas obstawiania.
Żetony są przedmiotami w głównym ekwipunku gracza; przedmioty w drugiej ręce
nie są liczone do salda. Przenieś je do zwykłego ekwipunku.

Receptury bezkształtne:
5 × 5 <-> 1 × 25 (łączenie wymaga crafting table)
2 × 25 <-> 1 × 50
2 × 50 <-> 1 × 100
2 × 100 <-> 1 × 200
5 × 100 <-> 1 × 500 (połączenie wymaga większej siatki craftingu)

Wszystkie receptury rozmieniania zachowują sumę wartości.
Komenda operatora /casinochips <gracz> <wartosc> przyznaje wartość, a nie liczbę
sztuk konkretnego żetonu. Wartość musi być wielokrotnością 5, od 5 do 1000000.
Stół obsługuje bank o nieograniczonych rezerwach żetonów, bez zewnętrznego moda ekonomii.

## Receptura stołu
Górny rząd: papier, papier, papier.
Środkowy: zielona wełna, skrzynia, zielona wełna.
Dolny: dębowy płotek, puste, dębowy płotek.

## Zasady i obsługa
- Jedna świeżo tasowana talia 52 kart na rozdanie, wspólna dla wszystkich.
- As = 1 lub 11. J/Q/K = 10. Blackjack = as i karta o wartości 10 w pierwszych 2 kartach.
- Krupier dobiera poniżej 17; zatrzymuje się na 17, także miękkim (np. as + 6).
- Stawka początkowa 50–5000, co 50. +/-500 pozwala szybciej wybrać kwotę.
- Stawka jest pobierana podczas jej zwiększania; obniżenie zwraca różnicę.
- Gotowy blokuje własną stawkę; ponowne kliknięcie przed startem ją odblokowuje.
- Gdy wszyscy zajmujący miejsca mają stawkę i są gotowi, rozdanie zaczyna się od razu.
- W przeciwnym razie rozdanie zaczyna się 20 s po pierwszej stawce.
  Gracze ze stawką 0 pomijają rozdanie.
- Dobierz: kolejna karta. Pas: zakończenie własnej tury.
- Podwój: dopłata pierwotnej stawki, jedna karta i koniec tury; tylko przy 2 kartach.
- Każda tura gracza trwa maksymalnie 30 s, po czym następuje automatyczny pas.
- Krupier odkrywa zakrytą kartę po turach graczy i dobiera z odstępem 1 s.
- Zwykła wygrana: zwrot 2 × stawka (zysk 1:1).
- Blackjack: zwrot 2,5 × stawka (zysk 3:2).
- Remis: zwrot stawki. Przegrana: brak zwrotu.
- Wyniki są pokazywane przez 8 s, następnie można obstawiać kolejny raz.
- Cyfra przy wyniku miejsca pokazuje ZYSK/STRATĘ netto, nie całkowity zwrot.
- Oznaczenia kolorów kart: C = trefl, D = karo, H = kier, S = pik.
- Ta wersja nie zawiera splitu, ubezpieczenia ani surrender.

## Blackjack: multiplayer, zamknięcie okna i zapis
Obsługa kart, losowanie, weryfikacja tur, pobieranie stawek i wypłaty odbywają się
na głównym wątku serwera. Klient wysyła tylko identyfikator wybranej czynności.
Zakryta karta krupiera i pozostała talia nie są wysyłane w podglądzie GUI ani NBT chunka.

Zamknięcie okna podczas obstawiania zwraca własną stawkę i zwalnia miejsce.
Zamknięcie podczas rozdania oznacza pas; wynik nadal zostaje rozliczony.
Rozłączenie, śmierć lub odejście poza zasięg 8 bloków nie blokują stołu.
Wypłaty dla nieobecnych graczy są zapisane i odbierane po ponownym zalogowaniu.
Przy pełnym ekwipunku reszta żetonów wypada obok gracza z przypisanym właścicielem.

Runda (karty, talia, stawki i czas) jest zapisywana w NBT stołu.
Gdy chunk nie jest załadowany, zegar rundy nie biegnie. Po załadowaniu runda trwa dalej.
Normalny zapis i restart serwera zachowują dane; po restarcie nieobecni przy GUI
gracze wykonują automatyczny pas, a niewykorzystane stawki z fazy obstawiania wracają.
Jak w innych modach opartych na zapisie świata, twarda awaria przed zapisem może
cofnąć ostatnie zmiany ekwipunku lub świata — nie jest to transakcyjny system bankowy.

W survivalu stół jest zablokowany przed wykopaniem, dopóki są na nim stawki lub
trwa runda. Usunięcie przez administratora zwraca nierozliczone stawki.

## Sklepikarz i przedmioty specjalne (1.2.0)

Szczegółowa konfiguracja: [docs/SHOP_PL.md](docs/SHOP_PL.md).
Przykładowy plik: [config-examples/dobroszycecasino-shop.json](config-examples/dobroszycecasino-shop.json).

Operator w trybie Creative otrzymuje przedmiot do ustawiania NPC:
```mcfunction
/give @p dobroszycecasino:shopkeeper_placer 1
```
Kliknij podłoże prawym przyciskiem. Sklepikarz nie chodzi, nie znika i nie przyjmuje
obrażeń. PPM otwiera sklep. Shift+PPM przez operatora w Creative usuwa NPC.
Sklep używa zwykłych żetonów w ekwipunku; wydaje resztę, także w nominale 5.

| Przedmiot | Działanie |
| --- | --- |
| Fish's Feet | Buty o parametrach diamentowych; chodzenie po wodzie. Shift umożliwia zanurzenie. |
| Banana Special | Mikstura Siły II na 30 sekund; po wypiciu oddaje pustą butelkę. |
| Daniel's 4-hand Club | PPM lub trafienie LPM: pełne obrażenia wszystkim widocznym celom w promieniu 3 bloków. Koszt 0,5 HP użytkownika, odstęp 1 s. |
| Sister's device | PPM: chmura dymu do 6 bloków przed graczem, promień 3 bloki, czas 6 s. Bez obrażeń i oślepienia; odstęp 4 s. |
| Malboro Red | Przytrzymanie PPM zużywa jedną sztukę; Szybkość I przez 30 sekund. |

Maczuga ma bazowe obrażenia diamentowego miecza (7); każdy cel otrzymuje pełny
atak z uwzględnieniem jego pancerza i odporności. Uwzględnia ustawienia PvP i drużyn.
Koszt własny 0,5 HP to ćwierć serduszka i omija pancerz; Creative jest zwolniony.
Maczuga ma 500 użyć, urządzenie dymne 128. Specjalne przedmioty nie mają receptur:
pochodzą ze sklepu lub komend/Creative. NPC stawia wyłącznie operator w Creative.

Oferty są domyślnie wyłączone do czasu ustawienia cen przez administratora.
Na serwerze edytuj `config/dobroszycecasino-shop.json`, następnie `/casinoshop reload`.
Restart nie jest potrzebny. Przeładowanie zamyka otwarte sklepy, aby gracz nie kupił
produktu po cenie innej niż ta, którą widział przed zmianą.

## Ruletka 3 × 2 bloki

```mcfunction
/give @p dobroszycecasino:roulette_table 1
```
Stół zajmuje od klikniętego pola 3 bloki na wschód (+X) i 2 na południe (+Z).
Koło i kulka są widoczne w świecie. PPM otwiera zakłady; Esc zamyka ekran,
**zachowując zakłady**, dzięki czemu można oglądać losowanie obok stołu.

- Koło europejskie 0–36; do 4 obstawiających, pozostali mogą oglądać.
- Wszystkie nominały: 5, 25, 50, 100, 200, 500; limit 5000 na osobę/rundę.
- 20 s na zakłady od pierwszej stawki, 12 s obrotu, 8 s prezentacji wyniku.
- Numer: zwrot 36× stawka; kolor/parzystość/połowa: 2×; tuzin/kolumna: 3×.
- Zero przegrywa zakłady grupowe. Zwroty zawierają pierwotną stawkę.
- Cofnij i Anuluj zwracają zakłady tylko przed rozpoczęciem obrotu.
- Stan rundy zapisuje się ze światem; oczekujące wypłaty wracają po zalogowaniu.
- Receptura: złoto/redstone/złoto; papier/papier/papier; deski/puste/deski.

## Edycja w IntelliJ IDEA
Otwórz folder casino-mod zawierający build.gradle.
Project SDK: JDK 8; Gradle JVM: JDK 8; dystrybucja Gradle: Wrapper (4.9).
Kliknij Reload All Gradle Projects.
Run > Edit Configurations > + > Gradle:
- nazwa: Casino Minecraft
- projekt: casino-mod
- Tasks / Run: runClient

Pierwsze uruchomienie pobiera biblioteki i zasoby Minecrafta.
Opcjonalnie uruchom genIntellijRuns, aby wygenerować konfiguracje Run/Debug.
Nie aktualizuj automatycznie Gradle do nowej głównej wersji.

Budowanie: zadanie build. Wynik: build/libs/dobroszyce-casino-1.12.2-1.2.0.jar.
Testy: logicTest, rouletteTest, shopTest (wszystkie wykonywane przez build).
W terminalu Windows, przy JAVA_HOME wskazującym JDK 8:
.\gradlew.bat build
.\gradlew.bat runClient

## Główne pliki
BlackjackRound.java: zasady i talia; czysta Java, testowana bez uruchamiania Minecrafta.
TileBlackjack.java: miejsca, stawki, timery, zapis rundy i rozliczenia.
CasinoContainer.java / Snapshot.java: akcje i synchronizacja widoku z serwerem.
GuiCasino.java: ekran stołu.
Chips.java / PayoutLedger.java: żetony, wymiana i oczekujące wypłaty.
EntityDealer.java / ClientProxy.java: krupier i renderer.
assets/dobroszycecasino/: modele, tekstury i receptury.

## Zakres sprawdzenia wydania
- Kompilacja i reobfJar dla dokładnie Forge 14.23.5.2860: poprawne.
- Testy reguł: 10000 symulowanych rozdań z czterema graczami; 193030 sprawdzeń.
- Testy obejmują asy, naturals, remisy, bust, double, soft 17, obce tury i brak powtórzeń kart.
- Sprawdzono JSON-y, wartość receptur żetonów i zawartość archiwum.
- Waluta i ceny: 1400205 sprawdzeń rozmieniania oraz poprawnej/błędnej konfiguracji JSON.
- Ruletka: wszystkie zakłady i wyniki dla stawek 5–5000 co 5 oraz 18500 końcowych pozycji kulki.
- Test uruchomionego klienta i serwera zintegrowanego: GUI i zakup butów, pobranie
  205 żetonów, blokada powtórnego żądania, zakotwiczenie/zapis NPC, chodzenie po wodzie,
  zanurzanie, lądowanie na wodzie, czas/poziom mikstur, obrażenia dwóch celów, koszt
  własny maczugi, cooldown i utworzenie chmury dymu — poprawne.
- Testowa konfiguracja cen nie jest dołączona do wydania; przykład ma wyłączone oferty.
- NIE wykonano testu czterech oddzielnych klientów ani całej paczki RLCraft.
- Lokalne środowisko testowe wymagało zastąpienia wadliwej klasy Side w cache
  ForgeGradle oficjalną wersją Forge (błąd dodatkowej wartości BUKKIT).
  Nie jest to część JAR-a moda.

Przed użyciem na głównym świecie przetestuj na osobnym świecie z 2–4 klientami:
1. Cztery miejsca i odmowa wejścia piątej osoby.
2. Stawki, kolejność tur, wyniki i stan żetonów przed/po.
3. Wyjście i ponowne wejście gracza w środku rundy.
4. Zamknięcie okna w czasie obstawiania oraz przekroczenie czasu tury.
5. Zapis i restart w środku rozdania, następnie odbiór wypłaty.
