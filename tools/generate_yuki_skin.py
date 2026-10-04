"""Silver-shaded British Shorthair coat, authored for ModelOcelot's 64x32 UV.
Uses the same model-native pixel-art workflow as generate_companion_assets.py.
No vanilla textures are copied. Run with Python and Pillow.
"""
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1] / 'src/main/resources/assets/dobroszycecasino'
im = Image.new('RGBA', (64,32), '#D8DDE1')
d = ImageDraw.Draw(im)

def rect(box, color): d.rectangle(box, fill=color)

# Head 5x4x5 at (0,0): pale cheeks, silver crown and green eyes.
rect((0,0,19,8), '#C5CBD1')
rect((5,0,14,4), '#ADB6C0')
rect((0,5,4,8), '#BEC6CF')
rect((10,5,14,8), '#BEC6CF')
rect((15,5,19,8), '#B0BAC5')
rect((5,5,9,8), '#E9ECEB')
rect((5,5,9,5), '#C2CAD2')
for x in (5,9):
    d.point((x,6), '#578669')
    d.point((x,7), '#243A32')
d.point((7,5), '#A4AFBC')
# Nose 3x2x2 at (0,24); a pink nose with pale whisker pads.
rect((0,24,9,27), '#F0EFEB')
d.point((3,26), '#B77C88')
d.point((3,27), '#7D727D')
d.point((0,26), '#B8BEC5')
d.point((6,26), '#B8BEC5')
# Small ears 1x1x2 at (0,10) and (6,10).
rect((0,10,11,12), '#B9C2CC')
d.point((2,12), '#D5ABAF'); d.point((8,12), '#D5ABAF')
# Body 4x16x6 at (20,0). Silver tipping along the back, white underside.
rect((20,0,39,21), '#D5DBDF')
rect((26,0,33,5), '#CDD4DB')
rect((26,6,29,21), '#EEF0EC')
rect((36,6,39,21), '#AEB9C5')
rect((20,6,21,21), '#BCC6D0')
rect((34,6,35,21), '#BCC6D0')
for x,y in [(22,8),(23,12),(24,17),(32,9),(33,14),(31,19),(37,9),(38,15)]:
    d.point((x,y), '#C0CAD3')
# Back legs 2x6x2 at (8,13), front legs 2x10x2 at (40,0).
rect((8,13,15,20), '#D4DBE0')
rect((8,19,15,20), '#ECEEEB')
rect((40,0,47,11), '#D4DBE0')
rect((40,9,47,11), '#ECEEEB')
# Two tail segments, each 1x8x1; silver tip (not ocelot spots).
rect((0,15,3,23), '#BBC5CF')
rect((4,15,7,23), '#BCC6D0')
rect((4,21,7,23), '#929EAD')

path = ROOT / 'textures/entity/yuki.png'
path.parent.mkdir(parents=True, exist_ok=True)
im.save(path)
