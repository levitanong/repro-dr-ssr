(ns repro.root
  (:require [fulcro.client.primitives :as prim :refer [defsc]]
            [fulcro.incubator.dynamic-routing :as dr :refer [defrouter defsc-route-target]]
            #?(:cljs [fulcro.client.localized-dom :as dom]
               :clj [fulcro.client.localized-dom-server :as dom])
            ))

(defsc-route-target Main [this props]
  {:ident           (fn [] [:view/by-id :main])
   :query           []
   :initial-state   (fn [p])
   :route-segment   (fn [] ["results"])
   :will-enter      (fn [_ _] (dr/route-immediate [:view/by-id :main]))
   :will-leave      (fn [_] true)
   :route-cancelled (fn [_])}
  (dom/div "main"))

(defrouter RootRouter [this {:keys [current-state pending-path-segment]}]
  {:router-targets [Main]})

(def ui-root-router (prim/factory RootRouter))

(defsc Root [this {:keys [router]}]
  {:ident         (fn [] [:view/by-id :root])
   :query         [{:router (prim/get-query RootRouter)}]
   :initial-state (fn [p]
                    {:router (prim/get-initial-state RootRouter {})})}
  (dom/div "hello world!"
    (ui-root-router router)))
