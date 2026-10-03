# Przedmioty legendarne — 1.3.0

W Creative otwórz zakładkę **Dobroszyce Casino** (ikona stołu). Zawiera oba stoły, wszystkie nominały żetonów, przedmioty specjalne, Karp George i przedmiot do stawiania sklepikarza. Przy wielu modach zakładka może być na kolejnej stronie zakładek.

Sklep ma 3 strony po maksymalnie 5 ofert. Przyciski `<` i `>` zmieniają stronę. Płatność i wydanie przedmiotu nadal kontroluje serwer.

| ID w domenie dobroszycecasino | Działanie |
|---|---|
| legendary_kfc_bucket | Uzupełnia głód do 20 i nasycenie, Absorpcja I 60 s, Regeneracja I 10 s. Osobny przedmiot, bez zależności od kfcmod. |
| white_monster | Pośpiech I, Szybkość I i Widzenie w ciemności I przez 60 s. |
| dzik_energy | Te same efekty co White Monster, osobna oferta. |
| karp_george | 1% szansy na dodatkowego karpia przy połowie ryby. Nie pojawia się przy śmieciach ani skarbach, nie jest ofertą sklepu. Szansa dotyczy połowów z tabeli ryb, a nie wszystkich rzutów wędki. |
| ceasars_brown_leggings | Podczas noszenia: niewidzialność i losowa próba teleportacji co 10–20 s, do 8 bloków w poziomie i 3 w pionie. Wybiera powietrze nad pełnym blokiem, w załadowanym obszarze i granicach świata. Brak odpowiedniego miejsca oznacza brak teleportu. Niewidzialność wygasa do 2 s po zdjęciu; vanilla nadal pokazuje pancerz. |
| amnesias_weed | Lewitacja I, Spowolnienie I i Ślepota I przez 10 s. To wyłącznie mechanika fikcyjnego przedmiotu. |
| red_bull_tabacco | Pośpiech I i Odporność I przez 60 s. |
| watrouble | Absorpcja I: dwa dodatkowe serca, okres 30 s. Nie uzupełnia tarczy co tick ani po szybkim zdjęciu i założeniu. Nie nadpisuje aktywnej absorpcji. |

Przedmioty zużywalne działają po zakończeniu używania (32 ticki), nie po samym kliknięciu. W Survival zużywa się jedna sztuka; w Creative pozostaje. Efekty stosuje serwer.

## Biżuteria

Jeśli zainstalowano Baubles dla 1.12.2, Watrouble pasuje do slotu **AMULET**. Integracja używa opcjonalnego capability Baubles i nie dołącza kopii jego API do JAR-a. Bez Baubles trzymaj Watrouble w drugiej ręce. Noszenie w zwykłym ekwipunku nie działa. Po zdjęciu już przyznana absorpcja wygasa naturalnie (do 30 s). Pełna paczka RLCraft i wyposażenie Baubles wymagają osobnego testu zgodności.

## Crafting Fish's Feet

Dwa Karp George nad dwiema skórami, z pustą środkową kolumną (stół rzemieślniczy):

```text
F F
L L
```

F = `dobroszycecasino:karp_george`, L = `minecraft:leather`. Wynik: jedne Fish's Feet. To alternatywa do zakupu butów w sklepie.

## Ceny i aktualizacja

Zachowaj dotychczasowy `config/dobroszycecasino-shop.json`. Dopisz wybrane nowe wpisy z `config-examples/dobroszycecasino-shop.json`, ustaw `enabled: true` i cenę dodatnią, podzielną przez 5. Wykonaj `/casinoshop reload`. Brak wpisu oznacza wyłączoną ofertę. Nowa instalacja generuje pełny katalog z wyłączonymi cenami; aktualizacja nie nadpisuje cen administratora.

Przykład:
```json
"white_monster": { "enabled": true, "price": 200 }
```

## Grafiki i zakres

Wszystkie osiem nowych przedmiotów ma własne ikony. KFC używa dokładnie oryginalnej tekstury z kfc-mod, bez zależności od tamtego moda. Dzik odwzorowuje puszkę WK Dzik Sour Apple, White Monster — Monster Energy Ultra. Karp George nosi ciemne okulary aviator także w ręce; model ma osobne ustawienia dla obu rąk w pierwszej i trzeciej osobie. Spodnie mają brązowy, luźny krój, Amnesia przedstawia marihuanę, Red Bull pudełko tabaki ze zdjęcia, a Watrouble wątrobę. Szczegóły źródeł grafik: `ITEM_ART.md`.

Punkt issue #2 dotyczący seksualnego zachowania NPC nie został zaimplementowany; issue pozostaje otwarte.

## Testy

`gradlew build` uruchamia istniejące testy logiki i rozszerzony test katalogu oraz zgodności starego pliku cen. Scenariusz testowy klienta: `gradlew -I tools/legendary-qa.gradle runClient` — tworzy oddzielny świat i sprawdza zakładkę, powiązania ofert, zużywanie oraz efekty. Kod QA jest poza domyślnym source set i nie trafia do wydania. Po QA wykonaj `gradlew clean build` przed dystrybucją.
