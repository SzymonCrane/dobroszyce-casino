# Grafiki przedmiotów

Tekstury: `src/main/resources/assets/dobroszycecasino/textures/items/`.
Modele: `src/main/resources/assets/dobroszycecasino/models/item/`.

| Przedmiot | Grafika / referencja |
| --- | --- |
| Legendary KFC Bucket | Oryginalny `assets/kfcmod/textures/items/kfc_bucket.png` ze starego kfc-mod, skopiowany bez zmian |
| Dzik Energy | [WK Dzik Sour Apple](https://wkdzik.pl/dzik-energy-apple-sour), zielono-czarna puszka z różowym pasem |
| White Monster | [Monster Energy Ultra](https://www.monsterenergy.com/en-gb/energy-drinks/monster-ultra/zero-sugar-ultra/), biała puszka ze srebrnym ornamentem |
| Karp George | Karp w ciemnych aviatorach według zdjęcia użytkownika `image(2).png` |
| Ceasar's Brown Leggings | Brązowe, luźne eleganckie jeansy |
| Amnesia's Weed | Liść i kwiat marihuany |
| Red Bull Tabacco | Czerwone pudełko Red Bull Snuff według zdjęcia użytkownika `image(1).png` |
| Watrouble | Gładka bordowobrązowa wątroba |

Siedem nowych grafik przygotowano wbudowanym imagegen, następnie przeskalowano do PNG RGBA 256×256 z zachowaniem przezroczystości. KFC zachowuje oryginalną rozdzielczość i zawartość pliku. Grafiki opakowań są ilustracjami opartymi na referencjach, nie reprodukcjami piksel w piksel.

Karp używa tej samej tekstury z okularami w ekwipunku i w obu rękach. Ustawienia `display` powiększają i pochylają przedmiot w pierwszej i trzeciej osobie. Jest to model `item/generated` (sprite z grubością), nie pełny przestrzenny model ryby. Pozostałe ikony także używają standardowego modelu generowanego.

Spodnie i Fish's Feet są teraz akcesoriami Baubles, bez modelu założonej zbroi. Ikony pozostają bez zmian. Sloty i efekty: `BAUBLES_PL.md`.

## Prompty (imagegen)

### dzik_energy

Faithful isolated WK DZIK ENERGY SOUR APPLE 500ml can matching reference packaging exactly: lime green can black vertical panel large white vertical DZIK, magenta bottom band white boar, metallic blue silver rim. Remove all scene background. Single upright can centered square transparent canvas, fills 92% height. Polished faithful product game inventory icon, crisp details, no added objects.

### white_monster

Extract and faithfully reproduce this exact white Monster Energy Ultra can as a clean sharp game inventory icon. Preserve exact white silver ornate label, silver black claw M, MONSTER ENERGY ULTRA lettering and ZERO SUGAR top. Single upright can centered square canvas fills 92% height, transparent background, no shadow outside silhouette.

### karp_george

Create a beautiful game item icon of a whole common carp wearing the same large black aviator sunglasses with thin silver frame as reference fish. Three-quarter side view so whole golden olive scaled plump carp, fins tail barbels and BOTH dark aviator lenses clearly visible. Head towards lower left tail upper right, no hands no water no background. Polished detailed hand-painted realistic RPG inventory art, strong silhouette, glasses prominent even at small size. Centered square transparent canvas, occupies 90%.

### ceasars_brown_leggings

Single beautiful RPG inventory icon of brown baggy elegant jeans trousers. Front view full pair, broad loose straight legs, rich chocolate brown denim, tailored waistband belt loops button fly, neat pockets and subtle seams, tasteful folds. No person no shoes no hanger. Polished detailed hand-painted realistic game item art, strong readable silhouette, centered square transparent canvas fills 90% height.

### amnesias_weed

Single beautiful RPG inventory item icon of marijuana: one lush recognizable seven-point serrated cannabis leaf behind a small dense green cannabis flower bud with tiny amber hairs. Botanical accurate rich greens, crisp detailed hand-painted realistic game art, strong readable silhouette, no text no pot no bag no background, centered square transparent canvas fills 88%.

### red_bull_tabacco

Faithfully reproduce this exact RED BULL SNUFF tobacco tin as isolated inventory icon, front view with subtle tin thickness along bottom edge. Preserve red rounded rectangular box, cream RED BULL letters, gold SNUFF upper right corner, circular crest top center and white black-bordered Polish warning at bottom exactly from reference. Remove white background to true transparency. Single tin fills 90% width square canvas. Crisp polished product illustration, no extra objects.

### watrouble

Beautiful polished RPG game inventory icon of a human liver, anatomically recognizable broad large right lobe tapering smaller left lobe separated by subtle fissure. Warm deep reddish brown burgundy organic smooth glossy surface with soft highlights and gentle volume. Non-gory clean medical object, no blood drips no wounds no other organs no jewelry no text. Detailed hand-painted realistic item art, strong silhouette, centered square transparent canvas fills 88% width.
