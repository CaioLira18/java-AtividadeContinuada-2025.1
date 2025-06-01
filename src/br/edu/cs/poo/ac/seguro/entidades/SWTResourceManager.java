/**
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2003 - 2005, Instantiations, Inc. <br>
 * All Rights Reserved
 */
package br.edu.cs.poo.ac.seguro.entidades;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class for managing OS resources associated with SWT controls such as
 * colors, fonts, images, etc.
 *
 * !!! IMPORTANT !!! Application code must explicitly invoke the
 * <code>dispose()</code> method to release the operating system resources
 * managed by cached objects when those objects and OS resources are no longer
 * needed (e.g. on application shutdown)
 */
public class SWTResourceManager {

	public static void dispose() {
		disposeColors();
		disposeFonts();
		disposeImages();
		disposeCursors();
	}

	//////////////////////////////
	// Color support
	//////////////////////////////

	private static HashMap<RGB, Color> m_ColorMap = new HashMap<RGB, Color>();

	public static Color getColor(final int systemColorID) {
		final Display display = Display.getCurrent();
		return display.getSystemColor(systemColorID);
	}

	public static Color getColor(final int r, final int g, final int b) {
		return getColor(new RGB(r, g, b));
	}

	public static Color getColor(final RGB rgb) {
		Color color = m_ColorMap.get(rgb);
		if (color == null) {
			final Display display = Display.getCurrent();
			color = new Color(display, rgb);
			m_ColorMap.put(rgb, color);
		}
		return color;
	}

	public static void disposeColors() {
		for (final Color color : m_ColorMap.values()) {
			color.dispose();
		}
		m_ColorMap.clear();
	}

	//////////////////////////////
	// Image support
	//////////////////////////////

	private static HashMap<String, Image> m_ClassImageMap = new HashMap<String, Image>();
	private static HashMap<Image, HashMap<Image, Image>> m_ImageToDecoratorMap = new HashMap<Image, HashMap<Image, Image>>();

	public static final int TOP_LEFT = 1;
	public static final int TOP_RIGHT = 2;
	public static final int BOTTOM_LEFT = 3;
	public static final int BOTTOM_RIGHT = 4;

	public static void disposeImages() {
		for (final Image image : m_ClassImageMap.values()) {
			image.dispose();
		}
		m_ClassImageMap.clear();

		for (final HashMap<Image, Image> decoratedMap : m_ImageToDecoratorMap.values()) {
			for (final Image image : decoratedMap.values()) {
				image.dispose();
			}
		}
		m_ImageToDecoratorMap.clear();
	}

	public static void disposeImages(final String section) {
		for (final Iterator<String> I = m_ClassImageMap.keySet().iterator(); I.hasNext();) {
			final String key = I.next();
			if (!key.startsWith(section + '|')) {
				continue;
			}
			final Image image = m_ClassImageMap.get(key);
			image.dispose();
			I.remove();
		}
	}

	/**
	 * Returns an image from a file path.
	 *
	 * @param path the absolute or relative file path to the image
	 * @return Image the loaded image
	 */
	public static Image getImage(final String path) {
		Image image = m_ClassImageMap.get(path);
		if (image == null) {
			image = new Image(Display.getCurrent(), path);
			m_ClassImageMap.put(path, image);
		}
		return image;
	}

	/**
	 * Returns an image stored in the file with the given name relative to the
	 * specified class
	 *
	 * @param clazz Class The class relative to which to find the image
	 * @param path  String The path to the image file
	 * @return Image The image stored in the file
	 */
	public static Image getImage(Class<?> clazz, String path) {
		final String key = clazz.getName() + '|' + path;
		Image image = m_ClassImageMap.get(key);
		if (image == null) {
			image = new Image(Display.getCurrent(), clazz.getResourceAsStream(path));
			m_ClassImageMap.put(key, image);
		}
		return image;
	}

	//////////////////////////////
	// Font support
	//////////////////////////////

	private static HashMap<String, Font> m_FontMap = new HashMap<String, Font>();
	private static HashMap<Font, Font> m_FontToBoldFontMap = new HashMap<Font, Font>();

	public static Font getFont(final String name, final int height, final int style) {
		return getFont(name, height, style, false, false);
	}

	public static Font getFont(final String name, final int size, final int style, final boolean strikeout,
							   final boolean underline) {
		final String fontName = name + '|' + size + '|' + style + '|' + strikeout + '|' + underline;
		Font font = m_FontMap.get(fontName);
		if (font == null) {
			final FontData fontData = new FontData(name, size, style);
			if (strikeout || underline) {
				try {
					final Class<?> logFontClass = Class.forName("org.eclipse.swt.internal.win32.LOGFONT");
					final Object logFont = FontData.class.getField("data").get(fontData);
					if (logFont != null && logFontClass != null) {
						if (strikeout) {
							logFontClass.getField("lfStrikeOut").set(logFont, new Byte((byte) 1));
						}
						if (underline) {
							logFontClass.getField("lfUnderline").set(logFont, new Byte((byte) 1));
						}
					}
				} catch (final Throwable e) {
					System.err.println("Unable to set underline or strikeout (probably on a non-Windows platform). " + e);
				}
			}
			font = new Font(Display.getCurrent(), fontData);
			m_FontMap.put(fontName, font);
		}
		return font;
	}

	public static Font getBoldFont(final Font baseFont) {
		Font font = m_FontToBoldFontMap.get(baseFont);
		if (font == null) {
			final FontData fontDatas[] = baseFont.getFontData();
			final FontData data = fontDatas[0];
			font = new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.BOLD);
			m_FontToBoldFontMap.put(baseFont, font);
		}
		return font;
	}

	public static void disposeFonts() {
		for (final Font font : m_FontMap.values()) {
			font.dispose();
		}
		m_FontMap.clear();
		for (final Font font : m_FontToBoldFontMap.values()) {
			font.dispose();
		}
		m_FontToBoldFontMap.clear();
	}

	//////////////////////////////
	// CoolBar support
	//////////////////////////////

	public static void fixCoolBarSize(final CoolBar bar) {
		final CoolItem[] items = bar.getItems();
		for (final CoolItem item : items) {
			if (item.getControl() == null) {
				item.setControl(new Canvas(bar, SWT.NONE) {
					@Override
					public Point computeSize(final int wHint, final int hHint, final boolean changed) {
						return new Point(20, 20);
					}
				});
			}
		}
		for (final CoolItem item : items) {
			final Control control = item.getControl();
			control.pack();
			final Point size = control.getSize();
			item.setSize(item.computeSize(size.x, size.y));
		}
	}

	//////////////////////////////
	// Cursor support
	//////////////////////////////

	private static HashMap<Integer, Cursor> m_IdToCursorMap = new HashMap<Integer, Cursor>();

	public static Cursor getCursor(final int id) {
		final Integer key = new Integer(id);
		Cursor cursor = m_IdToCursorMap.get(key);
		if (cursor == null) {
			cursor = new Cursor(Display.getDefault(), id);
			m_IdToCursorMap.put(key, cursor);
		}
		return cursor;
	}

	public static void disposeCursors() {
		for (final Cursor cursor : m_IdToCursorMap.values()) {
			cursor.dispose();
		}
		m_IdToCursorMap.clear();
	}
}
