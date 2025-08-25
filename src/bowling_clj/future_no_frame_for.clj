(ns bowling-clj.future-no-frame-for)

(defn get-future-roll [acc idx plus]
  (second (get acc (+ idx plus))))

(defn score
  [rolls]
  (let [indexed-rolls (into [] (map-indexed vector rolls))
        total (atom 0)
        is-new-frame (atom false)
        current-frame (atom 0)]
    (doseq [[idx roll] indexed-rolls]
      (swap! is-new-frame (fn [previous]
                            (or (not previous)
                                (= 10 (second (get indexed-rolls (- idx 1)))))))
      (swap! current-frame (fn [previous] (if @is-new-frame
                                           (+ 1 previous)
                                           previous)))
      (swap! total (partial + (+ (if (<= @current-frame 10) roll 0)
                                 (if (and (<= @current-frame 10) (= roll 10))
                                   (get-future-roll indexed-rolls idx 1)
                                   0)
                                 (if (and (<= @current-frame 10) (= roll 10))
                                   (get-future-roll indexed-rolls idx 2)
                                   0)
                                 (if (and (<= @current-frame 10)
                                          @is-new-frame
                                          (= 10 (+ roll (get-future-roll indexed-rolls idx 1))))
                                   (get-future-roll indexed-rolls idx 2)
                                   0)
                                 ))))
    @total))
