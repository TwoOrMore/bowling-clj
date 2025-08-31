(ns bowling-clj.past-frame)

;;; On this version we first create a collection of frames
;;; A frame could be of four types
;;; partial -> Missing the second roll
;;; full normal -> two rolls no adding to 10
;;; strike -> single roll of 10
;;; spare -> two rolls adding to 10

(defn- is-strike-frame [roll]
  (= 10 roll))

(defn- is-last-frame [updated-frames]
  (= 10 (count updated-frames)))

(defn- is-spare-frame [total]
  (= total 10))

(defn- update-previous-strike [roll previous [last-frame & rest-frames :as frames]]
  (if (= :strike previous)
    (cons (update last-frame :score + roll)
          rest-frames)
    frames))

(defn- add-first-roll [roll frames]
  (if (is-strike-frame roll)
    (cons {:score roll :frame-type :strike} frames)
    (cons {:score roll :frame-type :partial} frames)))


(defn- add-second-roll [roll previous-frame rest-frames]
  (let [updated-frames (update-previous-strike
                        roll
                        (:previous previous-frame)
                        rest-frames)
        total (+ roll (:score previous-frame))]
    (if (is-spare-frame total)
      (cons {:score total :frame-type :spare} updated-frames)
      (cons {:score total :frame-type :full} updated-frames))))

(defn- add-roll-after-strike [roll previous-frame rest-frames]
  (let [updated-frames (->> rest-frames
                            (update-previous-strike
                             roll
                             (:previous previous-frame))
                            (cons {:score (+ roll
                                             (:score previous-frame))
                                   :frame-type :strike}))]
    (if (is-last-frame updated-frames)
      updated-frames
      (if (is-strike-frame roll)
        (cons
         {:score roll :frame-type :strike :previous :strike}
         updated-frames)
        (cons
         {:score roll :frame-type :partial :previous :strike}
         updated-frames)))))

(defn- add-roll-after-spare [roll previous-frame rest-frames]
  (let [updated-frames (cons (update previous-frame :score + roll)
                             rest-frames)]
    (if (is-last-frame updated-frames)
      updated-frames
      (add-first-roll roll updated-frames))))

(defn- add-roll [roll frames]
  (let [[previous-frame & rest-frames] frames]
    (if (= (count frames) 0)
      (add-first-roll roll frames)
      (condp = (:frame-type previous-frame)
        :strike (add-roll-after-strike roll previous-frame rest-frames)
        :spare (add-roll-after-spare roll previous-frame rest-frames)
        :partial (add-second-roll roll previous-frame rest-frames)
        :full (add-first-roll roll frames)))))

(defn- create-frames [rolls]
  (loop [[current & remaining-rolls] rolls
         current-frames []]
    (let [frames (add-roll current current-frames)]
      (if (= (count remaining-rolls) 0)
        frames
        (recur remaining-rolls frames)))))

(defn- score-frames [frames]
  (->> frames
       (map (fn [frame] (:score frame)))
       (reduce +)))

(defn score
  [rolls]
  (-> rolls
      create-frames
      score-frames))
