import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
    static Window WINDOW;
    static BufferedImage IMAGE;
    static int SIZE;
    static boolean isMultiThreaded;
    static ImageProcessor IProc;

    public static final String STR_referToHelp = "Type \"java <classname> -help\" for the usage.\n ;";

    public static void main(String[] args) throws Exception {
	Exception e = null;
	try {
	    // if (args[0].equals("help"))
	    // printHelp();
	    // processImage(args[0]);
	    // processSize(args[1]);
	    // processMode(args[2]);
	    IMAGE = ImageIO.read(new File("image.jpg"));
	    SIZE = 33;
	    isMultiThreaded = true;

	    IProc = new ImageProcessor(SIZE);
	    startWindow();
	    if (isMultiThreaded)
		multiThreadedBlurring();
	    else
		singleThreadedBlurring();
	} catch (ArrayIndexOutOfBoundsException ex) {
	    e = new ArrayIndexOutOfBoundsException("Not enough arguements were given. " + STR_referToHelp);
	} catch (Exception ex) {
	    e = ex;
	} finally {
	    if (e != null)
		e.printStackTrace();
	}

    }

    static BufferedImage multiThreadedBlurring() throws InterruptedException {

	int processors = Runtime.getRuntime().availableProcessors();
	ExecutorService threadPool = Executors.newFixedThreadPool(processors);

	/* remove remainder from division and resize */
	int width = IMAGE.getWidth();
	int height = IMAGE.getHeight();

	IMAGE = Util.resize(IMAGE, width, height);
	WINDOW.setImage(IMAGE); // display the image

	/*** save the lazy evaluation to the threads to execute them later ***/
	/* <--- BLUR HORIZONTALLY ---> */
	List<BlurThread> futureList = new ArrayList<BlurThread>();

	for (int i = 0; i < processors; i++) {
	    BlurThread bt = new BlurThread(i) {
		public Boolean call() throws Exception {
		    int row = (width / processors + 1) * index;
		    int len = Math.min(width, (width / processors + 1) * (index + 1));

		    for (; row < len; row++) {
			for (int col = 0; col < height; col += SIZE) {
			    IMAGE = IProc.HBlur(IMAGE, row, col);
			}
			WINDOW.setImage(IMAGE);
		    }
		    return true;
		};
	    };
	    futureList.add(bt);
	}

	/* Wait until the image was blurred horizontally */
	try {
	    threadPool.invokeAll(futureList);
	} catch (Exception err) {
	    err.printStackTrace();
	} finally {
	    futureList.clear();
	}

	/* <--- BLUR VERTICALLY ---> */
	for (int i = 0; i < processors; i++) {
	    BlurThread bt = new BlurThread(i) {
		public Boolean call() throws Exception {
		    int col = (height / processors + 1) * index;
		    int len = Math.min(height, (height / processors + 1) * (index + 1));
		    for (; col < len; col++) {
			for (int row = 0; row < width; row += SIZE) {
			    IMAGE = IProc.VBlur(IMAGE, row, col);
			}
			WINDOW.setImage(IMAGE);
		    }
		    return true;
		};
	    };
	    futureList.add(bt);
	}
	try {
	    threadPool.invokeAll(futureList);
	} catch (Exception err) {
	    err.printStackTrace();
	} finally {
	    futureList.clear();
	}
	return IMAGE;

    }

    static BufferedImage singleThreadedBlurring() {

	/* remove remainder from division and resize */
	int width = IMAGE.getWidth();
	int height = IMAGE.getHeight();

	IMAGE = Util.resize(IMAGE, width, height);
	WINDOW.setImage(IMAGE); // display the image

	/* procces the image */
	for (int row = 0; row < width; row++) {
	    for (int col = 0; col < height; col += SIZE) {
		IMAGE = IProc.HBlur(IMAGE, row, col);
	    }
	    WINDOW.setImage(IMAGE);
	}

	for (int col = 0; col < height; col++) {
	    for (int row = 0; row < width; row += SIZE) {
		IMAGE = IProc.VBlur(IMAGE, row, col);
	    }
	    WINDOW.setImage(IMAGE);
	}
	return IMAGE;
    }

    static void startWindow() throws Exception {
	try {
	    WINDOW = new Window();
	    WINDOW.setVisible(true);
	} catch (Exception e) {
	    throw new Exception("Could not open the window!");
	}
    }

    static void writeImage() throws IOException {
	/* write the image */
	try {
	    File file = new File("./blurredImage.png");
	    ImageIO.write(IMAGE, "png", file);
	} catch (IOException e) {
	    throw new IOException("Image was not saved successfully!");
	}
    }

    static void processMode(String args) throws IOException {

	switch (args) {
	case "S":
	    isMultiThreaded = false;
	    break;
	case "M":
	    isMultiThreaded = true;
	    break;
	default:
	    throw new IOException("Entered Thread mode is not identified. " + STR_referToHelp);
	}
    }

    static void processSize(String args) throws NumberFormatException {
	try {
	    SIZE = Integer.parseInt(args);
	} catch (NumberFormatException e) {
	    throw new NumberFormatException("The size of the blur blocks is in invalid format. " + STR_referToHelp);
	}
    }

    static void processImage(String args) throws IOException {
	try {
	    IMAGE = ImageIO.read(new File("./" + args));
	} catch (IOException e) {
	    throw new IOException("Could not process the given image file!");
	}
    }

    static void printHelp() {

    }

    static void printError() {
	System.out.println("ERROR!!!");
	System.exit(0);
    }

    static class BlurThread implements Callable<Boolean> {
	int index;

	public BlurThread(int index) {
	    this.index = index;
	}

	@Override
	public Boolean call() throws Exception {
	    // TODO Auto-generated method stub
	    return null;
	}

    }
}
