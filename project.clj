(defproject rm-hull/markov-chains "0.0.1"
  :description "A library (and application examples) of stochastic discrete-time Markov Chains (DTMC) in Clojure"
  :url "https://github.com/rm-hull/markov-chains"
  :license {
    :name "The MIT License (MIT)"
    :url "http://opensource.org/licenses/MIT"}
  :dependencies [
    [org.clojure/clojure "1.8.0"]]
  :scm {:url "git@github.com:rm-markov-chains.git"}
  :plugins [
    [codox "0.9.4"] ]
  :source-paths ["src"]
  :jar-exclusions [#"(?:^|/).git"]
  :codox {
    :sources ["src"]
    :output-dir "doc/api"
    :src-dir-uri "http://github.com/rm-markov-chains/blob/master/"
    :src-linenum-anchor-prefix "L" }
  :min-lein-version "2.6.1"
  :profiles {
    :dev {
      :global-vars {*warn-on-reflection* true}
      :dependencies [
        [overtone "0.10.1"]
        [incanter/incanter "1.5.7"]
        [rm-hull/turtle "0.1.9"]]
      :plugins [
        [lein-cloverage "1.0.6"]]}})
