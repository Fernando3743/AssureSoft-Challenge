(ns dev.core
  (:require
    [java-time.api :as jt]))

;;Double Booked
;;When maintaining a calendar of events, it is important to know if an event overlaps with another event.
;;Given a sequence of events, each having a start and end time, write a program that will return the sequence of all pairs of overlapping events.
;;If you have any questions or need clarification on the problem, please let me know.

(defprotocol Overlap
  (overlap? [this event])
  (interval [this]))

(defrecord Event [name start end]
  Overlap
  (interval [_]  (jt/interval (jt/instant start) (jt/instant end)))
  (overlap? [this event2]
    (jt/overlaps? (interval this) (interval event2))))

(defn get-overlapping-events [events]
  (into #{}
   (for [event1 events
         event2 events
         :when (and (not= event1 event2)
                    (overlap? event1 event2))]
     #{event1 event2})))

