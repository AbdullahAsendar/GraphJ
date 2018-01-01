/**
 * 
 */
package com.asendar.view.images;

import java.awt.TrayIcon.MessageType;
import java.net.URL;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Asendar
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageLoader implements ImagesConstants {

	public static FontAwesomeIconView getGraphic(MessageType messageType) {
		if (messageType.equals(MessageType.ERROR))
			return ImagesConstants.IMG_ERROR;

		if (messageType.equals(MessageType.WARNING))
			return ImagesConstants.IMG_WARNING;

		if (messageType.equals(MessageType.INFO))
			return ImagesConstants.IMG_INFO;

		if (messageType.equals(MessageType.NONE))
			return ImagesConstants.IMG_SUCCESS;

		return null;

	}
	
	/**
	 * @param fontAwesomeIcon
	 * @param dimention
	 * @return
	 */
	public static FontAwesomeIconView getIcon(FontAwesomeIcon fontAwesomeIcon, int dimention) {
		FontAwesomeIconView fontAwesomeIconView = new FontAwesomeIconView(fontAwesomeIcon);
		fontAwesomeIconView.setSize(dimention + "");
		fontAwesomeIconView.setFill(Color.web("#626973"));

		return fontAwesomeIconView;

	}
	

	/**
	 * @param fontAwesomeIcon
	 * @param color
	 * @param dimention
	 * @return
	 */
	public static FontAwesomeIconView getIcon(FontAwesomeIcon fontAwesomeIcon, Color color, int dimention) {
		FontAwesomeIconView fontAwesomeIconView = new FontAwesomeIconView(fontAwesomeIcon);
		fontAwesomeIconView.setSize(dimention + "");
		fontAwesomeIconView.setFill(color);

		return fontAwesomeIconView;

	}

	/**
	 * @param materialDesignIcon
	 * @param dimention
	 * @return
	 */
	public static MaterialDesignIconView getIcon(MaterialDesignIcon materialDesignIcon, int dimention) {
		MaterialDesignIconView materialDesignIconView = new MaterialDesignIconView(materialDesignIcon);
		materialDesignIconView.setSize(dimention + "");
		materialDesignIconView.setFill(Color.web("#626973"));
		return materialDesignIconView;

	}


	/**
	 * @param materialDesignIcon
	 * @param color
	 * @param dimention
	 * @return
	 */
	public static MaterialDesignIconView getIcon(MaterialDesignIcon materialDesignIcon, Color color, int dimention) {
		MaterialDesignIconView materialDesignIconView = new MaterialDesignIconView(materialDesignIcon);
		materialDesignIconView.setSize(dimention + "");
		materialDesignIconView.setFill(color);

		return materialDesignIconView;

	}

	/**
	 * @param materialIcon
	 * @param dimention
	 * @return
	 */
	public static MaterialIconView getIcon(MaterialIcon materialIcon, int dimention) {
		MaterialIconView materialIconView = new MaterialIconView(materialIcon);
		materialIconView.setSize(dimention + "");
		materialIconView.setFill(Color.web("#626973"));
		return materialIconView;

	}



	/**
	 * @param materialIcon
	 * @param color
	 * @param dimention
	 * @return
	 */
	public static MaterialIconView getIcon(MaterialIcon materialIcon, Color color, int dimention) {
		MaterialIconView materialIconView = new MaterialIconView(materialIcon);
		materialIconView.setSize(dimention + "");
		materialIconView.setFill(color);

		return materialIconView;

	}

	/**
	 * @param imgName
	 * @return
	 */
	public static ImageView getGraphic(String imgName,int size) {
		ImageView img = new ImageView();
		Image loadedImg = loadImage(imgName);

		img.setImage(loadedImg);

		img.setFitHeight(size);
		img.setFitWidth(size);
		
		return img;
	}
	
	/**
	 * @param imgName
	 * @return
	 */
	public static ImageView getAutoGraphic(String imgName,int height) {
		ImageView img = new ImageView();
		Image loadedImg = loadImage(imgName);

		img.setImage(loadedImg);

		img.setFitHeight(height);
//		img.setFitWidth(width);
		
		return img;
	}
	
	/**
	 * @param imgName
	 * @return
	 */
	public static Image loadImage(String imgName) {

		Image loadedImg = null;

		for (ImageLocation location : ImageLocation.values()) {

			for (ImageExtention extention : ImageExtention.values()) {
				URL imgUrl = getImageURL(imgName, location, extention);

				loadedImg = getImageFromURL(imgUrl);

				if (loadedImg != null)
					return loadedImg;
			}

		}

		return loadedImg;
	}
	
	/**
	 * @param imgName
	 * @param location
	 * @param imgExtension
	 * @return
	 */
	private static URL getImageURL(String imgName, ImageLocation location, ImageExtention imgExtension) {

		String path = location.toString();
		String extension = imgExtension.toString();

		String strURL = path + imgName + extension;

		URL imageLocation = ImageLoader.class.getResource(strURL);

		return imageLocation;

	}


	/**
	 * @param url
	 * @param w
	 * @param h
	 * @return
	 */
	public static Image getImageFromURL(URL url) {
		try {

			Image image = new Image(url.toString());
			return image;
		} catch (Exception e) {
			return null;
		}

	}
}
