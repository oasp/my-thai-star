package com.devonfw.application.mtsj.imagemanagement.common.api;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;
import com.devonfw.application.mtsj.imagemanagement.common.api.datatype.ContentType;

/**
 * Interface for {@link Image}
 *
 */
public interface Image extends ApplicationEntity {

  /**
   * @return name of the {@link Image}
   */
  public String getName();

  /**
   * @param name of the {@link Image}
   */
  public void setName(String name);

  /**
   * @return content of the {@link Image}
   */
  public String getContent();

  /**
   * @param content of the {@link Image}
   */
  public void setContent(String content);

  /**
   * @return contentType of the {@link Image}
   */
  public ContentType getContentType();

  /**
   * @param contentType of the {@link Image}
   */
  public void setContentType(ContentType contentType);

  /**
   * @return mimeType of the {@link Image}
   */
  public String getMimeType();

  /**
   * @param mimeType of the {@link Image}
   */
  public void setMimeType(String mimeType);

}
