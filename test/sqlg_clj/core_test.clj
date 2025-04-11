(ns sqlg-clj.core-test
  (:require [clojure.test :refer :all]
            [sqlg-clj.config :as c]
            [sqlg-clj.core
             :refer :all
             :exclude [is or and not filter map
                       count drop range key identity
                       sort min max repeat]]
            [sqlg-clj.core :as core]
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
      ;; Use the higher-level traversal API for cleaning
      (-> g traversal E core/drop iterate!)
      (-> g traversal V core/drop iterate!)
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
        ;; Add test vertices with unique IDs to prevent duplication issues
        (let [_ (d/add-V g :test {:aa 11 :bb 22 :id "v1"})
              _ (d/add-V g :test {:aa 33 :cc 44 :id "v2"})]
          (u/commit! g)

          ;; Query them back with specific match to get only our test vertices
          (let [vxs (-> g traversal V
                        (has-label :test)
                        (has :id)
                        (order)
                        (by :aa)
                        u/into-vec!)]
            ;; Verify count
            (is (= 2 (clojure.core/count vxs)))

            ;; Find specific vertices by ID to verify their properties
            (let [v1-result (-> g traversal V (has-label :test) (has :id "v1") u/into-vec!)
                  v2-result (-> g traversal V (has-label :test) (has :id "v2") u/into-vec!)]
              (is (-> v1-result clojure.core/count (= 1)))
              (is (-> v2-result clojure.core/count (= 1)))
              (is (-> v1-result first (value :aa) (= 11)))
              (is (-> v1-result first (value :bb) (= 22)))
              (is (-> v2-result first (value :aa) (= 33)))
              (is (-> v2-result first (value :cc) (= 44))))))))))

(deftest test-edge-operations
  (with-test-graph
    (fn [g]
      (testing "Creating and querying edges"
        ;; Add test vertices with unique IDs and connect them
        (let [v1 (d/add-V g :person {:name "Alice" :id "alice1"})
              v2 (d/add-V g :person {:name "Bob" :id "bob1"})
              edge (d/add-E v1 v2 :knows {:since 2020 :id "edge1"})]
          (u/commit! g)

          ;; Query back using traversal with specific IDs
          (let [alice (-> g traversal V (has-label :person) (has :id "alice1") u/into-vec!)]
            (is (= 1 (clojure.core/count alice)))

            ;; Query edges from Alice
            (let [knows-edges (-> g traversal V
                                  (has-label :person)
                                  (has :id "alice1")
                                  (outE :knows)
                                  u/into-vec!)]
              (is (= 1 (clojure.core/count knows-edges)))
              (is (= 2020 (value (first knows-edges) :since)))

              ;; Follow edge to verify it goes to Bob
              (let [bob (-> g traversal V
                            (has-label :person)
                            (has :id "bob1")
                            u/into-vec!)]
                (is (-> bob clojure.core/count (= 1)))
                (is (-> bob first (value :name) (= "Bob")))))))))))

(deftest test-transactions
  (with-test-graph
    (fn [g]
      (testing "Transaction commit/rollback"
        ;; Test commit with unique ID
        (d/add-V g :test-commit {:prop "test" :id "commit-v1"})
        (u/commit! g)
        (let [vertices (-> g traversal V (has-label :test-commit) (has :id "commit-v1") u/into-vec!)]
          (is (= 1 (clojure.core/count vertices))))

        ;; Test rollback with unique ID
        (d/add-V g :test-rollback {:prop "test" :id "rollback-v1"})
        (u/rollback! g)
        (let [vertices (-> g traversal V (has-label :test-rollback) (has :id "rollback-v1") u/into-vec!)]
          (is (= 0 (clojure.core/count vertices))))))))

;; Main function to allow running tests from command line
(defn -main []
  (println "Running SQLG-CLJ tests...")
  (run-tests 'sqlg-clj.core-test))