/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.stampe.pdf;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * Loghi dell'ente usati nei test di stampa (Issue #902): un caso per ogni formato
 * e variante che un ente puo' caricare. Le immagini raster sono generate con
 * ImageIO; i formati che ImageIO non sa scrivere (JPEG CMYK, GIF animata, WebP)
 * sono file in src/test/resources/loghi.
 */
public final class LoghiDiProva {

	/**
	 * @param nome       nome del caso, usato nei test parametrici
	 * @param valore     il logo come viene salvato nel dominio, in base64 o data URI
	 * @param stampabile true se il logo deve comparire nel documento; false se deve
	 *                   essere rifiutato al caricamento e sostituito in stampa
	 */
	public record Logo(String nome, String valore, boolean stampabile) {
		@Override
		public String toString() {
			return this.nome;
		}
	}

	private static final int LARGHEZZA = 240;
	private static final int ALTEZZA = 120;

	private LoghiDiProva() {
	}

	/** Ogni logo in base64 semplice e come data URI, come lo salva la console. */
	public static List<Logo> tutti() {
		List<Logo> loghi = new ArrayList<>();
		for (Logo logo : base()) {
			loghi.add(logo);
		}
		for (Logo logo : base()) {
			if (logo.valore().startsWith("data:") || logo.nome().startsWith("base64-")) {
				continue;
			}
			loghi.add(new Logo(logo.nome() + " (data URI)", "data:" + mime(logo.nome()) + ";base64," + logo.valore(), logo.stampabile()));
		}
		return loghi;
	}

