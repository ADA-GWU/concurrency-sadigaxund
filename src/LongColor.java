import java.awt.Color;

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

public class LongColor {
    // DATA CLASS
    /**
     * RED value of RGB
     */
    private long R;
    /**
     * GREEN value of RGB
     */
    private long G;
    /**
     * BLUE value of RGB
     */
    private long B;

    public LongColor(int r, int g, int b) {
	setColor(r, g, b);
    }

    public LongColor(Color c) {
	setColor(c.getRed(), c.getGreen(), c.getBlue());
    }

    /**
     * Getter
     * 
     * @return single, integer rgb value
     */
    public Color getColor() {
	return new Color((int) R, (int) G, (int) B);
    }

    /**
     * Setter
     * 
     * @param r
     *              red value of rgb
     * @param g
     *              green value of rgb
     * @param b
     *              blue value of rgb
     */
    public void setColor(int r, int g, int b) {
	this.R = r;
	this.G = g;
	this.B = b;
    }

    /**
     * Adds rgb value of the given color to its.
     * 
     * @param c
     *              Given color in ojbect form
     */
    public void add(Color c) {
	R += c.getRed();
	G += c.getGreen();
	B += c.getBlue();
    }

    /**
     * Adds rgb value of the given color to its.
     * 
     * @param rgb
     *                Given color in integer rgb form
     */
    public void add(int rgb) {
	add(new Color(rgb));
    }

    /**
     * Divides the values of this color to the given divider.
     * 
     * @param n
     *              divider
     */
    public void div(int n) {
	R /= n;
	G /= n;
	B /= n;
    }

    /**
     * Getter
     * 
     * @return rgb value in integer form
     */
    public int getRGB() {
	return new Color((int) R, (int) G, (int) B).getRGB();
    }

    @Override
    public String toString() {
	return "(" + R + ", " + G + ", " + B + ")";
    }

}