package com.restaurante.util;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Métodos auxiliares para interactuar con el escritorio del sistema operativo.
 */
public final class DesktopUtils {

    private DesktopUtils() {
    }

    /**
     * Abre un archivo PDF en el navegador predeterminado del usuario.
     *
     * @param archivo ruta al archivo
     * @throws IOException si el archivo no puede abrirse
     */
    public static void abrirEnNavegador(Path archivo) throws IOException {
        if (!Desktop.isDesktopSupported()) {
            throw new IOException("El escritorio no está soportado en este sistema");
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            throw new IOException("No se puede abrir el navegador automáticamente");
        }

        desktop.browse(archivo.toUri());
    }
}
