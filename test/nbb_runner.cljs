(ns nbb-runner
  (:require [borkdude.deflet-test]
            [cljs.test]))

(cljs.test/run-tests 'borkdude.deflet-test)
