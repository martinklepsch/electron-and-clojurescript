(ns app.main)

(def electron      (js/require "electron"))
(def app           (.-app electron))
(def BrowserWindow (.-BrowserWindow electron))

(goog-define dev? false)

(defn load-page
  "When compiling with `:none` the compiled JS that calls .loadURL is
  in a different place than it would be when compiling with optimizations
  that produce a single artifact (`:whitespace, :simple, :advanced`).

  Because of this we need to dispatch the loading based on the used
  optimizations, for this we defined `dev?` above that we can override
  at compile time using the `:clojure-defines` compiler option."
  [window]
  (if dev?
      (.loadURL window (str "file://" js/__dirname "/../../index.html"))
      (.loadURL window (str "file://" js/__dirname "/index.html"))))

(def main-window (atom nil))

(defn make-window
  [& {:keys [width height frame? show?] :or {width 800 height 600 frame? true show? true}}]
  (BrowserWindow. #js {:width width :height height :frame frame? :show show?}))

(defn init-browser
  []
  (reset! main-window (make-window))
  (load-page @main-window)
  (if dev? (.openDevTools @main-window))
  (.on @main-window "closed" #(reset! main-window nil)))

(defn init
  []
  (.on app "window-all-closed" #(when-not (= js/process.platform "darwin") (.quit app)))
  (.on app "ready" init-browser)
  (set! *main-cli-fn* (fn [] nil)))
