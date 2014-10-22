package com.pyrohail.averageframe;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

public final class FrameCalculator {
	private final VideoReader videoReader;
	private final int totalFrames;
	private int currentFrame;
	private int[][][] imageRGB;
	private BufferedImage image;

	public FrameCalculator(final VideoReader videoReader) {
		this.videoReader = videoReader;
		// 7 = CV_CAP_PROP_FRAME_COUNT
		totalFrames = (int) videoReader.getVideoCapture().get(7);
	}

	public void calculateAverageFrame() {
		while (true) {
			final Mat image = videoReader.getFrame();
			if (image.empty()) {
				normalizeImageRGB();
				break;
			}

			currentFrame++;
			if (currentFrame == 1) {
				imageRGB = new int[image.width()][image.height()][3];
			}

			for (int y = 0; y < image.height(); y++) {
				for (int x = 0; x < image.width(); x++) {
					final double[] pixelRGB = image.get(y, x);

					imageRGB[x][y][0] += pixelRGB[0];
					imageRGB[x][y][1] += pixelRGB[1];
					imageRGB[x][y][2] += pixelRGB[2];
				}
			}

			System.out.printf("\r%.2f%%", (double) currentFrame / totalFrames * 100);
		}
	}

	private void normalizeImageRGB() {
		for (int x = 0; x < imageRGB.length; x++) {
			for (int y = 0; y < imageRGB[0].length; y++) {
				imageRGB[x][y][0] = imageRGB[x][y][0] / totalFrames;
				imageRGB[x][y][1] = imageRGB[x][y][1] / totalFrames;
				imageRGB[x][y][2] = imageRGB[x][y][2] / totalFrames;
			}
		}
	}

	public int[][][] getImageRGB() {
		return imageRGB;
	}
}