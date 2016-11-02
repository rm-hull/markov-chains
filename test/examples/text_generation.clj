(ns examples.text-generation
  (:require
   [clojure.string :as s]
   [markov-chains.core :refer :all]))

(def justified-sinner
  (->
   (slurp "test/examples/resources/2276.txt")
   (s/split #"\s+")
   (collate 2)))

(def three-men-in-a-boat
  (->
   (slurp "test/examples/resources/308.txt")
   (s/split #"\s+")
   (collate 2)))

(->>
 (generate three-men-in-a-boat)
 (take 60)
 (clojure.string/join " "))

