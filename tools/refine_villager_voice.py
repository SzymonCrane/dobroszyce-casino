#!/usr/bin/env python3
"""Reshape the original announcement into a nasal, voiced grunt while retaining words.
Requires numpy, scipy and ffmpeg. Input is pinned to v1 to avoid repeated processing.
"""
from pathlib import Path
import subprocess, tempfile
import numpy as np
from scipy.io import wavfile
from scipy.signal import butter, sosfilt
root=Path(__file__).resolve().parents[1]
source=root/'tools/audio/no_more_bets_v1.ogg'
output=root/'src/main/resources/assets/dobroszycecasino/sounds/no_more_bets.ogg'
with tempfile.TemporaryDirectory() as tmp:
    dry=Path(tmp)/'dry.wav'; wet=Path(tmp)/'wet.wav'
    subprocess.run(['ffmpeg','-v','error','-y','-i',str(source),'-ar','44100','-ac','1',str(dry)],check=True)
    sr,raw=wavfile.read(dry); x=raw.astype(float)/32768
    t=np.arange(len(x))/sr
    # A rounded glottal buzz with slow "hrrm" intonation, not merely a lowered voice.
    f0=112+9*np.sin(2*np.pi*1.8*t)+2*np.sin(2*np.pi*5.3*t)
    phase=2*np.pi*np.cumsum(f0)/sr
    carrier=sum(np.sin(k*phase)/(k**1.15) for k in range(1,36))
    voiced=np.zeros_like(x)
    bands=[90,180,330,550,850,1250,1850,2700,4000,6500]
    for lo,hi in zip(bands,bands[1:]):
        filt=butter(2,[lo,hi],btype='bandpass',fs=sr,output='sos')
        speech=sosfilt(filt,x)
        envelope=sosfilt(butter(2,35,fs=sr,output='sos'),np.abs(speech))
        band=sosfilt(filt,carrier)
        band/=max(np.sqrt(np.mean(band**2)),1e-6)
        voiced+=band*envelope
    voiced*=np.sqrt(np.mean(x*x))/max(np.sqrt(np.mean(voiced*voiced)),1e-6)
    # Keep consonants and enough original speech for the phrase to remain legible.
    consonants=sosfilt(butter(2,2500,btype='highpass',fs=sr,output='sos'),x)
    y=0.6*voiced+0.4*x+0.18*consonants
    y=np.tanh(y*2)/2
    y*=0.85/max(np.max(np.abs(y)),1e-6)
    wavfile.write(wet,sr,np.int16(y*32767))
    subprocess.run(['ffmpeg','-v','error','-y','-i',str(wet),'-af',
        'equalizer=f=850:t=q:w=1.2:g=3,loudnorm=I=-12:TP=-1:LRA=5',
        '-ar','44100','-ac','1','-c:a','libvorbis','-q:a','5',str(output)],check=True)
print(output)
