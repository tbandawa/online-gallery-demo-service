package me.tbandawa.api.gallery.services;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.tbandawa.api.gallery.exceptions.FileStorageException;
import me.tbandawa.api.gallery.exceptions.InvalidFileTypeException;
import me.tbandawa.api.gallery.props.FolderProperties;

import javax.imageio.ImageIO;

/**
 * Defines image file operations.
 */
@Service
public class ImageServiceImpl implements ImageService {
	
	private static final List<String> imageTypes = Arrays.asList("image/png", "image/jpeg", "image/jpeg", "image/gif");

	private static FolderProperties folderProperties;
	
	public void setFolderProperties(FolderProperties folderProperties) {
		ImageServiceImpl.folderProperties = folderProperties;
	}

	/**
	 * Iterate through images, check if image is valid
	 * and save to a directory.
	 * @param galleryId directory name
	 * @return images list of images
	 */
	@Override
	public List<String> saveImages(Long galleryId, MultipartFile[] images) {

		// Iterate through images and check if it's a valid image file
		for (MultipartFile image : images)
			if(!imageTypes.contains(image.getContentType()))
				throw new InvalidFileTypeException(image.getOriginalFilename() + " is not a valid image.");

		// For save each image and return its URI relative to the server
		return IntStream.range(0, images.length)
		         .mapToObj(i -> saveImage(galleryId, (i + 1), images[i]))
		         .collect(Collectors.toList());
	}

	/**
	 * Get folder contents as a list of URIs.
	 * @param galleryId directory name
	 * @return list of URIs
	 */
	@Override
	public List<String> getImages(Long galleryId) {
		List<String> imageURIs;
		try {
		// Iterate folder and return its content as a list of URIs
			imageURIs = Files.list(Paths.get(folderProperties.getImagesFolder() + File.separatorChar + galleryId + File.separatorChar))
		            .map(Path::toFile)
		            .map(File::getPath)
		            .map(filePath ->
		            	ServletUriComponentsBuilder.fromCurrentContextPath()
		            		.path(filePath)
		            		.toUriString()
		            )
		            .collect(Collectors.toList());
		} catch (IOException e) {
			imageURIs = new ArrayList<String>();
		}
		return imageURIs;
	}

	/**
	 * Delete recursively directory.
	 * @param galleryId folder name
	 */
	@Override
	public void deleteImages(Long galleryId) {
		// Delete image folder using Spring's file utilities
		FileSystemUtils.deleteRecursively(new File(folderProperties.getImagesFolder() + File.separatorChar + galleryId));
	}
	
	/**
	 * Save image in the folder created and named using galleryId. The
	 * image name is created by appending <b>imageIndex</b> to <b>image_</b>.
	 * @param galleryId id of the created gallery
	 * @param imageIndex index position of the image
	 * @param image file to save
	 * @return URI of the saved image
	 */
	private static String saveImage(Long galleryId, int imageIndex, MultipartFile image) {
		String imageUri;

		// Build image path using galleryId
		Path imageUploadPath = Paths
				.get(folderProperties.getImagesFolder() + File.separatorChar + galleryId + File.separatorChar)
				.toAbsolutePath()
				.normalize();

		// Create image destination folder
		try {
            Files.createDirectories(imageUploadPath);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory.", ex);
        }

		// Get image extension
		String imageExtension = Objects.requireNonNull(image.getOriginalFilename()).split("\\.")[1];

		// Name the original image file by appending imageIndex to "image_"
		String originalImageName = "image_" + imageIndex + "." + imageExtension;

		// Name the thumbnail image file by appending imageIndex to "thumbnail_"
		String thumbnailImageName = "thumbnail_" + imageIndex + "." + imageExtension;

		// Create and save thumbnail
		saveThumbnail(imageUploadPath, thumbnailImageName, imageExtension, image);

		// Save image and return URI
		return copyImageToFileSystem(imageUploadPath, originalImageName, galleryId, image);
	}

	private static String copyImageToFileSystem(Path path, String imageName, Long galleryId, MultipartFile image) {
		String imageUri;
		try {

			// Copy image file storage
			Path targetLocation = path.resolve(imageName);
			Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			// Generate and return image URI
			imageUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path(folderProperties.getImagesFolder() + File.separatorChar + galleryId + File.separatorChar)
					.path(imageName)
					.toUriString();

		} catch (IOException ex) {
			throw new FileStorageException("Could not store image " + imageName, ex);
		}
		return imageUri;
	}

	private static void saveThumbnail(Path path, String imageName, String imageExtension, MultipartFile image) {
		File imageFile = null;
		try {
			imageFile = convert(image);
			BufferedImage inputImage = ImageIO.read(imageFile);
			BufferedImage outputImage = resizeImage(inputImage, imageExtension);
			Path targetLocation = path.resolve(imageName);
			ImageIO.write(outputImage, imageExtension, targetLocation.toAbsolutePath().toFile());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new FileStorageException("Could not save thumbnail " + imageName, ex);
		} finally {
			// Delete imageFile created
			assert imageFile != null;
			if(imageFile.exists()){
				imageFile.delete();
			}

		}
	}

	private static BufferedImage resizeImage(BufferedImage originalImage,String imageExtension) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Thumbnails.of(originalImage)
				.size(300, 300)
				.outputFormat(imageExtension)
				.outputQuality(0.8f)
				.toOutputStream(outputStream);
		byte[] data = outputStream.toByteArray();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		return ImageIO.read(inputStream);
	}

	private static File convert(MultipartFile multipartFile) {
		File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
		try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(multipartFile.getBytes());
			fos.close();
		} catch (IOException e) {
			convFile = null;
		}
		return convFile;
	}
}