import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
    /**
     * The size of the blurring block
     */
    private int SIZE;

    public ImageProcessor(int SIZE) {
	this.SIZE = SIZE;
    }

    /**
     * <h1>Vertical Blur</h1> Method that blurs a block of the image from given
     * range. In mathematical term the range is in the following form:<br>
     * <code>[x, x] & [y, y + SIZE)</code>
     * <ul>
     * <li><b>x</b> stands for <code>xPixel</code></li>
     * <li><b>y</b> stands for <code>yPixel</code></li>
     * </ul>
     * 
     * 
     * @param image
     *                   the given image to blur a block from
     * @param xPixel
     *                   starting coordinate on the x-axis.
     * @param yPixel
     *                   starting coordinate on the y-axis.
     * @return partially blurred version of the image
     */
    public BufferedImage VBlur(BufferedImage image, int xPixel, int yPixel) {
	/* Total sum of pixels */
	LongColor averageColor = new LongColor(0, 0, 0);
	/***
	 * IMPORTANT: Since the dimensions of the image is not limited, they can
	 * sometimes be divided by the <b>size of block</b> unevenly, with remainder. In
	 * order to avoid <code>ArrayIndexOutOfBoundsException</code> we take minimum
	 * between the given range and the max possible index.
	 ***/
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

    /**
     * <h1>Vertical Blur</h1> Method that blurs a block of the image from given
     * range. In mathematical term the range is in the following form:<br>
     * <code>[x, x + SIZE) & [y, y]</code>
     * <ul>
     * <li><b>x</b> stands for <code>xPixel</code></li>
     * <li><b>y</b> stands for <code>yPixel</code></li>
     * </ul>
     * 
     * 
     * @param image
     *                   the given image to blur a block from
     * @param xPixel
     *                   starting coordinate on the x-axis.
     * @param yPixel
     *                   starting coordinate on the y-axis.
     * @return partially blurred version of the image
     */
    public BufferedImage HBlur(BufferedImage image, int xPixel, int yPixel) {
	/* Total sum of pixels */
	LongColor averageColor = new LongColor(0, 0, 0);
	/***
	 * IMPORTANT: Since the dimensions of the image is not limited, they can
	 * sometimes be divided by the <b>size of block</b> unevenly, with remainder. In
	 * order to avoid <code>ArrayIndexOutOfBoundsException</code> we take minimum
	 * between the given range and the max possible index.
	 ***/
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

    /**
     * The method used to save the given image to the file in jpg format
     * 
     * @param image
     *                  the source of Image to be written
     * @param out
     *                  output path to which the image is going to be saved
     * @throws IOException
     *                         If the saving was unsuccessful the following message
     *                         is printed:<br>
     *                         <code>"Image was not saved successfully!"</code>
     */
    public static void writeImage(BufferedImage image, String out) throws IOException {
	/* write the image */
	try {
	    File file = new File("./" + out);
	    ImageIO.write(image, "jpg", file);
	} catch (IOException e) {
	    throw new IOException("Image was not saved successfully!");
	}
    }

    /**
     * The method is used to resize a given image
     * 
     * @param image
     *                      an image to be resized
     * @param newWidth
     *                      desired width of an image
     * @param newHeight
     *                      desired height of an image
     * @return a resized image
     */
    public static BufferedImage resize(BufferedImage image, int newWidth, int newHeight) {
	Image tmp = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

	Graphics2D g2d = newImage.createGraphics();
	g2d.drawImage(tmp, 0, 0, null);
	g2d.dispose();

	return newImage;
    }
}
