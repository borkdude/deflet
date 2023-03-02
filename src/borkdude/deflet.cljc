(ns borkdude.deflet
  {:clj-kondo/config
   '{:config-in-call
     {borkdude.deflet/deflet {:linters {:inline-def {:level :off}}}}}})

#?(:org.babashka/nbb
   (do
     (def nbb? true)
     (require 'promesa.core)
     (defmacro defp [name value]
       `(def ~name (nbb.core/await ~value))))
   :default (def nbb? false))

(defmacro deflet [& forms]
  (let [f (first forms)
        r (next forms)]
    (if (and (seq? f) (= 'def (first f)))
      `(let [~(second f) ~(nth f 2)]
         (deflet ~@r))
      (if-not r f
              (list 'do f `(deflet ~@(rest forms)))))))

#?(:org.babashka/nbb
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
                     (list 'do f `(defletp ~@(rest forms))))))))


