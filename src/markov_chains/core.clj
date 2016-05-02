(ns markov-chains.core)

(defn cumulative
  "Reassemble the map of probabilities in cumulative order"
  [probabilities]
  (let [desc (sort-by (comp - second) probabilities)]
    (map list
      (map first desc)
      (reductions + (map second desc)))))

(defn select
  "Given a map of probabilities, select one at random."
  ([probabilities]
   (select (rand) probabilities))

  ([r probabilities]
   (let [cumu (cumulative probabilities)
         maxv (or (second (last cumu)) 1)
         r (* r maxv)]
     (ffirst
       (drop-while
         #(> r (second %))
         cumu)))))

(defn generate
  ([probabilities-matrix]
   (let [initial (rand-nth (keys probabilities-matrix))]
     (lazy-cat
       initial
       (generate
         initial
         probabilities-matrix))))

  ([state probabilities-matrix]
    (let [next-selection (select (get probabilities-matrix state))
          prev-state (vec (next state))]
      (if-not (nil? next-selection)
        (cons
          next-selection
          (lazy-seq
            (generate
              (conj prev-state next-selection)
              probabilities-matrix)))))))
