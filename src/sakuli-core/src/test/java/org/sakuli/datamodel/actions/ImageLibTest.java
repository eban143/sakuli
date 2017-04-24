/*
 * Sakuli - Testing and Monitoring-Tool for Websites and common UIs.
 *
 * Copyright 2013 - 2017 the original author or authors.
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

package org.sakuli.datamodel.actions;

import org.sakuli.BaseTest;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertNotNull;

/**
 * @author tschneck
 *         Date: 09.04.2015
 */
public class ImageLibTest {

    @Test
    public void testAddImagesFromFolder() throws Exception {
        Path path = Paths.get(BaseTest.getResource(".", this.getClass()));
        ImageLib imageLib = new ImageLib();
        imageLib.addImagesFromFolder(path);

        List<String> strings = Arrays.asList("calc.jpged.jpg", "calc.jpged", "calc.pngfile.PNG", "calc.pngfile");
        for (String s : strings) {
            assertNotNull(imageLib.getImage(s));
            assertNotNull(imageLib.getPattern(s));
        }
    }
}