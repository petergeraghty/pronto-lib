package org.github.prontolib.integrationtest;

import org.junit.Before;

public class IntegrationTest {

    private boolean enabled;

    @Before
    public void setup() {
        enabled = "true".equals(System.getProperty("enable.integration.tests"));
    }

    public boolean isEnabled() {
        return enabled;
    }

}
