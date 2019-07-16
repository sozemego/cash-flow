package com.soze.defense;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAssetManager {

  private static final Logger LOG = LoggerFactory.getLogger(MyAssetManager.class);

  private final AssetManager assetManager = new AssetManager();

  public void loadAssets() {
    LOG.info("Loading all assets...");

    List<File> textures = findAllTextures();
    textures.forEach(texture -> {
      LOG.info("Scheduling texture at {} to load", texture.getPath());
      assetManager.load(texture.getPath(), Texture.class);
    });

    assetManager.finishLoading();
  }

  private List<File> findAllTextures() {
    LOG.info("Loading all textures...");
    File root = new File("textures");
    List<File> textures = new ArrayList<>();
    findAllTextures(root, textures);
    LOG.info("Found {} textures to load", textures.size());
    return textures;
  }

  private void findAllTextures(File root, List<File> textures) {
    if (root.isDirectory()) {
      for (File file : root.listFiles()) {
        findAllTextures(file, textures);
      }
    } else {
      textures.add(root);
    }
  }

  public Texture getTexture(String name) {
    return getAsset(name, Texture.class);
  }

  public <T> T getAsset(String name, Class<T> clazz) {
    return clazz.cast(assetManager.get(name, clazz));
  }

}
