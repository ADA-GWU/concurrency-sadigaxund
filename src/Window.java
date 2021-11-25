import java.awt.BorderLayout;
import java.awt.Point;
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
    private JPanel contentPane;
    private JLabel imageLabel;
    /**
     * The width and height of the window.
     */
    public final Point MAX_RES = new Point(800, 600);

    public Window(BufferedImage image, int blockSize) {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(0, 0, image.getWidth() + blockSize, image.getHeight() + blockSize * 2);
	setLocationRelativeTo(null);

	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	contentPane.setLayout(new BorderLayout(0, 0));
	setContentPane(contentPane);

	imageLabel = new JLabel("");
	contentPane.add(imageLabel, BorderLayout.CENTER);

    }

    public void setImage(BufferedImage image) {
	/* Update image on the window */
	imageLabel.setIcon(new ImageIcon(image));
    }

    public void repaint() {
	contentPane.repaint();
    }

}
