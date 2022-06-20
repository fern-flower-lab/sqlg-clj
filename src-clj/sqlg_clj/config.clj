(ns sqlg-clj.config
  (:require [java-properties.core :as jconf])
  (:import (org.apache.commons.configuration2 BaseConfiguration Configuration)
           (org.apache.tinkerpop.gremlin.structure Graph)
           (org.umlg.sqlg.structure SqlgGraph)))

(def load-config jconf/load-config)

(defn db-config ^Configuration [{:keys [type host port name user pass]}]
  (let [conf (doto (BaseConfiguration.)
               (.addProperty "jdbc.url"
                             (format "jdbc:%s://%s:%s/%s"
                                     type host port name))
               (.addProperty "jdbc.username" user)
               (.addProperty "jdbc.password" pass))]
    conf))

(defn graph ^Graph [^Configuration config]
  (SqlgGraph/open config))

