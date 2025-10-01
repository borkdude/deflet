(ns example
  (:require ["playwright$default" :refer [chromium]]
            [clojure.test :as t :refer [deftest is async]]
            [borkdude.deflet :refer [defletp defp]]
            [promesa.core :as p]))

(def headless (boolean (.-CI js/process.env)))

(deftest my-test
  (async
   done
   (defletp
     (defp browser-ref (atom nil))
     (->
      (defletp
        ;; Let the story begin!
        (defp browser (.launch chromium #js {:headless headless}))
        (reset! browser-ref browser)
        (defp context (.newContext browser))
        (defp page (.newPage context))
        (.goto page "https://clojure.org" #js{:waitUntil "networkidle"})
        (defp h2 (p/-> (.locator page "h2")
                       (.allInnerTexts)
                       first))
        (is (= h2 "The Clojure Programming Language")))
      (p/finally #(do (.close @browser-ref)
                      (done)))))))

(defn -main []
  (t/run-tests 'example))

(comment

  (t/run-tests *ns*))
