# Akcesoria Baubles

| Przedmiot | Typ / slot | Efekt |
| --- | --- | --- |
| Ceasar's Brown Leggings | BELT — pas (slot 3) | Niewidzialność i teleport co 10–20 s |
| Fish's Feet | CHARM — talizman (slot 6) | Chodzenie po powierzchni wody; Shift pozwala się zanurzyć |
| Watrouble | AMULET — amulet (slot 0) | Dotychczasowa absorpcja |

Wszystkie trzy przedmioty można nosić równocześnie. Spodnie i stopy są teraz akcesoriami, nie zbroją: nie zajmują slotów nóg/butów, nie dają punktów pancerza ani modelu założonej zbroi. Ikony, identyfikatory, receptury i ceny pozostają zachowane. Przedmioty nie zużywają wytrzymałości.

Po aktualizacji przenieś stare egzemplarze ze slotów zbroi do odpowiednich slotów Baubles. Zwykły ekwipunek, główna ręka ani stare sloty zbroi nie aktywują ich efektów. Zdjęcie spodni zatrzymuje kolejne teleportacje; już nałożona niewidzialność wygasa maksymalnie po 2 s.

Bez zainstalowanego Baubles każde akcesorium działa w drugiej ręce, zgodnie z dotychczasowym fallbackiem Watrouble. Dostępny jest wtedy tylko jeden aktywny przedmiot naraz. Integracja jest opcjonalna i nie dodaje wymaganej zależności.

Wspólny adapter `CasinoBaubles` rejestruje capability IBauble i sprawdza aktywny ekwipunek przez `BaublesApi.isBaubleEquipped`. Efekt chodzenia po wodzie nadal działa po obu stronach, a efekty spodni po stronie serwera. Kontrole Shift, latania i obserwatora są zachowane.

Źródła API: https://github.com/Azanor/Baubles/blob/master/src/main/java/baubles/api/BaubleType.java oraz https://github.com/Azanor/Baubles/blob/master/src/main/java/baubles/api/BaublesApi.java.

Do sprawdzenia w kliencie: jednoczesne noszenie trzech akcesoriów, poprawne/niepoprawne sloty, zdejmowanie, Shift nad wodą, zwykły ekwipunek bez efektów, zapis/odczyt świata oraz uruchomienie bez Baubles. Nie potwierdzono jeszcze tych scenariuszy w uruchomionej paczce RLCraft.
