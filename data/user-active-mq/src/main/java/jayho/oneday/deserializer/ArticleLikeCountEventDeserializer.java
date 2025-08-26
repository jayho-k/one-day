package jayho.oneday.deserializer;

import jayho.oneday.event.ArticleLikeCountEvent;

public class ArticleLikeCountEventDeserializer extends EventDeserializer<ArticleLikeCountEvent>{
    public ArticleLikeCountEventDeserializer() {
        super(ArticleLikeCountEvent.class);
    }
}
