package domain;

import java.util.LinkedHashMap;

public class FlashCard {

    LinkedHashMap<String, LinkedHashMap<String,Integer>> flashCard = new LinkedHashMap<>();

    public LinkedHashMap<String, LinkedHashMap<String,Integer>> getFlashCard() {
        return flashCard;
    }

    public void setFlashCard(String card, LinkedHashMap<String,Integer> info) {
        flashCard.put(card,info);
    }

}
