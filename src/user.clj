(ns user
  (:require [nextjournal.clerk :as clerk]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn start-clerk
  "Start clerk, open the browser and watch the notebooks directory."
  []
  (clerk/serve! {:browse? true
                 :watch-paths ["notebooks"]})
  (println "You can shut clerk down with (clerk/halt!) or (stop-clerk)")
  (clerk/show! 'circle))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn stop-clerk
  "Shut down clerk"
  []
  (clerk/halt!))

(println "You can start the clerk notebook server
with `(start-clerk)`. Launch a specific notebook by calling `(clerk/show! 'ns)`")

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
