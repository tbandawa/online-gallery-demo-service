package me.tbandawa.api.gallery.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("folder")
public class FolderProperties {
	
	private String imagesFolder;

	public String getImagesFolder() {
		return imagesFolder;
	}

	public void setImagesFolder(String imagesFolder) {
		this.imagesFolder = imagesFolder;
	}

}
