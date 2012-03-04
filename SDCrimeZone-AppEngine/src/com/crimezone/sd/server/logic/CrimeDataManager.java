package com.crimezone.sd.server.logic;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.crimezone.sd.server.domain.Incident;
import com.crimezone.sd.server.persistence.CrimeDataStore;

/**
 * 
 * @author Amy
 * 
 */
public class CrimeDataManager {

  private static final Logger log = Logger.getLogger(CrimeDataManager.class.getName());

  private CrimeDataStore crimeDao;

  // Private constructor prevents instantiation from other classes
  private CrimeDataManager() {
    crimeDao = new CrimeDataStore();
  }

  /**
   * SingletonHolder is loaded on the first execution of Singleton.getInstance()
   * or the first access to SingletonHolder.INSTANCE, not before.
   */
  private static class SingletonHolder {
    public static final CrimeDataManager instance = new CrimeDataManager();
  }

  public static CrimeDataManager getInstance() {
    return SingletonHolder.instance;
  }

  public Incident createIncident() {
    // initializes ID
    return crimeDao.updateIncident(new Incident());
  }

  public Incident findIncident(Long id) {
    Incident incident = crimeDao.findIncident(id);
    return incident;
  }

  public Incident updateIncident(Incident incident) {
    return crimeDao.updateIncident(incident);
  }

  public void deleteIncident(Incident incident) {
    crimeDao.deleteIncident(incident.getId());
  }

  public List<Incident> findAllIncidents() {
    List<Incident> incidents = crimeDao.findAllIncidents();
    return incidents;
  }

  public List<Incident> findIncidentsByStartDate(Date startDate) {
    List<Incident> incidents = crimeDao.findIncidentsByStartDate(startDate);
    return incidents;
  }

}