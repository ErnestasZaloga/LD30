package com.ld30.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.ld30.game.Model.Tiles.Tile;

public class Map {

	private int tileWidth;
	private int tileHeight;
	private int width;
	private int height;
	private Tile[][] tiles;
	
	public void setTiles (final Tile[][] tiles) {
		this.tiles = tiles;
		width = tiles.length;
		height = tiles[0].length;
	}
	
	public Tile[][] getTiles () {
		return tiles;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public Tile getTile (final int x, final int y) {
		return tiles[x][y];
	}
	
	public Tile getTileFromScreenCoords (final float x, 
										 final float y) {
		
		return tiles[MathUtils.roundPositive(width * (x / Gdx.graphics.getWidth()))][MathUtils.roundPositive(height * (y / Gdx.graphics.getHeight()))];
	}
}
