package com.sefihuom.myhuaweiapplication.utilities;

import android.os.Handler;
import android.os.Looper;

import com.sefihuom.myhuaweiapplication.interfaces.ThreadAction;


public final class UiThread implements ThreadAction {
  private static final Handler handler = new Handler(Looper.getMainLooper());

  @Override
  public void execute(Runnable action) {
    handler.post(action);
  }
}