(ns examples.algorithmic-music
  (:require
    [markov-chains.core :refer :all]))

(def first-order-prob-matrix {
  :A { :A 0.1 :C♯ 0.6 :E♭ 0.3 }
  :C♯ { :A 0.25 :C♯ 0.05 :E♭ 0.7 }
  :E♭ { :A 0.7 :C♯ 0.3 :E♭ 0 }})

