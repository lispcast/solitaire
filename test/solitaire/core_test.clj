(ns solitaire.core-test
  (:require [clojure.test :refer :all]
            [solitaire.core :refer :all]
            [solitaire.card :as card]))

(deftest tableau0-tableau1
  (let [game {:tableau [{:up (list
                               (card/make :diamonds :3)
                               (card/make :clubs :4)
                               (card/make :hearts :5))}
                        {:up (list
                               (card/make :spades :6))}]}
        game2 {:tableau [{:up (list)}
                         {:up (list
                                (card/make :diamonds :3)
                                (card/make :clubs :4)
                                (card/make :hearts :5)
                                (card/make :spades :6))}]}
        move (transfer-fn 3 [:tableau 0 :up] [:tableau 1 :up])]
    (is (= game2 (move game)))))

(deftest tableau0-foundation0
  (let [game {:foundations [{:up (list
                                   (card/make :diamonds :2)
                                   (card/make :diamonds :A))}]
              :tableau [{:up (list
                               (card/make :diamonds :3)
                               (card/make :clubs :4)
                               (card/make :hearts :5))}]}
        game2 {:foundations [{:up (list
                                    (card/make :diamonds :3)
                                    (card/make :diamonds :2)
                                    (card/make :diamonds :A))}]
               :tableau [{:up (list
                                (card/make :clubs :4)
                                (card/make :hearts :5))}]}
        move (transfer-fn 1 [:tableau 0 :up] [:foundations 0 :up])]
    (is (= game2 (move game)))))

(deftest stock-down-stock-up
  (let [game {:stock [{:down (list
                               (card/make :spades :A)
                               (card/make :diamonds :K)
                               (card/make :diamonds :Q)
                               (card/make :clubs :6))
                       :up (list
                             (card/make :diamonds :2)
                             (card/make :diamonds :A))}]}
        game2 {:stock [{:down (list
                                (card/make :clubs :6))
                        :up (list
                              (card/make :diamonds :Q)
                              (card/make :diamonds :K)
                              (card/make :spades :A)
                              (card/make :diamonds :2)
                              (card/make :diamonds :A))}]}
        move (flip-fn 3 [:stock 0 :down] [:stock 0 :up])]
    (is (= game2 (move game)))))

(deftest tableau-down-tableau-up
  (let [game {:tableau [{:down (list
                                 (card/make :spades :A)
                                 (card/make :diamonds :K)
                                 (card/make :diamonds :Q)
                                 (card/make :clubs :6))
                         :up (list)}]}
        game2 {:tableau [{:down (list
                                  (card/make :diamonds :K)
                                  (card/make :diamonds :Q)
                                  (card/make :clubs :6))
                          :up (list
                                (card/make :spades :A))}]}
        move (flip-fn 1 [:tableau 0 :down] [:tableau 0 :up])]
    (is (= game2 (move game)))))
