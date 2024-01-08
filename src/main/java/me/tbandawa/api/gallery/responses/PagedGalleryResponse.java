package me.tbandawa.api.gallery.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagedGalleryResponse {
	private int count;
	private int perPage;
	private int currentPage;
	private int nextPage;
	private List<GalleryResponse> gallaries;
}
