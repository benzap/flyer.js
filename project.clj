(defproject flyer "0.1.0-SNAPSHOT"
  :description "Clojurescript + Javascript Broadcast Messaging between
  iFrames, Framesets, and Windows"
  :url "https://github.com/benzap/flyer.js"
  :license {:name "Apache License V2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2197"
                  :exclusions [org.apache.ant/ant]]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :cljsbuild {:builds {:dev
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/flyer-debug.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       :prod
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/flyer.js"
                                   :optimizations :advanced
                                   :pretty-print false}}}})

 
