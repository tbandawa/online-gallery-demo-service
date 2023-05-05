package me.tbandawa.api.gallery.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import me.tbandawa.api.gallery.entities.Gallery;
import me.tbandawa.api.gallery.responses.GalleryResponse;

@Component
public class GalleryMapperImpl implements GalleryMapper {

	@Override
	public List<GalleryResponse> mapToGalleryResponse(List<Gallery> galleries) {
		return galleries.stream()
				.map(gallery -> new GalleryResponse(gallery.getId(), gallery.getTitle(), gallery.getDescription(), gallery.getImages(), gallery.getCreated()))
				.collect(Collectors.toList());
	}

}
