import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

public class Blur {
    /**
     * Visual frame to illustrate the proccess of blurring.
     */
    static Window WINDOW;

    /**
     * Processor tool that is used to blur the image
     */
    static ImageProcessor IProc;

    /**
     * The image instance that stores our target image
     */
    static BufferedImage IMAGE;

    /**
     * The size of a block. If it equals to <b>N</b> then each iteration of blur
     * algorithm will sample the squares of size <b>N x N</b> and change them to the
     * average of their color.
     */
    static int SIZE;

    /**
     * Defines in which mode the blurring will be executed.
     */
    static boolean isMultiThreaded;

    public static final String STR_referToHelp = "\nType \"<program> help\" for the usage.\n";
    public static final String STR_HELP = "\nUsage: <program> [image] [size] [mode]\n" + // <br>
	    "\nArguments:" + // <br>
	    "\n\timage\t\tThe name of the graphic file of jpg format." + // <br>
	    "\n\tsize\t\tThe side of the square for the averaging." + // <br>
	    "\n\tmode\t\t'S' - single threaded and 'M' - multi threaded.";

    public static void main(String[] args) throws Exception {
	Exception e = null;
	try {
	    // parsing
	    if (args[0].contains("help")) { // parse help
		System.out.println(STR_HELP);
		return;
	    }
	    parseImage(args[0]); // parse [image]
	    parseSize(args[1]); // parse [size]
	    parseMode(args[2]);// parse [mode]

	    // init program
	    IProc = new ImageProcessor(SIZE);
	    WINDOW = new Window();
	    WINDOW.setVisible(true);

	    // start procedure
	    if (isMultiThreaded)
		multiThreadedBlurring();
	    else
		singleThreadedBlurring();

	    // save processed image
	    ImageProcessor.writeImage(IMAGE, "result.jpg");
	} catch (ArrayIndexOutOfBoundsException ex) {
	    e = new ArrayIndexOutOfBoundsException("Not enough arguments were given. " + STR_referToHelp);
	} catch (Exception ex) {
	    e = ex;
	} finally {
	    if (e != null)
		System.out.println(e.getMessage());
	}

    }

    /**
     * From left to right, top to bottom find the average color for the (square
     * size) x (square size) boxes and set the color of the whole square to this
     * average color.
     * 
     * In the multi-processing mode, blurring procedure is performed in parallel
     * threads. The number of threads shall be selected according to the computer's
     * CPU cores.
     * 
     * 
     * @throws InterruptedException
     */
    static void multiThreadedBlurring() throws InterruptedException {

	WINDOW.setImage(IMAGE); // display the image

	/* Initialize Thread pool */
	int processors = Runtime.getRuntime().availableProcessors(); // No. of processors
	ExecutorService threadPool = Executors.newFixedThreadPool(processors);

	/* Dimensions of the image */
	int width = IMAGE.getWidth();
	int height = IMAGE.getHeight();

	/* save the lazy evaluation to the threads to execute them later */
	List<BlurThread> futureList = new ArrayList<BlurThread>();

	/* <--- BLUR HORIZONTALLY ---> */
	for (int i = 0; i < processors; i++) {

	    BlurThread bt = new BlurThread(i) {
		public Boolean call() throws Exception {
		    /* divide range into the number of processors */
		    int row = (width / processors + 1) * index;
		    /***
		     * IMPORTANT: Since the dimensions of the image is not limited, they can
		     * sometimes be divided by the <b>size of block</b> unevenly, with remainder. In
		     * order to avoid <code>ArrayIndexOutOfBoundsException</code> we take minimum
		     * between the given range and the max possible index.
		     ***/
		    int len = Math.min(width, (width / processors + 1) * (index + 1));

		    while (row < len) {
			for (int col = 0; col < height; col += SIZE)
			    IMAGE = IProc.HBlur(IMAGE, row, col);

			WINDOW.setImage(IMAGE); // update image
			row++;
		    }
		    return true;
		}
	    };

	    futureList.add(bt); // save the thread
	}

	/* Wait until the image was blurred horizontally */
	try {
	    threadPool.invokeAll(futureList); // activate threads
	} catch (Exception err) {
	    err.printStackTrace();
	} finally {
	    futureList.clear();
	}

	/* <--- BLUR VERTICALLY ---> */
	for (int i = 0; i < processors; i++) {
	    BlurThread bt = new BlurThread(i) {
		public Boolean call() throws Exception {
		    /* divide range into the number of processors */
		    int col = (height / processors + 1) * index;
		    /***
		     * IMPORTANT: Since the dimensions of the image is not limited, they can
		     * sometimes be divided by the <b>size of block</b> unevenly, with remainder. In
		     * order to avoid <code>ArrayIndexOutOfBoundsException</code> we take minimum
		     * between the given range and the max possible index.
		     ***/
		    int len = Math.min(height, (height / processors + 1) * (index + 1));
		    while (col < len) {
			for (int row = 0; row < width; row += SIZE)
			    IMAGE = IProc.VBlur(IMAGE, row, col);

			WINDOW.setImage(IMAGE); // update image
			col++;
		    }
		    return true;
		}
	    };
	    futureList.add(bt);
	}
	try {
	    threadPool.invokeAll(futureList);
	} catch (Exception err) {
	    err.printStackTrace();
	} finally {
	    futureList.clear(); // save the thread
	}

    }

