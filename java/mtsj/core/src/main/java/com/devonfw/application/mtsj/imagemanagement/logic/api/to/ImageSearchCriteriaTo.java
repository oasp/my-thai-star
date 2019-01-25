package com.devonfw.application.mtsj.imagemanagement.logic.api.to;

import com.devonfw.application.mtsj.general.common.api.to.AbstractSearchCriteriaTo;
import com.devonfw.application.mtsj.imagemanagement.common.api.datatype.ContentType;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * used to find
 * {@link com.devonfw.application.mtsj.imagemanagement.common.api.Image}s.
 *
 */
public class ImageSearchCriteriaTo extends AbstractSearchCriteriaTo {

	private static final long serialVersionUID = 1L;

	private String name;

	private String content;

	private ContentType contentType;

	private String mimeType;

	private StringSearchConfigTo nameOption;

	private StringSearchConfigTo contentOption;

	private StringSearchConfigTo mimeTypeOption;

	/**
	 * The constructor.
	 */
	public ImageSearchCriteriaTo() {

		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the contentType
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return the nameOption
	 */
	public StringSearchConfigTo getNameOption() {
		return nameOption;
	}

	/**
	 * @param nameOption the nameOption to set
	 */
	public void setNameOption(StringSearchConfigTo nameOption) {
		this.nameOption = nameOption;
	}

	/**
	 * @return the contentOption
	 */
	public StringSearchConfigTo getContentOption() {
		return contentOption;
	}

	/**
	 * @param contentOption the contentOption to set
	 */
	public void setContentOption(StringSearchConfigTo contentOption) {
		this.contentOption = contentOption;
	}

	/**
	 * @return the mimeTypeOption
	 */
	public StringSearchConfigTo getMimeTypeOption() {
		return mimeTypeOption;
	}

	/**
	 * @param mimeTypeOption the mimeTypeOption to set
	 */
	public void setMimeTypeOption(StringSearchConfigTo mimeTypeOption) {
		this.mimeTypeOption = mimeTypeOption;
	}

}
