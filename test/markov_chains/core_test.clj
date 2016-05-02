(ns markov-chains.core-test
  (:require
    [clojure.test :refer :all]
    [markov-chains.core :refer :all]))

(def bag {:red 0.2 :black 0.5 :blue 0.3})

(deftest check-cumulative
  (is (= [[:black 0.5] [:blue 0.8] [:red 1.0]]
         (cumulative bag))))

(deftest check-select
  (is (= :black (select 0.0 bag)))
  (is (= :black (select 0.5 bag)))
  (is (= :blue (select 0.51 bag)))
  (is (= :blue (select 0.7 bag)))
  (is (= :red (select 0.80001 bag)))
  (is (= :red (select 1.0 bag)))
  (is (nil? (select 1.5 bag))))