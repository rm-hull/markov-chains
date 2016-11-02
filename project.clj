(defproject rm-hull/markov-chains "0.1.0"
  :description "A library (and application examples) of stochastic discrete-time Markov Chains (DTMC) in Clojure"
  :url "https://github.com/rm-hull/markov-chains"
  :license {
    :name "The MIT License (MIT)"
    :url "http://opensource.org/licenses/MIT"}
  :dependencies [
    [org.clojure/clojure "1.8.0"]]
  :scm {:url "git@github.com:rm-markov-chains.git"}
  :plugins [
            ]
  :source-paths ["src"]
  :jar-exclusions [#"(?:^|/).git"]
  :codox {
    :source-paths ["src"]
    :output-path "doc/api"
    :source-uri "http://github.com/rm-hull/markov-chains/blob/master/{filepath}#L{line}"  }
  :min-lein-version "2.6.1"
  :profiles {
    :dev {
      :global-vars {*warn-on-reflection* true}
      :dependencies [
        [overtone "0.10.1"]
        [incanter/incanter "1.5.7"]
        [rm-hull/turtle "0.1.9"]]
      :plugins [
        [lein-cljfmt "0.5.6"]
        [lein-codox "0.10.1"]
        [lein-cloverage "1.0.9"]]}})
