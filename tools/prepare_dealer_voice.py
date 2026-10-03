#!/usr/bin/env python3
"""Prepare the unmodified female Piper/LJSpeech voice; no pitch or timbre effects."""
from pathlib import Path
import tempfile, subprocess
import numpy as np
from scipy.io import wavfile
root=Path(__file__).resolve().parents[1]
sr,data=wavfile.read(root/'tools/audio/no_more_bets_female.wav')
x=data.astype(float); x/=max(np.max(np.abs(x)),1)
active=np.flatnonzero(np.abs(x)>0.008)
if not len(active): raise ValueError('Silent speech source')
x=x[max(0,active[0]-int(sr*.04)):min(len(x),active[-1]+int(sr*.15))]
x*=0.89
with tempfile.TemporaryDirectory() as tmp:
    wav=Path(tmp)/'speech.wav';wavfile.write(wav,sr,np.int16(x*32767))
    subprocess.run(['ffmpeg','-v','error','-y','-i',str(wav),'-af',
        'acompressor=threshold=0.18:ratio=2:attack=10:release=90:makeup=1.5,alimiter=limit=0.89:level=false',
        '-ar','44100','-ac','1','-c:a','libvorbis','-q:a','5',
        str(root/'src/main/resources/assets/dobroszycecasino/sounds/no_more_bets.ogg')],check=True)
