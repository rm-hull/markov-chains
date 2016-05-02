(ns examples.algorithmic-music
  (:require
    [markov-chains.core :refer [generate]]
    [overtone.core :refer :all]))

(def first-order-prob-matrix {
  [:A4]  { :A4 0.1  :C#4 0.6  :Eb4 0.3 }
  [:C#4] { :A4 0.25 :C#4 0.05 :Eb4 0.7 }
  [:Eb4] { :A4 0.7  :C#4 0.3  :Eb4 0 }})

(take 12 (generate first-order-prob-matrix))

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

  (chord-progression-time (take 24 (generate first-order-prob-matrix)))
)

(def second-order-prob-matrix {
  [:A4 :A4] { :A4 0.18 :D4 0.6  :G4 0.22 }
  [:A4 :D4] { :A4 0.5  :D4 0.5  :G4 0    }
  [:A4 :G4] { :A4 0.15 :D4 0.75 :G4 0.1  }
  [:D4 :D4] { :A4 0    :D4 0    :G4 1    }
  [:D4 :A4] { :A4 0.25 :D4 0    :G4 0.75 }
  [:D4 :G4] { :A4 0.9  :D4 0.1  :G4 0    }
  [:G4 :G4] { :A4 0.4  :D4 0.4  :G4 0.2  }
  [:G4 :A4] { :A4 0.5  :D4 0.25 :G4 0.25 }
  [:G4 :D4] { :A4 1    :D4 0    :G4 0    }})

(take 12 (generate second-order-prob-matrix))

(comment

  (chord-progression-time (take 40 (generate second-order-prob-matrix)))
)

