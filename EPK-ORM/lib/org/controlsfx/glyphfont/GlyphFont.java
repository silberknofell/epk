/**
 * Copyright (c) 2013, ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.controlsfx.glyphfont;

import java.io.InputStream;
import java.util.Map;

import com.sun.javafx.css.StyleManager;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *  Represents a glyph font, which can be loaded locally or from a specified URL.
 *  {@link Glyph}s can be created easily using specified character defined in the
 *  font. For example, &#92;uf013 in FontAwesome is used to represent
 *  a gear icon.
 *  
 *  <p>To simplify glyph customization, methods can be chained, for example:
 *   
 *  <pre>
 *  Glyph glyph = fontAwesome.fontSize(28).fontColor(Color.RED).create('&#92;uf013'); //GEAR
 *  </pre>
 *  
 *  <p>Here's a screenshot of two font packs being used to render images into 
 *  JavaFX Button controls:
 *  
 * <br>
 * <center><img src="glyphFont.png"></center>
 */
public abstract class GlyphFont {
    
    static {
        StyleManager.getInstance().addUserAgentStylesheet(
                GlyphFont.class.getResource("glyphfont.css").toExternalForm()); //$NON-NLS-1$
    }

	private final String fontName;
	
	private final double defaultSize;
	private double size;
	private Color color;
	
	private boolean  fontLoaded = false;
	private Runnable fontLoader = null;
	

	/**
	 * Loads glyph font from specified {@link InputStream}
	 * @param fontName glyph font name
	 * @param defaultSize default font size
	 * @param in input stream to load the font from
	 */
	public GlyphFont( String fontName, int defaultSize, final InputStream in ) {
		this.fontName = fontName;
		this.defaultSize = defaultSize;
		this.size = defaultSize;
		fontLoader = new Runnable() {
			public void run() {
				Font.loadFont(in, -1);
				fontLoaded = true;
			}
		};
	}
	
	/**
	 * Load glyph font from specified URL. 
	 * @param fontName glyph font name
	 * @param defaultSize default font size
	 * @param urlStr A URL to load the font from
	 */
	public GlyphFont( String fontName, int defaultSize, final String urlStr ) {
		this.fontName = fontName;
		this.defaultSize = defaultSize;
		this.size = defaultSize;
		fontLoader = new Runnable() {
			public void run() {
				Font.loadFont(urlStr, -1);
				fontLoaded = true;
			}
		};
	}
	
	/**
	 * Returns font name
	 * @return font name
	 */
	public String getName() {
		return fontName;
	}
	
	/**
	 * Returns the default font size
	 * @return default font size
	 */
	public double getDefaultSize() {
		return defaultSize;
	}
	
	/**
	 * Sets font size
	 * @param size
	 * @return font instance so the calls can be chained
	 */
	public GlyphFont fontSize(double size) {
	    this.size = size;
	    return this;
	}
	
	/**
	 * Sets font color
	 * @param color
	 * @return font instance so the calls can be chained
	 */
	public GlyphFont fontColor(Color color) {
	    this.color = color;
	    return this;
	}
	
	/**
	 * Creates an instance of {@link Glyph} using specified font character
	 * @param character font character
	 * @return instance of {@link Glyph}
	 */
	public Glyph create(char character) {
		if ( !fontLoaded ) fontLoader.run();
	    return new Glyph(fontName, character, size, color);
	}
	
	/**
	 * Creates and instance of {@link Glyph} using glyph name
	 * @param glyphName glyph name
	 * @return glyph by its name or null if name is not found
	 */
	public Glyph create(String glyphName) {
		Character ch = getGlyphs().get(glyphName);
		return ch==null?null:create(ch);
	}
	
	/**
	 * Returns glyph dictionary
	 * @return {@link Map} of glyph name to character
	 */
	public abstract Map<String, Character> getGlyphs();
}
