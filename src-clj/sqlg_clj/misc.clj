(ns sqlg-clj.misc
  (:require [sqlg-clj.util :as util]
            [sqlg-clj.core :refer [value-map]]))

(defmacro select->clj [& t]
  `(mapv #(util/map->native % {:keywordize? true :clj? true})
         (-> ~@t value-map util/into-seq!)))
