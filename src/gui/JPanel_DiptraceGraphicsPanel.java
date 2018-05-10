/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javadiptraceasciilib.DiptraceGraphics;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JPanel;

/**
 *
 * @author Daniel Bergqvist
 */
public final class JPanel_DiptraceGraphicsPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, Printable
{
	
	private final DiptraceGraphics fDiptraceGraphics;
	
	private final static boolean ONLY_SINGLE_SWITCH = false;
//	private final static boolean ONLY_SINGLE_SWITCH = true;
	
//	private final static boolean DRAW_ID = false;
	private final static boolean DRAW_ID = true;
	
	double printStartPositionX;
	double printStartPositionY;
	int numPagesToPrintX;
	int numPagesToPrintY;
	
	Graphics2D bufferGraphics;
	Image offscreenImage;
	Rectangle bounds;
	
//	double railroadWidth = 5000;	// Hela banan
	//double railroadHeight = 3900;	// Hela banan
//	double scaleFactor = 0.18;		// Hela banan
//////	double scaleFactor = 165;		// Hela banan
	double scaleFactor = 1;		// Hela banan
//	double scaleFactor = 10000;		// Hela banan
	
//	double railroadCenterX = railroadWidth / 2;
//	double railroadCenterY = railroadHeight / 2;
	double railroadCenterX = 0;
	double railroadCenterY = 0;
	double angle = Math.toRadians(45);
	
	boolean leftMouseButtonDown = false;
	int lastX = 0;
	int lastY = 0;
	
