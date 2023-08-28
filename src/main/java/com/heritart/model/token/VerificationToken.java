package com.heritart.model.token;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Calendar;
import java.util.Date;

@Document("token")
public class VerificationToken {
    private @Id String id;
    private @Indexed(unique = true) String email;
    private Date expiration;

    public VerificationToken(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(int expirationHours) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, expirationHours);
        this.expiration = calendar.getTime();
    }

    public boolean isExpired(){
        Date now = new Date();
        return now.after(this.expiration);
    }
}
