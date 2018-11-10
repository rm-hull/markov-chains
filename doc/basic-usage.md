# Basic Usage

The library consists of two functions `markov-chains.core/collate` and
`markov-chains.core/generate`.

### Table building

_collate_ takes a list of tokens and a number representing the desired
remembered state. It will produce a map of state transitions and their
probabilities.  **IMPORTANT NOTE:** rather than representing the probabilities
in the range 0..1, _collate_ just returns a tally of counts.  The _generate_
function is aware of tally counts and makes the necessary adjustments to turn
them into probabilities.

```clojure
(use 'markov-chains.core)

(def prob-matrix
  (collate (seq "AEAEAAAAAEAAAAAAEEAEEAEAEEEAAAEAAAA") 1))

(clojure.pprint/pprint prob-matrix)
; => {(\A) {\E 8, \A 14},
;     (\E) {\A 8, \E 4}}
```

### Output generator

_generate_ takes a probability matrix, and produces an infinite stream of
tokens, taking into consideration any remembered state. The stream is lazily
evaluated, so it is important to use it with `take` or some other similar sink
function.

```clojure
(apply str
  (take 40
    (generate prob-matrix)))
; => "EAAAAAAEAEAAEAEAEEAAAEEEEEEAAAAAAAAEEAAE"
```