	private static List<Logo> base() {
		byte[] png = png(raster(BufferedImage.TYPE_INT_RGB));
		byte[] jpeg = scrivi(raster(BufferedImage.TYPE_INT_RGB), "jpeg");

		return List.of(
				// SVG
				svg("svg-semplice", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"240\" height=\"120\"><rect width=\"200\" height=\"80\" fill=\"#0066cc\"/><text x=\"10\" y=\"110\">Ente</text></svg>", true),
				svg("svg-doctype", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"240\" height=\"120\"><rect width=\"200\" height=\"80\"/></svg>", true),
				new Logo("svg-bom", b64(concat(new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}, "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"240\" height=\"120\"><rect width=\"200\" height=\"80\"/></svg>".getBytes(StandardCharsets.UTF_8))), true),
				svg("svg-png-incorporato", "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"240\" height=\"120\"><image width=\"240\" height=\"120\" xlink:href=\"data:image/png;base64," + b64(png) + "\"/></svg>", true),
				svg("svg-jpeg-incorporato", "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"240\" height=\"120\"><image width=\"240\" height=\"120\" xlink:href=\"data:image/jpeg;base64," + b64(jpeg) + "\"/></svg>", true),
				// non stampabili: Batik non carica risorse esterne (CVE-2022-44729, CVE-2022-44730)
				svg("svg-senza-namespace", "<svg width=\"240\" height=\"120\"><rect width=\"200\" height=\"80\"/></svg>", false),
				svg("svg-immagine-esterna", "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"240\" height=\"120\"><image width=\"240\" height=\"120\" xlink:href=\"https://example.org/logo.png\"/></svg>", false),
				svg("svg-css-import", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"240\" height=\"120\"><style>@import url(https://example.org/stile.css);</style><text x=\"10\" y=\"50\">Ente</text></svg>", false),
				svg("svg-riferimento-mancante", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"240\" height=\"120\"><rect width=\"200\" height=\"80\" fill=\"url(#mancante)\"/></svg>", false),
				svg("svg-malformato", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"240\" height=\"120\"><rect width=\"200\"", false),
				// PNG
				new Logo("png-rgb", b64(png), true),
				new Logo("png-rgba", b64(png(trasparente())), true),
				new Logo("png-palette-8bit", b64(png(indicizzata(false))), true),
				new Logo("png-grigi", b64(png(raster(BufferedImage.TYPE_BYTE_GRAY))), true),
				new Logo("png-16bit", b64(png(raster(BufferedImage.TYPE_USHORT_GRAY))), true),
				new Logo("png-4000x4000", b64(png(grande())), true),
				// JPEG
				new Logo("jpeg-rgb", b64(jpeg), true),
				new Logo("jpeg-grigi", b64(scrivi(raster(BufferedImage.TYPE_BYTE_GRAY), "jpeg")), true),
				new Logo("jpeg-progressivo", b64(jpegProgressivo()), true),
				new Logo("jpeg-cmyk", b64(risorsa("logo-cmyk.jpg")), true),
				// altri formati
				new Logo("gif", b64(scrivi(indicizzata(false), "gif")), true),
				new Logo("gif-trasparente", b64(scrivi(indicizzata(true), "gif")), true),
				new Logo("gif-animata", b64(risorsa("logo-animata.gif")), true),
				new Logo("bmp-24bit", b64(scrivi(raster(BufferedImage.TYPE_3BYTE_BGR), "bmp")), true),
				new Logo("bmp-8bit", b64(scrivi(indicizzata(false), "bmp")), true),
				new Logo("tiff", b64(scrivi(raster(BufferedImage.TYPE_INT_RGB), "tiff")), true),
				new Logo("webp", b64(risorsa("logo.webp")), false),
				// file corrotti
				new Logo("png-troncato", b64(Arrays.copyOf(png, png.length / 2)), false),
				new Logo("byte-casuali", b64(casuali()), false),
				new Logo("base64-non-valido", "%%% non e' base64 %%%", false));
	}

	private static String mime(String nome) {
		String formato = nome.substring(0, nome.indexOf('-') > 0 ? nome.indexOf('-') : nome.length());
		switch (formato) {
		case "svg":
			return "image/svg+xml";
		case "jpeg":
			return "image/jpeg";
		case "byte":
			return "application/octet-stream";
		default:
			return "image/" + formato;
		}
	}

	private static Logo svg(String nome, String svg, boolean stampabile) {
		return new Logo(nome, b64(svg.getBytes(StandardCharsets.UTF_8)), stampabile);
	}

	private static String b64(byte[] contenuto) {
		return Base64.getEncoder().encodeToString(contenuto);
	}

	private static byte[] concat(byte[] a, byte[] b) {
		byte[] c = Arrays.copyOf(a, a.length + b.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	private static BufferedImage raster(int tipo) {
		BufferedImage img = new BufferedImage(LARGHEZZA, ALTEZZA, tipo);
		Graphics2D g = img.createGraphics();
		g.setPaint(new GradientPaint(0, 0, new Color(0, 102, 204), LARGHEZZA, ALTEZZA, Color.WHITE));
		g.fillRect(0, 0, LARGHEZZA, ALTEZZA);
		g.setColor(Color.BLACK);
		g.drawString("Ente", 10, 60);
		g.dispose();
		return img;
	}

	private static BufferedImage trasparente() {
		BufferedImage img = new BufferedImage(LARGHEZZA, ALTEZZA, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(0, 102, 204, 128));
		g.fillOval(0, 0, LARGHEZZA, ALTEZZA);
		g.dispose();
		return img;
	}

	private static BufferedImage indicizzata(boolean trasparente) {
		byte[] r = {0, (byte) 255, 0};
		byte[] g = {102, (byte) 255, 0};
		byte[] b = {(byte) 204, (byte) 255, 0};
		IndexColorModel palette = trasparente ? new IndexColorModel(8, 3, r, g, b, 1) : new IndexColorModel(8, 3, r, g, b);
		BufferedImage img = new BufferedImage(LARGHEZZA, ALTEZZA, BufferedImage.TYPE_BYTE_INDEXED, palette);
		Graphics2D gr = img.createGraphics();
		gr.setColor(Color.WHITE);
		gr.fillRect(0, 0, LARGHEZZA, ALTEZZA);
		gr.setColor(new Color(0, 102, 204));
		gr.fillRect(20, 20, 200, 80);
		gr.dispose();
		return img;
	}

	private static BufferedImage grande() {
		BufferedImage img = new BufferedImage(4000, 4000, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(0, 102, 204));
		g.fillRect(0, 0, 4000, 4000);
		g.dispose();
		return img;
	}

	private static byte[] casuali() {
		byte[] b = new byte[2048];
		new Random(902).nextBytes(b);
		return b;
	}

	private static byte[] png(BufferedImage img) {
		return scrivi(img, "png");
	}

	private static byte[] scrivi(BufferedImage img, String formato) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			if (!ImageIO.write(img, formato, baos)) {
				throw new IllegalStateException("Nessun writer ImageIO per " + formato + " e tipo " + img.getType());
			}
			return baos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static byte[] jpegProgressivo() {
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
			writer.setOutput(ios);
			writer.write(null, new IIOImage(raster(BufferedImage.TYPE_INT_RGB), null, null), param);
			ios.flush();
			return baos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} finally {
			writer.dispose();
		}
	}

	private static byte[] risorsa(String nome) {
		try (InputStream is = LoghiDiProva.class.getResourceAsStream("/loghi/" + nome)) {
			if (is == null) {
				throw new IllegalStateException("Risorsa di test mancante: /loghi/" + nome);
			}
			return is.readAllBytes();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
