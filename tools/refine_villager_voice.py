#!/usr/bin/env python3
"""Natural voiced variant: preserve speech, no vocoder/carrier or ring modulation.
Requires numpy, scipy, ffmpeg. The source is the pre-vocoder v1 recording.
"""
from pathlib import Path
import subprocess, tempfile
import numpy as np
from scipy.io import wavfile
root=Path(__file__).resolve().parents[1]
source=root/'tools/audio/no_more_bets_v1.ogg'
output=root/'src/main/resources/assets/dobroszycecasino/sounds/no_more_bets.ogg'
with tempfile.TemporaryDirectory() as tmp:
    dry=Path(tmp)/'dry.wav'; normalized=Path(tmp)/'normalized.wav'
    subprocess.run(['ffmpeg','-v','error','-y','-i',str(source),'-ar','44100','-ac','1',str(dry)],check=True)
    sr,raw=wavfile.read(dry)
    x=raw.astype(float)
    x*=0.82/max(np.max(np.abs(x)),1e-9)
    wavfile.write(normalized,sr,np.int16(x*32767))
    filters=','.join([
        # v1 already lowers the source substantially. Recover some articulation,
        # preserving its low formants and the speaker's original intonation.
        'rubberband=tempo=1.04:pitch=1.12:formant=preserved:pitchq=quality',
        'highpass=f=85',
        'equalizer=f=420:t=q:w=0.8:g=2',
        'equalizer=f=950:t=q:w=1.2:g=2.5',
        'equalizer=f=2400:t=q:w=0.7:g=2',
        'acompressor=threshold=0.12:ratio=3:attack=12:release=100:makeup=1.5',
        'loudnorm=I=-14:TP=-1:LRA=5',
    ])
    subprocess.run(['ffmpeg','-v','error','-y','-i',str(normalized),'-af',filters,
        '-ar','44100','-ac','1','-c:a','libvorbis','-q:a','5',str(output)],check=True)
print(output)
