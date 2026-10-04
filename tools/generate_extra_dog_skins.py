"""Additional coats for the existing DogModel, exact 64x32 wolf UV atlas."""
from pathlib import Path
from PIL import Image, ImageDraw
ROOT=Path(__file__).resolve().parents[1]/'src/main/resources/assets/dobroszycecasino/textures/entity'
for name,base,light,shade,muzzle in [
 ('caucasian_shepherd','#94877A','#C8BDAA','#504B48','#494443'),
 ('maltipoo','#E8D9BE','#FFF0D5','#C6AD88','#F4E5CA')]:
 im=Image.new('RGBA',(64,32),base);d=ImageDraw.Draw(im)
 def rect(box,c):d.rectangle(box,fill=c)
 rect((0,0,19,9),shade);rect((4,0,15,3),base)
 rect((4,4,9,9),light);rect((4,7,9,9),muzzle)
 rect((0,10,13,16),muzzle)
 rect((4,14,6,14),'#302B29');d.point((5,14),'#171718');d.point((5,16),'#675146')
 for x in (4,9):d.point((x,6),'#211E1D')
 rect((21,0,50,12),base);rect((28,7,35,12),light)
 rect((18,14,41,28),base);rect((24,20,29,28),light);rect((36,20,41,28),shade)
 rect((0,18,7,27),light);rect((0,26,7,27),muzzle if name=='maltipoo' else '#D6CDBF')
 rect((9,18,16,27),base);rect((11,24,12,27),light)
 rect((48,16,55,22),shade)
 # Chunky tufts follow individual UV islands. Maltipoo has cream curls;
 # Caucasian Shepherd has a grey-sable jacket, pale ruff and dark face mask.
 for x,y in [(25,21),(27,24),(24,27),(30,8),(33,10),(12,22),(38,22),(40,26)]:
  d.point((x,y),light);d.point((x+1,y+1),base)
 if name=='maltipoo':
  rect((48,16,55,22),'#D2BB98')
  for x,y in [(5,4),(8,4),(1,6),(12,6),(49,18),(52,20),(26,22),(28,26)]:
   d.point((x,y),'#FFF3DF');d.point((x+1,y),'#D8C4A6')
 else:
  rect((4,5,5,7),shade);rect((8,5,9,7),shade)
  d.point((4,6),'#1F1D1C');d.point((9,6),'#1F1D1C')
  rect((6,4,7,6),base)
 ROOT.mkdir(parents=True,exist_ok=True);im.save(ROOT/(name+'.png'))
