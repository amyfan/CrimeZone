package com.crimezone.sd.server.persistence;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.crimezone.sd.server.domain.AverageIncidentNumber;
import com.crimezone.sd.server.domain.Incident;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Query;

/**
 * 
 * @author Amy
 * 
 */
public class CrimeDataStore {
  private static final Logger log = Logger.getLogger(CrimeDataStore.class.getName());

  private DAO dao;

  public CrimeDataStore() {
    dao = new DAO();
  }

  /**
   * Find a {@link Incident} by id.
   * 
   * @param id
   *          the {@link Incident} id
   * @return the associated {@link Incident}, or null if not found
   */
  public Incident findIncident(Long id) {
    if (id == null) {
      return null;
    }

    Incident incident = dao.getObjectify().get(Incident.class, id);
    return incident;
  }

  public Incident findIncidentByKey(Key<Incident> incidentKey) {
    if (incidentKey == null) {
      return null;
    }

    Incident incident = dao.getObjectify().get(incidentKey);
    return incident;
  }

  public List<Incident> findAllIncidents() {
    Query<Incident> q = dao.getObjectify().query(Incident.class);
    return q.list();
  }

  public List<Incident> findIncidentsByStartDate(Date startDate) {
    // TODO: this query is wrong, implement appropriate date range filter here
    Query<Incident> q = dao.getObjectify().query(Incident.class).filter("startDate", startDate);
    return q.list();
  }

  public List<Incident> findIncidentsByYear(Integer year) {
    Query<Incident> q = dao.getObjectify().query(Incident.class).filter("year", year);
    return q.list();
  }

  public List<Incident> findIncidentsByYearAndRadius(Integer year, Integer radius) {
    Query<Incident> q = dao.getObjectify().query(Incident.class).filter("year", year)
        .filter("radius", radius);
    return q.list();
  }

  public Incident updateIncident(Incident incident) {
    dao.getObjectify().put(incident); // id populated in this statement
    System.out.println("Updated Incident to datastore: " + incident.toString());
    return incident;
  }

  public void deleteIncident(Long id) {
    System.out.println("Deleting Incident from datastore: " + id);
    dao.getObjectify().delete(Incident.class, id);
  }

  /**
   * TODO: verify that this works
   */
  public void deleteAllIncidents() {
    System.out.println("Deleting all Incidents from datastore: ");
    dao.getObjectify().delete(dao.getObjectify().query(Incident.class).fetchKeys());
  }

  public List<AverageIncidentNumber> findAverageIncidentNumbersByYearAndRadius(Integer year,
      Integer radius) {
    Query<AverageIncidentNumber> q = dao.getObjectify().query(AverageIncidentNumber.class)
        .filter("year", year).filter("radius", radius);
    return q.list();
  }
}