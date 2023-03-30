(ns borkdude.deflet
  #?(:cljs (:require-macros [borkdude.deflet :refer [deflet bind]])))

(defmacro bind [name value]
  `(do (def ~name ~value)
       ~value))

(defmacro deflet [& forms]
  (let [f (first forms)
        r (next forms)]
    (if (and (seq? f) (#{'def 'bind} (first f)))
      `(let [~(second f) ~(nth f 2)]
         (deflet ~@r))
      (if-not r f
              (list 'do f `(deflet ~@r))))))

#?(:org.babashka/nbb
   (do
     (require 'promesa.core)
     (defmacro defp [name value]
       `(def ~name (nbb.core/await ~value)))
     (defmacro bindp [name value]
       `(do (defp ~name ~value)
            ~name))
     (defmacro defletp [& forms]
       (let [f (first forms)
             r (next forms)]
         (cond (and (seq? f) (#{'defp 'bindp} (first f)))
               `(promesa.core/let [~(second f) ~(nth f 2)]
                  (defletp ~@r))
               (and (seq? f) (= 'def (first f)))
               `(let [~(second f) ~(nth f 2)]
                  (defletp ~@r))
               :else
               (if-not r f
                       (list 'promesa.core/do f `(defletp ~@(rest forms))))))))
   :cljs (do
           ;; for clj-kondo
           (declare defp defletp bindp)))


