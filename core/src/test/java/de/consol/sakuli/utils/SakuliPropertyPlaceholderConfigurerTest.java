/*
 * Sakuli - Testing and Monitoring-Tool for Websites and common UIs.
 *
 * Copyright 2013 - 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.consol.sakuli.utils;

import de.consol.sakuli.BaseTest;
import de.consol.sakuli.PropertyHolder;
import de.consol.sakuli.datamodel.properties.ActionProperties;
import de.consol.sakuli.datamodel.properties.SahiProxyProperties;
import de.consol.sakuli.datamodel.properties.SakuliProperties;
import de.consol.sakuli.datamodel.properties.TestSuiteProperties;
import de.consol.sakuli.loader.BeanLoader;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class SakuliPropertyPlaceholderConfigurerTest extends BaseTest {
    @Spy
    private SakuliPropertyPlaceholderConfigurer testling;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testling.setWritePropertiesToSahiConfig(false);
        testling.setLoadTestSuiteProperties(true);
        testling.setLoadSakuliProperties(true);
    }

    @Test
    public void testLoadPropertiesTestSuiteFolder() throws Exception {
        Properties props = spy(new Properties());
        testling.setLoadSakuliProperties(false);
        testling.loadProperties(props);
        verify(props).put(TestSuiteProperties.TEST_SUITE_FOLDER, TEST_FOLDER_PATH);
        verify(props).put(SakuliProperties.INCLUDE_FOLDER, INCLUDE_FOLDER_PATH);
        verify(props).put(SahiProxyProperties.PROXY_HOME_FOLDER, SAHI_FOLDER_PATH);
        verify(testling, never()).addPropertiesFromFile(props,
                Paths.get(INCLUDE_FOLDER_PATH).normalize().toAbsolutePath().toString() + SakuliProperties.SAKULI_PROPERTIES_FILE_APPENDER, true);
        verify(testling).addPropertiesFromFile(props,
                Paths.get(TEST_FOLDER_PATH).normalize().toAbsolutePath().toString() + TestSuiteProperties.TEST_SUITE_PROPERTIES_FILE_APPENDER, true);
        verify(testling, never()).modifyPropertiesConfiguration(anyString(), anyListOf(String.class), any(Properties.class));
        assertNull(props.getProperty(ActionProperties.ENCRYPTION_INTERFACE_TEST_MODE), null);
        assertEquals(props.getProperty(TestSuiteProperties.SUITE_ID), "0001_testsuite_example");
    }

    @Test
    public void testLoadPropertiesIncludeFolder() throws Exception {
        Properties props = spy(new Properties());
        testling.setLoadTestSuiteProperties(false);
        testling.loadProperties(props);
        verify(props).put(TestSuiteProperties.TEST_SUITE_FOLDER, TEST_FOLDER_PATH);
        verify(props).put(SakuliProperties.INCLUDE_FOLDER, INCLUDE_FOLDER_PATH);
        verify(testling).addPropertiesFromFile(props,
                Paths.get(INCLUDE_FOLDER_PATH).normalize().toAbsolutePath().toString() + SakuliProperties.SAKULI_PROPERTIES_FILE_APPENDER, true);
        verify(testling, never()).addPropertiesFromFile(props,
                Paths.get(TEST_FOLDER_PATH).normalize().toAbsolutePath().toString() + TestSuiteProperties.TEST_SUITE_PROPERTIES_FILE_APPENDER, true);

        assertNotNull(props.getProperty(SakuliProperties.INCLUDE_FOLDER));
        assertNull(props.getProperty(TestSuiteProperties.SUITE_ID));
    }

    @Test
    public void testLoadPropertiesSahiHomeNotset() throws Exception {
        SakuliPropertyPlaceholderConfigurer.SAHI_PROXY_HOME_VALUE = "";
        Properties props = spy(new Properties());
        testling.loadProperties(props);
        verify(props, never()).put(SahiProxyProperties.PROXY_HOME_FOLDER, "");
        SakuliPropertyPlaceholderConfigurer.SAHI_PROXY_HOME_VALUE = SAHI_FOLDER_PATH;
    }

    @Test
    public void testAddPropertiesFromFile() throws Exception {
        Path sakuliPropFile = Paths.get(getClass().getResource("/JUnit-sakuli.properties").toURI());
        assertTrue(Files.exists(sakuliPropFile));
        Properties props = new Properties();
        testling.addPropertiesFromFile(props, sakuliPropFile.toAbsolutePath().toString(), true);
        assertTrue(props.size() > 0);
        assertEquals(props.getProperty(ActionProperties.ENCRYPTION_INTERFACE_TEST_MODE), "true");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Error by reading the property file '.*invalid.properties'")
    public void testAddPropertiesFromFileRuntimeException() throws Exception {
        Properties props = new Properties();
        testling.addPropertiesFromFile(props, "invalid.properties", true);
    }

    @Test
    public void testTestSuiteFolder() throws IOException {
        PropertyHolder properties = BeanLoader.loadBean(PropertyHolder.class);
        Path tsFolder = Paths.get(properties.getTestSuiteFolder());

        assertTrue(Files.exists(tsFolder), "test suite folder doesn't exists or have not been set correctly");
        System.out.println(tsFolder.toFile().getAbsolutePath());
        assertTrue(tsFolder.toFile().getAbsolutePath().contains(TEST_FOLDER_PATH));
    }

    @Test
    public void testIncludeFolder() throws IOException {
        SakuliProperties properties = BeanLoader.loadBean(SakuliProperties.class);

        assertTrue(Files.exists(properties.getIncludeFolder()), "include folder doesn't exists");
        assertTrue(properties.getIncludeFolder().toString().contains(INCLUDE_FOLDER_PATH.substring(2)));

        assertNotNull(properties.getLogPattern());
    }
}