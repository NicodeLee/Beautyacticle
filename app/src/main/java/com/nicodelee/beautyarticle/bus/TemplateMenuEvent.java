package com.nicodelee.beautyarticle.bus;

import com.nicodelee.beautyarticle.viewhelper.viewtoimage.ViewToImageHelper;
import com.nicodelee.view.CropImageView;

/**
 * Created by Nicodelee on 15/9/28.
 */
public class TemplateMenuEvent {
  ViewToImageHelper.SaveImageAction saveImageAction;
  private int position;

  public TemplateMenuEvent(ViewToImageHelper.SaveImageAction imageAction, int sPosition) {
    saveImageAction = imageAction;
    position = sPosition;
  }

  public ViewToImageHelper.SaveImageAction getSaveImageAction() {
    return saveImageAction;
  }

  public void setSaveImageAction(ViewToImageHelper.SaveImageAction saveImageAction) {
    this.saveImageAction = saveImageAction;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }
}
