(ns demos.filesystem)

(comment

  (def fs (js/require "fs"))

  (def path (js/require "path"))

  (def current-dir (.resolve path "."))

  (.writeFile fs
              (str current-dir "/written-from-cljs.txt")
              "I didn't expect it to be so warm in Finland.")

  )
