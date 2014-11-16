/**
 * 
 */
package org.iiitb.api.rs.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.iiitb.api.rs.dao.PathDAO;
import org.iiitb.api.rs.model.Geofence;
import org.iiitb.api.rs.model.PathData;
import org.iiitb.api.rs.model.Vehicle;
import org.iiitb.util.ConnectionPool;

/**
 * @author Prashanth Reddy
 * 
 */
public class PathDAOImpl implements PathDAO {

	/**
	 * 
	 */

	private static final String PATHID = "pathid";

	private static final String PATHNAME = "pathname";
	private static final String PATHLENGTH = "pathlength";

	private static final String GEOFENCEID = "idPoint";
	private static final String LATITUTE = "latitute";
	private static final String LONGITUTE = "longitute";
	private static final String RADIUS = "radius";

	private static final String EXPIRE_DURATION = "expire_duration";

	private static final String IDVEHICLE = "idvehicle";
	private static final String VEH_NAME = "veh_name";
	private static final String VEH_REGNO = "veh_regno";
	private static final String ALARMFLAG = "alarmflag";

	private static final String GET_ALL_PATHS_QUERY = "select * from path p, geofence g, vehicle v where p.pathid=g.pathid and p.pathid=v.pathid order by p.pathid asc, g.idPoint asc, v.idvehicle";

	private static final String GET_PATH = "select * from path p, geofence g where p.pathid=g.pathid and p.pathid =? order by p.pathid asc, g.idPoint asc";

	private static final String GET_ALL_PATHS_LOCATIONS = "select * from path p, geofence g where p.pathid=g.pathid  order by p.pathid asc, g.idPoint asc";

	private static final String GET_ALL_PATHS_VEHICLES = "select * from path p,vehicle v where p.pathid=v.pathid order by p.pathid asc, v.idvehicle asc";

	

	@Override
	public Map<String, PathData> getAllPathsAllVechiles() {

		Map<String, PathData> paths = new HashMap<String, PathData>();

		Connection conn = ConnectionPool.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(GET_ALL_PATHS_LOCATIONS);

			rs = pstmt.executeQuery();

			PathData path = null;
			Geofence geofence = null;
			Vehicle vehicle = null;
			while (rs.next()) {

				if (paths.get(rs.getString(PATHNAME)) == null) {
					path = new PathData();
					path.setPathId(rs.getInt(PATHID));
					path.setPathName(rs.getString(PATHNAME));
					path.setPathLen(rs.getInt(PATHLENGTH));
					paths.put(path.getPathName(), path);
				}

				geofence = new Geofence();
				geofence.setGeofenceId(rs.getInt(GEOFENCEID));
				geofence.setLatitude(rs.getDouble(LATITUTE));
				geofence.setLongitude(rs.getDouble(LONGITUTE));
				geofence.setPathId(rs.getInt(PATHID));
				geofence.setFenceRadius(rs.getInt(RADIUS));
				geofence.setExpireDuration(rs.getInt(EXPIRE_DURATION));

				paths.get(rs.getString(PATHNAME)).getLocations().add(geofence);
			}

			rs.close();
			pstmt.close();

			pstmt = conn.prepareStatement(GET_ALL_PATHS_VEHICLES);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (paths.get(rs.getString(PATHNAME)) == null) {
					path = new PathData();
					path.setPathId(rs.getInt(PATHID));
					path.setPathName(rs.getString(PATHNAME));
					path.setPathLen(rs.getInt(PATHLENGTH));
					paths.put(path.getPathName(), path);
				}

				vehicle = new Vehicle();
				vehicle.setVehicleId(rs.getInt(IDVEHICLE));
				vehicle.setVehicleName(rs.getString(VEH_NAME));
				vehicle.setVehicleRegNo(rs.getString(VEH_REGNO));
				// vehicle.setAlarmflag(alarmflag);

				vehicle.setPathId(rs.getInt(PATHID));

				paths.get(rs.getString(PATHNAME)).getVehicles().add(vehicle);

			}

			rs.close();
			pstmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionPool.freeConnection(conn);
		}

		// TODO Auto-generated method stub
		return paths;
	}

	@Override
	public PathData getPath(int pathId) {

		PathData path = new PathData();

		Connection conn = ConnectionPool.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(GET_PATH);

			pstmt.setInt(1, pathId);
			rs = pstmt.executeQuery();

			Geofence geofence = new Geofence();

			boolean firstpath = true;
			while (rs.next()) {

				if (firstpath) {

					firstpath = false;
					path = new PathData();
					path.setPathId(rs.getInt(PATHID));
					path.setPathName(rs.getString(PATHNAME));
					path.setPathLen(rs.getInt(PATHLENGTH));
				}

				geofence = new Geofence();
				geofence.setGeofenceId(rs.getInt(GEOFENCEID));
				geofence.setLatitude(rs.getDouble(LATITUTE));
				geofence.setLongitude(rs.getDouble(LONGITUTE));
				geofence.setPathId(rs.getInt(PATHID));
				geofence.setFenceRadius(rs.getInt(RADIUS));
				geofence.setExpireDuration(rs.getInt(EXPIRE_DURATION));

				path.getLocations().add(geofence);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return path;
	}
}