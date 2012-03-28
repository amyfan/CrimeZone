package com.crimezone.sd.server.persistence;

import com.crimezone.sd.server.domain.Incident;
import com.crimezone.sd.server.domain.IncidentsOneMile;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

/**
 * Per http://code.google.com/p/objectify-appengine/wiki/BestPractices
 * 
 * @author Amy
 * 
 */
public class DAO extends DAOBase {

  static {
    ObjectifyService.register(Incident.class);
    ObjectifyService.register(IncidentsOneMile.class);
  }

  /**
   * afan: clearer naming convention
   * 
   * @return
   */
  public Objectify getObjectify() {
    return ofy();
  }

}