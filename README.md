# deflet

Make let-expressions REPL-friendly!

## Usage

``` clojure
(require '[borkdude.deflet :refer [deflet]])
```

Write inline-defs like you would in a Rich comment form, so you can evaluate
things expression by expression as you go:

``` clojure
(comment
  (def x 10)
  (inc x) ;;=> 11
)
```

but now without polluting the global environment in production / library code:

``` clojure
(deflet
  (def x 10)
  (inc x)
  ) ;;=> 11
```

The above `deflet` form expands into:

``` clojure
(let [x 10]
  (inc x))
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
         '[promesa.core :as p])

(deftest defletp-test
  (defletp
    (defp x (p/delay 100 :result))
    (is (= :result x))))
```

The `defp` works like `def` but wraps the result with `nbb.core/await` to await
top level promises. So when evaluating `(defp x (p/delay 100 :result))` in the
nbb REPL, you'll get a var `x` bound to `100` instead of a promise. But the
`defletp` macro expands this into a `promesa.core/let` expression.
