# Konfiguracja sklepu

Plik serwera: `config/dobroszycecasino-shop.json`. Tworzy się przy pierwszym uruchomieniu moda.
Gotowy szablon jest w `config-examples/`. Klient otrzymuje ceny od serwera;
zmiana pliku na komputerze klienta nie zmienia ofert serwera.

```json
{
  "schemaVersion": 1,
  "offers": {
    "fishs_feet": { "enabled": false, "price": 0 },
    "banana_special": { "enabled": false, "price": 0 },
    "daniels_4_hand_club": { "enabled": false, "price": 0 },
    "sisters_device": { "enabled": false, "price": 0 },
    "malboro_red": { "enabled": false, "price": 0 }
  }
}
```

Aby włączyć ofertę, ustaw `enabled` na `true` i wpisz `price`, np. `205`.
Cena to wartość żetonów za jedną sztukę, a nie liczba przedmiotów-żetonów.
Dozwolone ceny: całkowite wielokrotności 5 od 5 do 1000000. Zero jest dozwolone
wyłącznie dla wyłączonej oferty. Brak oferty w pliku oznacza jej wyłączenie.
Nieznane identyfikatory, niecałkowite ceny i nieprawidłowe typy danych są odrzucane.
Plik JSON nie dopuszcza komentarzy ani przecinka po ostatniej pozycji.

Po zapisaniu pliku użyj jako operator:
```mcfunction
/casinoshop reload
```
Poprawne przeładowanie zamyka aktualnie otwarte sklepy; należy otworzyć je ponownie.
Jeśli JSON jest błędny, serwer zachowuje ostatnią poprawną konfigurację i pokazuje
błąd operatorowi. Przy błędzie podczas pierwszego uruchomienia sklep pozostaje wyłączony.
Sklepy mają wspólne ceny i nieograniczony zapas. W tej wersji nie odkupują przedmiotów.
Zakup pobiera żetony na serwerze i wydaje resztę. Jeśli ekwipunek jest pełny, towar
lub reszta wypada obok kupującego z przypisanym właścicielem.
