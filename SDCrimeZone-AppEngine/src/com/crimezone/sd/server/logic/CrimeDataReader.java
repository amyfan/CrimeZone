package com.crimezone.sd.server.logic;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.crimezone.sd.server.domain.AverageIncidentNumber;
import com.crimezone.sd.server.domain.Incident;
import com.crimezone.sd.server.domain.IncidentsOneMile;
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

  public List<Incident> findIncidentsByYearAndRadius(Integer year, Double latitude,
      Double longitude, Integer radius) {
    List<Incident> incidents = crimeDao.findIncidentsByYearAndRadius(year, latitude, longitude,
        radius);
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

public IncidentsOneMile findIncidentsByCloseLatitudeLongitude(Double latitude,
		Double longitude, Integer radius) {
	
	for (double lat = 32.5011583; lat <= 34.9359403; lat += 0.115942) {
		for (double lng  = -120.43924; lng <= -116.7025101; lng += 0.115942) {
			if (Math.abs(latitude.doubleValue() - lat) <= 0.115942) {
				if (Math.abs(longitude.doubleValue() - lng) <= 0.115942) {
					
					List<IncidentsOneMile> incidents = crimeDao.findAllIncidentsByOneMile(String.format("%.3f", lat), String.format("%.3f", lng));
					System.out.println(incidents.get(0).getResult());
					return incidents.get(0);
				}
			}
		}
	}
	return null;
}

}