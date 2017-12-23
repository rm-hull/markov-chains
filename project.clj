(defproject rm-hull/markov-chains "0.1.1"
  :description "A library (and application examples) of stochastic discrete-time Markov Chains (DTMC) in Clojure"
  :url "https://github.com/rm-hull/markov-chains"
  :license {
    :name "The MIT License (MIT)"
    :url "http://opensource.org/licenses/MIT"}
  :scm {:url "git@github.com:rm-markov-chains.git"}
  :source-paths ["src"]
  :jar-exclusions [#"(?:^|/).git"]
  :codox {
    :source-paths ["src"]
    :output-path "doc/api"
    :source-uri "http://github.com/rm-hull/markov-chains/blob/master/{filepath}#L{line}"  }
  :min-lein-version "2.7.1"
  :profiles {
    :dev {
      :global-vars {*warn-on-reflection* true}
      :dependencies [
        [org.clojure/clojure "1.9.0"]
        [overtone "0.10.3"]
        [incanter "1.9.1"]
        [rm-hull/turtle "0.1.11"]]
      :plugins [
        [lein-cljfmt "0.5.7"]
        [lein-codox "0.10.3"]
        [lein-cloverage "1.0.10"]]}})
