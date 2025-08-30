(ns bowling-clj.past-frame)

;;; On this version we first create a collection of frames
;;; A frame could be of six types
;;; partial -> Missing the second roll
;;; full normal -> two rolls no adding to 10
;;; strike -> single roll of 10
;;; spare -> two rolls adding to 10
;;; final strike -> First roll of 10 plus 2 bonus rolls
;;; final spare -> two rolls adding to 10 plus 1 bonus roll

(defn create-frames [rolls]

  
  )


(defn score-frames [frames])

(defn score
  [rolls]
  (-> rolls
      create-frames
      score-frames))
