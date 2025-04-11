(ns sqlg-clj.config
  (:require [java-properties.core :as jconf])
  (:import (org.apache.commons.configuration2 BaseConfiguration Configuration ConfigurationConverter)
           (org.apache.tinkerpop.gremlin.structure Graph)
           (org.umlg.sqlg.structure SqlgGraph)
           (org.apache.tinkerpop.gremlin.structure.util GraphFactory)
           (java.util Map)))

(def ^{:doc "Load configuration from properties files.
            Delegates to java-properties.core/load-config."}
  load-config jconf/load-config)

(defn db-config
  "Creates a Configuration object from a map of database parameters.
   
   Expected keys:
     :type - Database type (e.g. 'postgresql')
     :host - Database host
     :port - Database port
     :name - Database name
     :user - Database username (optional)
     :pass - Database password (optional)
   
   Returns an Apache Commons Configuration object ready for use with SqlgGraph."
  ^Configuration [{:keys [type host port name user pass]}]
  (let [conf (doto (BaseConfiguration.)
               (.addProperty "jdbc.url"
                             (format "jdbc:%s://%s:%s/%s"
                                     type host port name)))]
    (when user (.addProperty conf "jdbc.username" (str user)))
    (when pass (.addProperty conf "jdbc.password" (str pass)))
    conf))

(defn config->clj
  "Converts an Apache Commons Configuration object to a Clojure map.
   
   Parameters:
     cf - The Configuration object to convert
   
   Returns a map of configuration properties."
  [^Configuration cf]
  (into {} (ConfigurationConverter/getMap cf)))

(defn graph
  "Creates a SqlgGraph instance from a Configuration.
   
   Parameters:
     config - An Apache Commons Configuration object (typically created with db-config)
   
   Returns a Graph instance with the specified configuration."
  ^Graph [^Configuration config]
  (SqlgGraph/open config))

(defn open-graph
  "Opens a new TinkerGraph with default configuration or open a new Graph instance with the specified
   configuration. The configuration may be a path to a file or a Map of configuration options.
   
   Parameters:
     conf - One of:
            - Apache Commons Configuration object (returns SqlgGraph)
            - Clojure map of configuration options
            - String path to a configuration file
   
   Returns a Graph instance with the specified configuration."
  ([conf]
   (cond
     (instance? Configuration conf)
     (SqlgGraph/open ^Configuration conf)
     (map? conf)
     (GraphFactory/open ^Map conf)
     (string? conf)
     (GraphFactory/open ^String conf))))
