# Applications / Examples

The full code for these examples can be found in the [test/examples](https://github.com/rm-hull/markov-chains/blob/master/test/examples) directory.

### Text Generation

Using some common (free) literature from [project gutenberg](https://www.gutenberg.org/):

```clojure
(use 'markov-chains.core)

(def three-men-in-a-boat
  (->
    (slurp "test/examples/resources/308.txt")
    (clojure.string/split #"\s+")
    (collate 2)))

(->>
  (generate three-men-in-a-boat)
  (take 60)
  (clojure.string/join " "))
; => "it oozed over the blackness, and heard Harris’s sleepy voice asking
;     where we drew near it, so they spread their handkerchiefs on the back
;     of Harris and Harris’s friend as to avoid running down which, we managed
;     to get out of here while this billing and cooing is on. We’ll go down
;     to eat vegetables. He said they were demons."
```

### Algorithmic Music

The [Wikipedia page](https://en.wikipedia.org/wiki/Markov_chain#Music) on
Markov chains shows a simple 1st-order matrix as follows:

| Note   | A    | C♯   | E♭   |
|--------|-----:|-----:|-----:|
| **A**  | 0.1  | 0.6  | 0.3  |
| **C♯** | 0.25 | 0.05 | 0.7  |
| **E♭** | 0.7  | 0.3  | 0    |

We can easily represent this in code as (using the same MIDI notation as used
with `overtone.live/note`):

```clojure
(use 'markov-chains.core)

(def first-order-prob-matrix {
  [:A4]  { :A4 0.1  :C#4 0.6  :Eb4 0.3 }
  [:C#4] { :A4 0.25 :C#4 0.05 :Eb4 0.7 }
  [:Eb4] { :A4 0.7  :C#4 0.3  :Eb4 0 }})

(take 12 (generate first-order-prob-matrix))
; => (:Eb4 :A4 :C#4 :Eb4 :A4 :Eb4 :A4 :C#4 :Eb4 :A4 :C#4 :Eb4)
```

Obviously, the random nature means that the sequence will be different on each
evaluation. _(Note to self: maybe should use a state monad for the random
function)_

The keys on the matrix represent the current system state, along with any
previous state that also needs to be remembered: as this is a first-order
matrix, only the current state is used. As shown in the next example,
higher-order systems will need to consider past state as well in order to
produce results with a sense of phrasal structure, rather than the aimless
wandering of a first-order system.

This generated MIDI sequence can be then fed into overtone (make sure you have
installed supercollider first):

```clojure
(use 'overtone.live)

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
```

For a 2nd-order matrix, we take the previous state into consideration as well
as the current state:

| Note   | A    | D    | G    |
|--------|-----:|-----:|-----:|
| **AA** | 0.18 | 0.6  | 0.22 |
| **AD** | 0.5  | 0.5  | 0    |
| **AG** | 0.15 | 0.75 | 0.1  |
| **DD** | 0    | 0    | 1    |
| **DA** | 0.25 | 0    | 0.75 |
| **DG** | 0.9  | 0.1  | 0    |
| **GG** | 0.4  | 0.4  | 0.2  |
| **GA** | 0.5  | 0.25 | 0.25 |
| **GD** | 1    | 0    | 0    |

Representing this as follows:

```clojure
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
; => (:D4 :A4 :G4 :G4 :D4 :A4 :G4 :D4 :A4 :A4 :A4 :D4)
```

Playing in overtone, gives a slighly less jarring rendition than the
first-order system:

```clojure
(chord-progression-time (take 40 (generate second-order-prob-matrix)))
```

> TODO: Row-row-row your boat example

### Artificial Botany

A realistic shrub can be generated with a formal
[L-system](https://github.com/rm-hull/lindenmayer-systems) grammar by
repeatedly iterating the following rule and running the resultant operations
through a [logo-style interpreter](https://github.com/rm-hull/turtle):

    F → F[+F]F[-F][F]

This results in a fairly uniform shrub structure as follows:

![SVG](https://rawgithub.com/rm-hull/markov-chains/master/test/examples/resources/formal_grammar_shrub.svg)

By taking the stream of operations that the L-system outputs (`:fwd`, `:left`,
`:right`, etc.) and collating them into a probability matrix, we are now able
to get the markov chains generator to emit a realistic stream of operations
to pass to the logo drawing system.

```clojure
(def shrub
  (->>
    (l-system "F" ("^") ("F=F[+F]F[-F][F]"))
    (drop 5)
    first
    flatten))

(def second-order-prob-matrix (collate shrub 2))

(clojure.pprint/pprint second-order-prob-matrix)
; => {(:right :fwd) {:restore 625, :save 156},
;     (:save :fwd) {:restore 625, :save 156},
;     (:restore :save) {:fwd 781, :left 156, :right 156},
;     (:restore :restore) {:fwd 156, :save 218, :restore 93},
;     (:restore :fwd) {:save 781},
;     (:fwd :save) {:left 625, :right 625},
;     (:left :fwd) {:restore 625, :save 156},
;     (:save :right) {:fwd 781},
;     (:save :left) {:fwd 781},
;     (:fwd :restore) {:fwd 625, :save 875, :restore 375}}

(spit "markov_shrub.svg"
  (draw!
    ->svg
    (augment (take 10000 (generate second-order-prob-matrix)))
    [800 600]))
```

See the [source](https://github.com/rm-hull/markov-chains/blob/master/test/examples/botany.clj)
for details on the `l-system` implementation. The generated SVG is random of
course, but would look something like this:

![SVG](https://rawgithub.com/rm-hull/markov-chains/master/test/examples/resources/markov_shrub.svg)

### Population Modelling

In _Mathematische Abenteuer mit dem Computer_, **L. Råde** & **R.D. Nelson**
pose exercise 15.1 as follows:

![SVG](https://rawgithub.com/rm-hull/markov-chains/master/doc/population-modelling.svg)

> In a certain country the movement of people between cities
> and the countryside is such that, in any one year, 20 per cent of
> those living in the countryside move to the cities, while 10 per
> cent of those living in the cities move to live in the countryside.
>
> Suppose that at the beginning of a year 5 million people live in the
> cities and 4 million live in the countryside. Write a program so that
> you can study the development year by year of the numbers of
> inhabitants in the cities and the countryside.

Writing this program directly, might yield a solution as follows:

```clojure
(defn population-modelling [town country]
  (cons
    [town country]
    (lazy-seq
      (population-modelling
        (+ (* town 0.9) (* country 0.2))
        (+ (* town 0.1) (* country 0.8))))))

(first (drop 1000 (population-modelling 5 4)))
; => [6.000000000000029 3.000000000000017]
```

Clearly the combined population of 9 million settles quite quicky into 6
million townsfolk and 3 million in the countryside. What is interesting is
that any pair combination that adds upto 9 million ends up with the same
value.

```clojure
(first (drop 1000 (population-modelling 8.5 0.5)))
; => [6.000000000000021 3.0000000000000115]
```

Actual population migration will never of course be exactly as the model
suggests, but we can use the probabilities in the exercise and create a
corresponding probability matrix:

```clojure
(def model {
  [:town]    { :town 0.9 :country 0.1 }
  [:country] { :town 0.2 :country 0.8 }})
```

And then some machinery to run the model and graph the results:

```clojure
(use '(incanter core stats datasets charts))

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
```

Running this a few times illustrates that whatever starting point, the ratio of town
dwellers to countryfolk settles to 2 to 1 over time:

![PNG](https://rawgithub.com/rm-hull/markov-chains/master/doc/pop1.png) ![PNG](https://rawgithub.com/rm-hull/markov-chains/master/doc/pop2.png) ![PNG](https://rawgithub.com/rm-hull/markov-chains/master/doc/pop3.png)

Using an even longer timeframe:

```clojure
(->>
  (generate model)
  (take 1000000)
  frequencies
  ((ratio :town))
  (* 9))
; => 5.9997419999999995
```

