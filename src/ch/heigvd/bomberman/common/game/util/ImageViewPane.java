package ch.heigvd.bomberman.common.game.util;

/**
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author akouznet
 */
public class ImageViewPane extends Region implements Serializable {

	private ObjectProperty<ImageView> imageViewProperty;
	private String path;
	private UUID uuid;

	public ImageViewPane(String path, UUID uuid) {
		initialize(path, uuid);
	}

	private void initialize(String path, UUID uuid) {
		this.path = path;
		this.uuid = uuid;

		imageViewProperty = new SimpleObjectProperty<>();
		imageViewProperty.addListener((arg0, oldIV, newIV) -> {
			if (oldIV != null) {
				getChildren().remove(oldIV);
			}
			if (newIV != null) {
				getChildren().add(newIV);
			}
		});
		try{
			this.imageViewProperty.set(new ImageView(path));
		} catch (Throwable e){
			Log logger = LogFactory.getLog(ImageViewPane.class);
			logger.fatal("Couldn't create image : " + path);
			throw e;
		}

	}

	public ObjectProperty<ImageView> imageViewProperty() {
		return imageViewProperty;
	}

	public ImageView getImageView() {
		return imageViewProperty.get();
	}

	public void setImageView(ImageView imageView) {
		this.imageViewProperty.set(imageView);
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) || (o instanceof ImageViewPane && ((ImageViewPane) o).uuid.equals(this.uuid));
	}

	@Override
	protected void layoutChildren() {
		ImageView imageView = imageViewProperty.get();
		if (imageView != null) {
			imageView.setFitWidth(getWidth());
			imageView.setFitHeight(getHeight());
			layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
		}
		super.layoutChildren();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeUTF(path);
		out.writeObject(uuid);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		initialize(in.readUTF(), (UUID) in.readObject());
	}
}