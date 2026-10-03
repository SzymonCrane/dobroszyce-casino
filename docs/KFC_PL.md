# KFC: przeniesiony łańcuch wytwarzania

Wszystkie przedmioty należą do `dobroszycecasino` i są w zakładce Creative **Dobroszyce Casino**. Oddzielny kfc-mod nie jest potrzebny.

## Receptury

| Etap | Składniki / układ | Wynik |
| --- | --- | --- |
| Crafting paper bowl | Pierwszy rząd: papier, papier, papier. Drugi: kość, czerwony barwnik, kość. | 1 × `paper_bowl` |
| Piec | 1 × surowy kurczak + paliwo | 1 × `kfc_wing`, 0,35 XP |
| Crafting kubełka | Pierwszy rząd: skrzydełko, skrzydełko, skrzydełko. Drugi: pusto, paper bowl, pusto. | 1 × `legendary_kfc_bucket` |

Czerwony barwnik w 1.12.2 to `minecraft:dye` z `data: 1`. Receptura paper bowl została odtworzona z wcześniejszych ustaleń; skrzydełka i układ kubełka pochodzą ze źródeł kfc-mod 1.0. Drewnianą miskę zastępuje paper bowl.

Tak jak w starym modzie, pieczenie surowego kurczaka **zastępuje** standardowy wynik pieca skrzydełkami. Nie zmienia przepisów innych mięs. Aby uniknąć konfliktów podmiany tej samej receptury, nie instaluj równolegle starego kfc-mod; jego istniejące przedmioty nie są automatycznie migrowane między identyfikatorami modów.

## Jedzenie i opakowanie

- Skrzydełka: 6 punktów głodu (3 ikony) i mnożnik nasycenia 0,6, jak w starym modzie.
- Kubełek zachowuje obecne efekty legendarnego przedmiotu: pełny głód/nasycenie, Absorpcja I na 60 s i Regeneracja I na 10 s.
- Survival: zjedzenie ostatniej sztuki zastępuje ją paper bowl. Przy większym stosie paper bowl trafia do ekwipunku, a przy braku miejsca wypada przy graczu. Jedno zjedzenie = jedno opakowanie.
- Creative: nie zużywa kubełka i nie tworzy dodatkowego opakowania.
- Przerwanie jedzenia nie zużywa przedmiotu i nie oddaje opakowania.

## Grafiki

- Pełny kubełek: dotychczasowa dokładna kopia `kfc_bucket.png` z odnalezionych źródeł kfc-mod 1.0, 16×16.
- Skrzydełka: dokładna kopia `kfc_wing.png` z tych samych źródeł, 16×16.
- Paper bowl: wcześniejsza grafika „Pusty papierowy kubełek na kurczaka.png”, odzyskana z plików użytkownika i przeskalowana do 256×256 RGBA.
- Nie odnaleziono innej, późniejszej wersji grafiki pełnego kubełka ani skrzydełek.

## Kontrola w grze

Sprawdź oba craftingi i piec, zwrot opakowania dla 1/2 kubełków, pełny ekwipunek, Creative i przerwanie jedzenia. Build/testy logiki oraz kontrola receptur i zasobów nie zastępują tego testu klienta.
