(ns user)

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro timeit
  "Like the standard time macro but it returns nil instead of what is
  evaluated. This is useful if you want to gather rough timing of a
  form execution and you don't want to blow the repl up with the
  results."
  [form]
  `(time
    (do
      ~form
      nil)))
