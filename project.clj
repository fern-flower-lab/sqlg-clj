(defproject sqlg-clj "0.0.1"
  :description "The SQL Graph with Tinkerpop3 and Clojure"
  :url "https://github.com/fern-flower-lab/sqlg-clj"
  :license {:name "CURRENTLY UNSET"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [potemkin "0.4.5"]
                 ;[commons-io/commons-io "2.11.0"]
                 ;[org.apache.commons/commons-configuration2 "2.7"]
                 [ai.z7/java-properties "1.0.1"]
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
                       :junit        ["src-java"]}
             :uberjar {:aot :all}}
  :plugins [[lein-junit "1.1.9"]]
  :global-vars {*warn-on-reflection* true
                *assert*             false})
