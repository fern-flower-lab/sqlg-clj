(ns sqlg-clj.data
  (:require [sqlg-clj.util :as util])
  (:import (org.umlg.sqlg.structure SqlgGraph)
           (org.apache.tinkerpop.gremlin.process.traversal.dsl.graph GraphTraversalSource)
           (org.umlg.sqlg.structure.topology Topology VertexLabel IndexType EdgeLabel)
           (org.apache.tinkerpop.gremlin.structure Vertex)))

(defn topology ^GraphTraversalSource [^SqlgGraph g]
  (.topology g))

(defn topology* ^Topology [^SqlgGraph g]
  (.getTopology g))

(defn ensure-V ^VertexLabel
  ([^Topology t label]
   (.ensureVertexLabelExist t
                            ^String (util/cast-param label)))
  ([^Topology t label m]
   (.ensureVertexLabelExist t
                            ^String (util/cast-param label)
                            (util/map->native m))))

(def ensureV ensure-V)

(defn V-property [^VertexLabel vl prop]
  (some-> vl (.getProperty (util/cast-param prop)) (.orElse nil)))

(def Vproperty V-property)

(defn ensure-Idx [^VertexLabel vl ^IndexType itype & props]
  (some-> vl (.ensureIndexExists itype props)))

(def ensureIdx ensure-Idx)

(defn ensure-E ^EdgeLabel
  ([^VertexLabel vl-from ^VertexLabel vl-to label]
   (.ensureEdgeLabelExist vl-from vl-to ^String (util/cast-param label)))
  ([^VertexLabel vl-from ^VertexLabel vl-to label m]
   (.ensureEdgeLabelExist vl-from vl-to ^String (util/cast-param label) (util/map->native m))))

(def ensureE ensure-E)

(defn add-V ^Vertex
  ([^SqlgGraph g label & params]
   (if (empty? params)
     (.addVertex g ^String (util/cast-param label))
     (if (and (= (count params) 1) (-> params first map?))
       (.addVertex g ^String (util/cast-param label) (util/map->native (first params)))
       (.addVertex g ^String (util/cast-param label) (util/cast-every-other-param params))))))

(def addV add-V)

(defn add-E [^Vertex v-from ^Vertex v-to label & params]
  (.addEdge v-from (util/cast-param label) v-to (util/cast-every-other-param params)))

(def addE add-E)