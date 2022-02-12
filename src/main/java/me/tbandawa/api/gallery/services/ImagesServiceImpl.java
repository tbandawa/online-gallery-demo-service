package me.tbandawa.api.gallery.services;

import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ImagesServiceImpl implements ImageService, ImageProducer {

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