	// americaN
	int printStartPage = 0;
	int printNumPages = 30;
	
	
	public JPanel_DiptraceGraphicsPanel(DiptraceGraphics diptraceGraphics)
	{
		super();
		this.fDiptraceGraphics = diptraceGraphics;
		init();
	}
	
	
	private void init()
	{
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	
	
	public PrintService getPrinter()
	{
		PrintService[] printServices;

		String printerName = "Dell C1660w Color Printer (kopia 1)";
		
		// Print to PDF?
		if (1 == 1)
			printerName = "Bullzip PDF Printer";
		
		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(new PrinterName(printerName, null));
		printServices = PrintServiceLookup.lookupPrintServices(null, printServiceAttributeSet);
		
		System.out.println("List of printers:");
		for (PrintService printService1 : printServices)
		{
			System.out.println(printService1.getName());
		}
		
		try
		{
			return printServices[0];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.err.println("Error: No printer named '" + printerName + "', using default printer.");
			return null;
		}
	}
	
	
	public void print()
	{
		printStartPositionX = 0;
		printStartPositionY = 0;
		
		numPagesToPrintX = 2;
		numPagesToPrintY = 1;
		
		
		PrintService printService = getPrinter();
		
		PrinterJob job = PrinterJob.getPrinterJob();
		
		try
		{
			job.setPrintService(printService);   // Try setting the printer you want
		}
		catch (PrinterException ex)
		{
			System.err.println("Printing error: " + ex);
		}
		
		PageFormat pf = job.defaultPage();
		Paper paper = new Paper();
//		double margin = 36; // half inch
		double margin = 18; // quarter inch
		paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight() - margin * 2);
	    pf.setPaper(paper);
		
		System.out.println("ccc");
		PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();
		attr_set.add(OrientationRequested.LANDSCAPE);
//		attr_set.add(MediaSize.ISO_A4);
//		attr_set.add(new Copies(3));
		
		job.setPrintable(this, pf);
		
		try {
			job.print(attr_set);
		} catch (PrinterException e) {
			System.out.println(e.getMessage());
			// The job did not successfully complete
		}
	}
	
	
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
	{
//		if (pageIndex > 0 )
//			return NO_SUCH_PAGE;
		
		// 'page' is zero-based
		if (pageIndex >= Math.min(printNumPages, (numPagesToPrintX * numPagesToPrintY)) ) {
			return NO_SUCH_PAGE;
		}
		
		int row = (printStartPage + pageIndex) / numPagesToPrintX;
		int col = (printStartPage + pageIndex) % numPagesToPrintX;
		
		System.out.format("page: %d, row: %d, col: %d\n", printStartPage+pageIndex, row, col);
		
		// User (0,0) is typically outside the
		// imageable area, so we must translate
		// by the X and Y values in the PageFormat
		// to avoid clipping.
		Graphics2D graphics2D = (Graphics2D)graphics;
		graphics2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		
		// Now we perform our rendering
//		graphics2D.drawString("Hello world!", 100, 100);
		
		draw(graphics2D, true, pageFormat, printStartPositionX, printStartPositionY, col, row);
		
		// tell the caller that this page is part
		// of the printed document
		return PAGE_EXISTS;
	}
	
	
	public void printToPDF()
	{
		Rectangle oldBounds = bounds;
		
		bounds = new Rectangle(Math.round(420), Math.round(297));
//		bounds = new Rectangle(Math.round(420*MM_TO_UNITS), Math.round(297*MM_TO_UNITS));
		
//		drawPCB(graphics, false, null, 0, 0, 0, 0);
		
//			graphics.drawLine(0, 0, 297, 297);
//			graphics.drawLine(297, 297, 420, 0);
//			graphics.scale(1.3, 1.3);
//			graphics.scale(MM_TO_UNITS, MM_TO_UNITS);
//			drawPCB(graphics, false, null, 0, 0, 0, 0);
//			drawPCB(new PDF_Graphics(contentStream), false, null, 0, 0, 0, 0);
		
		bounds = oldBounds;
	}
	
	
	private double getLength(double X1, double Y1, double X2, double Y2)
	{
		return Math.sqrt((X1-X2)*(X1-X2) + (Y1-Y2)*(Y1-Y2));
	}
	
	
	private void draw(Graphics2D graphics, boolean print, PageFormat pageFormat, double x0, double y0, int pageX, int pageY)
	{
		AffineTransform oldXForm = graphics.getTransform();
		
		if (print)
			graphics.setStroke(new BasicStroke(0.1f));
		else
			graphics.setStroke(new BasicStroke(0.01f));
		
//		System.out.format("Width: %d, height: %d\n", bounds.width, bounds.height);
		
//		graphics.drawRect(0, 0, bounds.width, bounds.height);
//		graphics.drawRect(10, 10, bounds.width-20, bounds.height-20);
		
		// Transformera så att ritningen får lagom storlek
		if (print)
		{
			double realWidth = 250.0;
			double paperWidth = 88.05;
//			double paperWidth = 176.1;
			double realHeight = 200.0;
			double paperHeigth = 70.0;
			
//			System.out.format("ScaleX: %1.2f, ScaleY: %1.2f\n", realWidth/paperWidth, realHeight/paperHeigth);
			
//			graphics.translate(pageFormat.getImageableX() + pageFormat.getImageableWidth()/2, pageFormat.getImageableY() + pageFormat.getImageableHeight()/2);
			graphics.translate(pageFormat.getImageableWidth()/2, pageFormat.getImageableHeight()/2);
			
			graphics.scale(realWidth/paperWidth, realHeight/paperHeigth);
			
//			double scale = 1.0;
//			graphics.scale(scale, scale);
			
			// Rotera
//			bufferGraphics.rotate(angle);
			
			// Transformera så att ritningens centrum hamnar i origo
//			graphics.translate(-250/2, -200/2);
			
			double x = x0 + 250*(1.0/2+pageX);
			double y = y0 + 200*(1.0/2+pageY);
			
			System.out.format("X: %1.0f, Y: %1.0f, pageX: %d, pageY: %d\n", x, y, pageX, pageY);
//			System.out.format("X: %1.2f, Y: %1.2f, pageX: %d, pageY: %d\n", x, y, pageX, pageY);
			
			graphics.translate(-x, -y);
/*			
			graphics.setColor(Color.black);
			Font f = new Font("Dialog", Font.PLAIN, 12);
			graphics.setFont(f);
			graphics.drawString(String.format("x: %d, y: %d", pageX, pageY), (float)x+10, (float)y+10);
*/			
//			graphics.translate(-(x0 + 250*(1.0/2+pageX)), -(y0+200*(1.0/2+pageY)));
//			graphics.translate(-railroadCenterX, -railroadCenterY);
		}
		else
		{
			// Transformera till fönstret
			graphics.translate(bounds.width/2, bounds.height/2);
			
//			double scale = 0.09;
////			double scale = 0.18;	// Hela banan
			
//			if (ONLY_SINGLE_SWITCH)
//				scale = 3.0;		// En växel
			
//			scale = 1.0;		// Test
			
			graphics.scale(scaleFactor, scaleFactor);
//			graphics.scale(scale, scale);
			
			// Rotera
//			bufferGraphics.rotate(angle);
			
			// Transformera så att ritningens centrum hamnar i origo
			graphics.translate(-railroadCenterX, -railroadCenterY);
		}
		
		// Test
		graphics.setColor(Color.blue);
		
//		graphics.drawLine(0, 0, 250, 200);
//		graphics.drawLine(250, 0, 0, 200);
		
//		graphics.drawLine(250, 0, 500, 200);
//		graphics.drawLine(500, 0, 250, 200);
		
		
		if (ONLY_SINGLE_SWITCH)
		{
			// Rita rektanglar runt växeln
//			graphics.drawRect(0, 0, 250, 200);		// Draw a rectangle that is 250 x 200 mm big
//			graphics.drawRect(250, 0, 250, 200);	// Draw a rectangle that is 250 x 200 mm big
		}
		else
		{
/*
			// Slå på denna vid hela spårplanen
	//		graphics.setStroke(new BasicStroke(15));
	////		graphics.drawRect(0, 0, (int) Math.round(railroadWidth), (int) Math.round(railroadHeight));
			graphics.drawRect(0, (int) Math.round(railroadHeight), 740-50, 160);
	//		graphics.drawRect(0, (int) Math.round(railroadHeight), 1700, 1900+160);
			graphics.drawLine(0, 3900, 0, 3900+1900+160);
			graphics.drawLine(0, 3900+1900+160, 1700, 3900+1900+160);
			graphics.drawLine(1700, 3900+1900+160, 1700, 3900);
			graphics.drawLine(1700, 3900, 5000, 3900);
*/
			// Rita vägg i rummet
	//		graphics.drawRect(1800, 1800, (int) Math.round(railroadWidth)-1800, 50);
//			graphics.drawLine(1800, 1700, (int) Math.round(railroadWidth), 1800);
//			graphics.drawLine(1800, 1700+50, (int) Math.round(railroadWidth), 1800+50);
//			graphics.drawLine(1800, 1700, 1800, 1700+50);
//			graphics.drawRect(1700-30, (int) Math.round(railroadHeight), 30, 160);
		}
		// Rita "dörröppning"
//		graphics.setColor(Color.red);
//		graphics.drawRect(740-50, (int) Math.round(railroadHeight), 1010, 160);
		
		
//		graphics.drawRect(0, 0, (int) Math.round(railroadWidth), 1750);
//		graphics.drawLine(2000, 1750, (int) Math.round(railroadWidth), 1750);
		
		
		graphics.setColor(Color.black);
		
		fDiptraceGraphics.drawPCB(graphics);
		
		
		if (1 == 0)
		{
			// Stationshus och godsmagasin
			// Slå på denna vid hela spårplanen
	//		graphics.setStroke(new BasicStroke());
			graphics.setColor(Color.blue);
			graphics.fillRect(1000, 20, 200, 280);
			graphics.setColor(Color.black);
			graphics.drawRect(1000, 20, 200, 280);

			graphics.setColor(new Color(230,0,0));
			graphics.fillRect(1600, 60, 200, 150);
			graphics.setColor(Color.black);
			graphics.drawRect(1600, 60, 200, 150);
		}
		
		
		
		
//		graphics.drawRect(0, 0, (int) Math.round(railroadWidth), (int) Math.round(railroadHeight));
////		graphics.setColor(Color.black);
		
		// Draw railroad
//		railroad.drawPCB(graphics, DrawStyle.TRACK_CENTRE);
////		railroad.drawPCB(graphics, DrawStyle.RAILS);
		
//		railroad.drawPCB(graphics, DrawStyle.RAILS);
//		railroad.drawPCB(graphics, DrawStyle.RAIL_FOOT);
		
/*		
		double x = (lastX - bounds.width/2.0) / scaleFactor + railroadCenterX;
		double y = (lastY - bounds.height/2.0) / scaleFactor + railroadCenterY;
		double len = 10;
		System.out.format("\n");
		System.out.format("X: %d, Y: %d\n", lastX, lastY);
		System.out.format("railroadCenterX: %1.0f, railroadCenterY: %1.0f\n", railroadCenterX, railroadCenterY);
		System.out.format("b.x: %d, b.y: %d\n", bounds.width/2, bounds.height/2);
		System.out.format("XX: %1.0f, YY: %1.0f\n", x, y);
		System.out.format("\n");
		graphics.drawPCB(new Line2D.Double(x-len, y, x+len, y));
		graphics.drawPCB(new Line2D.Double(x, y-len, x, y+len));
*/		
		graphics.setTransform(oldXForm); // Restore transform
	}
	
	
	@Override
    protected void paintComponent(Graphics g)
	{
		Rectangle newBounds = this.getBounds();
		
		if ((bounds == null) || (! bounds.equals(newBounds)))
		{
			// Force creation of new double buffer
			bufferGraphics = null;
			bounds = newBounds;
		}
		
		// Init double buffering
		if (bufferGraphics == null)
		{
			offscreenImage = createImage(bounds.width, bounds.height);
			bufferGraphics = (Graphics2D) offscreenImage.getGraphics();
		}
		
		
//		bufferGraphics.drawRect(0, 0, bounds.width-1, bounds.height-1);
		
		// Vit bakgrundsfärg
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, bounds.width, bounds.height);
		
