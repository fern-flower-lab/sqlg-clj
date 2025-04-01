{:dev          {:global-vars {*assert* true}
                #_#_:junit ["src-java"]}

 :logging      {:dependencies [[ch.qos.logback/logback-classic "1.4.11"
                                :exclusions [org.slf4j/slf4j-api]]
                               [org.slf4j/jul-to-slf4j "2.0.7"]
                               [org.slf4j/jcl-over-slf4j "2.0.7"]
                               [org.slf4j/log4j-over-slf4j "2.0.7"]
                               [org.clojure/tools.logging "1.2.4"]]}

 :lib-3.1.1    {:managed-dependencies ^:replace
                                      [[org.umlg/sqlg-postgres-dialect "3.1.1"]
                                       [org.umlg/sqlg-hsqldb-dialect "3.1.1"]
                                       [org.umlg/sqlg-h2-dialect "3.1.1"]
                                       [org.umlg/sqlg-mariadb-dialect "3.1.1"]
                                       [org.umlg/sqlg-mysql-dialect "3.1.1"]
                                       [org.umlg/sqlg-mssqlserver-dialect "3.0.4"]
                                       [org.umlg/sqlg-c3p0 "3.1.1"]]}

 :lib-3.0.4    {:managed-dependencies ^:replace
                                      [[org.umlg/sqlg-postgres-dialect "3.0.4"]
                                       [org.umlg/sqlg-hsqldb-dialect "3.0.4"]
                                       [org.umlg/sqlg-h2-dialect "3.0.4"]
                                       [org.umlg/sqlg-mariadb-dialect "3.0.4"]
                                       [org.umlg/sqlg-mysql-dialect "3.0.4"]
                                       [org.umlg/sqlg-mssqlserver-dialect "3.0.4"]
                                       [org.umlg/sqlg-c3p0 "3.0.4"]]}

 :lib-3.0.1    {:managed-dependencies ^:replace
                                      [[org.umlg/sqlg-postgres-dialect "3.0.1"]
                                       [org.umlg/sqlg-hsqldb-dialect "3.0.1"]
                                       [org.umlg/sqlg-h2-dialect "3.0.1"]
                                       [org.umlg/sqlg-mariadb-dialect "3.0.1"]
                                       [org.umlg/sqlg-mysql-dialect "3.0.1"]
                                       [org.umlg/sqlg-mssqlserver-dialect "3.0.1"]
                                       [org.umlg/sqlg-c3p0 "3.0.1"]]}

 :lib-2.1.6    {:managed-dependencies ^:replace
                                      [[org.umlg/sqlg-postgres-dialect "2.1.6"]
                                       [org.umlg/sqlg-hsqldb-dialect "2.1.6"]
                                       [org.umlg/sqlg-h2-dialect "2.1.6"]
                                       [org.umlg/sqlg-mariadb-dialect "2.1.6"]
                                       [org.umlg/sqlg-mysql-dialect "2.1.6"]
                                       [org.umlg/sqlg-mssqlserver-dialect "2.1.6"]
                                       [org.umlg/sqlg-c3p0 "2.1.6"]]}

 :updated-deps {:dependencies [[potemkin "0.4.8"]
                               [org.apache.commons/commons-configuration2 "2.11.0"]]}

 :uberjar      {:aot :all}}