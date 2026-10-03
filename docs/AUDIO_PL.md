# Audio ruletki i palenia

- Ruletka: `no_more_bets` jest emitowany z serwera w `beginSpin`, po zmianie fazy BETTING → SPINNING. Nie jest odtwarzany co tick ani przy ponownym otwarciu GUI. Słychać go przestrzennie przy stole, kategoria Główne (MASTER), głośność 2, zasięg około 32 bloków.
- Blackjack: ten sam `no_more_bets` rozbrzmiewa raz z serwera przy rozpoczęciu rozdania — zarówno po gotowości wszystkich graczy, jak i po upływie odliczania. Brak zakładów, trwająca runda i strona klienta nie uruchamiają komunikatu.
- Marlboro i Sister's device: przytrzymaj użycie przez 32 ticki (1,6 s). Animacja pierwszoosobowa płynnie unosi przedmiot do ust, bez kołysania jedzenia. Obsługuje obie ręce i leworęczną postać. W trzeciej osobie nie ma animacji jedzenia; widoczny jest wydech.
- Po pełnym użyciu serwer odtwarza jeden wydech i wysyła cząstki dymu sprzed twarzy wszystkim pobliskim graczom, także używającemu. Zwolnienie przycisku wcześniej nie zużywa przedmiotu i nie odtwarza wydechu.
- Marlboro zachowuje Szybkość I przez 30 s oraz zużycie jednej sztuki poza Creative.
- Vape zachowuje chmurę pod graczem (promień 3, czas 6 s), 4 s cooldown i koszt jednej wytrzymałości poza Creative. Teraz powstaje ona po pełnym zaciągnięciu, zamiast natychmiast po kliknięciu.
- `EnumAction.NONE` wyłącza vanilla dźwięki jedzenia/picia i okruchy. Inne jedzenie i napoje pozostają bez zmian.
- Napisy PL/EN dostępne przez standardową opcję napisów Minecrafta. Głośność respektuje kategorie Główne/Gracze.

## Pliki i pochodzenie

Wszystkie pliki są dołączone do JAR-a w `assets/dobroszycecasino/sounds/`; rozgrywka nie potrzebuje internetu ani syntezatora.

- `no_more_bets.ogg`: syntetyczny komunikat „No more bets.”, zmodulowany na niski, nosowy głos w stylu villagera, Piper, model `en_US-ljspeech-medium`, https://huggingface.co/rhasspy/piper-voices/tree/main/en/en_US/ljspeech/medium. Karta modelu wskazuje dataset LJSpeech w domenie publicznej: https://keithito.com/LJ-Speech-Dataset/. Nie jest nagraniem konkretnego kasyna ani imitacją wskazanej osoby. Generowanie: `python -m piper -m en_US-ljspeech-medium.onnx -f no_more_bets.wav -- 'No more bets.'`; usunięto ciszę brzegową, dodano 150 ms końca, normalizacja FFmpeg loudnorm I=-18:TP=-2:LRA=7.
- Modulacja komunikatu: `python tools/modulate_dealer_voice.py /path/to/no_more_bets.wav`. Wysokość głosu ×0,62 z przesunięciem formantów, tempo ×0,92, podbicie nosowego pasma, delikatne vibrato i kompresja. Ruletka i blackjack używają dokładnie tego samego pliku, z pitch=1 w grze. To stylizacja głosu, nie próbka audio skopiowana z Minecrafta.
- `smoke_exhale.ogg`: oryginalny proceduralny efekt miękkiego wydmuchu powietrza, 1,35 s. Odtworzenie: `python tools/generate_smoke_audio.py` (numpy, scipy, ffmpeg).
- Oba: Ogg Vorbis, mono, 44,1 kHz, jakość 5; mono umożliwia pozycjonowanie dźwięku w świecie.

## Walidacja

