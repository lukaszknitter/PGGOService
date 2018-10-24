package app.models.building;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

@Component
public class PictureService {

	public static String encodeToString(BufferedImage image, String type) {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			imageString = Base64.getEncoder().encodeToString(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

	String getPicture(String name) throws IOException {
		String resources = "resources";

		ArrayList<File> files = new ArrayList<>();
		try {
			Files.walk(Paths.get(resources))
					.filter(Files::isRegularFile)
					.forEach(path -> {
						files.add(new File(path.toUri()));
					});
		} catch (IOException e) {
			e.printStackTrace();
		}

		File photoFile = files.stream()
				.filter(file -> file.getName().equals(name))
				.findFirst().get();

		final InputStream inputStream = new DataInputStream(new FileInputStream(photoFile));
		return encodeToString(ImageIO.read(inputStream), "png");
	}
}
