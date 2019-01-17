(ns repro.server
  (:require [hiccup2.core :as hiccup]
            [clojure.java.io :as io]
            [fulcro.server-render :as ssr]
            [fulcro.client.primitives :as prim]
            [fulcro.client.dom-server :as dom]
            [repro.root :as root]))

(defn template
  [{:keys [head-content body-content]}]
  (hiccup/html
   (hiccup/raw "<!DOCTYPE html>")
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible"
             :content "IE=edge"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     [:title "Repro"]
     (hiccup/raw head-content)]
    [:body
     [:div {:id "app"}
      (hiccup/raw body-content)]
     [:script {:type "text/javascript"
               :src "js/main.js"}]
     ]]))

(defn generate-index-html [state-db app-html]
  (let [initial-state-script (ssr/initial-state->script-tag state-db)]
    (template {:head-content initial-state-script
               :body-content app-html})))

(defn index-html []
  (let [initial-tree (prim/get-initial-state root/Root {})
        initial-db   (ssr/build-initial-state initial-tree root/Root)
        ui-root      (prim/factory root/Root)]
    (generate-index-html initial-db (dom/render-to-str (ui-root initial-tree)))))

(defn -main []
  (spit (io/resource "public/index.html") (index-html))
  )
