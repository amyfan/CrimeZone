package com.crimezone.sd.server.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

import com.crimezone.sd.server.domain.Incident;
import com.crimezone.sd.server.persistence.CrimeDataStore;

/**
 * Class to populate the datastore with crime data
 * 
 * @author Amy
 * 
 */
public class CrimeDataLoader {
  private final static int DATE_INDEX = 0;
  private final static int TIME_INDEX = 1;
  private final static int BCC_INDEX = 2;
  private final static int ADDRESS_INDEX = 3;
  private final static int LATITUDE_INDEX = 4;
  private final static int LONGITUDE_INDEX = 5;

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

  public void insertIncidents(BufferedReader incidentFile) {
    // parse byte array and for every row, create a new Incident and persist
    String line;
    try {
      line = incidentFile.readLine();
      while (line != null) {
        Incident incident = new Incident();
        String[] fields = line.split(",");
        incident.setAddress(fields[ADDRESS_INDEX]);
        incident.setBccCode(fields[BCC_INDEX]);
        incident.setLatitude(new BigDecimal(fields[LATITUDE_INDEX]));
        incident.setLongitude(new BigDecimal(fields[LONGITUDE_INDEX]));
        crimeDao.updateIncident(incident);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void deleteAllIncidents() {
    crimeDao.deleteAllIncidents();
  }

}