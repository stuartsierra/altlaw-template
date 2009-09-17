(ns runtests
  (:require org.altlaw.util.test-template)
  (:use clojure.test))

(run-all-tests #"^org\.altlaw\.util\..*test-.*$")
