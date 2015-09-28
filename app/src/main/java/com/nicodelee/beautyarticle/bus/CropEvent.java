package com.nicodelee.beautyarticle.bus;

import com.nicodelee.view.CropImageView;

/**
 * Created by Nicodelee on 15/9/28.
 */
public class CropEvent {
  private String imagePath;
  private CropImageView.CropMode cropMode;

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public CropImageView.CropMode getCropMode() {
    return cropMode;
  }

  public void setCropMode(CropImageView.CropMode cropMode) {
    this.cropMode = cropMode;
  }
}
