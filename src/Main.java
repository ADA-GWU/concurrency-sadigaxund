import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;

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

    public static void main(String[] args) {
	if (args[0].equals("help")) {
	    printHelp();
	}

	try {
	    processImage(args[1]);
	    processSize(args[2]);
	    processMode(args[3]);
	} catch (ArrayIndexOutOfBoundsException e) {
	    printError(); // TODO: Maybe specify: Not enough arguments
	}
	
	boxBlur();
    }	

    static void boxBlur() {
	
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
	    printError(); // TODO: Maybe specify: Unidentified mode type
	    break;
	}
    }

    static void processSize(String args) {
	try {
	    size = Integer.parseInt(args);
	} catch (NumberFormatException nfe) {
	    printError(); // TODO: Maybe specify: Numeric error
	}
    }

    static void processImage(String args) {
	try {
	    image = ImageIO.read(URI.create(args).toURL());
	} catch (IOException e) {
	    printError(); // TODO: Maybe specify: IO error
	}
    }

    static void printHelp() {

    }

    static void printError() {

	System.exit(0);
    }
}