    /**
     * From left to right, top to bottom find the average color for the (square
     * size) x (square size) boxes and set the color of the whole square to this
     * average color.
     * 
     * In the multi-processing mode, blurring procedure is performed in parallel
     * threads. The number of threads shall be selected according to the computer's
     * CPU cores.
     * 
     * 
     * @throws InterruptedException
     */
    static void singleThreadedBlurring() {

	WINDOW.setImage(IMAGE); // display the image

	/* Dimensions of the image */
	int width = IMAGE.getWidth();
	int height = IMAGE.getHeight();

	/* <--- BLUR HORIZONTALLY ---> */
	for (int row = 0; row < width; row++) {
	    for (int col = 0; col < height; col += SIZE)
		IMAGE = IProc.HBlur(IMAGE, row, col);

	    WINDOW.setImage(IMAGE);
	}
	/* <--- BLUR VERTICALLY ---> */
	for (int col = 0; col < height; col++) {
	    for (int row = 0; row < width; row += SIZE)
		IMAGE = IProc.VBlur(IMAGE, row, col);

	    WINDOW.setImage(IMAGE);
	}
    }

    /**
     * Process the given arguments parameter to define in what mode the blurring
     * procedure is going to be performed
     * 
     * @param args
     *                 Command-Line Arguments
     * @throws InvalidParameterException
     *                                       exception is thrown if the given
     *                                       arguments are not correct. Only two
     *                                       correct possible inputs: <br>
     *                                       <ul>
     *                                       <li>'S' - single threaded mode</li>
     *                                       <li>'M' - multi threaded mode</li>
     *                                       </ul>
     */
    static void parseMode(String args) throws InvalidParameterException {

	switch (args) {
	case "S":
	    isMultiThreaded = false;
	    break;
	case "M":
	    isMultiThreaded = true;
	    break;
	default:
	    throw new InvalidParameterException("Entered Thread mode is not identified. " + STR_referToHelp);
	}
    }

    /**
     * Process the given arguments parameter to define the size of a block of image
     * that is going to be blurred in each iteration.
     * 
     * @param args
     *                 Command-Line Arguments
     * @throws NumberFormatException
     *                                   the given input must be a numeric value, an
     *                                   exception is thrown otherwise
     */
    static void parseSize(String args) throws NumberFormatException {
	try {
	    SIZE = Integer.parseInt(args);
	} catch (NumberFormatException e) {
	    throw new NumberFormatException("The size of the blur blocks is in invalid format. " + STR_referToHelp);
	}
    }

    /**
     * Process the given arguments parameter which is a path for the image to be
     * blurred.
     * 
     * @param args
     *                 Command-Line Arguments
     * @throws IOException
     *                         An exception is thrown when the given path is either
     *                         incorrect, does not exists or something else went
     *                         wrong while fetching the image.
     */
    static void parseImage(String args) throws IOException {
	try {
	    IMAGE = ImageIO.read(new File("./" + args));
	} catch (IOException e) {
	    throw new IOException("Could not process the given image file!");
	}
    }

    /**
     * Static class used to instantiate a callable instance which are used to split
     * the procedure parallely.
     * 
     */
    static abstract class BlurThread implements Callable<Boolean> {
	int index;

	public BlurThread(int index) {
	    this.index = index;
	}
    }
}
