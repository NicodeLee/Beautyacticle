package com.nicodelee.beautyarticle.utils;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Description 按两次返回键退出程序
 *
 * Author lirizhi Create date 2014-1-10
 */

public class IsExit {

  private boolean isExit = false;
  private Runnable task = new Runnable() {
    @Override public void run() {
      isExit = false;
    }
  };

  public void doExitInThreeSecond() {
    isExit = true;
    HandlerThread thread = new HandlerThread("doTask");
    thread.start();
    new Handler(thread.getLooper()).postDelayed(task, 1500);
  }

  public boolean isExit() {
    return isExit;
  }

  public void setExit(boolean isExit) {
    this.isExit = isExit;
  }
}
