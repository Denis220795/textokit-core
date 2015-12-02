/*
 *    Copyright 2015 Textocat
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.textocat.textokit.eval;

import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.*;
import java.util.Map;
import java.util.Properties;

import static com.textocat.textokit.eval.ConfigurationKeys.PREFIX_LISTENER_ID;
import static com.textocat.textokit.eval.ConfigurationKeys.PREFIX_LISTENER_PROPERTY;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

/**
 * @author Rinat Gareev
 */
public class EvaluationLauncher {

    private static final String APP_CONTEXT_LOCATION =
            "classpath:com/textocat/textokit/eval/app-context.xml";

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(
                    "Usage: <properties-config-filepath>");
            return;
        }
        File propsFile = new File(args[0]);
        if (!propsFile.isFile()) {
            System.err.println("Can't find file " + propsFile);
            return;
        }
        Properties configProperties = readProperties(propsFile);

        runUsingProperties(configProperties);
    }

    public static void runUsingProperties(Properties configProperties) throws Exception {
        GenericApplicationContext appCtx = new GenericApplicationContext();

        appCtx.getEnvironment().getPropertySources().addLast(
                new PropertiesPropertySource("configFile", configProperties));

        XmlBeanDefinitionReader xmlBDReader = new XmlBeanDefinitionReader(appCtx);
        xmlBDReader.loadBeanDefinitions(APP_CONTEXT_LOCATION);

        // register listeners
        Map<String, String> listenerImpls = getPrefixedKeyPairs(configProperties,
                PREFIX_LISTENER_ID);
        for (String listenerId : listenerImpls.keySet()) {
            String listenerClass = listenerImpls.get(listenerId);
            BeanDefinitionBuilder bb = genericBeanDefinition(listenerClass);
            Map<String, String> listenerProperties = getPrefixedKeyPairs(configProperties,
                    PREFIX_LISTENER_PROPERTY + listenerId + ".");
            for (String propName : listenerProperties.keySet()) {
                bb.addPropertyValue(propName, listenerProperties.get(propName));
            }
            appCtx.registerBeanDefinition(listenerId, bb.getBeanDefinition());
        }

        appCtx.refresh();

        appCtx.registerShutdownHook();

        GoldStandardBasedEvaluation eval = appCtx.getBean(GoldStandardBasedEvaluation.class);
        eval.run();
    }

    private static Properties readProperties(File srcFile) throws IOException {
        Properties result = new Properties();
        InputStream srcIS = new FileInputStream(srcFile);
        Reader srcReader = new BufferedReader(new InputStreamReader(srcIS, "utf-8"));
        try {
            result.load(srcReader);
        } finally {
            IOUtils.closeQuietly(srcReader);
        }
        return result;
    }

    private static Map<String, String> getPrefixedKeyPairs(Properties props, String prefix) {
        Map<String, String> result = Maps.newHashMap();
        for (String key : props.stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                result.put(key.substring(prefix.length()), props.getProperty(key));
            }
        }
        return result;
    }

    private EvaluationLauncher() {
    }
}