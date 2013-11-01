package uk.co.o2.stockservice.configuration

import com.mongodb.MongoURI

class MongoURIFactory {
    private static final String UNIQUE_DATABASE_NAME_CONNECTION_STRING_TOKEN = '^UNIQUE_DATABASE_NAME^'

    static MongoURI getURI(String connectionString) {
        new MongoURI(connectionString.replace(UNIQUE_DATABASE_NAME_CONNECTION_STRING_TOKEN, generateUniqueDatabaseNameBasedOnHostName()))
    }

    private static String generateUniqueDatabaseNameBasedOnHostName() {
        InetAddress.localHost.hostName.replace('.', '_')
    }
}
