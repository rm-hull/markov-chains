# Markov Chains

[![Build Status](https://travis-ci.org/rm-hull/markov-chains.svg?branch=master)](http://travis-ci.org/rm-hull/markov-chains) [![Coverage Status](https://coveralls.io/repos/rm-hull/markov-chains/badge.svg?branch=master)](https://coveralls.io/r/rm-hull/markov-chains?branch=master) [![Dependencies Status](https://jarkeeper.com/rm-hull/markov-chains/status.svg)](https://jarkeeper.com/rm-hull/markov-chains) [![Downloads](https://jarkeeper.com/rm-hull/markov-chains/downloads.svg)](https://jarkeeper.com/rm-hull/markov-chains) [![Clojars Project](https://img.shields.io/clojars/v/rm-hull/markov-chains.svg)](https://clojars.org/rm-hull/markov-chains)

A library (and application examples) of stochastic discrete-time Markov Chains (DTMC) in Clojure:
a random process that undergoes state transitions according to a pre-determined probability distribution
describing a state space.

**A. K. Dewdney** describes the process succinctly in _The Tinkertoy Computer, and other machinations_:

> Simple in principle, Mark V. Shaney consists of two parts, a table builder and a text generator.
> After scanning an input text and constructing the table of follower probabilities, Mark V. Shaney
> is ready to "talk". It begins with a single pair of words. The generating algorithm is simple:

    repeat
        r ← random
        determine pair follower
        output follower
        first ← second
        second ← word
    until someone complains

> When a random number _r_ is selected, it determines a follower by the process of adding together
> the probabilities store for each of the words that follow the given pair until those probabilities
> first equal or exceed _r_. In this way, each follower word will be selected, in the long run, with
> a frequency that reflects its frequency in the original text. And in this way, the text so generated
> bears an eerie resemblance to the original:
>
> "When I meet someone on a professional basis, I want them to shave their arms. While at a conference
> a few weeks back, I spen an interesting evening with a grain of salt. I wouldn't dare take them
> seriously! This brings me back to the brash people who dare others to do so or not. I love a good
> flame argument, probably more than anyone...

Aside from text generation, there are many other applications for using markov-chains for generating
superficially realistic data sets, some of which will be explored in this project:

* **algorithmic music generation**, using [supercollider](http://supercollider.github.io/) and
  [overtone](http://overtone.github.io/) where the states of the system represent notes, and the
  probability matrix derived from an existing corpus of traditional music.

* **stochastic L-systems**, modelling the growth processes of plans with probalistic
  state rather than formalized rules.

* **machine code generation**, using a simple [redcode VM](https://github.com/rm-hull/corewars),
  we might be able to "grow" core-wars contestants, find the most resilient and strongest contenders,
  breed them with genetic algorithms, and eventually they will take over.

### Pre-requisites

You will need [Leiningen](https://github.com/technomancy/leiningen) 2.6.1 or above installed.

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

# Basic Usage

TODO

# Applications / Examples

## Algorithmic Music

The [Wikipedia page](https://en.wikipedia.org/wiki/Markov_chain#Music) on Markov chains page
illustrates a simple 1st order matrix as follows:

| Note   | A    | C♯   | E♭  |
|--------|------|------|-----|
| **A**  | 0.1  | 0.6  | 0.3 |
| **C♯** | 0.25 | 0.05 | 0.7 |
| **E♭** | 0.7  | 0.3  | 0   |

We can easily represent this in code as:

```clojure
(def 1st-order-prob-matrix {
  :A { :A 0.1 :C♯ 0.6 :E♭ 0.3 }
  :C♯ { :A 0.25 :C♯ 0.05 :E♭ 0.7 }
  :E♭ { :A 0.7 :C♯ 0.3 :E♭ 0 }})
```

# References

* [The Algorithmic Beauty of Plants](http://algorithmicbotany.org/papers/abop/abop.pdf) [PDF]
* [The Tinkertoy Computer and other machinations](https://www.amazon.co.uk/Tinkertoy-Computer-Other-Machinations-Recreations/dp/B019L537LS/)

# License

## The MIT License (MIT)

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
