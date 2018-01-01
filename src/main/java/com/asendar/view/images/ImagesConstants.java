/**
 * 
 */
package com.asendar.view.images;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * @author Asendar
 *
 */
public interface ImagesConstants {

	public static final FontAwesomeIconView IMG_SUCCESS = ImageLoader.getIcon(FontAwesomeIcon.CHECK, Color.web("#388E3C"), 13);
	public static final FontAwesomeIconView IMG_INFO = ImageLoader.getIcon(FontAwesomeIcon.INFO, Color.web("#2C54AB"), 13);
	public static final FontAwesomeIconView IMG_WARNING = ImageLoader.getIcon(FontAwesomeIcon.WARNING, Color.web("#FBC02D"), 13);
	public static final FontAwesomeIconView IMG_ERROR = ImageLoader.getIcon(FontAwesomeIcon.CLOSE, Color.web("#D32F2F"), 13);
	public static final Image LOADING_IMAGE = ImageLoader.loadImage("wait");
	public static final ImageView LOADING_IMAGE_GRAPHIC = ImageLoader.getGraphic("wait", 25);


	public enum ImageLocation {
		/**
		 * 
		 */
		GENERAL("/images/"),
		/**
		 * 
		 */
		ICONS("/images/icon/");

		/**
		 * 
		 */
		private final String name;

		/**
		 * @param s
		 */
		private ImageLocation(String s) {
			name = s;
		}

		/**
		 * @param otherName
		 * @return
		 */
		public boolean equalsName(String otherName) {
			return (otherName == null) ? false : name.equals(otherName);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		public String toString() {
			return this.name;
		}
	}


	public enum ImageExtention {
		/**
		 * 
		 */
		PNG(".png"),
		/**
		 * 
		 */
		JPG(".jpg"),
		/**
		 * 
		 */
		SVG(".svg"),
		/**
		 * 
		 */
		ICO(".ico"),

		/**
		 * 
		 */
		GIF(".gif");

		/**
		 * 
		 */
		private final String name;

		/**
		 * @param s
		 */
		private ImageExtention(String s) {
			name = s;
		}

		/**
		 * @param otherName
		 * @return
		 */
		public boolean equalsName(String otherName) {
			return (otherName == null) ? false : name.equals(otherName);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		public String toString() {
			return this.name;
		}
	}
}
