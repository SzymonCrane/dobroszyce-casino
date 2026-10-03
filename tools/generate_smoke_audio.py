#!/usr/bin/env python3
"""Generate an original, soft breath/exhale SFX. Requires numpy, scipy, ffmpeg."""
from pathlib import Path
import subprocess
import tempfile
import numpy as np
from scipy.signal import butter, sosfilt
from scipy.io.wavfile import write

rate = 44100
seconds = 1.35
t = np.arange(round(rate * seconds)) / rate
rng = np.random.default_rng(20261003)
noise = rng.standard_normal(len(t))
# Broadband breath with a softer low-air component; no flame crackle.
air = sosfilt(butter(2, [450, 5500], btype='bandpass', fs=rate, output='sos'), noise)
body = sosfilt(butter(2, [160, 1100], btype='bandpass', fs=rate, output='sos'), noise)
attack = 1 - np.exp(-t / 0.07)
release = np.maximum(0, 1 - t / seconds) ** 1.5
modulation = 0.93 + 0.05 * np.sin(2 * np.pi * 5.1 * t) + 0.02 * np.sin(2 * np.pi * 11 * t)
signal = (0.7 * air + 0.3 * body) * attack * release * modulation
signal *= 0.65 / np.max(np.abs(signal))
output = Path(__file__).resolve().parents[1] / 'src/main/resources/assets/dobroszycecasino/sounds/smoke_exhale.ogg'
output.parent.mkdir(parents=True, exist_ok=True)
with tempfile.TemporaryDirectory() as tmp:
    wav = Path(tmp) / 'breath.wav'
    write(wav, rate, np.int16(signal * 32767))
    subprocess.run(['ffmpeg', '-v', 'error', '-y', '-i', str(wav), '-c:a', 'libvorbis', '-q:a', '5', str(output)], check=True)
print(output)
