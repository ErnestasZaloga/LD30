package com.ld30.game.View.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label {

	public Label(CharSequence text, Skin skin) {
		super(text, skin);
	}
	
	public Label(CharSequence text, Skin skin, Color color) {
		super(text, skin);
	}
	
	@Override
	public void setText(CharSequence text) {
		super.setText(text);
		pack();
	}
	
}
