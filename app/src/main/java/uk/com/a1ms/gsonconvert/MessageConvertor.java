package uk.com.a1ms.gsonconvert;

import android.text.SpannableString;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.dto.LongMessage;
import uk.com.a1ms.dto.Message;
import uk.com.a1ms.dto.ShortMessage;

/**
 * Created by priju.jacobpaul on 13/10/2016.
 */

public class MessageConvertor implements JsonDeserializer<Message> {

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Message message = new Message();
        JsonObject jsonObject = json.getAsJsonObject();

        if(jsonObject.has("longMessage")) {
            LongMessage longMessage = new LongMessage();
            longMessage.setLongMessage(new SpannableString(jsonObject.get("longMessage").getAsString()));
            message.setMessage(longMessage);
        }


        if(jsonObject.has("shortMessage")) {
            ShortMessage shortMessage = new ShortMessage();
            shortMessage.setShortMessage(new SpannableString(jsonObject.get("shortMessage").getAsString()));
            message.setShortMessage(shortMessage);
        }

        if(jsonObject.has("userId")) {
            A1MSUser a1MSUser = new A1MSUser();
            a1MSUser.setUserId(jsonObject.get("userId").getAsString());
            message.setIdUser(a1MSUser);
        }

        if(jsonObject.has("toUserId")) {
            A1MSUser a1MSUser = new A1MSUser();
            a1MSUser.setUserId(jsonObject.get("toUserId").getAsString());
            message.setIdToUser(a1MSUser);
        }

        return message;
    }
}
