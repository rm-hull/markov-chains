(ns examples.population-modelling
  (:require
   [markov-chains.core :refer [generate]]))

(comment

  (defn population-modelling [town country]
    (cons
     [town country]
     (lazy-seq
      (population-modelling
       (+ (* town 0.9) (* country 0.2))
       (+ (* town 0.1) (* country 0.8))))))

  (first (drop 1000 (population-modelling 5 4)))
  (first (drop 1000 (population-modelling 8.5 0.5)))

  (use '(incanter core stats datasets charts))

  (def model {[:town]    {:town 0.9 :country 0.1}
              [:country] {:town 0.2 :country 0.8}})

  (defn accumulator [data]
    (reductions
     (fn [acc value] (update acc value inc))
     (into {} (map vector (distinct data) (repeat 0)))
     data))

  (defn ratio [kw]
    (fn [m]
      (double (/ (kw m) (apply + (vals m))))))

  (view
   (line-chart
    (iterate inc 0)
    (->>
     (generate model)
     (take 5000)
     accumulator
     next
     (map (ratio :town))
     (map (partial * 9)))
    :x-label "time"
    :y-label "town dwellers"))

  (* 9 ((ratio :town) (frequencies (take 1000000 (generate model)))))

  (->>
   (generate model)
   (take 1000000)
   frequencies
   ((ratio :town))
   (* 9)))
