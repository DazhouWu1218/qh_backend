/**
 * RandomValidateCode.java
 * <p>(一句话描述功能):
 * <p>@author: David
 * <p>@date: 2019年4月10日
 */
package com.njht.webyun.zuul.access.config;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;


/**
 * @author David
 *         <p>
 *         描述 验证码生成器
 *         <p>
 *         2019年4月10日
 *         <p>
 */
public class RandomValidateCode {

	/** width: 图片宽度 */

	private int width = 80;

	/** height: 图片高度 */

	private int height = 30;

	/** codeCount: 验证码个数 */

	private int codeCount = 4;

	/** lineCount: 干扰线数 */

	private int lineCount = 40;

	/** code: 验证码 */

	private String code = null;

	/** buffImg: 验证码图片 */

	private BufferedImage buffImg;

	/** charSequence: 生产字符序列 */

	private String charSequence = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";

	public static final String RANDOMVALIDATECODE = "RANDOMVALIDATECODE";

	/**
	 * 构造方法
	 */
	public RandomValidateCode() {
		// Auto-generated constructor stub
		this.createCode();
	}

	/**
	 * 构造方法
	 * 
	 * @param width
	 * @param height
	 */
	public RandomValidateCode(int width, int height) {
		this.width = width;
		this.height = height;
		this.createCode();
	}

	/**
	 * 构造方法
	 * 
	 * @param width
	 * @param height
	 * @param codeCount
	 * @param lineCount
	 */
	public RandomValidateCode(int width, int height, int codeCount, int lineCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		this.lineCount = lineCount;
		this.createCode();
	}

	public void createCode() {
		int x = 0, fontHeight = 0, codeY = 0;
		int red = 0, green = 0, blue = 0;
		int codeSpace = 0;

		x = width / (codeCount + codeCount - 1); // 每个字符的宽度，加上两个边界,以及所有字符间隙算一个字符宽度
		fontHeight = height - 2; // 字体的高度
		codeY = height - 4;
		codeSpace = x / (codeCount - 1);

		// 图像buff
		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics2D graphics2d = buffImg.createGraphics();
		// 生产随机数
		Random random = new Random();

		// 将图片填充为白色
		graphics2d.setColor(Color.WHITE);
		graphics2d.fillRect(0, 0, width, height);

		// 创建字体
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		graphics2d.setFont(font);

		// 绘制干扰线
		for (int i = 0; i < lineCount; i++) {
			int xstart = random.nextInt(width);
			int ystart = random.nextInt(height);
			int xend = xstart + random.nextInt(width / 8);
			int yend = ystart + random.nextInt(height / 8);

			red = 110 + random.nextInt(7);
			green = 110 + random.nextInt(9);
			blue = 110 + random.nextInt(5);

			Color color = new Color(red, green, blue);
			graphics2d.setColor(color);
			graphics2d.drawLine(xstart, ystart, xend, yend);
		}

		// 记录随机产生的验证码
		StringBuffer randomCode = new StringBuffer();
		for (int i = 0; i < codeCount; i++) {
			String randStr = String.valueOf(charSequence.charAt(random.nextInt(charSequence.length())));
			// 产生随机的颜色，让产生的每个字符颜色都不相同
			red = random.nextInt(101);
			green = random.nextInt(111);
			blue = random.nextInt(121);
			graphics2d.setColor(new Color(red, green, blue));
			graphics2d.drawString(randStr, (i + 1) * x + (i * codeSpace), codeY);
			randomCode.append(randStr);
		}

		// 将临时记录的转化为真正的code
		code = randomCode.toString();

	}

	/**
	 * (描述):
	 * <p>
	 * (说明):
	 * <p>
	 * 
	 * @param path
	 *            <p>
	 * @throws Exception
	 *             <p>
	 *             date: 2019年4月10日
	 *             <p>
	 *             createdBy: guoy
	 */
	public void write(String path) throws Exception {
		OutputStream os = new FileOutputStream(path);
		this.write(os);
	}

	/**
	 * (描述):
	 * <p>
	 * (说明): 将图片写到输出流
	 * <p>
	 * 
	 * @param os
	 *            <p>
	 * @throws Exception
	 *             <p>
	 *             date: 2019年4月10日
	 *             <p>
	 *             createdBy: guoy
	 */
	public void write(OutputStream os) throws Exception {
		ImageIO.write(buffImg, "png", os);
		os.close();
	}

	public String getCode() {
		System.out.println(code);
		return code;
	}

	public BufferedImage getBuffImg() {
		return buffImg;
	}
}
