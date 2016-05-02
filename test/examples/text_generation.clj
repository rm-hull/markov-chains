(ns examples.text-generation
  (:require
    [clojure.string :as s]
    [markov-chains.core :refer :all]))

(def justified-sinner
  (->
    (slurp "resources/data/2276.txt")
    (s/split #"\s+")
    (collate 2)))

(def three-men-in-a-boat
  (->
    (slurp "resources/data/308.txt")
    (s/split #"\s+")
    (collate 2)))

(->>
  (generate three-men-in-a-boat)
  (take 60)
  (clojure.string/join " "))

