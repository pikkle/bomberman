package ch.heigvd.bomberman.client.views.render;/*
	 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
	 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
	 */

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import java.io.Serializable;

/**
 * @author akouznet
 */
public class ImageViewPane extends Region implements Serializable {

	private ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<>();

	public ImageViewPane() {
		this(new ImageView());
	}

	public ImageViewPane(ImageView imageView) {
		imageViewProperty.addListener((arg0, oldIV, newIV) -> {
			if (oldIV != null) {
				getChildren().remove(oldIV);
			}
			if (newIV != null) {
				getChildren().add(newIV);
			}
		});
		this.imageViewProperty.set(imageView);
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
	protected void layoutChildren() {
		ImageView imageView = imageViewProperty.get();
		if (imageView != null) {
			imageView.setFitWidth(getWidth());
			imageView.setFitHeight(getHeight());
			layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
		}
		super.layoutChildren();
	}
}