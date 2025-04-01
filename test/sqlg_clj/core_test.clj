(ns sqlg-clj.core-test
  (:require [clojure.test :refer :all]
            [sqlg-clj.config :as c]
            [sqlg-clj.core :refer :all]
            [sqlg-clj.util :as u]
            [sqlg-clj.data :as d])
  (:import (org.apache.commons.configuration2 Configuration)
           (org.apache.tinkerpop.gremlin.structure Graph T)
           (org.umlg.sqlg.structure SqlgGraph)))

;; Note: These tests are designed to be run on a local PostgreSQL database
;; Configuration should be provided via environment variables or system properties
;; For CI/CD, consider using an in-memory database or test containers

(def test-config
  "Test configuration - replace with your own connection settings or 
   set up environment variables for CI."
  {:type "postgresql"
   :host (or (System/getenv "SQLG_TEST_HOST") "localhost")
   :port (or (System/getenv "SQLG_TEST_PORT") "5432")
   :name (or (System/getenv "SQLG_TEST_DB") "sqlgtest")
   :user (or (System/getenv "SQLG_TEST_USER") "postgres")
   :pass (or (System/getenv "SQLG_TEST_PASS") "postgres")})

(defn clean-test-graph [g]
  "Removes all edges and vertices from the test graph"
  (try
    (when g
      (doto g
        (.traversal)
        (.E)
        (.drop)
        (.iterate))
      (doto g
        (.traversal)
        (.V)
        (.drop)
        (.iterate))
      (u/commit! g))
    (catch Exception e
      (println "Error cleaning test graph:" (.getMessage e)))))

(defn with-test-graph [f]
  "Fixture that provides a graph to tests and ensures cleanup"
  (let [config (c/db-config test-config)
        g (try
            (c/graph config)
            (catch Exception e
              (println "Error creating test graph:" (.getMessage e))
              nil))]
    (try
      (clean-test-graph g)
      (f g)
      (finally
        (clean-test-graph g)
        (when g
          (.close g))))))

;; Example-based tests from README.adoc

(deftest test-basic-config
  (testing "Basic configuration loading"
    (let [config (c/db-config test-config)]
      (is (instance? Configuration config))
      (let [config-map (c/config->clj config)]
        (is (contains? config-map "jdbc.url"))
        (is (contains? config-map "jdbc.username"))
        (is (contains? config-map "jdbc.password"))))))

(deftest test-graph-creation
  (with-test-graph
    (fn [g]
      (testing "Graph creation"
        (is (instance? SqlgGraph g))
        (is (instance? Graph g))))))

(deftest test-vertex-operations
  (with-test-graph
    (fn [g]
      (testing "Creating and querying vertices"
        ;; Add test vertices
        (let [v1 (d/add-V g :test {:aa 11 :bb 22})
              v2 (d/add-V g :test {:aa 33 :cc 44})]
          (u/commit! g)

          ;; Query them back
          (let [vxs (-> g traversal V (has-label :test) (has :aa) u/into-vec!)]
            (is (= 2 (count vxs)))
            (is (= [11 33] (map #(value % :aa) vxs)))
            (is (= [22 nil] (map #(value % :bb) vxs)))
            (is (= [nil 44] (map #(value % :cc) vxs)))))))))

(deftest test-edge-operations
  (with-test-graph
    (fn [g]
      (testing "Creating and querying edges"
        ;; Add test vertices and connect them
        (let [v1 (d/add-V g :person {:name "Alice"})
              v2 (d/add-V g :person {:name "Bob"})
              edge (d/add-E v1 v2 :knows {:since 2020})]
          (u/commit! g)

          ;; Query back using traversal
          (let [knows-edges (-> g traversal V
                                (has-label :person)
                                (has :name "Alice")
                                (outE :knows)
                                u/into-vec!)]
            (is (= 1 (count knows-edges)))

            ;; Follow edge to vertex
            (let [friends (-> g traversal V
                              (has-label :person)
                              (has :name "Alice")
                              (out :knows)
                              u/into-vec!)]
              (is (= 1 (count friends)))
              (is (= "Bob" (value (first friends) :name))))))))))

(deftest test-transactions
  (with-test-graph
    (fn [g]
      (testing "Transaction commit/rollback"
        ;; Test commit
        (d/add-V g :test-commit {:prop "test"})
        (u/commit! g)
        (let [vertices (-> g traversal V (has-label :test-commit) u/into-vec!)]
          (is (= 1 (count vertices))))

        ;; Test rollback
        (d/add-V g :test-rollback {:prop "test"})
        (u/rollback! g)
        (let [vertices (-> g traversal V (has-label :test-rollback) u/into-vec!)]
          (is (= 0 (count vertices))))))))

;; Main function to allow running tests from command line
(defn -main []
  (println "Running SQLG-CLJ tests...")
  (run-tests 'sqlg-clj.core-test))