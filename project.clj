(defproject ai.z7/sqlg-clj "0.0.9"
  :description "The SQL Graph with Tinkerpop3 and Clojure"
  :url "https://github.com/fern-flower-lab/sqlg-clj"
  :license {:name "MIT"}

  :managed-dependencies [[org.umlg/sqlg-postgres-dialect "3.0.1"]
                         [org.umlg/sqlg-hsqldb-dialect "3.0.1"]
                         [org.umlg/sqlg-h2-dialect "3.0.1"]
                         [org.umlg/sqlg-mariadb-dialect "3.0.1"]
                         [org.umlg/sqlg-mysql-dialect "3.0.1"]
                         [org.umlg/sqlg-mssqlserver-dialect "3.0.1"]
                         [org.umlg/sqlg-c3p0 "3.0.1"]]

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [potemkin "0.4.6"]
                 [org.apache.commons/commons-configuration2 "2.9.0"]
                 [com.google.guava/guava "31.1-jre"]
                 [ai.z7/java-properties "1.2.1"]

                 ;; upstream version of the SqlG library
                 [org.umlg/sqlg-postgres-dialect :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-hsqldb-dialect :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-h2-dialect :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-mariadb-dialect :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-mysql-dialect :exclusions [mysql/mysql-connector-java
                                                           com.google.guava/guava]]
                 [org.umlg/sqlg-mssqlserver-dialect :exclusions [com.google.guava/guava]]
                 [org.umlg/sqlg-c3p0 :exclusions [com.google.guava/guava]]]

  :source-paths ["src-clj"]
  #_#_:java-source-paths ["src-java"]
  :provided {:javac-options []}
  :plugins [[lein-junit "1.1.9"]]
  :global-vars {*warn-on-reflection* true
                *assert*             false})
