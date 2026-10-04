"""Model-native pixel textures and extinguisher cuboids, using the exact 1.12 UV layout.
Run with Python + Pillow. Separate from the older shop generator to preserve its assets.
"""
from pathlib import Path
import json
from PIL import Image, ImageDraw
ROOT = Path(__file__).resolve().parents[1] / 'src/main/resources/assets/dobroszycecasino'

def rect(draw, xy, color): draw.rectangle(xy, fill=color)
def save(im, name):
    path = ROOT / name
    path.parent.mkdir(parents=True, exist_ok=True)
    im.save(path)

# ModelWolf atlas: head 0,0; muzzle 0,10; legs 0,18; tail 9,18;
# mane 21,0; body 18,14. DogModel uses the spare 48,16 island for floppy ears.
for name, fur, light, shade, muzzle in [
    ('azor', '#CE984E', '#E9BD76', '#AD773C', '#F3D59F'),
    ('morty', '#282421', '#443B32', '#18191A', '#B27A45'),
]:
    im = Image.new('RGBA', (64,32), fur); d = ImageDraw.Draw(im)
    rect(d,(0,0,3,9),shade); rect(d,(10,4,19,9),shade)
    rect(d,(4,4,9,9),light); rect(d,(4,8,9,9),muzzle)
    rect(d,(0,10,13,16),muzzle)
    rect(d,(4,14,6,14),'#25201E'); d.point((4,14), '#595047')
    rect(d,(5,15,5,16),'#493729')
    # Front-facing eyes and highlights, above the projecting snout.
    for x in (4,9):
        d.point((x,6), '#292018'); d.point((x,5), '#EFD3A2' if name=='azor' else '#BC8349')
    rect(d,(0,18,7,27),light if name=='azor' else muzzle)
    rect(d,(0,26,7,27),muzzle)
    rect(d,(9,18,16,27),fur); rect(d,(11,26,12,27),light)
    rect(d,(21,0,50,12),fur); rect(d,(28,7,35,12),light)
    rect(d,(18,14,41,28),fur); rect(d,(24,20,29,28),light)
    rect(d,(36,20,41,28),shade)
    rect(d,(48,16,55,22),shade)
    rect(d,(51,19,51,22),fur)
    # Small, aligned tufts rather than random noise on UV islands.
    for x,y in [(25,21),(27,24),(24,27),(30,8),(33,10),(12,22)]:
        d.point((x,y),light)
    if name=='morty':
        # Jagdterrier: dark jacket, tan eyebrows, muzzle, chest and lower legs.
        rect(d,(28,10,35,12),muzzle); rect(d,(25,20,28,23),muzzle)
        rect(d,(4,5,5,5),muzzle); rect(d,(8,5,9,5),muzzle)
        rect(d,(4,6,4,6),'#0B0B0C'); rect(d,(9,6,9,6),'#0B0B0C')
    save(im,f'textures/entity/{name}.png')

# ModelBiped(64,64), classic 4-pixel arms and mirrored legs. Adult alternative
# outfit: charcoal jacket, violet tee, black jeans/boots, purple streak in hair.
im=Image.new('RGBA',(64,64),(0,0,0,0));d=ImageDraw.Draw(im)
rect(d,(0,0,31,15),'#251F2C')
rect(d,(8,8,15,15),'#DFC1B1')
rect(d,(8,8,15,9),'#302536');rect(d,(8,10,9,13),'#302536')
rect(d,(14,8,15,12),'#85569D');rect(d,(15,9,15,13),'#AC7EC1')
rect(d,(10,11,10,11),'#22202B');rect(d,(13,11,13,11),'#22202B')
rect(d,(11,14,12,14),'#8B5369');d.point((14,13),'#C3C4CE')
# Hat overlay: transparent except side/back hair, slightly wider than head.
rect(d,(32,8,39,15),'#302536');rect(d,(48,8,63,15),'#302536')
rect(d,(40,0,55,7),'#302536');rect(d,(54,8,55,15),'#78528E')
rect(d,(16,16,39,31),'#292732')
rect(d,(20,20,27,31),'#3B304A');rect(d,(20,20,21,29),'#22232C');rect(d,(26,20,27,29),'#22232C')
rect(d,(22,20,25,21),'#DFC1B1');rect(d,(22,22,25,28),'#776083')
# Necklace and silver belt.
rect(d,(22,22,25,22),'#B2ADBD');rect(d,(23,23,24,23),'#DAD7E3')
rect(d,(20,30,27,30),'#18181E');d.point((23,30),'#B7AFC3')
rect(d,(40,16,55,31),'#292732');rect(d,(40,29,55,31),'#DFC1B1')
rect(d,(44,27,47,28),'#15151D');d.point((45,28),'#B3ADBD')
rect(d,(0,16,15,31),'#25232E');rect(d,(0,28,15,31),'#14151C')
rect(d,(4,20,4,27),'#393440');rect(d,(4,30,7,30),'#4D4755')
save(im,'textures/entity/alternatywka.png')

