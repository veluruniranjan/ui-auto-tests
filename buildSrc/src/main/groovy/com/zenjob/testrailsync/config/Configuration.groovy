package com.zenjob.testrailsync.config

class Configuration {
        static final String PROPERTY_FILE_NAME = 'environment.properties'

        static File propertiesFile = new File(PROPERTY_FILE_NAME)

        static Properties properties = new Properties()

        static String getConfigProperty(String propertyName, String defaultValue = null) {
            if (System.getenv(propertyName)) {
                return System.getenv(propertyName)
            } else if (propertiesFile.exists()) {
                // located in the top directory
                properties.load(propertiesFile.newInputStream())

                if (properties.getProperty(propertyName)) {
                    return properties.getProperty(propertyName)
                } else {
                    throw new Exception("Property $propertyName isn't set")
                }
            } else if (defaultValue) {
                return defaultValue
            } else {
                throw new Exception("Property $propertyName isn't set")
            }
        }
}
