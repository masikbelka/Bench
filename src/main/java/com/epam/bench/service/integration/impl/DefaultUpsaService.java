package com.epam.bench.service.integration.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epam.bench.domain.integration.upsa.CustomEmployeeComposeObjectDto;
import com.epam.bench.domain.integration.upsa.EmployeeSimpleView;
import com.epam.bench.domain.integration.upsa.UpsaRestApiUrls;
import com.epam.bench.service.integration.UpsaService;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public class DefaultUpsaService implements UpsaService {

    private final Logger LOG = LoggerFactory.getLogger(DefaultUpsaService.class);
    @Inject
    private RestTemplate restTemplate;

    @Override
    public EmployeeSimpleView getEmployee(String employeeId) {
        HttpEntity<String> entity = getStringHttpEntity();

        try {
            return restTemplate.exchange(UpsaRestApiUrls.BASE_URL_PRODUCTION_LATEST + "employees/{employeeId}?compose=probation,unit,location", HttpMethod.GET, entity,
                CustomEmployeeComposeObjectDto.class, employeeId).getBody().getEmployeeSimpleView();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return new EmployeeSimpleView();
    }

    private HttpEntity<String> getStringHttpEntity() {
        //TODO proper implementation!! Token will stores in another place
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> details = (HashMap<String, String>) SecurityContextHolder.getContext().getAuthentication().getDetails();
        HashMap<String, String> values = new HashMap<>();
        values.put("Authorization", "bearer  " + details.get("token"));
        headers.setAll(values);

        return new HttpEntity<>("parameters", headers);
    }
}
