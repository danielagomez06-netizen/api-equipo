# API Equipo — Repositorio base GitFlow

Repositorio de práctica para el codelab **GitFlow colaborativo**.

## Estructura

```
src/main/java/com/equipo/api/
├── ApiApplication.java        ← no modificar
└── EquipoController.java      ← aquí agrega tu endpoint
```

## Cómo correr el proyecto

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080/equipo`.

## Ramas del proyecto

| Rama | Propósito |
|---|---|
| `main` | Código en producción (no hacer push directo) |
| `develop` | Integración del equipo (base para feature branches) |
| `feature/tu-nombre` | Tu rama de trabajo personal |

## Reglas del equipo

- Nunca hagas `push` directo a `main` ni a `develop`
- Siempre crea tu rama desde `develop`
- Haz `git pull origin develop` antes de abrir un Pull Request
- Resuelve los conflictos en tu máquina antes de hacer push
