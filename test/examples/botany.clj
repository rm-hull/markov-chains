(ns examples.botany
  (:require
    [markov-chains.core :refer [generate collate]]
    [turtle.core :refer [draw!]]
    [turtle.renderer.vector :refer [->svg]]))

;; --------------------------------------------------------------------------
;; Cut-down L-system compiler from:
;;     https://github.com/rm-hull/lindenmayer-systems
;; --------------------------------------------------------------------------
(def symbol-table {
    \- :right
    \+ :left
    \^ :fwd
    \[ :save
    \] :restore })

(defn split-on-assignment [symbols]
  [ (first symbols) (vec (drop 2 symbols)) ])

(defn make-converter [symbol-table]
  (fn [rule]
    (mapv #(get symbol-table % (symbol (str %))) rule)))

(defn builder [converter rules]
  (->>
    rules
    (map (comp split-on-assignment converter))
    (into (array-map))))

(defmacro l-system [axiom constants rules ]
  (let [convert (make-converter symbol-table)
        rules (builder convert rules)
        params (keys rules)
        init-args (map convert constants)]
    `(letfn [(seq0# [~@params]
              (cons ~(convert axiom) (lazy-seq (seq0# ~@(vals rules)))))]
       (seq0# ~@init-args))))
;; --------------------------------------------------------------------------

;; Firsly define a shrub using F=F[+F]F[-F][F]
(def shrub
  (->>
    (l-system "F" ("^") ("F=F[+F]F[-F][F]"))
    (drop 5)
    first
    flatten))

(println (take 30 shrub))

(clojure.pprint/pprint second-order-prob-matrix)

;; We can now work out some probability matrices
(def first-order-prob-matrix (collate shrub 1))
(def second-order-prob-matrix (collate shrub 2))
(def third-order-prob-matrix (collate shrub 3))
(def fourth-order-prob-matrix (collate shrub 4))

;; A few helper methods to slot in some angles/distances
;; (so that the instructions may be drawn with a turtle)

(defn add-scalar [op]
  (condp = op
    :right [:right 21.3]
    :left  [:left  21.3]
    :fwd   [:fwd   25]
    :save   :save
    :restore :restore))

(defn augment [ops]
  (reduce
    (fn [acc value]
      (conj acc (add-scalar value)))
    []
    ops))

;; Generate some examples

(spit "shrub1.svg"
  (draw!
    ->svg
    (augment (take 10000 (generate first-order-prob-matrix)))
    [800 600]))

(spit "shrub2.svg"
  (draw!
    ->svg
    (augment (take 10000 (generate second-order-prob-matrix)))
    [800 600]))

(spit "shrub3.svg"
  (draw!
    ->svg
    (augment (take 10000 (generate third-order-prob-matrix)))
    [800 600]))

(spit "shrub4.svg"
  (draw!
    ->svg
    (augment (take 10000 (generate fourth-order-prob-matrix)))
    [800 600]))

