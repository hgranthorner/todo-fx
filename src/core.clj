(ns core
  (:gen-class)
  (:require [cljfx.api :as fx]))

;; Define application state

(defmulti event-handler :event/type)

(def *state
  (atom {:title "App title"}))

;; Define render functions

(defn title-input [{:keys [title]}]
  {:fx/type :text-field
   :on-text-changed #(swap! *state assoc :title %)
   :text title})

(defn root [{:keys [title]}]
  {:fx/type :stage
   :showing true
   :title title
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :children [{:fx/type :label
                              :text "Window title input"}
                             {:fx/type title-input
                              :title title}
                             {:fx/type :button
                              :text "B button"
                              :on-action (fn [_] (println "Clicked"))}]}}})

;; Create renderer with middleware that maps incoming data - description -
;; to component description that can be used to render JavaFX state.
;; Here description is just passed as an argument to function component.

(def renderer
  (fx/create-renderer
    :middleware (fx/wrap-map-desc (fn [state] {:fx/type root
                                               :state state}))
    :opts {:fx.opt/map-event-handler event-handler}))

;; Convenient way to add watch to an atom + immediately render app

(defn -main [& args]
  (fx/mount-renderer *state renderer))

(comment
 (-main)
 ; To re-render without restarting the app
 (renderer)
 )
