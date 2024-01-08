package me.tbandawa.api.gallery.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedGallery {
	private int count;
	private int perPage;
	private int currentPage;
	private int nextPage;
	private List<Gallery> gallaries;
}
