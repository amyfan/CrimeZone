package com.crimezone.sd.server.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
  private static final int DATE_INDEX = 0;
  private static final int TIME_INDEX = 1;
  private static final int BCC_INDEX = 2;
  private static final int ADDRESS_INDEX = 3;
  private static final int LATITUDE_INDEX = 4;
  private static final int LONGITUDE_INDEX = 5;
  private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

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
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateString = fields[DATE_INDEX] + " " + fields[TIME_INDEX];
        try {
          Date date = sdf.parse(dateString);
        } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          continue;
        }
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