(ns bowling-clj.future-no-frame)


(defn calculate-roll [acc [idx roll]]
  (let [is-new-frame (or (not (:new-frame acc))
                         (= 10 (second (get (:rolls acc) (- idx 1)))))
        current-frame (if is-new-frame
                        (+ 1 (:frame-counter acc))
                        (:frame-counter acc))]
    {:running-total (+ (:running-total acc)
                       (if (<= current-frame 10) roll 0)
                       (if (and (<= current-frame 10) (= roll 10))
                         (second (get (:rolls acc) (+ idx 1)))
                         0)
                       (if (and (<= current-frame 10) (= roll 10))
                         (second (get (:rolls acc) (+ idx 2)))
                         0)
                       (if (and (<= current-frame 10)
                                is-new-frame
                                (= 10 (+ roll (second (get (:rolls acc) (+ idx 1))))))
                         (second (get (:rolls acc) (+ idx 2)))
                         0)
                       )
     
     :new-frame is-new-frame
     :frame-counter current-frame
     :rolls (:rolls acc)}))

(defn score
  [rolls]
  (let [indexed-rolls (map-indexed vector rolls)]
    (:running-total
     (reduce calculate-roll
             {:running-total 0
              :frame-counter 0
              :new-frame false
              :rolls (into [] indexed-rolls)}
             indexed-rolls))))
