package com.devilo.chickchick;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Squarato";
		cfg.width = 320;
		cfg.height = 480;
        cfg.useGL30 = true; //this is important
		new LwjglApplication(new ChickChick(), cfg);
	}
}
