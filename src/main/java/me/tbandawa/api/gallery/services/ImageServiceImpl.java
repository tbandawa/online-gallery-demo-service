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

@Service
public class ImageServiceImpl implements ImageService {
	
	private static final List<String> imageTypes = Arrays.asList("image/png", "image/jpeg", "image/jpeg", "image/gif");

	private FolderProperties folderProperties;
	
	public void setFolderProperties(FolderProperties folderProperties) {
		this.folderProperties = folderProperties;
	}

	@Override
	public List<String> saveImages(Long gelleryId, MultipartFile[] images) {
		for (MultipartFile image : images)
			if(!imageTypes.contains(image.getContentType()))
				throw new InvalidFileTypeException(image.getOriginalFilename() + " is not a valid image.");	
		return IntStream.range(0, images.length)
		         .mapToObj(i -> saveImage(gelleryId, (i + 1), images[i]))
		         .collect(Collectors.toList());
	}

	@Override
	public List<String> getImages(Long galleryId) {
		List<String> imageURIs;
		try {
			imageURIs = Files.list(Paths.get(folderProperties.getImagesFolder() + File.pathSeparator + String.valueOf(galleryId)))
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

	@Override
	public void deleteImages(Long galleryId) {
		FileSystemUtils.deleteRecursively(new File(folderProperties.getImagesFolder() + File.pathSeparator + String.valueOf(galleryId)));
	}
	
	private String saveImage(Long galleryId, int imageIndex, MultipartFile image) {		
		String imageUri;
		Path imageLocation = Paths
				.get(folderProperties.getImagesFolder() + File.separatorChar + String.valueOf(galleryId) + File.separatorChar)
				.toAbsolutePath()
				.normalize();
		try {
            Files.createDirectories(imageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory.", ex);
        }
		String fileName = "image_" + imageIndex + "." + image.getOriginalFilename().split("\\.")[1];
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