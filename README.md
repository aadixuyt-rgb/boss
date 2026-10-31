# GhostPlayers — Fabric 1.21.4

Mod kliencki.

- **Prawy Shift** – włącz / wyłącz (domyślnie wyłączony).
- **K** – ustawienia: przycisk ON/OFF i suwak **ukrycia 0–100%** (0% = widoczni, 100% = całkiem niewidoczni). Wartość zapisuje się w `config/ghostplayers.properties`.

Gdy włączony:
- inni gracze są półprzezroczyści (krycie skina = 100% − ukrycie),
- **zawsze** bez nicka nad głową i bez pancerza (także elytry), niezależnie od suwaka,
- hitboxy **tylko graczy** są pomijane: celownik, bicie i interakcje przechodzą przez nich. Moby działają normalnie.

Przy 100% skin jest całkiem niewidoczny (trzymane przedmioty też znikają).

Oba klawisze zmienisz w Sterowanie → Różne.

## Build
Java 21: `./gradlew build` → `build/libs/ghostplayers-1.0.0.jar` (wymaga Fabric API 1.21.4).
