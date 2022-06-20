(ns sqlg-clj.config
  (:require [java-properties.core :as jconf])
  (:import (org.apache.commons.configuration2 BaseConfiguration Configuration ConfigurationConverter)
           (org.apache.tinkerpop.gremlin.structure Graph)
           (org.umlg.sqlg.structure SqlgGraph)
           (org.apache.tinkerpop.gremlin.structure.util GraphFactory)
           (java.util Map)))

(def load-config jconf/load-config)

(defn db-config ^Configuration [{:keys [type host port name user pass]}]
  (let [conf (doto (BaseConfiguration.)
               (.addProperty "jdbc.url"
                             (format "jdbc:%s://%s:%s/%s"
                                     type host port name)))]
    (when user (.addProperty conf "jdbc.username" (str user)))
    (when pass (.addProperty conf "jdbc.password" (str pass)))
    conf))

(defn config->clj [^Configuration cf]
  (into {} (ConfigurationConverter/getMap cf)))

;; Local SQLGraph instance
(defn graph ^Graph [^Configuration config]
  (SqlgGraph/open config))

; GraphFactory
(defn open-graph
  "Opens a new TinkerGraph with default configuration or open a new Graph instance with the specified
   configuration. The configuration may be a path to a file or a Map of configuration options.
   In order to get an SqlgGraph instance - pass prepared BaseConfiguration or Configuration object as
   an argument."
  ([conf]
   (cond
     (instance? Configuration conf)
     (SqlgGraph/open ^Configuration conf)
     (map? conf)
     (GraphFactory/open ^Map conf)
     (string? conf)
     (GraphFactory/open ^String conf))))
