package me.tbandawa.api.gallery.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.validation.constraints.NotBlank;

@Entity
@Indexed
@Table(name = "gallery")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"created"}, allowGetters = true)
public class Gallery {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private Long user_id;
	
    @Field(analyze= Analyze.YES, store= Store.YES)
	@NotBlank(message = "Gallery title can not be empty")
	@Length(max = 150, message = "Gallery title can not be longer than 150 characters")
    private String title;

    @Field(analyze= Analyze.YES, store= Store.YES)
	@NotBlank(message = "Gallery description can not be empty")
	@Column(columnDefinition="TEXT")
    private String description;
	
	private transient List<Images> images;
	
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserid() {
		return user_id;
	}

	public void setUserid(Long userid) {
		this.user_id = userid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Images> getImages() {
		return images;
	}

	public void setImages(List<Images> images) {
		this.images = images;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}