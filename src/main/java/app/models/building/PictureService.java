package app.models.building;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Component
public class PictureService {

	public static String encodeToString(BufferedImage image, String type) {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			BASE64Encoder encoder = new BASE64Encoder();
			imageString = encoder.encode(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

	String getPicture(String name) throws IOException {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());

		Resource[] resources = resolver.getResources("classpath:/*");
		for (Resource resource : resources) {
			File[] files = resource.getFile().listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.getName().equals(name)) {
						final InputStream inputStream = new DataInputStream(new FileInputStream(file));
						return encodeToString(ImageIO.read(inputStream), "png");
					}
				}
			}
		}
		return "";
	}
}
