package com.soze.defense.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.soze.defense.GreatDefense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DesktopLauncher {

	private final static Logger LOG = LoggerFactory.getLogger(DesktopLauncher.class);

	public static void main(String[] arg) {
		LOG.info("Starting desktop application");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1050;
		config.useGL30 = true;
		config.gles30ContextMinorVersion = 3;

		LOG.info("Starting config {}", config);
		//
		//		TexturePacker.Settings settings = new TexturePacker.Settings();
		//		settings.maxWidth = 2048;
		//		settings.maxHeight = 2048;
		//		settings.debug = true;
		//
		//		TexturePacker.process(settings, "pre-pack", "sheets", "game");

		new LwjglApplication(new GreatDefense(), config);
	}
}
