(ns examples.algorithmic-music
  (:require
    [markov-chains.core :refer [generate]]
    [overtone.core :refer :all]))

(def first-order-prob-matrix {
  :A4  { :A4 0.1  :C#4 0.6  :Eb5 0.3 }
  :C#4 { :A4 0.25 :C#4 0.05 :Eb5 0.7 }
  :Eb5 { :A4 0.7  :C#4 0.3  :Eb5 0 }})

(take 30 (generate first-order-prob-matrix))

(comment

  (boot-internal-server)

  (definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
    (* (env-gen (env-lin attack sustain release) 1 1 0 1 FREE)
       (saw freq)
       vol))

  (defn saw2 [music-note]
      (saw-wave (midi->hz (note music-note))))

  (defn play-chord [a-chord]
    (doseq [note a-chord] (saw2 note)))

  (defn chord-progression-time [notes]
    (doseq [[time note] (map list (iterate (partial + 400) (now)) notes)]
      (at time (play-chord (chord note :major)))))

  (chord-progression-time (take 30 (generate first-order-prob-matrix)))
)
