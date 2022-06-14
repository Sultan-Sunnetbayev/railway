package tm.salam.hazarLogistika.railway.helper;

import java.util.Base64;

public class ParseJwtToken {

    public static String getEmail(final String token){

        String[] chunks=token.split("\\.");
        Base64.Decoder decoder=Base64.getUrlDecoder();

        String decodedToken[]=new String(decoder.decode(chunks[1])).split(",");
        String email=decodedToken[0].split(":")[1];
        email=email.substring(1,email.length()-1);

        return email;
    }
}
