package me.tbandawa.api.gallery.services;

import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import me.tbandawa.api.gallery.exceptions.FileStorageException;
import me.tbandawa.api.gallery.props.FolderProperties;

public class ImagesServiceImpl implements ImageService, ImageProducer {

	
	public void setFolderProperties(FolderProperties folderProperties) {
		Path fileStorageLocation = Paths
				.get(folderProperties.getImagesFolder())
				.toAbsolutePath()
				.normalize();
		try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the " + folderProperties.getImagesFolder() + " directory.", ex);
        }
	}

	@Override
	public void addConsumer(ImageConsumer ic) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isConsumer(ImageConsumer ic) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeConsumer(ImageConsumer ic) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startProduction(ImageConsumer ic) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestTopDownLeftRightResend(ImageConsumer ic) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> saveImages(String gelleryId, MultipartFile[] images) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getImages(String gelleryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteImages(String gelleryId) {
		// TODO Auto-generated method stub

	}

}
