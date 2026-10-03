#!/usr/bin/env python3
"""Short inward breath for smoking; numpy, scipy, ffmpeg required."""
from pathlib import Path
import tempfile, subprocess
import numpy as np
from scipy.signal import butter, sosfilt
from scipy.io.wavfile import write
rate=44100
t=np.arange(round(rate*0.38))/rate
rng=np.random.default_rng(173)
air=sosfilt(butter(2,[700,6500],btype='bandpass',fs=rate,output='sos'),rng.standard_normal(len(t)))
# Rising, narrow airflow with a soft mouth resonance, distinct from the exhale.
body=sosfilt(butter(2,[250,750],btype='bandpass',fs=rate,output='sos'),rng.standard_normal(len(t)))
envelope=np.minimum(t/0.035,1)*np.minimum((0.38-t)/0.035,1)*(0.7+0.3*t/0.38)
x=(air+0.3*body)*envelope
x*=0.8/np.max(np.abs(x))
root=Path(__file__).resolve().parents[1]
with tempfile.TemporaryDirectory() as tmp:
    p=Path(tmp)/'inhale.wav';write(p,rate,np.int16(x*32767))
    subprocess.run(['ffmpeg','-v','error','-y','-i',str(p),'-ac','1','-c:a','libvorbis','-q:a','5',str(root/'src/main/resources/assets/dobroszycecasino/sounds/smoke_inhale.ogg')],check=True)
