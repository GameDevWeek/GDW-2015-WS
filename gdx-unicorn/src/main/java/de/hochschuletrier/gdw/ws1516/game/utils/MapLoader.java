package de.hochschuletrier.gdw.ws1516.game.utils;

import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ws1516.game.Game;

public interface MapLoader {

	public void parseMap(TiledMap map,Game game, PooledEngine engine);
	
}
