(defproject sqlg-clj "0.0.1"
  :description "The SQL Graph with Tinkerpop3 and Clojure"
  :url "https://github.com/fern-flower-lab/sqlg-clj"
  :license {:name "CURRENTLY UNSET"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [potemkin "0.4.5"]
                 [org.apache.tinkerpop/gremlin-core "3.6.0"]]
  :source-paths ["src-clj"]
  :java-source-paths ["src-java"]
  :profiles {:dev {:global-vars {*assert* true}
                   :dependencies [[commons-io/commons-io "2.11.0"]]
                   :junit ["src-java"]}
             :uberjar {:aot :all}}
  :plugins [[lein-junit "1.1.9"]]
  :global-vars {*warn-on-reflection* true
                *assert* false})
