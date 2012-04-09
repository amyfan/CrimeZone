package com.crimezone.sd.server.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;
import com.crimezone.sd.server.domain.AverageIncidentNumber;
import com.crimezone.sd.server.domain.Incident;
import com.crimezone.sd.server.domain.IncidentSet;
import com.crimezone.sd.server.domain.IncidentSetTypeEnum;
import com.crimezone.sd.server.domain.IncidentsOneMile;
import com.crimezone.sd.server.persistence.CrimeDataStore;
import com.google.appengine.api.datastore.Text;

/**
 * Class to populate the datastore with crime data and averages
 * 
 * @author Amy
 * 
 */
public class CrimeDataLoader {
  private static final int INCIDENT_DATE_INDEX = 0;
  private static final int INCIDENT_TIME_INDEX = 1;
  private static final int INCIDENT_BCC_INDEX = 2;
  private static final int INCIDENT_ADDRESS_INDEX = 3;
  private static final int INCIDENT_LATITUDE_INDEX = 4;
  private static final int INCIDENT_LONGITUDE_INDEX = 5;
  private static final int INCIDENTS_LATITUDE_INDEX = 0;
  private static final int INCIDENTS_LONGITUDE_INDEX = 1;
  private static final int INCIDENTS_SET_INDEX = 2;
  private static final int INCIDENTS_RESULT_INDEX = 2;
  private static final String DATE_FORMAT = "M/d/yyyy HH:mm";

  private static final Logger log = Logger.getLogger(CrimeDataReader.class.getName());

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
    // parse input file and for every row, create a new Incident and persist
    String line;
    try {
      line = incidentFile.readLine();
      SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
      SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
      while (line != null) {
        try {
          Incident incident = new Incident();
          String[] fields = line.split(",");

          String dateString = fields[INCIDENT_DATE_INDEX] + " " + fields[INCIDENT_TIME_INDEX];
          Date date = dateFormat.parse(dateString);
          incident.setDate(date);
          incident.setYear(Integer.valueOf(yearformat.format(date)));

          incident.setAddress(fields[INCIDENT_ADDRESS_INDEX]);

          incident.setBccCode(fields[INCIDENT_BCC_INDEX]);

          Double latitude = new Double(fields[INCIDENT_LATITUDE_INDEX]);
          Double longitude = new Double(fields[INCIDENT_LONGITUDE_INDEX]);
          incident.setLatitude(latitude);
          incident.setLongitude(longitude);

          List<String> cells = GeocellManager.generateGeoCell(new Point(latitude.doubleValue(),
              longitude.doubleValue()));
          incident.setGeocells(cells);

          crimeDao.updateIncident(incident);

          line = incidentFile.readLine();
        } catch (ParseException e) {
          e.printStackTrace();
          continue;
        } catch (Exception e) {
          e.printStackTrace();
          continue;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void insertIncidentSet(BufferedReader incidentFile, IncidentSetTypeEnum setType) {
    // parse input file and for every row, create a new Incident and persist
    String line;
    try {
      line = incidentFile.readLine();
      while (line != null) {
        try {
          IncidentSet incidents;
          switch (setType) {
          case ONE_MILE_ALL_YEAR:
            incidents = new IncidentsOneMile();
            break;
          default:
            incidents = new IncidentsOneMile();
          }
          String[] fields = line.split(";");

          String latitude = String.format("%.3f", new Double(fields[INCIDENTS_LATITUDE_INDEX]));
          String longitude = String.format("%.3f", new Double(fields[INCIDENTS_LONGITUDE_INDEX]));
          String result = new String(fields[INCIDENTS_RESULT_INDEX]);
          incidents.setLatitude(latitude);
          incidents.setLongitude(longitude);

          incidents.setResult(result);

          incidents.setIncidentSet(new Text(fields[INCIDENTS_SET_INDEX]));

          switch (setType) {
          case ONE_MILE_ALL_YEAR:
            crimeDao.updateIncidentsOneMile((IncidentsOneMile) incidents);
            break;
          default:
            crimeDao.updateIncidentsOneMile((IncidentsOneMile) incidents);
          }

          line = incidentFile.readLine();
        } catch (Exception e) {
          e.printStackTrace();
          continue;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void deleteAllIncidents() {
    crimeDao.deleteAllIncidents();
  }

  public void deleteAllIncidentSets(IncidentSetTypeEnum setType) {
    switch (setType) {
    case ONE_MILE_ALL_YEAR:
      crimeDao.deleteAllIncidentsOneMiles();
      break;
    default:
      crimeDao.deleteAllIncidentsOneMiles();
    }
  }

  public Incident updateIncident(Incident incident) {
    return crimeDao.updateIncident(incident);
  }

  public void deleteIncident(Incident incident) {
    crimeDao.deleteIncident(incident.getId());
  }

  /**
   * Populate the database with incident averages by radius and year for the
   * entire city
   */
  public void calculateIncidentAverages() {
    // TODO: logic for calculating averages goes here
    AverageIncidentNumber average = new AverageIncidentNumber();
  }
}