(ns app.main) 

(def app           (js/require "app"))
(def BrowserWindow (js/require "browser-window"))

(goog-define dev? false)

(defn load-page
  "When compiling with `:none` the compiled JS that calls .loadUrl is
  in a different place than it would be when compiling with optimizations
  that produce a single artifact (`:whitespace, :simple, :advanced`).

  Because of this we need to dispatch the loading based on the used
  optimizations, for this we defined `dev?` above that we can override
  at compile time using the `:clojure-defines` compiler option."
  [window]
  (if dev?
      (.loadUrl window (str "file://" js/__dirname "/../../index.html"))
      (.loadUrl window (str "file://" js/__dirname "/index.html"))))

(def main-window (atom nil))

(defn mk-window [w h frame? show?]
  (BrowserWindow. #js {:width w :height h :frame frame? :show show?}))

(defn init-browser []
  (reset! main-window (mk-window 800 600 true true))
  (load-page @main-window)
  (if dev? (.openDevTools @main-window))
  (.on @main-window "closed" #(reset! main-window nil)))

(defn init []
  (.on app "window-all-closed" #(when-not (= js/process.platform "darwin") (.quit app)))
  (.on app "ready" init-browser)
  (set! *main-cli-fn* (fn [] nil)))
