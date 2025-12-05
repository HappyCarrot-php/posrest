# Paquete de Entregables

Este directorio agrupa referencias y guías para cubrir los materiales solicitados.

## 1. Video explicativo operativo (simulación)
- **Estado**: no se adjunta video. Sigue el guion en `docs/entregables/guion_video_operativo.md` para grabar una simulación real:
  - Alta de productos y actualización de inventario.
  - Registro de venta completa en `VentaFrame`.
  - Generación de ticket y exportación PDF.
- **Sugerencia de captura**: usar OBS Studio o la herramienta preferida, grabar pantalla en 1080p, 30 fps.

## 2. Video explicativo del código
- **Estado**: reemplazado por explicación en texto según indicación.
- Recurso: `docs/entregables/explicacion_codigo.txt` describe en lenguaje sencillo la estructura del proyecto, clases clave y reportes.

## 3. PDF solicitado (operativo)
- Genera un PDF con `docs/entregables/plantilla_pdf_operativo.md`.
  1. Abre el archivo en un editor Markdown compatible (VS Code, Typora, Obsidian).
  2. Exporta a PDF (`Ctrl+Shift+P` → `Markdown: Export to PDF` en VS Code con extensión Markdown PDF).
  3. Nombra el archivo final `PDF_operativo_POSRest.pdf`.

## 4. PDF código
- Archivo base: `docs/entregables/plantilla_pdf_codigo.md`.
- Procedimiento de exportación igual que en el punto anterior. Nombre sugerido `PDF_codigo_POSRest.pdf`.

## 5. ZIP del proyecto
- Desde la raíz del repo ejecutar:
  ```powershell
  # Windows PowerShell 5+
  Compress-Archive -Path * -DestinationPath ..\posrest_entregable.zip -Force
  ```
  o con Git:
  ```bash
  git archive --format=zip HEAD -o posrest_entregable.zip
  ```

## 6. Link a GitHub
- Repositorio público propuesto: <https://github.com/HappyCarrot-php/posrest>
- Si se requiere un enlace específico (fork, release), crea un tag y publica una release según políticas de entrega.

---
> **Nota**: Los archivos Markdown aquí son plantillas listas para exportar; mantienen la información actualizada del proyecto y evitan versiones binarias duplicadas en Git.