		draw(bufferGraphics, false, null, 0, 0, 0, 0);
		
		Font font = new Font("Verdana", Font.PLAIN, 10);
		bufferGraphics.setFont(font);
//		String str = String.format("%1.2f", scaleFactor);
		String str = String.format(
			"%1.0f, %1.0f - %1.0f",
			railroadCenterX,
			railroadCenterY,
			scaleFactor);
		bufferGraphics.drawString(str, 2, 10);
		
		g.drawImage(offscreenImage, 0, 0, this);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	
	@Override
	public void mousePressed(MouseEvent e)
	{
//		System.out.format("MousePressed: Button %d, BUTTON1: %d\n", e.getButton(), MouseEvent.BUTTON1);
		
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			leftMouseButtonDown = true;
			lastX = e.getX();
			lastY = e.getY();
			this.repaint();
		}
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			leftMouseButtonDown = false;
			
//			System.out.format("X: %d, Y: %d, lastX: %d, lastY: %d\n", e.getX(), e.getY(), lastX, lastY);
		}
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	
	
	@Override
	public void mouseExited(MouseEvent e)
	{
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	
	public void componentResized()
	{
		// Force update of text size
//		bufferGraphicsText = null;
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (leftMouseButtonDown)
		{
			double tempLastX = (lastX - bounds.width/2.0) / scaleFactor + railroadCenterX;
			double tempLastY = (lastY - bounds.height/2.0) / scaleFactor + railroadCenterY;
			double x = (e.getX() - bounds.width/2.0) / scaleFactor + railroadCenterX;
			double y = (e.getY() - bounds.height/2.0) / scaleFactor + railroadCenterY;
			
			railroadCenterX += tempLastX - x;
			railroadCenterY += tempLastY - y;
			
			lastX = e.getX();
			lastY = e.getY();
			
			this.repaint();
		}
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		// Do nothing
	}
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int wheelRotation = e.getWheelRotation();
		
		boolean isShiftDown = e.isShiftDown();
		
//		System.out.format("rotation: %d\n", wheelRotation);
		
		if (wheelRotation > 0)
		{
			for (int i=0; i < wheelRotation; i++)
				scaleFactor /= (isShiftDown ? 1.05 : 1.3);
			
			this.repaint();
		}
		else if (wheelRotation < 0)
		{
			for (int i=0; i < -wheelRotation; i++)
				scaleFactor *= (isShiftDown ? 1.05 : 1.3);
//				scaleFactor *= 2;
			
			this.repaint();
		}
	}
	
	
}
