package com.restaurante.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.restaurante.controller.VentaController;
import com.restaurante.model.Ticket;
import com.restaurante.model.Usuario;
import com.restaurante.util.FinancialUtils;
import com.restaurante.util.FinancialUtils.TotalesVenta;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.awt.Color;

/**
 * Genera un PDF estilizado para los tickets y lo abre automáticamente en el navegador.
 */
public final class TicketPdfGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final Font FONT_TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    private static final Font FONT_SUBTITLE = FontFactory.getFont(FontFactory.HELVETICA, 11);
    private static final Font FONT_SECTION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font FONT_TEXT = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private static final Font FONT_TABLE_HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD, Color.WHITE);

    private TicketPdfGenerator() {
    }

    /**
     * Genera el PDF del ticket y lo abre en el navegador por defecto del sistema.
     *
     * @param ticket datos del ticket generado
     * @param usuario usuario que realizó la venta
     * @param items lista de artículos vendidos
     * @return ruta del archivo PDF generado
     * @throws IOException si falla la escritura del archivo
     * @throws DocumentException si no se puede crear el PDF
     */
    public static Path generarYMostrar(Ticket ticket, Usuario usuario, List<VentaController.ItemVenta> items)
        throws IOException, DocumentException {

        Path directorioTickets = Paths.get(System.getProperty("user.home"), "posrest", "tickets");
        Files.createDirectories(directorioTickets);

        String nombreArchivo = "ticket_" + ticket.getFolio() + ".pdf";
        Path archivoPdf = directorioTickets.resolve(nombreArchivo);

        try (OutputStream salida = Files.newOutputStream(archivoPdf)) {
            Document documento = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            agregarEncabezado(documento, ticket);
            List<VentaController.ItemVenta> itemsClonados = FinancialUtils.clonarItems(items);

            agregarDatosGenerales(documento, ticket, usuario);
            agregarTablaProductos(documento, itemsClonados);
            agregarResumen(documento, ticket, itemsClonados);

            documento.close();
        }

        DesktopUtils.abrirEnNavegador(archivoPdf);
        return archivoPdf;
    }

    private static void agregarEncabezado(Document documento, Ticket ticket) throws DocumentException {
        Paragraph titulo = new Paragraph("SISTEMA POS RESTAURANTE", FONT_TITLE);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        Paragraph folio = new Paragraph("Ticket de Venta • Folio " + ticket.getFolio(), FONT_SUBTITLE);
        folio.setAlignment(Element.ALIGN_CENTER);
        documento.add(folio);

        documento.add(new Paragraph(" "));
    }

    private static void agregarDatosGenerales(Document documento, Ticket ticket, Usuario usuario)
        throws DocumentException {
        Paragraph seccion = new Paragraph("Datos Generales", FONT_SECTION);
        documento.add(seccion);

        if (ticket.getFechaEmision() != null) {
            documento.add(new Paragraph("Fecha de emisión: " + ticket.getFechaEmision().format(DATE_FORMAT), FONT_TEXT));
        }
        if (usuario != null) {
            documento.add(new Paragraph("Atendió: " + usuario.getNombre(), FONT_TEXT));
        }
        documento.add(new Paragraph("Canal: Punto de Venta", FONT_TEXT));
        documento.add(new Paragraph(" "));
    }

    private static void agregarTablaProductos(Document documento, List<VentaController.ItemVenta> items)
        throws DocumentException {
        Paragraph seccion = new Paragraph("Detalle de Productos", FONT_SECTION);
        documento.add(seccion);

        PdfPTable tabla = new PdfPTable(new float[]{4f, 1f, 1f, 1.5f});
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(8);
        tabla.setSpacingAfter(8);
        tabla.setHeaderRows(1);

        agregarCeldaHeader(tabla, "Producto");
        agregarCeldaHeader(tabla, "Cant.");
        agregarCeldaHeader(tabla, "Precio");
        agregarCeldaHeader(tabla, "Importe");

        if (items != null) {
            for (VentaController.ItemVenta item : items) {
                tabla.addCell(crearCeldaTexto(item.getProducto().getNombre(), Element.ALIGN_LEFT));
                tabla.addCell(crearCeldaTexto(String.valueOf(item.getCantidad()), Element.ALIGN_CENTER));
                double precioUnitario = item.getProducto().getPrecio();
                tabla.addCell(crearCeldaTexto(formatearMoneda(precioUnitario), Element.ALIGN_RIGHT));
                tabla.addCell(crearCeldaTexto(formatearMoneda(item.getSubtotal()), Element.ALIGN_RIGHT));
            }
        }

        documento.add(tabla);
    }

    private static void agregarResumen(Document documento, Ticket ticket, List<VentaController.ItemVenta> items)
        throws DocumentException {
        Paragraph seccion = new Paragraph("Resumen Financiero", FONT_SECTION);
        documento.add(seccion);

        TotalesVenta totales = FinancialUtils.calcularTotales(items);
        double total = FinancialUtils.redondear(ticket.getTotal());
        double subtotal = totales.getSubtotal();
        double iva = FinancialUtils.redondear(total - subtotal);

        if (iva < 0) {
            iva = totales.getIva();
            subtotal = FinancialUtils.redondear(total - iva);
        }

        double pagado = FinancialUtils.redondear(total + ticket.getCambio());
        double cambio = FinancialUtils.redondear(ticket.getCambio());

        PdfPTable resumen = new PdfPTable(new float[]{2f, 1f});
        resumen.setWidthPercentage(70);
        resumen.setSpacingBefore(8);
        resumen.setHorizontalAlignment(Element.ALIGN_LEFT);

        agregarFilaResumen(resumen, "Subtotal", formatearMoneda(subtotal), false);
        agregarFilaResumen(resumen, "IVA (16%)", formatearMoneda(iva), false);
        agregarFilaResumen(resumen, "Total", formatearMoneda(total), true);
        agregarFilaResumen(resumen, "Pagado", formatearMoneda(pagado), false);
        agregarFilaResumen(resumen, "Cambio", formatearMoneda(cambio), cambio >= 0);

        documento.add(resumen);
        documento.add(new Paragraph(" "));

        Paragraph mensaje = new Paragraph("¡Gracias por su preferencia!", FONT_TEXT);
        mensaje.setAlignment(Element.ALIGN_CENTER);
        documento.add(mensaje);
    }

    private static void agregarCeldaHeader(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_TABLE_HEADER));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(6);
        celda.setBackgroundColor(new Color(15, 23, 42));
        celda.setBorderColor(new Color(226, 232, 240));
        tabla.addCell(celda);
    }

    private static PdfPCell crearCeldaTexto(String texto, int alignment) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_TEXT));
        celda.setHorizontalAlignment(alignment);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(6);
        celda.setBorderColor(new Color(226, 232, 240));
        return celda;
    }

    private static String formatearMoneda(double valor) {
        return String.format(Locale.US, "$%,.2f", valor);
    }

    private static void agregarFilaResumen(PdfPTable tabla, String etiqueta, String valor, boolean resaltar) {
        PdfPCell etiquetaCelda = new PdfPCell(new Phrase(etiqueta, resaltar ? FONT_SECTION : FONT_TEXT));
        etiquetaCelda.setBorderColor(new Color(226, 232, 240));
        etiquetaCelda.setPadding(6);
        etiquetaCelda.setHorizontalAlignment(Element.ALIGN_LEFT);

        Font fuenteValor = resaltar ? FONT_SECTION : FONT_TEXT;
        if ("Cambio".equalsIgnoreCase(etiqueta) && valor.startsWith("-")) {
            fuenteValor = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD, new Color(220, 38, 38));
        }

        PdfPCell valorCelda = new PdfPCell(new Phrase(valor, fuenteValor));
        valorCelda.setBorderColor(new Color(226, 232, 240));
        valorCelda.setPadding(6);
        valorCelda.setHorizontalAlignment(Element.ALIGN_RIGHT);

        if (resaltar) {
            etiquetaCelda.setBackgroundColor(new Color(239, 246, 255));
            valorCelda.setBackgroundColor(new Color(239, 246, 255));
        }

        tabla.addCell(etiquetaCelda);
        tabla.addCell(valorCelda);
    }
}
