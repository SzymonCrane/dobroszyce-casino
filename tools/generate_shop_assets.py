"""Deterministic pixel art for the casino shop, no external assets required."""
from pathlib import Path
import json
from PIL import Image, ImageDraw
ROOT = Path(__file__).resolve().parents[1] / 'src/main/resources/assets/dobroszycecasino'
def sprite(name, draw):
    # These authored textures and custom transforms are maintained separately.
    if name in {'fishs_feet','banana_special','daniels_4_hand_club','sisters_device','malboro_red'}: return
    image=Image.new('RGBA',(32,32)); draw(ImageDraw.Draw(image)); p=ROOT/'textures/items'/f'{name}.png';p.parent.mkdir(parents=True,exist_ok=True);image.save(p)
    model={'parent':'item/handheld' if name in ['daniels_4_hand_club','sisters_device'] else 'item/generated','textures':{'layer0':f'dobroszycecasino:items/{name}'}}
    (ROOT/'models/item'/f'{name}.json').write_text(json.dumps(model,indent=2)+'\n')
DIGITS={'0':['111','101','101','101','111'],'2':['111','001','111','100','111'],'5':['111','100','111','001','111']}
def chip(d,value,base):
    d.ellipse((1,1,30,30),fill='#141d2d');d.ellipse((2,2,29,29),fill=base);d.ellipse((6,6,25,25),outline='#fff4cc',width=1)
    for box in [(13,2,18,4),(13,27,18,29),(2,13,4,18),(27,13,29,18),(5,5,7,7),(24,5,26,7),(5,24,7,26),(24,24,26,26)]:d.rectangle(box,fill='#fff4cc')
    size=2 if value==5 else 1;word=str(value);width=(4*len(word)-1)*size;xx=(32-width)//2;yy=(32-5*size)//2
    for digit in word:
        for y,row in enumerate(DIGITS[digit]):
            for x,c in enumerate(row):
                if c=='1':d.rectangle((xx+x*size,yy+y*size,xx+(x+1)*size-1,yy+(y+1)*size-1),fill='#fff4cc')
        xx+=4*size
sprite('chip_5',lambda d:chip(d,5,'#267daf'));sprite('chip_200',lambda d:chip(d,200,'#bc691a'))
def feet(d):
    for x,y in [(3,7),(17,4)]:
        d.rectangle((x+2,y,x+8,y+10),fill='#143e56');d.rectangle((x+3,y+1,x+7,y+8),fill='#29c8ba');d.polygon([(x+2,y+8),(x+9,y+8),(x+12,y+20),(x,y+20)],fill='#113d56');d.polygon([(x+3,y+9),(x+8,y+9),(x+10,y+18),(x+2,y+18)],fill='#45dfbf');d.line((x+5,y+10,x+5,y+17),fill='#edffcf',width=1)
sprite('fishs_feet',feet)
def potion(d):
    d.rectangle((12,2,19,5),fill='#865329');d.rectangle((12,6,19,13),fill='#c4ffff');d.ellipse((6,11,25,29),fill='#193951');d.ellipse((8,13,23,27),fill='#b0f2ed');d.pieslice((9,14,22,26),0,180,fill='#f5c633');d.rectangle((9,20,22,24),fill='#f4c531');d.line((10,15,10,19),fill='white',width=2);d.arc((12,14,21,24),35,180,fill='#af761c',width=2)
sprite('banana_special',potion)
def club(d):
    d.line((5,28,22,10),fill='#302231',width=6);d.line((5,27,23,9),fill='#895029',width=3)
    for x,y in [(11,8),(20,3),(21,13),(13,17)]:
        d.rectangle((x,y,x+6,y+6),fill='#332430');d.rectangle((x+1,y+1,x+5,y+5),fill='#aa6838');d.line((x+2,y+1,x+2,y+4),fill='#e6b361');d.point((x+5,y+2),fill='#d1dfe8')
sprite('daniels_4_hand_club',club)
def gun(d):
    d.rectangle((5,11,27,18),fill='#1d283d');d.rectangle((8,12,26,15),fill='#889da8');d.rectangle((5,18,11,27),fill='#35445b');d.rectangle((6,19,9,25),fill='#ba9a56');d.rectangle((13,17,18,21),outline='#c0cace',width=1);d.rectangle((24,9,28,18),fill='#4b5d70');d.ellipse((20,2,27,7),fill='#b9c6c8');d.ellipse((11,4,19,9),fill='#eceded')
sprite('sisters_device',gun)
def pack(d):
    d.rectangle((7,5,24,28),fill='#332635');d.rectangle((8,6,23,27),fill='#f2e5ca');d.polygon([(8,6),(23,6),(23,16),(16,12),(8,16)],fill='#b9323c');d.rectangle((11,19,20,20),fill='#b9323c');d.rectangle((13,22,18,23),fill='#ad7d42');d.rectangle((10,2,12,6),fill='#efdfbd');d.point((11,2),fill='#ea7629')
sprite('malboro_red',pack)
def placer(d):
    d.rectangle((9,3,23,16),fill='#33252c');d.rectangle((10,5,22,14),fill='#b88d62');d.rectangle((12,8,14,9),fill='#193c34');d.rectangle((19,8,21,9),fill='#193c34');d.rectangle((16,10,18,16),fill='#987049');d.polygon([(8,17),(24,17),(28,29),(4,29)],fill='#236859');d.rectangle((11,18,21,28),fill='#eddcb5');d.rectangle((14,20,18,22),fill='#ca9f45')
sprite('shopkeeper_placer',placer)
# Villager UV: original geometry with a green shop apron and gold trim.
im=Image.new('RGBA',(64,64),(36,84,69,255));d=ImageDraw.Draw(im)
d.rectangle((0,0,31,19),fill='#bb916e');d.rectangle((8,4,19,7),fill='#514035');d.rectangle((8,10,15,11),fill='#513d2e');d.rectangle((9,12,10,13),fill='#eff2d7');d.rectangle((13,12,14,13),fill='#eff2d7');d.point((10,13),fill='#24392f');d.point((13,13),fill='#24392f');d.rectangle((10,16,13,16),fill='#765b43');d.rectangle((0,20,31,47),fill='#e5d7b6');d.rectangle((16,24,27,43),fill='#377964');d.rectangle((16,33,27,35),fill='#c5a35f');d.rectangle((18,36,25,40),fill='#204d42');d.rectangle((0,48,63,63),fill='#214d40');d.rectangle((40,20,63,35),fill='#b18a67');(ROOT/'textures/entity').mkdir(exist_ok=True);im.save(ROOT/'textures/entity/shopkeeper.png')
im=Image.new('RGBA',(64,32));d=ImageDraw.Draw(im);d.rectangle((0,16,31,31),fill='#d79c78');d.rectangle((0,26,31,31),fill='#efb48d');d.rectangle((0,30,31,31),fill='#bd7c59');p=ROOT/'textures/models/armor';p.mkdir(parents=True,exist_ok=True);im.save(p/'fish_layer_1.png')
