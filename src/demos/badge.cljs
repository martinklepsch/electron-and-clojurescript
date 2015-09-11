(ns demos.dock)

(comment
  ;; Code for main process
  (def app (js/require "app"))
  (def ipc (js/require "ipc"))

  (.on ipc "bounce-dock" (fn [event arg]
                           (.. app -dock bounce)))
  (.on ipc "set-badge" (fn [event arg]
                         (.. app -dock (setBadge arg))))

  )

(comment
  (def ipc (js/require "ipc"))

  (.send ipc "bounce-dock")

  (.send ipc "set-badge" "122")

  )
