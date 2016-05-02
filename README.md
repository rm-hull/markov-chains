# Markov Chains

A library (and application examples) of stochastic discrete-time Markov Chains (DTMC) in Clojure: 
a random process that undergoes state transitions according to a pre-determined probability distribution
describing a state space.

**A. K. Dewdney** describes the process succinctly in _The Tinkertoy Computer, and other machinations_:

> Simple in principle, Mark V. Shaney consists of two parts, a table builder and a text generator.
> After scanning an input text and constructing the table of follower probabilities, Mark V. Shaney
> is ready to "talk". It begins with a single pair of words. The generating algorithm is simple:

    repeat
        r <- random
        determine pair follower
        output follower
        first <- second
        second <- word
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
  probability matrix derived from an existing corpus of traditional music. For example, the markov
  chains page on [wikipedia](https://en.wikipedia.org/wiki/Markov_chain#Music) illustrates a simple 
  1st order matrix as follows (which we will use later):

| Note   | A    | C♯   | E♭  |
|--------|------|------|-----| 
| **A**  | 0.1  | 0.6  | 0.3 |
| **C♯** | 0.25 | 0.05 | 0.7 |
| **E♭** | 0.7  | 0.3  | 0   |