# Cuboid extinguisher uses a palette atlas: its geometry is also its inventory icon.
colors=['#BE2634','#E34A46','#831E2E','#292D37','#B5C2C7','#F5ECD8','#45A365','#E9BE55']
im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
for i,c in enumerate(colors): rect(d,(i*2,0,i*2+1,15),c)
save(im,'textures/items/extinguisher.png')
elements=[]
def box(a,b,c):
    uv=[c*2+0.25,0.25,c*2+1.75,1.75]
    elements.append({'from':a,'to':b,'faces':{side:{'uv':uv,'texture':'#palette'} for side in ['north','south','east','west','up','down']}})
box([5,1,5],[11,11,11],0);box([6,0,6],[10,1,10],2)
box([6,11,6],[10,12,10],1);box([7,12,7],[9,14,9],4)
box([6,14,7],[11,15,9],3);box([6,15,7],[10,15.5,9],4)
box([10,12,7],[12,13,9],3);box([11,4,7],[12,13,9],3)
box([11,3,6.5],[13,6,9.5],3);box([12,3,6.5],[13,4,9.5],4)
box([6,4,4.9],[10,8,5],5);box([7,5,4.8],[9,7,4.9],0)
box([7.7,5.2,4.7],[8.3,6.8,4.8],5)
box([6,12,6],[7,13,7],7);box([6.2,12.2,5.9],[6.8,12.8,6],6)
model={'textures':{'palette':'dobroszycecasino:items/extinguisher','particle':'dobroszycecasino:items/extinguisher'},'elements':elements,
'display':{'gui':{'rotation':[20,135,0],'translation':[0,0,0],'scale':[0.9,0.9,0.9]},
'firstperson_righthand':{'rotation':[0,160,0],'translation':[2,-1,1],'scale':[0.65,0.65,0.65]},
'firstperson_lefthand':{'rotation':[0,-160,0],'translation':[2,-1,1],'scale':[0.65,0.65,0.65]},
'thirdperson_righthand':{'rotation':[0,0,0],'translation':[0,1,0],'scale':[0.65,0.65,0.65]},
'thirdperson_lefthand':{'rotation':[0,0,0],'translation':[0,1,0],'scale':[0.65,0.65,0.65]},
'ground':{'translation':[0,2,0],'scale':[0.5,0.5,0.5]},'fixed':{'rotation':[0,180,0],'scale':[0.8,0.8,0.8]}}}
(ROOT/'models/item/extinguisher.json').write_text(json.dumps(model,indent=2)+'\n')
# Match the vanilla spawn-egg silhouette with authored colours, no client tint dependency.
im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
d.polygon([(6,1),(9,1),(12,4),(13,9),(12,12),(10,14),(5,14),(3,12),(2,9),(3,5)],fill='#24212D')
rect(d,(5,4,7,6),'#9067AD');rect(d,(9,8,11,10),'#9067AD');rect(d,(5,11,6,12),'#9067AD');d.point((5,3),'#C4A7D4')
save(im,'textures/items/alternatywka_spawn_egg.png')
(ROOT/'models/item/alternatywka_spawn_egg.json').write_text(json.dumps({'parent':'item/generated','textures':{'layer0':'dobroszycecasino:items/alternatywka_spawn_egg'}},indent=2)+'\n')
recipe={'type':'minecraft:crafting_shaped','pattern':[' IL','IRI',' I '],'key':{'I':{'item':'minecraft:iron_ingot'},'L':{'item':'minecraft:lever'},'R':{'item':'minecraft:redstone'}},'result':{'item':'dobroszycecasino:extinguisher'}}
(ROOT/'recipes/extinguisher.json').write_text(json.dumps(recipe,indent=2)+'\n')
