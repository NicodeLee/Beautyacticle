package com.nicodelee.beautyarticle.bus;

import com.nicodelee.beautyarticle.mode.ActicleMod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicodelee on 15/3/27.
 */
public class ActicleEvent {

  private ArrayList<ActicleMod> acticleMods;

  public ActicleEvent(ArrayList<ActicleMod> items) {
    this.acticleMods = items;
  }

  public ArrayList<ActicleMod> getActicleMods() {
    return acticleMods;
  }
}
