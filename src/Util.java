import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/***************************************************************************
 * MIT License
 * 
 * Copyright (c) 2021 Sadig Akhund
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * 
 **************************************************************************/

public class Util {

    public static void pause(long ms) {
	try {
	    Thread.sleep(ms);
	} catch (InterruptedException e) {
	}
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
	Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	Graphics2D g2d = dimg.createGraphics();
	g2d.drawImage(tmp, 0, 0, null);
	g2d.dispose();

	return dimg;
    }

    // AUX METHODS
    static class LongColor {
	long R;
	long G;
	long B;

	public LongColor(int r, int g, int b) {
	    setColor(r, g, b);
	}

	public LongColor(Color c) {
	    setColor(c.getRed(), c.getGreen(), c.getBlue());
	}

	public Color getColor() {
	    return new Color((int) R, (int) G, (int) B);
	}

	public void setColor(int r, int g, int b) {
	    this.R = r;
	    this.G = g;
	    this.B = b;
	}

	public void add(Color c) {
	    R += c.getRed();
	    G += c.getGreen();
	    B += c.getBlue();
	}

	public void add(int rgb) {
	    add(new Color(rgb));
	}

	public void div(int n) {
	    R /= n;
	    G /= n;
	    B /= n;
	}

	public int getRGB() {
	    return new Color((int) R, (int) G, (int) B).getRGB();
	}

	@Override
	public String toString() {
	    return "(" + R + ", " + G + ", " + B + ")";
	}

    }
}
