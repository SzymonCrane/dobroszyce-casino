# Audio ruletki i palenia

- Ruletka: `no_more_bets` jest emitowany z serwera w `beginSpin`, po zmianie fazy BETTING → SPINNING. Nie jest odtwarzany co tick ani przy ponownym otwarciu GUI. Słychać go przestrzennie przy stole, kategoria Bloki.
- Marlboro i Sister's device: przytrzymaj użycie przez 32 ticki (1,6 s). Animacja pierwszoosobowa płynnie unosi przedmiot do ust, bez kołysania jedzenia. Obsługuje obie ręce i leworęczną postać. W trzeciej osobie nie ma animacji jedzenia; widoczny jest wydech.
- Po pełnym użyciu serwer odtwarza jeden wydech i wysyła cząstki dymu sprzed twarzy wszystkim pobliskim graczom, także używającemu. Zwolnienie przycisku wcześniej nie zużywa przedmiotu i nie odtwarza wydechu.
- Marlboro zachowuje Szybkość I przez 30 s oraz zużycie jednej sztuki poza Creative.
- Vape zachowuje chmurę pod graczem (promień 3, czas 6 s), 4 s cooldown i koszt jednej wytrzymałości poza Creative. Teraz powstaje ona po pełnym zaciągnięciu, zamiast natychmiast po kliknięciu.
- `EnumAction.NONE` wyłącza vanilla dźwięki jedzenia/picia i okruchy. Inne jedzenie i napoje pozostają bez zmian.
- Napisy PL/EN dostępne przez standardową opcję napisów Minecrafta. Głośność respektuje kategorie Bloki/Gracze.

## Pliki i pochodzenie

Wszystkie pliki są dołączone do JAR-a w `assets/dobroszycecasino/sounds/`; rozgrywka nie potrzebuje internetu ani syntezatora.

- `no_more_bets.ogg`: syntetyczny komunikat krupierki „No more bets.”, Piper, model `en_US-ljspeech-medium`, https://huggingface.co/rhasspy/piper-voices/tree/main/en/en_US/ljspeech/medium. Karta modelu wskazuje dataset LJSpeech w domenie publicznej: https://keithito.com/LJ-Speech-Dataset/. Nie jest nagraniem konkretnego kasyna ani imitacją wskazanej osoby. Generowanie: `python -m piper -m en_US-ljspeech-medium.onnx -f no_more_bets.wav -- 'No more bets.'`; usunięto ciszę brzegową, dodano 150 ms końca, normalizacja FFmpeg loudnorm I=-18:TP=-2:LRA=7.
- `smoke_exhale.ogg`: oryginalny proceduralny efekt miękkiego wydmuchu powietrza, 1,35 s. Odtworzenie: `python tools/generate_smoke_audio.py` (numpy, scipy, ffmpeg).
- Oba: Ogg Vorbis, mono, 44,1 kHz, jakość 5; mono umożliwia pozycjonowanie dźwięku w świecie.

## Walidacja

Build i istniejące testy logiki przeszły. Sprawdzono format audio, rejestrację zasobów i pakowanie do JAR-a. Nie wykonano odsłuchu ani testu renderowania w uruchomionym kliencie Minecrafta.

Do sprawdzenia w grze: jedna zapowiedź po odliczaniu; dwaj gracze przy stole; użycie/przerwanie obu przedmiotów; obie ręce i leworęczność; Creative i Survival; zachowanie cooldownu; brak chrupania i okruchów; zwykłe jedzenie nadal działa; napisy oraz suwaki głośności.
