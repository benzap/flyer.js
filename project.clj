(defproject flyer "1.0.3"
  :description "Clojurescript + Javascript Broadcast Messaging between
  iFrames, Framesets, and Windows"
  :url "https://github.com/benzap/flyer.js"
  :license {:name "Apache License V2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2173"
                  :exclusions [org.apache.ant/ant]]]
  :plugins [[lein-cljsbuild "1.0.2"]]
  :cljsbuild {:builds {:dev
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/flyer.js"
                                   :jar true
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       :prod
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/flyer.min.js"
                                   :optimizations :advanced
                                   :pretty-print false}}}})

 
