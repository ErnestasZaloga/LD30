package com.ld30.game.Model;

import com.ld30.game.Model.Tiles.Tile;

public class Map {

	private float tileWidth;
	private float tileHeight;
	private float tileScale;
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

	public float getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(float tileWidth) {
		this.tileWidth = tileWidth;
	}

	public float getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(float tileHeight) {
		this.tileHeight = tileHeight;
	}

	public float getTileScale() {
		return tileScale;
	}

	public void setTileScale(float tileScale) {
		this.tileScale = tileScale;
	}

	public Tile getTile (final int x, final int y) {
		return tiles[x][y];
	}
	
	public int getWidth () {
		return width;
	}
	
	public int getHeight () {
		return height;
	}
}
