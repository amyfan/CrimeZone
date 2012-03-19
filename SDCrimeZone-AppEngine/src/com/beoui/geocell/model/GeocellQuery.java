package com.beoui.geocell.model;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * GeocellQuery splits the traditional query in 3 parts:
 * the base query string,
 * the declared parameters
 * and the list of object parameters.
 *
 * Additional information on http://code.google.com/appengine/docs/java/datastore/queriesandindexes.html
 *
 * This allows us to create new queries and adding conditions/filters like in the proximity fetch.
 *
 * @author Alexandre Gellibert
 *
 */
public class GeocellQuery {

    /**
     * Base query string without the declared parameters and without the entity name. Ex: "lastName == lastNameParam"
     *
     * CAREFUL: must not contain "order" clauses!
     */
    private String baseQuery;

    /**
     * (Optional)
     * Declared parameters. Ex: "String lastNameParam"
     */
    private String declaredParameters;

    /**
     * (Optional)
     * List of parameters. Ex: Arrays.asList("Smith")
     */
    private List<Object> parameters;

    public GeocellQuery(String baseQuery, String declaredParameters,
            List<Object> parameters) {
        //Validate.notEmpty(baseQuery);
        this.baseQuery = baseQuery;
        this.declaredParameters = declaredParameters;
        this.parameters = parameters;
    }

    public String getBaseQuery() {
        return baseQuery;
    }

    public String getDeclaredParameters() {
        return declaredParameters;
    }

    public List<Object> getParameters() {
        return parameters;
    }

}
