(defproject flyer "1.1.2"
  :description "Clojurescript + Javascript Broadcast Messaging between iFrames, Framesets, and Windows"
  :url "https://github.com/benzap/flyer.js"
  :license {:name "Apache License V2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"
                  :exclusions [org.apache.ant/ant]]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-ancient "0.6.15"]]
  :hooks [leiningen.cljsbuild]
  :repositories [["clojars" {:sign-releases false}]]
  :source-paths ["src-cljs"]
  :cljsbuild {:builds {:dev
                       {:source-paths ["src-cljs"]
                        :compiler {:output-dir "resources/public/js"
                                   :output-to "resources/public/js/flyer.js"
                                   :optimizations :whitespace
                                   :pretty-print true
                                   :source-map "resources/public/js/flyer.js.map"}}
                       :prod
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/flyer.min.js"
                                   :optimizations :advanced
                                   :pretty-print false}}}})

 
