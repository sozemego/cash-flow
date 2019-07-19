package com.soze.defense;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class GreatDefense extends Game {

  @Override
  public void create() {
    ShaderProgram.pedantic = false;

    MyAssetManager assetManager = new MyAssetManager();
    assetManager.loadAssets();
    setScreen(new GameScreen(assetManager));
  }


}
