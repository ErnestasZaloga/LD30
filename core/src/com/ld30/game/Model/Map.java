package com.ld30.game.Model;

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
	
	public int getWidth () {
		return width;
	}
	
	public int getHeight () {
		return height;
	}
}
