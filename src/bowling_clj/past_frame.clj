(ns bowling-clj.past-frame)

;;; On this version we first create a collection of frames
;;; A frame could be of six types
;;; partial -> Missing the second roll
;;; full normal -> two rolls no adding to 10
;;; strike -> single roll of 10
;;; spare -> two rolls adding to 10
;;; final strike -> First roll of 10 plus 2 bonus rolls
;;; final spare -> two rolls adding to 10 plus 1 bonus roll

(defn add-roll [roll frames]
  (let [[first & rest] frames]
    (if (= (count frames) 0)
      (cons {:score roll :frame-type :partial} frames)
      (condp = (:frame-type first)
        :partial (cons {:score (+ roll (:score first)) :frame-type :full} rest)
        :full (cons {:score roll :frame-type :partial} frames)))))

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
