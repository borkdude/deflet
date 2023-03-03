# deflet

[![Clojars Project](https://img.shields.io/clojars/v/io.github.borkdude/deflet.svg)](https://clojars.org/io.github.borkdude/deflet)

Make let-expressions REPL-friendly!

> Let them have their inline-def and eat it too.

## Usage

``` clojure
(require '[borkdude.deflet :refer [deflet]])
```

Write inline-defs like you would in a Rich comment form, so you can evaluate
things expression by expression as you go:

``` clojure
(comment
  (def x 10) ;; => #'x
  (def y (inc x)) ;;=> #'y
  y ;;=> 11
)
```

but now without polluting the global environment in production / library code,
while still having the ability to evaluate expressions in the REPL:

``` clojure
(deflet
  ;; evaluation still works for individual forms in a REPL-connected editor:
  (def x 10) ;;=> #'x
  (def y (inc x)) ;;=> #'y
  ;; but the whole expression is compiled into a let-expression which returns the last value:
  y) ;;=> 11
```

The above `deflet` form expands into:

``` clojure
(let [x 10]
  (let [y (inc x)]
    y)) ;;=> 11
```

I find the inline-def style particularly helpful when exploring code in the REPL, e.g. when writing tests:

``` clojure
(deftest deflet-test
  (deflet
    (def x 10)
    (is (= 10 x))))
```

## Nbb

This library also contains an [nbb](https://github.com/babashka/nbb) REPL-friendly variant, called `defletp` which works in concert with [promesa](https://github.com/funcool/promesa):

``` clojure
(require '[borkdude.deflet :refer [defletp defp]]
         '[cljs.test :refer [deftest async is]]
         '[promesa.core :as p])

(deftest defletp-test
  (async
   done
   (-> (defletp
         (defp x (p/delay 100 :result))
         (is (= :result x)))
       (p/finally done))))
```

The `defp` works like `def` but wraps the result with `nbb.core/await` to await
top level promises. So when evaluating `(defp x (p/delay 100 :result))` in the
nbb REPL, you'll get a var `x` bound to `100` instead of a promise. But the
`defletp` macro expands this into a `promesa.core/let` expression.

[Here](examples/playwright) is a demo of how you can use deflet with nbb and playwright tests.

<img src="https://user-images.githubusercontent.com/284934/222552490-439cb704-d0b0-4650-b0fc-0e18f49423eb.png">
