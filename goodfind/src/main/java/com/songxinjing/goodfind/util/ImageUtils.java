package com.songxinjing.goodfind.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {

	public static BufferedImage cleanImage(BufferedImage image) throws IOException {

		// ImageIO.write(image, "png", new File("logs/" +
		// System.currentTimeMillis() + "-old.png"));

		int height = image.getHeight();
		int width = image.getWidth();

		int[][] imgArray = new int[width][height];
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				int argb = image.getRGB(w, h);
				int r = (int) (argb >> 16) & 0xFF;
				int g = (int) (argb >> 8) & 0xFF;
				int b = (int) (argb >> 0) & 0xFF;
				if (r >= 200 && g <= 50 && b <= 50) {
					imgArray[w][h] = 0x000000;
				} else {
					imgArray[w][h] = 0xFFFFFF;
				}
			}
		}

		imgArray = correde(imgArray);
		imgArray = dilate(imgArray);

		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				image.setRGB(w, h, imgArray[w][h]);
			}
		}
		//ImageIO.write(image, "png", new File("logs/" + System.currentTimeMillis() + "-new.png"));
		return image;
	}

	/**
	 * 腐蚀运算
	 * 
	 * @param source
	 * @return
	 */
	private static int[][] correde(int[][] source) {
		int width = source[0].length;
		int height = source.length;
		int[][] result = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				/// 边缘不进行操作，边缘内才操作
				if (i > 0 && j > 0 && i < height - 1 && j < width - 1) {
					if (source[i][j] == 0xFFFFFF) {
						result[i][j] = source[i][j];
					} else {
						int around = 0;
						for (int m = 0; m < 3; m++) {
							for (int n = 0; n < 3; n++) {
								if (source[i - 1 + m][j - 1 + n] == 0x000000) {
									around++;
								}
							}
						}
						if (around >= 5) {
							result[i][j] = source[i][j];
						} else {
							result[i][j] = 0xFFFFFF;
						}
					}
				} else {
					result[i][j] = source[i][j];
				}
			}
		}
		return result;
	}

	/**
	 * 膨胀运算
	 * 
	 * @param source
	 * @return
	 */
	private static int[][] dilate(int[][] source) {
		int width = source[0].length;
		int height = source.length;

		int[][] result = new int[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				/// 边缘不进行操作
				if (i > 0 && j > 0 && i < height - 1 && j < width - 1) {
					if (source[i][j] == 0x000000) {
						result[i][j] = source[i][j];
					} else {
						int around = 0;
						for (int m = 0; m < 3; m++) {
							for (int n = 0; n < 3; n++) {
								if (source[i - 1 + m][j - 1 + n] == 0xFFFFFF) {
									around++;
								}
							}
						}
						if (around >= 5) {
							result[i][j] = source[i][j];
						} else {
							result[i][j] = 0x000000;
						}
					}
				} else {
					/// 直接赋值
					result[i][j] = source[i][j];
				}

			}
		}
		return result;
	}

}
