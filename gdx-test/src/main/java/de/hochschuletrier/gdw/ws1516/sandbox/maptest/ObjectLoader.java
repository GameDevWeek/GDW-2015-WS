package de.hochschuletrier.gdw.ws1516.sandbox.maptest;

import java.util.ArrayList;
import java.util.LinkedList;

import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;

public class ObjectLoader {

	private LinkedList<String> names;
	private LinkedList<String> props;

	
	public ObjectLoader() {

		names = new LinkedList<String>();
		props = new LinkedList<String>();
	}

	/** reads a map and adds the names of objects to the array list */
	public void generateNameList(TiledMap map) {

		for (Layer layer : map.getLayers()) {
			if (layer.isObjectLayer()) {
				for(LayerObject obj: layer.getObjects()){
					names.add(obj.getName());
					props.add(obj.getProperty("test", "gab es nicht."));
				}
			}
		}
	}

	public void printNames(){
		for(String s:names){
			System.out.print(s +", ");
		}
		System.out.println();
		for(String s:props){
			System.out.print(s +", ");			
		}
		System.out.println();
	}
	
}
