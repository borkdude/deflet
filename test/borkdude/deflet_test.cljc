(ns borkdude.deflet-test
  (:require
   [borkdude.deflet :refer [deflet bind #?@(:org.babashka/nbb [defletp defp bindp])]]
   [clojure.test :as t :refer [deftest is #?(:org.babashka/nbb async)]]
   #?(:org.babashka/nbb [promesa.core :as p])))

(deftest deflet-test
  (deflet
    (def x 10)
    (bind y 11)
    (is (= 21 (+ x y)))))

#?(:org.babashka/nbb
   (deftest defletp-test
     (async
      done
      (-> (defletp
            (defp x (p/delay 100 42))
            (bindp y (p/delay 100 (+ x 1)))
            (is (= 85 (+ x y))))
          (p/finally done)))))

;;;; Scratch

(comment
  (require '[clojure.walk])
  (clojure.walk/macroexpand-all '(deflet
                  (def x 10)
                  (inc x)
                  ))

  (clojure.walk/macroexpand-all
   '(deflet
     (def x 10)
     (def y (inc x))
     y
     ))


  )
