(ns borkdude.deflet-test
  (:require
   [borkdude.deflet :refer [deflet #?@(:org.babashka/nbb [defletp defp])]]
   [clojure.test :as t :refer [deftest is #?(:org.babashka/nbb async)]]
   #?(:org.babashka/nbb [promesa.core :as p])))

(deftest deflet-test
  (deflet
    (def x 10)
    (is (= 10 x))))

#?(:org.babashka/nbb
   (deftest defletp-test
     (async
      done
      (-> (defletp
            (defp x (p/delay 100 :result))
            (is (= :result x)))
          (p/finally done)))))

;;;; Scratch

(comment
  (require '[clojure.walk])
  (clojure.walk/macroexpand-all '(deflet
                  (def x 10)
                  (inc x)
                  ))
  )
