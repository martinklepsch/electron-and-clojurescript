(set-env!
 :source-paths    #{"sass" "src"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs "1.7.48-3" :scope "test"]
                 [org.clojure/clojurescript "1.7.107"]
                 [adzerk/boot-cljs-repl "0.1.10-SNAPSHOT" :scope "test"]
                 [adzerk/boot-reload    "0.3.2-SNAPSHOT"  :scope "test"]
                 [pandeiro/boot-http    "0.6.3"           :scope "test"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]])

(deftask prod-build []
  (comp (cljs :ids #{"main"}
              :optimizations :simple)
        (cljs :ids #{"renderer"}
              :optimizations :advanced)))

(deftask dev-build []
  (comp ;; Audio feedback about warnings etc. =======================
        (speak)
        ;; Inject REPL and reloading code into renderer build =======
        (cljs-repl :ids #{"renderer"})
        (reload    :ids #{"renderer"}
                   :on-jsload 'app.renderer/init)
        ;; Compile renderer =========================================
        (cljs      :ids #{"renderer"})
        ;; Compile JS for main process ==============================
        ;; path.resolve(".") returns the directory `electron` was
        ;; invoked in and not the directory our main.js file is in.
        ;; Because of this we need to override the compilers `:asset-path option`
        (cljs      :ids #{"main"}
                   :optimizations :none
                   :compiler-options {:asset-path "target/main.out"
                                      :closure-defines {'app.main/dev? true}})))
