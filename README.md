# Markov Chains

[![Build Status](https://travis-ci.org/rm-hull/markov-chains.svg?branch=master)](http://travis-ci.org/rm-hull/markov-chains) [![Coverage Status](https://coveralls.io/repos/rm-hull/markov-chains/badge.svg?branch=master)](https://coveralls.io/r/rm-hull/markov-chains?branch=master) [![Dependencies Status](https://jarkeeper.com/rm-hull/markov-chains/status.svg)](https://jarkeeper.com/rm-hull/markov-chains) [![Downloads](https://jarkeeper.com/rm-hull/markov-chains/downloads.svg)](https://jarkeeper.com/rm-hull/markov-chains) [![Clojars Project](https://img.shields.io/clojars/v/rm-hull/markov-chains.svg)](https://clojars.org/rm-hull/markov-chains)

A library (and application examples) of stochastic discrete-time Markov Chains
(DTMC) in Clojure: a random process that undergoes state transitions according
to a pre-determined probability distribution matrix describing a state space.

**A. K. Dewdney** describes the process succinctly in _The Tinkertoy Computer,
and other machinations_:

> Simple in principle, Mark V. Shaney consists of two parts, a table builder
> and a text generator.  After scanning an input text and constructing the
> table of follower probabilities, Mark V. Shaney is ready to "talk". It begins
> with a single pair of words. The generating algorithm is simple:

    repeat
        r ← random
        determine pair follower
        output follower
        first ← second
        second ← word
    until someone complains

> When a random number _r_ is selected, it determines a follower by the process
> of adding together the probabilities store for each of the words that follow
> the given pair until those probabilities first equal or exceed _r_. In this
> way, each follower word will be selected, in the long run, with a frequency
> that reflects its frequency in the original text. And in this way, the text
> so generated bears an eerie resemblance to the original:
>
> "When I meet someone on a professional basis, I want them to shave their
> arms. While at a conference a few weeks back, I spen an interesting evening
> with a grain of salt. I wouldn't dare take them seriously! This brings me
> back to the brash people who dare others to do so or not. I love a good flame
> argument, probably more than anyone...

Aside from text generation, there are many other applications for using
markov-chains for generating superficially realistic data sets, some of which
will be explored in this project:

* **algorithmic music generation**, using [supercollider](http://supercollider.github.io/) and
  [overtone](http://overtone.github.io/) where the states of the system represent notes, and the
  probability matrix derived from an existing corpus of traditional music.

* **stochastic L-systems**, modelling the growth processes of plants with probalistic
  state rather than formalized rules.

* **machine code generation**, using a simple [redcode VM](https://github.com/rm-hull/corewar),
  we might be able to "grow" core-war contestants, find the most resilient and strongest contenders,
  breed them with genetic algorithms, and eventually they will take over.

### Pre-requisites

You will need [Leiningen](https://github.com/technomancy/leiningen) 2.6.1 or
above installed.

### Building

To build and install the library locally, run:

    $ cd markov-chains
    $ lein test
    $ lein install

### Including in your project

There is a version hosted at [Clojars](https://clojars.org/rm-hull/markov-chains).
For leiningen include a dependency:

```clojure
[rm-hull/markov-chains "0.0.1"]
```

For maven-based projects, add the following to your `pom.xml`:

```xml
<dependency>
  <groupId>rm-hull</groupId>
  <artifactId>markov-chains</artifactId>
  <version>0.0.1</version>
</dependency>
```

## Basic Usage

TODO

## Applications / Examples

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

## References

* [The Algorithmic Beauty of Plants](http://algorithmicbotany.org/papers/abop/abop.pdf) [PDF]
* [The Tinkertoy Computer and other machinations](https://www.amazon.co.uk/Tinkertoy-Computer-Other-Machinations-Recreations/dp/B019L537LS/)
* [Row, row, row your boat...](https://gist.github.com/ctford/2877443)

## License

### The MIT License (MIT)

Copyright (c) 2016 Richard Hull

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