Build i istniejące testy logiki przeszły. Sprawdzono format audio, rejestrację zasobów i pakowanie do JAR-a. Nie wykonano odsłuchu ani testu renderowania w uruchomionym kliencie Minecrafta.

Do sprawdzenia w grze: jedna zapowiedź po odliczaniu ruletki; blackjack po gotowości wszystkich graczy i po odliczaniu; dwaj gracze przy stole; użycie/przerwanie obu przedmiotów; obie ręce i leworęczność; Creative i Survival; zachowanie cooldownu; brak chrupania i okruchów; zwykłe jedzenie nadal działa; napisy oraz suwaki głośności.

## Poprawka słyszalności i barwy

Oba stoły odtwarzają komunikat w MASTER, tak jak skuteczna komenda diagnostyczna użytkownika; wyciszenie Bloków już go nie tłumi. Nadal respektowana jest głośność główna. Głośność wywołania 2 zwiększa zasięg; Pomiar dekodowanego OGG wykazał rzeczywiste -60,18 LUFS w v1 (mimo wcześniejszego celu -18). Nowy plik osiąga -15,70 LUFS, true peak -1,20 dBFS. Poprzednia normalizacja bardzo krótkiego pliku nie zapewniła oczekiwanego poziomu. Nie potwierdzono, czy poprzedni brak komunikatu przy stole wynikał z suwaka Bloki.

`tools/refine_villager_voice.py` używa zachowanego pliku v1, nadając głosowi harmoniczną, nosową barwę z intonacją hrrm przez wokoder pasmowy i domieszkę oryginalnych spółgłosek. Jest to stylizacja, nie oryginalny głos villagera. Ocena odsłuchowa użytkownika i próba w grze są nadal potrzebne.

Walidacja poprawki: oba pliki zdekodowano i zmierzono FFmpeg; sprawdzono identyczne wywołanie MASTER/2.0 przy obu stołach oraz JSON. Pełnego builda nie powtórzono: tymczasowy JDK 8 i cache Forge/Gradle z poprzedniej sesji nie są już dostępne.

## Dźwięk zaciągania

Marlboro i Sister's device mają teraz osobny `smoke_inhale`: krótki wdech w tickach 32, 24 i 16 użycia, przed dotychczasowym wydechem po ukończeniu. Dźwięk emituje wyłącznie serwer, do używającego i pobliskich graczy, w kategorii Gracze. Przerwanie użycia zatrzymuje następne porcje; rozpoczęta próbka kończy się w maksymalnie 0,38 s. Cooldown vape nadal blokuje rozpoczęcie użycia. Generator: `tools/generate_inhale_audio.py`. Napisy PL/EN.

## Czytelna mowa bez wokodera

Po uwadze użytkownika o robotycznym brzmieniu generator zastąpiono wariantem bez syntetycznego nośnika i wokodera. Używa przedwokoderowego pliku v1, zachowuje artykulację i intonację, lekko przywraca wysokość przy zachowaniu niskich formantów i delikatnie podkreśla nosowe pasmo. Użytkownik wybrał czytelne słowa „No more bets” z podobieństwem do villagera, nie oryginalne pomruki bez słów. Ostateczne podobieństwo wymaga odsłuchowej oceny użytkownika; nie jest to oryginalna próbka villagera.

## Aktualny głos — zwykły kobiecy

Na ostateczną prośbę użytkownika wszystkie efekty villagera wycofano z używanego nagrania. `no_more_bets.ogg` zawiera normalny syntetyczny kobiecy głos Piper `en_US-ljspeech-medium`, mówiący „No more bets.”, bez przesuwania tonu, formantów, vibrato i wokodera. Jedynie przycięto ciszę i wyrównano poziom. Źródło: `tools/audio/no_more_bets_female.wav`; aktualny generator: `tools/prepare_dealer_voice.py`. Wcześniejsze skrypty modulacji są historyczne i nie służą do generowania aktualnego dźwięku. Oba stoły korzystają z tego samego podmienionego OGG.
