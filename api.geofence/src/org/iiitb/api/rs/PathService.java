package org.iiitb.api.rs;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.iiitb.api.rs.dao.PathDAO;
import org.iiitb.api.rs.dao.impl.PathDAOImpl;
import org.iiitb.api.rs.model.PathData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/pathservice")
public class PathService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/allpaths")
	public String getAllPathsAllVehicles() throws JSONException {
		Gson gson = new GsonBuilder().create();

		PathDAO dao = new PathDAOImpl();
		Map<String,PathData> paths = dao.getAllPathsAllVechiles();
		return gson.toJson(paths);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/path/{id}")
	public String getPathById(@PathParam("id") int pathId) {

		Gson gson = new GsonBuilder().create();
		PathDAO dao = new PathDAOImpl();
		PathData path = dao.getPath(pathId);
		return gson.toJson(path);
	}

}
