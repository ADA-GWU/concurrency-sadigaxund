import java.awt.Color;
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

public class Main {

    static BufferedImage image;
    static int size;
    static boolean isMultiThreaded;

    public static void main(String[] args) throws IOException {

	try {
	    if (args[0].equals("help"))
		printHelp();

	    processImage(args[0]);
	    processSize(args[1]);
	    processMode(args[2]);
	} catch (ArrayIndexOutOfBoundsException e) {
	    printError(); // TODO: Maybe specify: Not enough arguments
	}

	boxBlur();
    }

    static void boxBlur() {

	/* remove remainder from division and resize */
	int width = size * (image.getWidth() / size);
	int height = size * (image.getHeight() / size);
	image = resize(image, width, height);

	/* procces the image */
	for (int row = 0; row < width; row++)
	    for (int col = 0; col < height; col += size) {
		verticalBlur(row, col);
	    }

	for (int col = 0; col < height; col++)
	    for (int row = 0; row < width; row += size) {
		horizontalBlur(row, col);
	    }

	/* write the image */
	File file = new File("./blurredImage.png");

	try {
	    ImageIO.write(image, "png", file);
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    static void horizontalBlur(int xPixel, int yPixel) {
	// Total sum of pixels
	LongColor sumColor = new LongColor(new Color(0, 0, 0));

	// adding pixels
	for (int i = xPixel; i < xPixel + size; i++) {
	    Color rgb = new Color(image.getRGB(i, yPixel));
	    sumColor.add(rgb);
	}

	// find average
	sumColor.div(size);

	// set pixels
	for (int i = xPixel; i < xPixel + size; i++)
	    image.setRGB(i, yPixel, sumColor.getColor().getRGB());

    }

    static void verticalBlur(int xPixel, int yPixel) {
	// Total sum of pixels
	LongColor sumColor = new LongColor(new Color(0, 0, 0));

	// adding pixels
	for (int i = yPixel; i < yPixel + size; i++) {
	    Color rgb = new Color(image.getRGB(xPixel, i));
	    sumColor.add(rgb);
	}

	// find average
	sumColor.div(size);

	// set pixels
	for (int i = yPixel; i < yPixel + size; i++)
	    image.setRGB(xPixel, i, sumColor.getColor().getRGB());

    }

    static void processMode(String args) {

	switch (args) {
	case "S":
	    isMultiThreaded = false;
	    break;
	case "M":
	    isMultiThreaded = true;
	    break;
	default:
	    System.out.println("MODE!!!");
	    printError(); // TODO: Maybe specify: Unidentified mode type
	    break;
	}
    }

    static void processSize(String args) {
	try {
	    size = Integer.parseInt(args);
	} catch (NumberFormatException nfe) {
	    System.out.println("SIZE!!!");
	    printError(); // TODO: Maybe specify: Numeric error
	}
    }

    static void processImage(String args) {
	try {
	    image = ImageIO.read(new File("./" + args));
	} catch (IOException e) {
	    System.out.println("IMAGE!!!:" + args);
	    printError(); // TODO: Maybe specify: IO error
	}
    }

    static void printHelp() {

    }

    static void printError() {
	System.out.println("ERROR!!!");
	System.exit(0);
    }

    // AUX METHODS
    static class LongColor {
	long R;
	long G;
	long B;

	public LongColor(Color c) {
	    setColor(c);
	}

	public Color getColor() {
	    return new Color((int) R, (int) G, (int) B);
	}

	public void setColor(Color c) {
	    R = c.getRed();
	    G = c.getGreen();
	    B = c.getBlue();
	}

	public void add(Color c) {
	    R += c.getRed();
	    G += c.getGreen();
	    B += c.getBlue();
	}

	public void div(int n) {
	    R /= n;
	    G /= n;
	    B /= n;
	}

	@Override
	public String toString() {
	    return "(" + R + ", " + G + ", " + B + ")";
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
}
