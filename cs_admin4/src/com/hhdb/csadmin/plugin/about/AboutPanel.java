package com.hhdb.csadmin.plugin.about;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Window;

import javax.swing.ImageIcon;

import com.hhdb.csadmin.plugin.menu.util.UIUtils;


/**
 * 关于
 * @author gd
 *
 */
public class AboutPanel extends Canvas{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4362978722659470354L;
	private FontMetrics fontMetrics;
	private Window window;
	private Image image;
	private Image offscreenImg;
	private Graphics offscreenGfx;
	private Color versionTextColour;
	private int versionLabelX;
	private int versionLabelY;
	private String message1;
	private String message2;
	private Color progressColour;
	private Color gradientColour;
	private String version;
	private int PROGRESS_HEIGHT=5;
	private int progress;
	int index;
	private ImageIcon[] imgs;
	
	
	public AboutPanel(Color progressBarColour, String imageResourcePath,String SecondImage, 
			String thirdImage,String ForthImage,String versionNumber,String msg1,String msg2,
			Color versionTextColour, int versionLabelX, int versionLabelY) {

		this.versionTextColour = versionTextColour;
		this.versionLabelX = versionLabelX;
		this.versionLabelY = versionLabelY;
		this.message1=msg1;
		this.message2=msg2;
		progressColour = progressBarColour;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setBackground(Color.white);

		gradientColour = UIUtils.getBrighter(progressBarColour, 0.75);

		Font font = new Font("SimSun", Font.BOLD, 15);
		
		setFont(font);
		fontMetrics = getFontMetrics(font);
		ImageIcon[] imgs = {
                new ImageIcon(imageResourcePath),
                new ImageIcon(SecondImage),
                new ImageIcon(thirdImage),
                new ImageIcon(ForthImage),
            };
		
		MediaTracker tracker = new MediaTracker(this);
		image =imgs[index%imgs.length].getImage();
		tracker.addImage(image, 0);
		if (versionNumber != null) {
			version = versionNumber;
		}
		try {
			tracker.waitForAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//实例化界面
		window = new Window(new Frame());

		Dimension size = new Dimension(imgs[index%imgs.length].getImage().getWidth(this), imgs[index%imgs.length].getImage().getHeight(this));
		window.setSize(size);
		window.setLayout(new BorderLayout());
		window.add(BorderLayout.CENTER, this);

		window.setLocation(UIUtils.getPointToCenter(window, size));

		window.validate();
		window.setVisible(true);
		this.imgs=imgs;
	}
	public synchronized void paint(Graphics g) {
		//添加背景图及背景文字
		Dimension size = getSize();
		offscreenImg = createImage(size.width, size.height);
		offscreenGfx = offscreenImg.getGraphics();
		offscreenGfx.setFont(getFont());
		
		offscreenGfx.drawImage(imgs[index%imgs.length].getImage(), 0, 0, this);
		offscreenGfx.setColor(progressColour);
		Graphics2D offscreenGfx2d = (Graphics2D) offscreenGfx;
		offscreenGfx2d.setPaint(new GradientPaint(0, imgs[index%imgs.length].getImage().getHeight(this) - PROGRESS_HEIGHT, gradientColour,// new
				0, imgs[index%imgs.length].getImage().getHeight(this), progressColour));
		offscreenGfx.fillRect(0, imgs[index%imgs.length].getImage().getHeight(this) - PROGRESS_HEIGHT, (window.getWidth() * progress) / 9, PROGRESS_HEIGHT);

		if (version != null) {

			if (versionLabelX == -1) {
				versionLabelX = (getWidth() - fontMetrics.stringWidth(version)) / 2;
			}

			if (versionLabelY == -1) {
				// if no y value - set just above progress bar
				versionLabelY = imgs[index%imgs.length].getImage().getHeight(this) - PROGRESS_HEIGHT - fontMetrics.getHeight();
			}

			offscreenGfx2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			offscreenGfx2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

			offscreenGfx2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			offscreenGfx.setColor(versionTextColour);
			offscreenGfx.drawString(version, versionLabelX, versionLabelY);
			offscreenGfx.drawString(message1, versionLabelX, versionLabelY+25);
			offscreenGfx.drawString(message2, versionLabelX, versionLabelY+50);
		}
		//下标加一
		index++;
		g.drawImage(offscreenImg, 0, 0, this);
		
		
	notify();
	}

	public void dispose() {
		window.dispose();
		
	}
	
	
//	public static void main(String args[]){
//		String s=AboutPanel.class.getResource("/icon/splash.png").toString();
//		System.out.println(s);
//	}
}
