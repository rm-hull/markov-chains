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
   (ffirst
     (drop-while
       #(> r (second %))
       (cumulative probabilities)))))

(defn generate
  ([probabilities-matrix]
   (generate (rand-nth (keys probabilities-matrix)) probabilities-matrix))

  ([start probabilities-matrix]
    (if-not (nil? start)
      (cons
        start
        (lazy-cat
          (generate
            (select (get probabilities-matrix start))
            probabilities-matrix))))))
