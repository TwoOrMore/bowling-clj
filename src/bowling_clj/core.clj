(ns bowling-clj.core)

(defn calculate-roll [acc current]
  {:previous-spare (if (:new-frame acc) false (= 10 (+ current (:last-roll acc))))
   :new-frame (not (:new-frame acc))
   :frame-counter (if (:new-frame acc) (+ 1 (:frame-counter acc)) (:frame-counter acc))
   :last-roll current
   :running-total (if (>= (:frame-counter acc) 10)
                    (+ (:running-total acc) current)
                    (+ (:running-total acc) current (if (:previous-spare acc) current 0)))})

(defn score
  "Calculate the total score of a whole game"
  [rolls]
  (:running-total
   (reduce calculate-roll
           {:previous-spare false
            :new-frame true
            :frame-counter 0
            :last-roll 0
            :running-total 0}
           rolls)))
