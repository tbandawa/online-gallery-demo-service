package me.tbandawa.api.gallery.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.tbandawa.api.gallery.exceptions.FileStorageException;
import me.tbandawa.api.gallery.exceptions.InvalidFileTypeException;
import me.tbandawa.api.gallery.props.FolderProperties;

/**
 * Defines image file operations.
 */
@Service
public class ImageServiceImpl implements ImageService {
	
	private static final List<String> imageTypes = Arrays.asList("image/png", "image/jpeg", "image/jpeg", "image/gif");

	private FolderProperties folderProperties;
	
	public void setFolderProperties(FolderProperties folderProperties) {
		this.folderProperties = folderProperties;
	}

	/**
	 * Iterate through images, check if image is valid
	 * and save to a directory.
	 * @param galleryId directory name
	 * @return images list of images
	 */
	@Override
	public List<String> saveImages(Long gelleryId, MultipartFile[] images) {
		// Iterate through images and check if its a valid image file
		for (MultipartFile image : images)
			if(!imageTypes.contains(image.getContentType()))
				throw new InvalidFileTypeException(image.getOriginalFilename() + " is not a valid image.");
		// For save each image and return its URI relative to the server
		return IntStream.range(0, images.length)
		         .mapToObj(i -> saveImage(gelleryId, (i + 1), images[i]))
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
			imageURIs = Files.list(Paths.get(folderProperties.getImagesFolder() + File.separatorChar + String.valueOf(galleryId) + File.separatorChar))
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
		FileSystemUtils.deleteRecursively(new File(folderProperties.getImagesFolder() + File.separatorChar + String.valueOf(galleryId)));
	}
	
	/**
	 * Save image in the folder created and named using gallerId. The
	 * image name is created by appending <b>imageIndex</b> to <b>image_</b>.
	 * @param galleryId id of the created gallery
	 * @param imageIndex index position of the image
	 * @param image file to save
	 * @return URI of the saved image
	 */
	private String saveImage(Long galleryId, int imageIndex, MultipartFile image) {		
		String imageUri;
		// Build image path using galleryId
		Path imageLocation = Paths
				.get(folderProperties.getImagesFolder() + File.separatorChar + String.valueOf(galleryId) + File.separatorChar)
				.toAbsolutePath()
				.normalize();
		// Create image destination folder
		try {
            Files.createDirectories(imageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory.", ex);
        }
		// Name file by appending imageIndex to "image_"
		String fileName = "image_" + imageIndex + "." + image.getOriginalFilename().split("\\.")[1];
		// Move image into created destination and generate its URI
        try {
            Path targetLocation = imageLocation.resolve(fileName);
            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            imageUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(folderProperties.getImagesFolder() + File.separatorChar + String.valueOf(galleryId) + File.separatorChar)
                    .path(fileName)
                    .toUriString();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName, ex);
        }
        return imageUri;
	}

}