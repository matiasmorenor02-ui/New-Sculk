# Scalk — bosque de abedules corrompido (NeoForge 26.3 + TerraBlender)

Un bioma basado en el **old growth birch forest** con **parches largos de scalk** (el sculk
marrón-morado), **venas de scalk sobre los troncos** y árboles **drenados** (madera y hojas grises).

## Qué agrega
| Bloque | Equivale a |
|---|---|
| `scalk:scalk` | sculk |
| `scalk:scalk_vein` | sculk vein |
| `scalk:scalk_catalyst` | sculk catalyst |
| `scalk:scalk_sensor` | sculk sensor |
| `scalk:scalk_shrieker` | sculk shrieker |
| `scalk:drained_birch_log` / `scalk:drained_birch_leaves` | tronco / hojas de abedul, en gris |

Bioma: `scalk:scalk_forest`. Se coloca con una región de TerraBlender que **reemplaza el old growth
birch forest** en los mismos parámetros climáticos (peso 10, igual que el overworld vanilla: aprox. la
mitad de los old growth birch quedan corrompidos; cámbialo en `ScalkMod.REGION_WEIGHT`).

## Compilar
Requisitos: JDK 25 y Gradle (o genera el wrapper con `gradle wrapper`).

```
gradle build          # genera build/libs/scalk-1.0.0.jar
gradle runClient      # prueba en desarrollo (TerraBlender se carga desde libs/)
```
Para jugar: pon `scalk-1.0.0.jar` y el jar de TerraBlender en tu carpeta `mods`.
En `gradle.properties`, ajusta `neo_version` a tu versión exacta de NeoForge 26.3.

## Cómo funciona el scalk "con la misma funcionalidad"
Los bloques heredan las clases de vanilla (`SculkBlock`, `SculkVeinBlock`, `SculkCatalystBlock`,
`SculkSensorBlock`, `SculkShriekerBlock`) y reutilizan sus block entities. Como vanilla tiene
`Blocks.SCULK`, `SCULK_VEIN`, etc. escritos a mano dentro del código de propagación, hay 5 mixins
(`com.scalk.mixin`) que, **solo mientras actúa un catalizador de scalk o se genera un parche en el
bioma scalk**, cambian esos bloques por sus equivalentes de scalk (`ScalkContext`). El sculk vanilla
no se ve afectado.

## Cosas que puedes ajustar
* Cantidad de parches por chunk: `data/scalk/worldgen/placed_feature/scalk_patch.json` (`count`).
* Tamaño de los parches: `data/scalk/worldgen/feature/scalk_patch.json` (`charge_count`, `spread_attempts`, `spread_rounds`).
* Venas en troncos: `placed_feature/scalk_vein_on_logs.json` (`count`).
* Colores del bioma (pasto, agua, niebla): `data/scalk/worldgen/biome/scalk_forest.json`.
* Texturas: `tools/make_textures.py` las regenera desde las de vanilla (paletas `SCALK`, `ASH`, `LEAF`).

## Estado de verificación
Verificado aquí: todos los JSON son válidos; el código Java compila contra las clases reales de
Minecraft 26.3 y TerraBlender 26.3.0.0.9 (usando stubs para NeoForge/Mixin); los objetivos de los
mixins existen en el bytecode de 26.3.
**No verificado** (no pude ejecutar el juego ni descargar NeoForge): los nombres de la API de NeoForge
(`DeferredRegister.registerBlock`, `BlockEntityTypeAddBlocksEvent`), las versiones de Gradle/MDG, y el
aspecto final en el juego.
