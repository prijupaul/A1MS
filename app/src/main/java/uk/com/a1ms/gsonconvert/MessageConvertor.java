package uk.com.a1ms.gsonconvert;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;

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
import uk.com.a1ms.util.StringUtil;

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
            Spanned spannedString;

            String jsonmsg = jsonObject.get("longMessage").getAsString().trim();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                spannedString = Html.fromHtml(jsonmsg,Html.FROM_HTML_MODE_LEGACY);

            } else {
                spannedString = Html.fromHtml(jsonmsg);
            }

            longMessage.setLongMessage(new SpannableString(StringUtil.noTrailingwhiteLines(spannedString)));
            message.setMessage(longMessage);
        }


        if(jsonObject.has("shortMessage")) {
            ShortMessage shortMessage = new ShortMessage();
            String jsonmsg = jsonObject.get("shortMessage").getAsString().trim();
            Log.d("short message",jsonmsg);

            Spanned spannedString;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                spannedString = Html.fromHtml(jsonmsg,Html.FROM_HTML_MODE_LEGACY);

            } else {
                spannedString = Html.fromHtml(jsonmsg);
            }

            shortMessage.setShortMessage(new SpannableString(StringUtil.noTrailingwhiteLines(spannedString)));
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
