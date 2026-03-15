# Documentación de Variables del Sistema de Mensajes

Las siguientes variables (placeholders) se pueden usar en los mensajes del archivo `config.yml`:

## Variables Comunes

- `{location}` - Coordenadas de ubicación del spawn (world:x:y:z)
- `{status}` - Estado booleano habilitado/deshabilitado con colores
- `{message}` - Texto de mensaje personalizado

## Variables por Categoría

### Comandos de Ayuda
No requiere variables especiales

### Comandos de Spawn
- `{location}` - Coordenadas del punto de spawn establecido

### Comandos de Estado
- `{location}` - Ubicación actual del spawn
- `{message}` - Mensaje de bienvenida completo

### Comandos de Toggle/Debug/ForceSpawn/WelcomeMessage
- `{status}` - Estado del sistema (habilitado/deshabilitado)

### Mensajes del Sistema (Startup/Shutdown)
- `{plugin_name}` - Nombre del plugin
- `{version}` - Versión del plugin
- `{author}` - Autor del plugin

## Uso de Variables

Las variables se reemplazan automáticamente en el mensaje por su valor correspondiente.
Todas las variables soportan formato MiniMessage para colores y estilos.

## Ejemplo

```yaml
spawn:
    set: "<green>Ubicación establecida en:</green> <yellow>{location}</yellow>"
```
