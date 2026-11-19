#!/usr/bin/env python3
"""Genera las texturas de Scalk y del abedul drenado a partir de las texturas vanilla.
Uso: python3 make_textures.py <carpeta con assets/minecraft/textures/block de vanilla> <salida textures/block>"""
import sys, os, shutil
from PIL import Image

SRC, DST = sys.argv[1], sys.argv[2]
os.makedirs(DST, exist_ok=True)

def lerp(a, b, t): return tuple(a[i] + (b[i] - a[i]) * t for i in range(3))
def gradient(stops, t):
    t = max(0.0, min(1.0, t))
    for (t0, c0), (t1, c1) in zip(stops, stops[1:]):
        if t <= t1:
            return lerp(c0, c1, (t - t0) / (t1 - t0))
    return stops[-1][1]

# Scalk: de azul-verdoso a marron-morado
SCALK = [(0.00, (40, 29, 36)), (0.12, (58, 42, 53)), (0.45, (104, 72, 98)),
         (0.75, (150, 100, 142)), (1.00, (204, 146, 194))]
# Madera drenada: gris ceniza (con un leve tono frio)
ASH = [(0.0, (34, 33, 35)), (0.5, (104, 103, 102)), (1.0, (178, 176, 172))]
# Hojas drenadas: gris apagado con apenas un resto de verde
LEAF = [(0.0, (60, 62, 58)), (0.5, (112, 115, 108)), (1.0, (156, 158, 150))]

def recolor(name, stops, lo, hi, gamma=1.0, out_name=None):
    im = Image.open(os.path.join(SRC, name + ".png")).convert("RGBA")
    px = im.load()
    for y in range(im.height):
        for x in range(im.width):
            r, g, b, a = px[x, y]
            if a == 0: continue
            L = 0.299 * r + 0.587 * g + 0.114 * b
            t = ((L - lo) / (hi - lo)) ** gamma
            c = gradient(stops, t)
            px[x, y] = (int(c[0]), int(c[1]), int(c[2]), a)
    out = out_name or name
    im.save(os.path.join(DST, out + ".png"))
    mc = os.path.join(SRC, name + ".png.mcmeta")
    if os.path.exists(mc): shutil.copy(mc, os.path.join(DST, out + ".png.mcmeta"))

# --- bloques de scalk (sculk -> scalk)
for n in ["sculk", "sculk_vein", "sculk_catalyst_bottom", "sculk_catalyst_side",
          "sculk_catalyst_side_bloom", "sculk_catalyst_top", "sculk_catalyst_top_bloom",
          "sculk_sensor_bottom", "sculk_sensor_side", "sculk_sensor_top",
          "sculk_sensor_tendril_active", "sculk_sensor_tendril_inactive",
          "sculk_shrieker_bottom", "sculk_shrieker_side", "sculk_shrieker_top",
          "sculk_shrieker_inner_top", "sculk_shrieker_can_summon_inner_top"]:
    recolor(n, SCALK, 14, 175, 0.85, out_name=n.replace("sculk", "scalk"))

# --- madera y hojas drenadas
recolor("birch_log", ASH, 45, 255, 1.1, "drained_birch_log")
recolor("birch_log_top", ASH, 70, 255, 1.0, "drained_birch_log_top")
recolor("birch_leaves", LEAF, 105, 190, 1.0, "drained_birch_leaves")
print("texturas generadas en", DST)
