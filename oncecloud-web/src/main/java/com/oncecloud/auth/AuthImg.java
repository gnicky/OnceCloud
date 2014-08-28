package com.oncecloud.auth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yly
 * @version 2014/06/28
 */
public class AuthImg extends HttpServlet {
	private static final long serialVersionUID = -3629314896116283428L;

	// 设置图形验证码中字符串的字体和大小
	private Font myFont = new Font("Arial Black", Font.PLAIN, 16);

	@Override
	public void init() throws ServletException {
		super.init();
	}

	// 生成随机颜色
	Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 阻止生成的页面内容被缓存，保证每次重新生成随机验证码
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 指定图形验证码图片的大小
		int width = 100, height = 20;
		// 生成一张新图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 在图片中绘制内容
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 102));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(myFont);
		// 随机生成线条，让图片看起来更加杂乱
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width - 1);// 起点的x坐标
			int y = random.nextInt(height - 1);// 起点的y坐标
			int x1 = random.nextInt(6) + 1;// x轴偏移量
			int y1 = random.nextInt(12) + 1;// y轴偏移量
			g.drawLine(x, y, x + x1, y + y1);
		}
		// 随机生成线条，让图片看起来更加杂乱
		for (int i = 0; i < 70; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int x1 = random.nextInt(12) + 1;
			int y1 = random.nextInt(6) + 1;
			g.drawLine(x, y, x - x1, y - y1);
		}

		// 该变量用来保存系统生成的随机字符串
		Integer num1 = random.nextInt(10);
		Integer num2 = random.nextInt(10);
		Integer totalNum = num1 + num2;
		g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString(num1.toString(), 15 * 0 + 10, 15);
		g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString("+", 15 * 1 + 10, 15);
		g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString(num2.toString(), 15 * 2 + 10, 15);
		g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString("=", 15 * 3 + 10, 15);
		g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString("?", 15 * 4 + 10, 15);

		// 取得用户Session
		HttpSession session = request.getSession(true);
		// 将系统生成的图形验证码添加
		session.setAttribute("rand", totalNum.toString());
		g.dispose();
		// 输出图形验证码图片
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}
}
