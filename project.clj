(defproject ai.z7/sqlg-clj "0.0.5"
  :description "The SQL Graph with Tinkerpop3 and Clojure"
  :url "https://github.com/fern-flower-lab/sqlg-clj"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [potemkin "0.4.6"]
                 [org.apache.commons/commons-configuration2 "2.8.0"]
                 [com.google.guava/guava "31.1-jre"]
                 [ai.z7/java-properties "1.1.1"]

                 [org.umlg/sqlg-postgres-dialect "2.1.6"
                  :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-hsqldb-dialect "2.1.6"
                  :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-h2-dialect "2.1.6"
                  :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-mariadb-dialect "2.1.6"
                  :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-mysql-dialect "2.1.6"
                  :exclusions [mysql/mysql-connector-java
                               com.google.guava/guava]]
                 [org.umlg/sqlg-mssqlserver-dialect "2.1.6"
                  :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-c3p0 "2.1.6"
                  :exclusions [com.google.guava/guava]]]
  :source-paths ["src-clj"]
  :java-source-paths ["src-java"]
  :profiles {:dev     {:global-vars  {*assert* true}
                       :dependencies [[ch.qos.logback/logback-classic "1.4.4"
                                       :exclusions [org.slf4j/slf4j-api]]
                                      [org.slf4j/jul-to-slf4j "2.0.3"]
                                      [org.slf4j/jcl-over-slf4j "2.0.3"]
                                      [org.slf4j/log4j-over-slf4j "2.0.3"]
                                      [org.clojure/tools.logging "1.2.4"]]
                       :junit        ["src-java"]}
             :uberjar {:aot :all}}
  :plugins [[lein-junit "1.1.9"]]
  :global-vars {*warn-on-reflection* true
                *assert*             false})
