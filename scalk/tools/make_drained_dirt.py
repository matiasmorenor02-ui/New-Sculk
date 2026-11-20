#!/usr/bin/env python3
"""Genera la textura de la tierra drenada (gris palido, con motas como la tierra normal).
No necesita las texturas de vanilla. Uso (desde scalk/):  python tools/make_drained_dirt.py"""
import random
from pathlib import Path
from PIL import Image

OUT = Path(__file__).resolve().parent.parent / "src/main/resources/assets/scalk/textures/block/drained_dirt.png"
SHADES = [(92, 92, 88), (106, 106, 102), (120, 120, 116), (134, 134, 130), (148, 148, 143), (162, 162, 156)]
WEIGHTS = [1, 3, 5, 5, 3, 1]

rng = random.Random(2026)
im = Image.new("RGBA", (16, 16))
px = im.load()
for y in range(16):
    for x in range(16):
        px[x, y] = rng.choices(SHADES, WEIGHTS)[0] + (255,)
# unas pocas piedritas oscuras y claras de 2 pixeles, para que no se vea como puro ruido
for _ in range(9):
    x, y = rng.randrange(15), rng.randrange(16)
    c = rng.choice([SHADES[0], SHADES[5]])
    px[x, y] = c + (255,)
    px[x + 1, y] = c + (255,)
OUT.parent.mkdir(parents=True, exist_ok=True)
im.save(OUT)
print("textura escrita en", OUT)
