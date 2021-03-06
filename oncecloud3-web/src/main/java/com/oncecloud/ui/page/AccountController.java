package com.oncecloud.ui.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oncecloud.entity.User;
import com.oncecloud.manager.UserManager;
import com.oncecloud.ui.model.LogOnModel;

@Controller
public class AccountController {
	private UserManager userManager;
	// 设置图形验证码中字符串的字体和大小
	private Font myFont = new Font("Arial Black", Font.PLAIN, 16);

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

	private UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	private void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@RequestMapping(value = "/backdoor", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView backdoor(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") != null) {
			return new ModelAndView(new RedirectView("/dashboard"));
		}

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		Map<String, String> model = new HashMap<String, String>();
		model.put("basePath", basePath);
		return new ModelAndView("account/backdoor", model);
	}

	@RequestMapping(value = "/backdoor", method = { RequestMethod.POST })
	@ResponseBody
	public ModelAndView backdoor(HttpServletRequest request,
			LogOnModel logOnRequestModel) {
		int result = -1;
		String captcha = (String) request.getSession().getAttribute("rand");
		// 会话存在过期现象
		if (logOnRequestModel.getVercode() == null || captcha == null) {
			return new ModelAndView(new RedirectView("/backdoor"));
		} else if (logOnRequestModel.getVercode().toLowerCase()
				.equals(captcha.toLowerCase())) {
			request.getSession().setAttribute("rand", null);
			if (logOnRequestModel.getUser() != null) {
				result = this.getUserManager().checkLogin(
						logOnRequestModel.getUser(),
						logOnRequestModel.getPassword());
			}
			if (result == 0) {
				User userlogin = this.getUserManager().getUser(logOnRequestModel.getUser());
				int level = userlogin.getUserLevel();
				if (level == 0) {
					String path = request.getContextPath();
					String basePath = request.getScheme() + "://"
							+ request.getServerName() + ":"
							+ request.getServerPort() + path + "/";
					request.getSession().setAttribute("user", userlogin);
					request.getSession().setAttribute("basePath", basePath);
					return new ModelAndView(new RedirectView("/dashboard"));
				} else {
					return new ModelAndView(new RedirectView("/backdoor"));
				}
			} else {
				return new ModelAndView(new RedirectView("/backdoor"));
			}
		} else {
			return new ModelAndView(new RedirectView("/backdoor"));
		}
	}

	@RequestMapping(value = "/logout", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView logout(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") != null) {
			request.getSession().removeAttribute("user");
			request.getSession().removeAttribute("basePath");
		}
		return new ModelAndView(new RedirectView("/backdoor"));
	}

	@RequestMapping(value = "/404", method = { RequestMethod.GET })
	@ResponseBody
	public ModelAndView pageNotFound(HttpServletRequest request) {
		return new ModelAndView("account/404");
	}

	@RequestMapping(value = "/captcha", method = { RequestMethod.GET })
	public ResponseEntity<byte[]> image(HttpServletRequest request)
			throws IOException {
		HttpHeaders headers = new HttpHeaders();
		// 阻止生成的页面内容被缓存，保证每次重新生成随机验证码
		headers.set("Pragma", "No-cache");
		headers.set("Cache-Control", "no-cache");
		headers.set("Expires", "0");
		headers.setContentType(MediaType.IMAGE_JPEG);
		// 指定图形验证码图片的大小
		int width = 100, height = 20;
		// 生成一张新图片
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
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
		g.setColor(new Color(20 + random.nextInt(110),
				20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString(num1.toString(), 15 * 0 + 10, 15);
		g.setColor(new Color(20 + random.nextInt(110),
				20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString("+", 15 * 1 + 10, 15);
		g.setColor(new Color(20 + random.nextInt(110),
				20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString(num2.toString(), 15 * 2 + 10, 15);
		g.setColor(new Color(20 + random.nextInt(110),
				20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString("=", 15 * 3 + 10, 15);
		g.setColor(new Color(20 + random.nextInt(110),
				20 + random.nextInt(110), 20 + random.nextInt(110)));
		g.drawString("?", 15 * 4 + 10, 15);
		// 取得用户Session
		HttpSession session = request.getSession(true);
		// 将系统生成的图形验证码添加
		session.setAttribute("rand", totalNum.toString());
		g.dispose();
		// 输出图形验证码图片
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", stream);
		byte[] data = stream.toByteArray();
		stream.close();
		return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
	}
}