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

(def second-order-prob-matrix {
  [:A5 :A5] { :A5 0.18 :D 0.6  :G 0.22 }
  [:A5 :D5] { :A5 0.5  :D 0.5  :G 0    }
  [:A5 :G5] { :A5 0.15 :D 0.75 :G 0.1  }
  [:D5 :D5] { :A5 0    :D 0    :G 1    }
  [:D5 :A5] { :A5 0.25 :D 0    :G 0.75 }
  [:D5 :G5] { :A5 0.9  :D 0.1  :G 0    }
  [:G5 :G5] { :A5 0.4  :D 0.4  :G 0.2  }
  [:G5 :A5] { :A5 0.5  :D 0.25 :G 0.25 }
  [:G5 :D5] { :A5 1    :D 0    :G 0    }})


