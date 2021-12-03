import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

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

public class Window extends JFrame {

    private static final long serialVersionUID = 1L;
    /** Frame, Canvas */
    private JPanel CANVAS;
    /**
     * Container for the image as <code>JLabel</code>
     */
    private JLabel IMAGE_LABEL;
    /**
     * The width and height of the screen.
     */
    public final Dimension RESOLUTION;

    public Window(BufferedImage image, int blockSize) {
	/* DEFINE IMAGE RESOLUTION */
	Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize(); // get screen resolution
	int w = (int) (screenRes.width * 0.8), h = (int) (screenRes.height * 0.8);
	screenRes = new Dimension(w, h); // adjust
	image = ImageProcessor.scale(image, screenRes); // fit to screen
	RESOLUTION = new Dimension(image.getWidth(), image.getHeight()); // save size for later

	/* INITIALIZE JFRAME */
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(0, 0, RESOLUTION.width + 25, RESOLUTION.height + 50);
	setLocationRelativeTo(null);
	setTitle("Image blurring program");

	CANVAS = new JPanel();
	CANVAS.setBorder(new EmptyBorder(5, 5, 5, 5));
	CANVAS.setLayout(new BorderLayout(0, 0));
	setContentPane(CANVAS);

	/* CREATE IMAGE HOLDER */
	IMAGE_LABEL = new JLabel("");
	IMAGE_LABEL.setIcon(new ImageIcon(image)); // display the original image
	/* ADD IMAGE TO THE CANVAS */
	CANVAS.add(IMAGE_LABEL, BorderLayout.CENTER);

    }

    public void setImage(BufferedImage image) {
	/* Update image on the window */
	image = ImageProcessor.resize(image, RESOLUTION); // fit to screen
	IMAGE_LABEL.setIcon(new ImageIcon(image));
    }

    public void repaint() {
	CANVAS.repaint();
    }

}
