#!/usr/bin/env python3
"""Turn the clean 'No more bets' WAV into a low, nasal villager-style voice.
Usage: python tools/modulate_dealer_voice.py /path/to/no_more_bets.wav
Requires ffmpeg with rubberband. Always use the clean source, not the output OGG.
"""
from pathlib import Path
import subprocess
import sys

source = Path(sys.argv[1])
output = Path(__file__).resolve().parents[1] / 'src/main/resources/assets/dobroszycecasino/sounds/no_more_bets.ogg'
filters = ','.join([
    'silenceremove=start_periods=1:start_threshold=-45dB',
    'areverse', 'silenceremove=start_periods=1:start_threshold=-45dB', 'areverse',
    # Lower both pitch and formants for the villager's chesty, rounded "hrrm".
    'rubberband=tempo=0.92:pitch=0.62:formant=shifted',
    'highpass=f=90', 'lowpass=f=3200',
    'equalizer=f=380:t=q:w=1.0:g=4',
    'equalizer=f=1050:t=q:w=1.4:g=6',
    'vibrato=f=5.4:d=0.12',
    'acompressor=threshold=0.16:ratio=2.5:attack=8:release=90',
    'apad=pad_dur=0.15', 'loudnorm=I=-18:TP=-2:LRA=7',
])
subprocess.run(['ffmpeg', '-v', 'error', '-y', '-i', str(source), '-af', filters,
                '-ac', '1', '-ar', '44100', '-c:a', 'libvorbis', '-q:a', '5', str(output)], check=True)
print(output)
