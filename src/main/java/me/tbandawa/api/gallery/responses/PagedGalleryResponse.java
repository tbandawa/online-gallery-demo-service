package me.tbandawa.api.gallery.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedGalleryResponse {
	private int count;
	private int perPage;
	private int currentPage;
	private int nextPage;
	private List<GalleryResponse> galleries;
}
