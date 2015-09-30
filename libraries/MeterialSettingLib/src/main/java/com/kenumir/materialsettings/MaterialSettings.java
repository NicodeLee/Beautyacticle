package com.kenumir.materialsettings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.kenumir.materialsettings.storage.SimpleStorageInterface;
import com.kenumir.materialsettings.storage.StorageInterface;
import java.util.HashMap;
import java.util.Map;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class MaterialSettings extends SwipeBackActivity {

  public enum ContentFrames {
    FRAME_TOP(0),
    FRAME_TOP_INSIDE(1),
    FRAME_BOTTOM(2),
    FRAME_BOTTOM_INSIDE(3);

    private int id;

    ContentFrames(int idx) {
      id = idx;
    }

    public int getValue() {
      return this.id;
    }
  }

  private static String SAVE_PREFIX = "SSI_";

  private LinearLayout material_settings_content;
  private Toolbar toolbar;
  private StorageInterface mStorageInterface;
  private HashMap<String, MaterialSettingsItem> items;
  private FrameLayout[] frames;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(com.kenumir.materialsettings.R.layout.activity_material_settings);

    items = new HashMap<>();

    toolbar = (Toolbar) findViewById(com.kenumir.materialsettings.R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle("设置");

    material_settings_content =
        (LinearLayout) findViewById(com.kenumir.materialsettings.R.id.material_settings_content);
    frames = new FrameLayout[4];
    frames[0] =
        (FrameLayout) findViewById(com.kenumir.materialsettings.R.id.material_settings_top_frame);
    frames[1] = (FrameLayout) findViewById(
        com.kenumir.materialsettings.R.id.material_settings_top_frame_inside);
    frames[2] = (FrameLayout) findViewById(
        com.kenumir.materialsettings.R.id.material_settings_bottom_frame_inside);
    frames[3] = (FrameLayout) findViewById(
        com.kenumir.materialsettings.R.id.material_settings_bottom_frame);

    mStorageInterface = initStorageInterface();

    if (savedInstanceState != null) {
      for (String key : savedInstanceState.keySet()) {
        if (key.startsWith(SAVE_PREFIX)) {
          String keyName = key.substring(SAVE_PREFIX.length());
          Object value = savedInstanceState.get(key);
          //Log.i("tests", "k: " + key + " k2: " + keyName  +", t=" + value.getClass().getName() + ", v=" + value);
          if (value instanceof String) {
            mStorageInterface.save(keyName, (String) value);
          } else if (value instanceof Integer) {
            mStorageInterface.save(keyName, (Integer) value);
          } else if (value instanceof Float) {
            mStorageInterface.save(keyName, (Float) value);
          } else if (value instanceof Long) {
            mStorageInterface.save(keyName, (Long) value);
          } else if (value instanceof Boolean) {
            mStorageInterface.save(keyName, (Boolean) value);
          } else {
            mStorageInterface.save(keyName, value.toString());
          }
        }
      }
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    StorageInterface si = getStorageInterface();
    if (si instanceof SimpleStorageInterface) {
      saveAll();
      Map<String, ?> all = ((SimpleStorageInterface) si).getAll();
      if (all.size() > 0) {
        // save to bundle
        for (String key : all.keySet()) {
          Object value = all.get(key);
          if (value instanceof String) {
            outState.putString(SAVE_PREFIX + key, (String) value);
          } else if (value instanceof Integer) {
            outState.putInt(SAVE_PREFIX + key, (Integer) value);
          } else if (value instanceof Float) {
            outState.putFloat(SAVE_PREFIX + key, (Float) value);
          } else if (value instanceof Long) {
            outState.putString(SAVE_PREFIX + key, (String) value);
          } else if (value instanceof Boolean) {
            outState.putBoolean(SAVE_PREFIX + key, (Boolean) value);
          } else {
            outState.putString(SAVE_PREFIX + key, value.toString());
          }
        }
      }
    }
    super.onSaveInstanceState(outState);
  }

  public FrameLayout getContentFrame(ContentFrames frame) {
    return frames[frame.getValue()];
  }

  /**
   * save all settings values at StorageInterface
   */
  public void saveAll() {
    for (String key : items.keySet()) {
      items.get(key).save();
    }
  }

  public void addItem(MaterialSettingsItem item) {
    View newView = item.getView(material_settings_content);
    if (newView != null) {
      material_settings_content.addView(newView);
      items.put(item.getName(), item);
    }
  }

  public MaterialSettingsItem getItem(String keyName) {
    return items.get(keyName);
  }

  public StorageInterface getStorageInterface() {
    return mStorageInterface;
  }

  public abstract StorageInterface initStorageInterface();
}
