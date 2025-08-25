(ns bowling-clj.past-no-frame)

(defn calculate-roll [acc current]
  {:previous-spare (if (:new-frame acc) false (= 10 (+ current (:last-roll acc))))
   :previous-strike (if (:new-frame acc)
                      (= 10 current)
                      (:previous-strike acc))
   :previous-previous-strike (and (:new-frame acc)
                                  (< (:frame-counter acc) 10)
                                  (:previous-strike acc))
   :new-frame (or (not (:new-frame acc)) (= 10 current))
   :frame-counter (if (:new-frame acc)
                    (+ 1 (:frame-counter acc))
                    (:frame-counter acc))
   :last-roll current
   :running-total (if (>= (:frame-counter acc) 10)
                    (+ (:running-total acc)
                       current
                       (if (:previous-previous-strike acc) current 0))
                    (+ (:running-total acc)
                       current
                       (if (:previous-spare acc) current 0)
                       (if (:previous-strike acc) current 0)
                       (if (:previous-previous-strike acc) current 0)))})

(defn score
  "Calculate the total score of a whole game"
  [rolls]
  (:running-total
   (reduce calculate-roll
           {:previous-spare false
            :previous-strike false
            :previous-previous-strike false
            :new-frame true
            :frame-counter 0
            :last-roll 0
            :running-total 0}
           rolls)))
