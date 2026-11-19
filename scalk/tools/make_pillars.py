#!/usr/bin/env python3
"""
Genera los pilares de scalk (features + placed features) del bioma scalk_forest.

Cada pilar es un conjunto de bloques de scalk colocados con 'simple_block' + 'offset'.
La altura de cada columna baja de forma gradual desde el centro hacia afuera, asi el
pilar tiene volumen (base ancha, escalones, punta). Cada variante tiene su propio seed,
asi que la forma es irregular y no todos los pilares se ven iguales.

Uso (desde la carpeta scalk/):   python tools/make_pillars.py
Edita VARIANTS para cambiar tamanos, o BLOCK para cambiar el bloque.
"""
import json
import math
import random
from pathlib import Path

BLOCK = "scalk:scalk"

# nombre, altura maxima H, radio de la base R, estiramiento en x, estiramiento en z, seed
VARIANTS = [
    ("small_a",  5, 2.0, 1.00, 1.00, 11),
    ("small_b",  6, 2.2, 1.25, 0.90, 12),
    ("medium_a", 8, 3.0, 1.00, 1.00, 13),
    ("medium_b", 9, 3.0, 0.90, 1.25, 14),
    ("large",   12, 4.0, 1.10, 1.00, 15),
]

# (variante, probabilidad) en orden; la ultima del VARIANTS que no salga queda como 'default'
SELECTOR = [("large", 0.08), ("medium_a", 0.25), ("medium_b", 0.25), ("small_a", 0.50)]
SELECTOR_DEFAULT = "small_b"

DEPTH = -2  # los pilares se hunden hasta aqui, para no quedar flotando en pendientes


def column_heights(H, R, ax, az, seed):
    rng = random.Random(seed)
    cols = {}
    reach = R + 0.5
    span = int(math.ceil(R * max(ax, az))) + 2
    for dx in range(-span, span + 1):
        for dz in range(-span, span + 1):
            d = math.hypot(dx / ax, dz / az)
            if d > reach + 0.8:
                continue
            if d <= reach:
                h = H * (1.0 - (d / reach) ** 1.5)
                h = int(round(h))
                if d > 0.9 and rng.random() < 0.25:
                    h -= 1  # borde un poco irregular
                h = max(h, 0)
                if (dx, dz) == (0, 0):
                    h = H
                cols[(dx, dz)] = h
            elif rng.random() < 0.7:
                cols[(dx, dz)] = 0  # "falda" a ras de suelo
    return cols


def blocks_for(cols):
    out = []
    for (dx, dz), h in sorted(cols.items()):
        if h >= 1:
            ys = range(DEPTH, h)
        else:
            ys = [-1]
        for y in ys:
            out.append((dx, y, dz))
    return out


def feature_json(blocks):
    entries = []
    for dx, y, dz in blocks:
        entries.append({
            "feature": {"type": "minecraft:simple_block", "to_place": {"id": BLOCK}},
            "placement": [{"type": "minecraft:offset", "x": dx, "y": y, "z": dz}],
        })
    return {"type": "minecraft:sequence", "features": entries}


def write_array_per_line(path, data):
    """JSON valido, con una feature por linea para que sea legible y no ocupe miles de lineas."""
    head = '{\n  "type": "minecraft:sequence",\n  "features": [\n'
    body = ",\n".join("    " + json.dumps(e, separators=(", ", ": ")) for e in data["features"])
    path.write_text(head + body + "\n  ]\n}\n", encoding="utf-8")


def main():
    root = Path(__file__).resolve().parent.parent
    base = root / "src/main/resources/data/scalk/worldgen"
    (base / "feature").mkdir(parents=True, exist_ok=True)
    (base / "placed_feature").mkdir(parents=True, exist_ok=True)

    for name, H, R, ax, az, seed in VARIANTS:
        blocks = blocks_for(column_heights(H, R, ax, az, seed))
        write_array_per_line(base / "feature" / f"scalk_pillar_{name}.json", feature_json(blocks))
        placed = {"feature": f"scalk:scalk_pillar_{name}", "placement": []}
        (base / "placed_feature" / f"scalk_pillar_{name}.json").write_text(
            json.dumps(placed, indent=2) + "\n", encoding="utf-8")
        print(f"{name}: {len(blocks)} bloques")

    selector = {
        "type": "minecraft:random_selector",
        "default": f"scalk:scalk_pillar_{SELECTOR_DEFAULT}",
        "features": [{"chance": c, "feature": f"scalk:scalk_pillar_{n}"} for n, c in SELECTOR],
    }
    (base / "feature" / "scalk_pillar.json").write_text(json.dumps(selector, indent=2) + "\n", encoding="utf-8")

    # Donde y cuantos: 1 a 3 intentos por chunk, solo en tierra firme del bioma.
    placed_main = {
        "feature": "scalk:scalk_pillar",
        "placement": [
            {"type": "minecraft:count",
             "count": {"type": "minecraft:uniform", "min_inclusive": 1, "max_inclusive": 3}},
            {"type": "minecraft:in_square"},
            {"type": "minecraft:surface_water_depth_filter", "max_water_depth": 0},
            {"type": "minecraft:heightmap", "heightmap": "WORLD_SURFACE_WG"},
            {"type": "minecraft:biome"},
        ],
    }
    (base / "placed_feature" / "scalk_pillar.json").write_text(json.dumps(placed_main, indent=2) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
