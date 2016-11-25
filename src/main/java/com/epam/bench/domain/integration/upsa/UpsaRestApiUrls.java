package com.epam.bench.domain.integration.upsa;

/**
 * Created by Tetiana_Antonenko1
 */
public interface UpsaRestApiUrls {
    String BASE_URL_PRODUCTION_LATEST = "https://upsa.epam.com/workload/rest/";
    String BASE_URL_PRODUCTION_V3 = "https://upsa.epam.com/workload/rest/v3/";
    String BASE_URL_STAGING_LATEST = "https://hrmstag.epam.com/kitty/rest/";
    String BASE_URL_STAGING_V3 = "https://hrmstag.epam.com/kitty/rest/v3/";
    String AUTHENTICATION_RELATIVE_PATH = "oauth/token";
    String HP_ASSET_MANAGER = "http://evbyminsd37fa:8080/AssetManagerWebService/";
    String AUTHENTICATION_URL_PRODUCTION_LATEST = "https://upsa.epam.com/workload/rest/oauth/token";
    String AUTHENTICATION_URL_PRODUCTION_V3 = "https://upsa.epam.com/workload/rest/v3/oauth/token";
    String AUTHENTICATION_URL_STAGING_LATEST = "https://hrmstag.epam.com/kitty/rest/oauth/token";
    String AUTHENTICATION_URL_STAGING_V3 = "https://hrmstag.epam.com/kitty/rest/v3/oauth/token";

}
