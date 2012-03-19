package com.beoui.geocell.model;

import java.util.List;


/**
 * To run proximity fetch method, entity has to implement LocationCapable.
 * Location and Key are used in the algorithm (to sort and compare entities).
 * Geocells is used in query (entity must have a GEOCELLS column).
 *
 * @author Alexandre Gellibert
 *
 */
public interface LocationCapable {

    /**
     *
     * @return the location in latitude/longitude
     */
    Point getLocation();

    /**
     *
     * @return the key of the entity used as a String
     */
    String getKeyString();

    /**
     *
     * @return the key of the entity used as a String
     */
    List<String> getGeocells();

}
