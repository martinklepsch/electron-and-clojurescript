(ns demos.shell)

(comment
  (def cp (js/require "child_process"))

  (.spawn cp "screencapture" #js ["-i" "screenshot.png"])

  )
