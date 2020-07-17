package com.imericxu.pathfinder.attempt_2.maps;

public enum MapList
{
    MAP_1("map_1.txt"),
    MAP_2("map_2.txt");
    
    private final String mapName;
    
    MapList(String mapName)
    {
        this.mapName = mapName;
    }
    
    public String getName()
    {
        return mapName;
    }
}
