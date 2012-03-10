package com.crimezone.sd.server.logic;

import java.util.logging.Logger;

import com.crimezone.sd.server.domain.Incident;
import com.crimezone.sd.server.persistence.CrimeDataStore;

/**
 * 
 * @author Amy
 * 
 */
public class CrimeDataLoader {

  private static final Logger log = Logger.getLogger(CrimeDataManager.class.getName());

  private CrimeDataStore crimeDao;

  // Private constructor prevents instantiation from other classes
  private CrimeDataLoader() {
    crimeDao = new CrimeDataStore();
  }

  /**
   * SingletonHolder is loaded on the first execution of Singleton.getInstance()
   * or the first access to SingletonHolder.INSTANCE, not before.
   */
  private static class SingletonHolder {
    public static final CrimeDataLoader instance = new CrimeDataLoader();
  }

  public static CrimeDataLoader getInstance() {
    return SingletonHolder.instance;
  }

  public void loadIncidents(byte[] incidentFile) {
    // parse byte array and for every row, create a new Incident and persist
    //...
    { // for loop
      Incident incident = new Incident();
      incident.setAddress("");
      crimeDao.updateIncident(incident);
    }
  }

  public void deleteAllIncidents() {
    crimeDao.deleteAllIncidents();
  }

}