(ns core-test
  (:require [clojure.test :refer :all]
            [java-time.api :as jt]
            [dev.core :as core]))

(deftest overlapping-events
  (let [test-events (->> [{:name "Event 1" :start [2024 6 1] :end [2024 6 15]}
                          {:name "Event 2" :start [2024 6 16] :end [2024 6 30]}
                          {:name "Event 3" :start [2024 6 1] :end [2024 6 30]}
                          {:name "Event 4" :start [2024 6 20] :end [2024 6 25]}]
                         (mapv (fn [{:keys [name start end]}]
                                 (core/->Event name (apply jt/zoned-date-time start) (apply jt/zoned-date-time end)))))
        overlapping-events (->> test-events
                                core/get-overlapping-events
                                (mapv (partial mapv :name)))]
    (testing "sequence of all pairs of overlapping events"
      (is (= overlapping-events [["Event 3" "Event 2"]
                                 ["Event 4" "Event 3"]
                                 ["Event 4" "Event 2"]
                                 ["Event 3" "Event 1"]])))))
(comment
  (run-tests))