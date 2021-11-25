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

public class ImageProcessor {
    private int SIZE;

    public ImageProcessor(int SIZE) {
	this.SIZE = SIZE;
    }

    public BufferedImage VBlur(BufferedImage image, int xPixel, int yPixel) {
	/* Total sum of pixels */
	Util.LongColor averageColor = new Util.LongColor(0, 0, 0);

	int len = Math.min(image.getWidth(), xPixel + SIZE);
	// adding pixels
	for (int i = xPixel; i < len; i++) {
	    averageColor.add(image.getRGB(i, yPixel));
	}

	// find average
	int divisor = Math.min(SIZE, image.getWidth() - xPixel);
	averageColor.div(divisor);

	// set pixels
	for (int i = xPixel; i < len; i++) {
	    image.setRGB(i, yPixel, averageColor.getRGB());
	}
	return image;
    }

    public BufferedImage HBlur(BufferedImage image, int xPixel, int yPixel) {
	/* Total sum of pixels */
	Util.LongColor averageColor = new Util.LongColor(0, 0, 0);

	int len = Math.min(image.getHeight(), yPixel + SIZE);
	// adding pixels
	for (int i = yPixel; i < len; i++) {
	    averageColor.add(image.getRGB(xPixel, i));
	}

	// find average
	int divisor = Math.min(SIZE, image.getHeight() - yPixel);
	averageColor.div(divisor);

	// set pixels
	for (int i = yPixel; i < len; i++) {
	    image.setRGB(xPixel, i, averageColor.getRGB());
	}

	return image;
    }

}
