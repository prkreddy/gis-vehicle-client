package org.iiitb.api.rs.dao;

import java.util.List;
import java.util.Map;

import org.iiitb.api.rs.model.PathData;

public interface PathDAO {



	public PathData getPath(int pathId);
	
	public Map<String, PathData> getAllPathsAllVechiles() ;

}
