package com.crimezone.sd.server.logic;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.crimezone.sd.server.domain.AverageIncidentNumber;
import com.crimezone.sd.server.domain.Incident;
import com.crimezone.sd.server.persistence.CrimeDataStore;

/**
 * Contains logic related to all requests made by the app client
 * 
 * @author Amy
 * 
 */
public class CrimeDataReader {

  private static final Logger log = Logger.getLogger(CrimeDataReader.class.getName());

  private CrimeDataStore crimeDao;

  // Private constructor prevents instantiation from other classes
  private CrimeDataReader() {
    crimeDao = new CrimeDataStore();
  }

  /**
   * SingletonHolder is loaded on the first execution of Singleton.getInstance()
   * or the first access to SingletonHolder.INSTANCE, not before.
   */
  private static class SingletonHolder {
    public static final CrimeDataReader instance = new CrimeDataReader();
  }

  public static CrimeDataReader getInstance() {
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

  public List<Incident> findAllIncidents() {
    List<Incident> incidents = crimeDao.findAllIncidents();
    return incidents;
  }

  public List<Incident> findIncidentsByYear(Integer year) {
    List<Incident> incidents = crimeDao.findIncidentsByYear(year);
    return incidents;
  }

  public List<Incident> findIncidentsByRadius(Date startDate) {
    List<Incident> incidents = crimeDao.findIncidentsByStartDate(startDate);
    return incidents;
  }

  public List<Incident> findIncidentsByYearAndRadius(Integer year, Integer radius) {
    List<Incident> incidents = crimeDao.findIncidentsByYearAndRadius(year, radius);
    return incidents;
  }

  public List<Incident> findIncidentsByStartDate(Date startDate) {
    List<Incident> incidents = crimeDao.findIncidentsByStartDate(startDate);
    return incidents;
  }

  public List<AverageIncidentNumber> findAverageIncidentNumbersByYearAndRadius(Integer year,
      Integer radius) {
    List<AverageIncidentNumber> averages = crimeDao.findAverageIncidentNumbersByYearAndRadius(year,
        radius);
    return averages;
  }

}