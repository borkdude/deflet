(ns playwright.example
  (:require ["playwright$default" :refer [chromium]]
            [clojure.test :as t :refer [deftest is async]]
            [borkdude.deflet :refer #_:clj-kondo/ignore [defletp defp]]
            [promesa.core :as p]))

(def headless (boolean (.-CI js/process.env)))

(deftest my-test
  (let [browser-ref (atom nil)]
    (async
     done
     (->
      (defletp
        ;; Let the story begin!
        (defp browser (.launch chromium #js {:headless headless}))
        (reset! browser-ref browser)
        (defp page (.newPage browser))
        (.goto page "https://clojure.org" #js{:waitUntil "networkidle"})
        (defp h2 (p/-> (.locator page "h2")
                       (.allInnerTexts)
                       first))
        (is (= h2 "The Clojure Programming Language")))
      (p/finally #(do (.close @browser-ref)
                      (done)))))))

(comment

  (t/run-tests *ns*)

  )
