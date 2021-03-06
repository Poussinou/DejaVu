package org.fitchfamily.android.dejavu;
/*
 *    DejaVu - A location provider backend for microG/UnifiedNlp
 *
 *    Copyright (C) 2017 Tod Fitch
 *
 *    This program is Free Software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by tfitch on 9/28/17.
 */

import android.accessibilityservice.AccessibilityService;
import android.location.Location;

public class BoundingBox {
    private double north;
    private double south;
    private double east;
    private double west;

    BoundingBox() {
        reset();
    }

    BoundingBox(Location loc) {
        reset();
        update(loc);
    }

    BoundingBox(double lat, double lon, float radius) {
        reset();
        update(lat, lon, radius);
    }

    /**
     * Expand, if needed, the bounding box to include the coverage area
     * implied by a location.
     * @param loc A record describing the coverage of an RF emitter.
     */
    public void update(Location loc) {
        update(loc.getLatitude(), loc.getLongitude(), loc.getAccuracy());
    }

    /**
     * Expand bounding box to include an emitter at a lat/lon with a
     * specified radius.
     *
     * @param lat The center latitude for the coverage area.
     * @param lon The center longitude for the coverage area.
     * @param radius The radius of the coverage area.
     */
    public void update(double lat, double lon, float radius) {
        double locNorth = lat + (radius * BackendService.METER_TO_DEG);
        double locSouth = lat - (radius * BackendService.METER_TO_DEG);
        double cosLat = Math.cos(Math.toRadians(lat));
        double locEast = lon + (radius * BackendService.METER_TO_DEG) * cosLat;
        double locWest = lon - (radius * BackendService.METER_TO_DEG) * cosLat;

        north = Math.max(north,locNorth);
        south = Math.min(south,locSouth);
        east = Math.max(east,locEast);
        west = Math.min(west,locWest);
    }

    /**
     * Update the bounding box to include a point at the specified lat/lon
     * @param lat The latitude to be included in the bounding box
     * @param lon The longitude to be included in the bounding box
     */
    public void update(double lat, double lon) {
        north = Math.max(north,lat);
        south = Math.min(south,lat);
        east = Math.max(east,lon);
        west = Math.min(west,lon);
    }

    public double getNorth() {
        return north;
    }

    public double getSouth() {
        return south;
    }

    public double getEast() {
        return east;
    }

    public double getWest() {
        return west;
    }

    @Override
    public String toString() {
        return "(" + north + "," + south + "," + east + "," + west + ")";
    }

    private void reset() {
        north = -91.0;      // Impossibly south
        south = 91.0;       // Impossibly north
        east = -181.0;      // Impossibly west
        west = 181.0;       // Impossibly east
    }

}
