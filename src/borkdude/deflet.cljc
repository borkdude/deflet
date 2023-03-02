(ns borkdude.deflet
  #?(:cljs (:require-macros [borkdude.deflet :refer [deflet]])))

(defmacro deflet [& forms]
  (let [f (first forms)
        r (next forms)]
    (if (and (seq? f) (= 'def (first f)))
      `(let [~(second f) ~(nth f 2)]
         (deflet ~@r))
      (if-not r f
              (list 'do f `(deflet ~@(rest forms)))))))

#?(:org.babashka/nbb
   (do
     (require 'promesa.core)
     (defmacro defp [name value]
       `(def ~name (nbb.core/await ~value)))
     (defmacro defletp [& forms]
       (let [f (first forms)
             r (next forms)]
         (cond (and (seq? f) (= 'defp (first f)))
               `(promesa.core/let [~(second f) ~(nth f 2)]
                  (defletp ~@r))
               (and (seq? f) (= 'def (first f)))
               `(let [~(second f) ~(nth f 2)]
                  (defletp ~@r))
               :else
               (if-not r f
                       (list 'do f `(defletp ~@(rest forms)))))))))


