(ns bowling-clj.past-frame)

;;; On this version we first create a collection of frames
;;; A frame could be of four types
;;; partial -> Missing the second roll
;;; full normal -> two rolls no adding to 10
;;; strike -> single roll of 10
;;; spare -> two rolls adding to 10

(defn update-previous-strike [roll previous [last-frame & rest-frames :as frames]]
  (if (= :strike previous)
    (cons (update last-frame :score + roll)
          rest-frames)
    frames))

(defn add-roll [roll frames]
  (let [[first-frame & rest-frames] frames]
    (if (= (count frames) 0)
      (if (= 10 roll)
        (cons {:score roll :frame-type :strike} frames)
        (cons {:score roll :frame-type :partial} frames))
      (condp = (:frame-type first-frame)
        :strike (let [updated-frames (->> rest-frames
                                          (update-previous-strike
                                           roll
                                           (:previous first-frame))
                                          (cons {:score (+ roll
                                                           (:score first-frame))
                                                 :frame-type :strike}))]
                  (if (= 10 (count updated-frames))
                    updated-frames
                    (if (= 10 roll)
                      (cons
                       {:score roll :frame-type :strike :previous :strike}
                       updated-frames)
                      (cons
                       {:score roll :frame-type :partial :previous :strike}
                       updated-frames))))
        :spare (let [updated-frames (cons {:score (+ roll (:score first-frame))
                                           :frame-type :partial}
                                          rest-frames)]
                 (if (= 10 (count updated-frames))
                   updated-frames
                   (cons {:score roll :frame-type :partial}
                         updated-frames)))
        :partial (let [updated-frames (update-previous-strike
                                       roll
                                       (:previous first-frame)
                                       rest-frames)
                       total (+ roll (:score first-frame))]
                   (if (= total 10)
                     (cons {:score total :frame-type :spare} updated-frames)
                     (cons {:score total :frame-type :full} updated-frames)))
        :full (if (= 10 roll)
                (cons {:score roll :frame-type :strike} frames)
                (cons {:score roll :frame-type :partial} frames))))))

(defn create-frames [rolls]
  (loop [[current & remaining-rolls] rolls
         current-frames []]
    (let [frames (add-roll current current-frames)]
      (if (= (count remaining-rolls) 0)
        (reverse frames)
        (recur remaining-rolls frames)))))

(defn score-frames [frames]
  (->> frames
       (map (fn [frame] (:score frame)))
       (reduce +)))

(defn score
  [rolls]
  (-> rolls
      create-frames
      score-frames))
