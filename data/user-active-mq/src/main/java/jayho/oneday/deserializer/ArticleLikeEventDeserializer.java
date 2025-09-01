package jayho.oneday.deserializer;


import jayho.oneday.event.ArticleLikeEvent;

public class ArticleLikeEventDeserializer extends EventDeserializer<ArticleLikeEvent> {

    public ArticleLikeEventDeserializer() {
        super(ArticleLikeEvent.class);
    }

}
